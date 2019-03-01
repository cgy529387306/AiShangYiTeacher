package com.weima.aishangyi.jiaoshi.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.hyphenate.chat.EMClient;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.mb.android.utils.AppHelper;
import com.mb.android.utils.Helper;
import com.mb.android.utils.JsonHelper;
import com.mb.android.utils.PreferencesHelper;
import com.mb.android.utils.ProgressDialogHelper;
import com.mb.android.utils.ToastHelper;
import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.activity.ClassSettingActivity;
import com.weima.aishangyi.jiaoshi.activity.HomeActivity;
import com.weima.aishangyi.jiaoshi.activity.IdentificationActivity;
import com.weima.aishangyi.jiaoshi.activity.MsgActivity;
import com.weima.aishangyi.jiaoshi.activity.NewVisitorActivity;
import com.weima.aishangyi.jiaoshi.activity.PersonInfoActivity;
import com.weima.aishangyi.jiaoshi.activity.QuelifyActivity;
import com.weima.aishangyi.jiaoshi.activity.SelectCityActivity;
import com.weima.aishangyi.jiaoshi.activity.TeacherGuideActivity;
import com.weima.aishangyi.jiaoshi.activity.UserClassroomOrderActivity;
import com.weima.aishangyi.jiaoshi.adapter.HeadWheelAdapter;
import com.weima.aishangyi.jiaoshi.chat.MyConversationListActivity;
import com.weima.aishangyi.jiaoshi.constants.ProjectConstants;
import com.weima.aishangyi.jiaoshi.entity.AdvertResp;
import com.weima.aishangyi.jiaoshi.entity.CommonEntity;
import com.weima.aishangyi.jiaoshi.entity.CurrentUser;
import com.weima.aishangyi.jiaoshi.entity.QQBean;
import com.weima.aishangyi.jiaoshi.entity.QQResp;
import com.weima.aishangyi.jiaoshi.http.RequestEntity;
import com.weima.aishangyi.jiaoshi.http.RequestHelper;
import com.weima.aishangyi.jiaoshi.http.ResponseListener;
import com.weima.aishangyi.jiaoshi.pop.QQServicePop;
import com.weima.aishangyi.jiaoshi.utils.LogHelper;
import com.weima.aishangyi.jiaoshi.utils.NavigationHelper;
import com.weima.aishangyi.jiaoshi.utils.ProjectHelper;

import java.util.HashMap;

/**
 * Created by cgy on 16/7/18.
 * 首页
 */
