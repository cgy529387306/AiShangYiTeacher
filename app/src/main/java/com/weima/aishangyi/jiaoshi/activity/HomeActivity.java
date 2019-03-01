package com.weima.aishangyi.jiaoshi.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.LocationSource;
import com.android.volley.VolleyError;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.mb.android.utils.AppHelper;
import com.mb.android.utils.DialogHelper;
import com.mb.android.utils.Helper;
import com.mb.android.utils.JsonHelper;
import com.mb.android.utils.PreferencesHelper;
import com.mb.android.utils.ProgressDialogHelper;
import com.mb.android.utils.ToastHelper;
import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.adapter.MyFragmentPagerAdapter;
import com.weima.aishangyi.jiaoshi.chat.utils.APPConfig;
import com.weima.aishangyi.jiaoshi.chat.utils.SharedPreferencesUtils;
import com.weima.aishangyi.jiaoshi.constants.ProjectConstants;
import com.weima.aishangyi.jiaoshi.entity.CommonEntity;
import com.weima.aishangyi.jiaoshi.entity.CurrentUser;
import com.weima.aishangyi.jiaoshi.entity.UpdateResp;
import com.weima.aishangyi.jiaoshi.fragment.AnswerFragment;
import com.weima.aishangyi.jiaoshi.fragment.FindFragment;
import com.weima.aishangyi.jiaoshi.fragment.HomeFragment;
import com.weima.aishangyi.jiaoshi.fragment.UserFragment;
import com.weima.aishangyi.jiaoshi.http.RequestEntity;
import com.weima.aishangyi.jiaoshi.http.RequestHelper;
import com.weima.aishangyi.jiaoshi.http.ResponseListener;
import com.weima.aishangyi.jiaoshi.utils.LogHelper;
import com.weima.aishangyi.jiaoshi.utils.ProjectHelper;
import com.weima.aishangyi.jiaoshi.widget.FragmentViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 首页
 * Created by cgy on 16/7/18.
 */
public class HomeActivity extends AppCompatActivity implements View.OnClickListener,AMapLocationListener,LocationSource{
    private TextView btn_home,btn_answer,btn_user,btn_find;
    public ImageView imv_find_new,imv_user_new;
    private FragmentViewPager vip_pager;
    private ArrayList<Fragment> fragmentList;
    public static boolean isForeground = false;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private LocalBroadcastManager mLocalBroadcastManager;
    private MyCityListener myCityListener;

    private BroadcastReceiver mUpdateUserInfoReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            if (imv_find_new!=null){
                int count = PreferencesHelper.getInstance().getInt(ProjectConstants.Preferences.KEY_TALENT_COUNT,0);
                imv_find_new.setVisibility(count==0?View.GONE:View.VISIBLE);
            }
        }
    };

    private BroadcastReceiver mUpdateNewFansReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            if (imv_user_new!=null){
                int count = PreferencesHelper.getInstance().getInt(ProjectConstants.Preferences.KEY_FANS_COUNT,0);
                imv_user_new.setVisibility(count==0?View.GONE:View.VISIBLE);
            }
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocalBroadcastManager.unregisterReceiver(mUpdateUserInfoReceiver);
        mLocalBroadcastManager.unregisterReceiver(mUpdateNewFansReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        requestData();
        initUI();
        initMap();
        initEasyChat();
        initJpush();
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        mLocalBroadcastManager.registerReceiver(mUpdateUserInfoReceiver, new IntentFilter(ProjectConstants.BroadCastAction.UPDATE_TALENT_NEW));
        mLocalBroadcastManager.registerReceiver(mUpdateNewFansReceiver, new IntentFilter(ProjectConstants.BroadCastAction.UPDATE_FANS_NEW));
    }


    private void initUI() {
        vip_pager = (FragmentViewPager) findViewById(R.id.vip_pager);
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new AnswerFragment());
        fragmentList.add(new FindFragment());
        fragmentList.add(new UserFragment());
        vip_pager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
        vip_pager.setOffscreenPageLimit(fragmentList.size());
        btn_home = (TextView) findViewById(R.id.btn_home);
        btn_answer = (TextView) findViewById(R.id.btn_answer);
        btn_find = (TextView) findViewById(R.id.btn_find);
        btn_user = (TextView) findViewById(R.id.btn_user);
        imv_find_new = (ImageView) findViewById(R.id.imv_find_new);
        imv_user_new = (ImageView) findViewById(R.id.imv_user_new);
        btn_home.setOnClickListener(this);
        btn_answer.setOnClickListener(this);
        btn_find.setOnClickListener(this);
        btn_user.setOnClickListener(this);
        showHomeFragment();
    }

    private void initJpush() {
        if (CurrentUser.getInstance().born()){
            JPushInterface.setAlias(HomeActivity.this, "" + CurrentUser.getInstance().getId(), new TagAliasCallback() {

                @Override
                public void gotResult(int responseCode, String alias, Set<String> tags) {
                    if (responseCode == 0) {
                        Log.e("Jpush", "别名设置成功:" + CurrentUser.getInstance().getId());
                    }else{
                        Log.e("Jpush", "别名设置失败:" + responseCode);
                    }
                }
            });
        }
    }

    private void initEasyChat() {
        if (CurrentUser.getInstance().born()){
            if (!EMClient.getInstance().isLoggedInBefore()){
                EMClient.getInstance().login("teach_"+CurrentUser.getInstance().getId(), "123456", new EMCallBack() {

                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }

                    @Override
                    public void onError(int code, String error) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "login failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }
    }

    /**
     * 显示首页
     */
    private void showHomeFragment() {
        btn_home.setSelected(true);
        btn_answer.setSelected(false);
        btn_find.setSelected(false);
        btn_user.setSelected(false);
        vip_pager.setCurrentItem(0);
    }

    /**
     * 显示问答
     */
    private void showAnswerFragment() {
        btn_home.setSelected(false);
        btn_answer.setSelected(true);
        btn_find.setSelected(false);
        btn_user.setSelected(false);
        vip_pager.setCurrentItem(1);
    }
    /**
     * 显示发现
     */
    private void showFindFragment() {
        btn_home.setSelected(false);
        btn_answer.setSelected(false);
        btn_find.setSelected(true);
        btn_user.setSelected(false);
        vip_pager.setCurrentItem(2);
    }


    /**
     * 显示用户中心
     */
    private void showUserFragment() {
        LocalBroadcastManager.getInstance(HomeActivity.this).sendBroadcast(new Intent(ProjectConstants.BroadCastAction.UPDATE_USER_INFO));
        btn_home.setSelected(false);
        btn_answer.setSelected(false);
        btn_find.setSelected(false);
        btn_user.setSelected(true);
        vip_pager.setCurrentItem(3);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_home:
                showHomeFragment();
                break;

            case R.id.btn_answer:
                showAnswerFragment();
                break;
            case R.id.btn_find:
                showFindFragment();
                break;

            case R.id.btn_user:
                showUserFragment();
                break;
            default:
                break;
        }
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                if (myCityListener!=null){
                    myCityListener.onCityGet(amapLocation.getCity());
                }
                PreferencesHelper.getInstance().putString(ProjectConstants.Preferences.KEY_LAT, String.valueOf(amapLocation.getLatitude()));
                PreferencesHelper.getInstance().putString(ProjectConstants.Preferences.KEY_LON, String.valueOf(amapLocation.getLongitude()));
                requestLongiLati(String.valueOf(amapLocation.getLongitude()),String.valueOf(amapLocation.getLatitude()));
                deactivate();
            } else {
                PreferencesHelper.getInstance().putString(ProjectConstants.Preferences.KEY_LAT, String.valueOf(26.08));
                PreferencesHelper.getInstance().putString(ProjectConstants.Preferences.KEY_LON, String.valueOf(119.3));
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    private void requestLongiLati(String longitude,String latitude){
        HashMap<String, Object> requestMap = new HashMap<String, Object>();
        requestMap.put("longitude",longitude);
        requestMap.put("latitude",latitude);
        post(ProjectConstants.Url.INFO_THUMB, requestMap,3);
    }

    private void requestData(){
        final RequestEntity entity = new RequestEntity.Builder().setTimeoutMs(20000).setUrl(ProjectConstants.Url.UPDATE)
                .setRequestHeader(ProjectHelper.getUserAgent1()).setRequestParamsMap(new HashMap<String, Object>()).getRequestEntity();
        RequestHelper.post(entity, new ResponseListener() {
            @Override
            public boolean onResponseSuccess(int gact, String response, Object... extras) {
                final UpdateResp entity = JsonHelper.fromJson(response, UpdateResp.class);
                if ("200".equals(entity.getCode())) {
                    if (Helper.isNotEmpty(entity.getData())) {
                        if (Helper.isNotEmpty(entity.getData().getUrl()) &&
                                !String.valueOf(AppHelper.getCurrentVersion()).equals(entity.getData().getVersion())) {
                            DialogHelper.showConfirmDialog(HomeActivity.this, "新版本", "有新版本哦，前去更新吧～", true,
                                    R.string.dialog_positive, new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent();
                                            intent.setAction("android.intent.action.VIEW");
                                            Uri content_url = Uri.parse(entity.getData().getUrl());
                                            intent.setData(content_url);
                                            startActivity(intent);
                                        }

                                    }, R.string.dialog_negative, null);
                        }
                    }
                } else {
                    ToastHelper.showToast(entity.getMessage());
                }
                return true;
            }

            @Override
            public boolean onResponseError(int gact, String response, VolleyError error, Object... extras) {
                return false;
            }
        });
    }

    public void post(String url, HashMap<String, Object> requestParamsMap, Object... extras) {
        LogHelper.i(url);
        final RequestEntity entity = new RequestEntity.Builder().setTimeoutMs(20000).setUrl(url).setRequestHeader(ProjectHelper.getUserAgent1()).setRequestParamsMap(requestParamsMap).getRequestEntity();
        RequestHelper.post(entity, new ResponseListener() {
            @Override
            public boolean onResponseSuccess(int gact, String response, Object... extras) {
                CommonEntity entity1 = JsonHelper.fromJson(response, CommonEntity.class);
                if ("200".equals(entity1.getCode())) {
                    ToastHelper.showToast("上传经纬成功！");
                }
                return false;
            }

            @Override
            public boolean onResponseError(int gact, String response, VolleyError error, Object... extras) {
                return false;
            }
        }, extras);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        deactivate();
    }

    private void initMap(){
        mlocationClient = new AMapLocationClient(this);
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置为高精度定位模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        mlocationClient.startLocation();
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(LocationSource.OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    // region 双击返回
    private static final long DOUBLE_CLICK_INTERVAL = 2000;
    private long mLastClickTimeMills = 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - mLastClickTimeMills > DOUBLE_CLICK_INTERVAL) {
            ToastHelper.showToast("再按一次返回退出");
            mLastClickTimeMills = System.currentTimeMillis();
            return;
        }
        finish();
    }
    // endregion 双击返回

    public interface MyCityListener {
        void onCityGet(String city);
    }

    public void setMyCityListener(MyCityListener listener) {
        this.myCityListener = listener;
    }
}