public class HomeFragment extends Fragment implements View.OnClickListener , ResponseListener ,HomeActivity.MyCityListener{
    private TextView txv_home_location;
    private ImageView imv_msg_new;
    private RollPagerView myViewPager1;
    private QQServicePop qqServicePop;
    private QQBean qqBean;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frg_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        initQQ();
        initChatMsg();
        requestAd();
    }

    private void initUI(View view) {
        String city = PreferencesHelper.getInstance().getString(ProjectConstants.Preferences.KEY_CITY);
        imv_msg_new = (ImageView) view.findViewById(R.id.imv_msg_new);
        txv_home_location = (TextView) view.findViewById(R.id.txv_home_location);
        txv_home_location.setText(Helper.isEmpty(city)?"选择城市":city);
        txv_home_location.setOnClickListener(this);
        myViewPager1 = (RollPagerView) view.findViewById(R.id.myViewPager1);
        myViewPager1.setHintView(new ColorPointHintView(getActivity(), getActivity().getResources().getColor(R.color.base_orange),
                getActivity().getResources().getColor(R.color.base_orange_light)));
        myViewPager1.setAdapter(new HeadWheelAdapter(getActivity(), null));
        ((HomeActivity)getActivity()).setMyCityListener(this);
        view.findViewById(R.id.imv_home_msg).setOnClickListener(this);
        view.findViewById(R.id.btn_new_visitor).setOnClickListener(this);
        view.findViewById(R.id.btn_new_order).setOnClickListener(this);
        view.findViewById(R.id.btn_user_info).setOnClickListener(this);
        view.findViewById(R.id.btn_user_identification).setOnClickListener(this);
        view.findViewById(R.id.btn_class_setting).setOnClickListener(this);
        view.findViewById(R.id.btn_teacher_guide).setOnClickListener(this);
        view.findViewById(R.id.btn_custom_service).setOnClickListener(this);
        qqServicePop = new QQServicePop(getActivity(), new QQServicePop.SelectListener() {
            @Override
            public void onSelected(int type) {

            }
        });
    }
    private void initQQ(){
        String response = PreferencesHelper.getInstance().getString(ProjectConstants.Preferences.KEY_QQ);
        if (Helper.isNotEmpty(response)){
            QQResp entity = JsonHelper.fromJson(response, QQResp.class);
            if (Helper.isNotEmpty(entity) && "200".equals(entity.getCode())) {
                if (Helper.isNotEmpty(entity.getData())){
                    qqBean = entity.getData();
                    qqServicePop.initData(qqBean);
                }
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        initChatMsg();
    }

    private void initChatMsg(){
        if (CurrentUser.getInstance().born() && imv_msg_new != null
                && EMClient.getInstance().chatManager()!=null){
            int count =  EMClient.getInstance().chatManager().getUnreadMsgsCount();
            imv_msg_new.setVisibility(count>0?View.VISIBLE:View.GONE);
        }
    }

    @Override
    public boolean onResponseSuccess(int gact, String response,
                                     Object... extras) {
        int requestType = extras.length != 0 ? (Integer) extras[0] : 0;
        switch (requestType){
            case 1:
                AdvertResp resp = JsonHelper.fromJson(response, AdvertResp.class);
                if(null != resp && Helper.isNotEmpty(resp.getData())){
                    myViewPager1.setAdapter(new HeadWheelAdapter(getActivity(), resp.getData()));
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onResponseError(int gact, String response,
                                   VolleyError error, Object... extras) {
        ToastHelper.showToast("请求失败");
        return false;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.txv_home_location) {
            Intent intent = new Intent(getActivity(), SelectCityActivity.class);
            startActivityForResult(intent, ProjectConstants.ActivityRequestCode.REQUEST_HOME_LOCATION);
        } else if (id == R.id.imv_home_msg) {
            NavigationHelper.startActivity(getActivity(), MyConversationListActivity.class,null,false);
        } else if (id == R.id.btn_new_visitor) {
            NavigationHelper.startActivity(getActivity(), NewVisitorActivity.class, null, false);
        } else if (id == R.id.btn_new_order) {
            NavigationHelper.startActivity(getActivity(), UserClassroomOrderActivity.class, null, false);
        } else if (id == R.id.btn_user_info) {
            NavigationHelper.startActivity(getActivity(), PersonInfoActivity.class, null, false);
        } else if (id == R.id.btn_user_identification) {
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(ProjectConstants.BroadCastAction.UPDATE_USER_INFO));
            NavigationHelper.startActivity(getActivity(), IdentificationActivity.class, null, false);
        } else if (id == R.id.btn_class_setting) {
            NavigationHelper.startActivity(getActivity(), ClassSettingActivity.class, null, false);
        } else if (id == R.id.btn_teacher_guide) {
            NavigationHelper.startActivity(getActivity(), TeacherGuideActivity.class, null, false);
        } else if (id == R.id.btn_custom_service) {
            if (qqServicePop.isShowing()) {
                qqServicePop.dismiss();
            } else {
                qqServicePop.show(v);
            }
        }
    }

    private void requestAd(){
        HashMap<String, Object> requestMap = new HashMap<String, Object>();
        requestMap.put("position","1");
        post(ProjectConstants.Url.HOME_AD, requestMap,1);
    }

    /**
     * 发送get请求
     * @param url 接口地址
     * @param requestParamsMap 请求参数Map
     * @param extras 附加参数（本地参数，将原样返回给回调）
     */
    public void post(String url, HashMap<String, Object> requestParamsMap, Object... extras) {
        LogHelper.i(url);
        RequestEntity entity = new RequestEntity.Builder().setTimeoutMs(20000).setUrl(url).setRequestHeader(ProjectHelper.getUserAgent1()).setRequestParamsMap(requestParamsMap).getRequestEntity();
        RequestHelper.post(entity, this, extras);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == ProjectConstants.ActivityRequestCode.REQUEST_HOME_LOCATION){
                if (data != null){
                    String city = data.getStringExtra(ProjectConstants.BundleExtra.KEY_CITY_NAME);
                    PreferencesHelper.getInstance().putString(ProjectConstants.Preferences.KEY_CITY, city);
                    txv_home_location.setText(city);
                }
            }
        }
    }


    @Override
    public void onCityGet(String city) {
        String cityStr = PreferencesHelper.getInstance().getString(ProjectConstants.Preferences.KEY_CITY);
        if (Helper.isEmpty(cityStr) || "选择城市".equals(cityStr)){
            if (txv_home_location!=null){
                txv_home_location.setText(Helper.isEmpty(city)?"选择城市":city);
            }
            PreferencesHelper.getInstance().putString(ProjectConstants.Preferences.KEY_CITY, city);
        }
    }
}
