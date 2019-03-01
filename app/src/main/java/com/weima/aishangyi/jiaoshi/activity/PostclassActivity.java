package com.weima.aishangyi.jiaoshi.activity;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.mb.android.utils.Helper;
import com.mb.android.utils.JsonHelper;
import com.mb.android.utils.PreferencesHelper;
import com.mb.android.utils.ToastHelper;
import com.mb.android.utils.view.LoadingView;
import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.adapter.ClassSetting2Adapter;
import com.weima.aishangyi.jiaoshi.adapter.ClassSettingAdapter;
import com.weima.aishangyi.jiaoshi.adapter.TalentCircleAdapter;
import com.weima.aishangyi.jiaoshi.base.BaseActivity;
import com.weima.aishangyi.jiaoshi.constants.ProjectConstants;
import com.weima.aishangyi.jiaoshi.entity.ClassTypeResp;
import com.weima.aishangyi.jiaoshi.entity.CommonEntity;
import com.weima.aishangyi.jiaoshi.entity.ItemLessonResp;
import com.weima.aishangyi.jiaoshi.entity.LessonBean;
import com.weima.aishangyi.jiaoshi.entity.LessonResp;
import com.weima.aishangyi.jiaoshi.utils.NavigationHelper;
import com.weima.aishangyi.jiaoshi.xlistview.XListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 发布课程
 */
public class PostclassActivity extends BaseActivity implements XListView.IXListViewListener {
    private XListView xListView;
    private ClassSetting2Adapter adapter;
    private int currentPage = 1;
    private TextView txv_type;
    private long classType = -1;
    private String className;
    private List<LessonBean> dataList = new ArrayList<>();
    private LoadingView loadingView;
    private LocalBroadcastManager mLocalBroadcastManager;
    /**
     * 更新用户信息广播接受者
     */
    private BroadcastReceiver mUpdateUserInfoReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            requestData();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocalBroadcastManager.unregisterReceiver(mUpdateUserInfoReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postclass);
        setCustomTitle("发布课程");
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(PostclassActivity.this);
        mLocalBroadcastManager.registerReceiver(mUpdateUserInfoReceiver, new IntentFilter(ProjectConstants.BroadCastAction.UPDATE_LESSON_LIST));
        setImageRightButton(R.drawable.bg_add, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (classType == -1) {
                    ToastHelper.showToast("请选择课程类别");
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putLong(ProjectConstants.BundleExtra.KEY_CLASS_TYPE_ID, classType);
                    NavigationHelper.startActivity(PostclassActivity.this, EditClassActivity.class, bundle, false);
                }
            }
        });
        initUI();
        initData();
    }

    private void initUI() {
        adapter = new ClassSetting2Adapter(this,dataList);
        txv_type = findView(R.id.txv_type);
        txv_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationHelper.startActivityForResult(PostclassActivity.this, ClassTypeActivity.class, null, ProjectConstants.ActivityRequestCode.REQUEST_SELECT_TYPE);
            }
        });
        loadingView = findView(R.id.loadingView);
        xListView = findView(R.id.xListView);
        xListView.setPullLoadEnable(true);
        xListView.setPullRefreshEnable(true);
        xListView.setXListViewListener(this);
        xListView.setAdapter(adapter);
    }


    private void initData() {
        classType = getIntent().getLongExtra(ProjectConstants.BundleExtra.KEY_CLASS_TYPE_ID, -1);
        className = getIntent().getStringExtra(ProjectConstants.BundleExtra.KEY_CLASS_TYPE_NAME);
        txv_type.setText(className == null ? "请选择" : className);
        if (classType != -1) {
            loadingView.postLoadState(LoadingView.State.LOADING);
            requestData();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == ProjectConstants.ActivityRequestCode.REQUEST_SELECT_TYPE){
            ClassTypeResp.DataBean.ChildrenBean childrenBean = (ClassTypeResp.DataBean.ChildrenBean) data.getSerializableExtra(ProjectConstants.BundleExtra.KEY_CLASS_TYPE_ID);
            if (Helper.isNotEmpty(childrenBean)){
                classType = childrenBean.getId();
                txv_type.setText(childrenBean.getName()==null?"":childrenBean.getName());
                if (classType != -1) {
                    loadingView.postLoadState(LoadingView.State.LOADING);
                    requestData();
                }
            }
        }
    }

    @Override
    public boolean onResponseSuccess(int gact, String response,
                                     Object... extras) {
        super.onResponseSuccess(gact, response, extras);
        loadingView.postLoadState(LoadingView.State.GONE);
        ItemLessonResp entity = JsonHelper.fromJson(response,ItemLessonResp.class);
        if ("200".equals(entity.getCode())){
            if (Helper.isNotEmpty(entity.getData().getData())){
                if (currentPage == 1){
                    adapter.clear();
                }
                if (currentPage >= entity.getData().getLast_page()){
                    xListView.setPullLoadEnable(false);
                }else{
                    xListView.setPullLoadEnable(true);
                }
                adapter.addMore(entity.getData().getData());
            }else{
                loadingView.postLoadState(LoadingView.State.LOADING_EMPTY);
                xListView.setPullLoadEnable(false);
            }
        }else{
            ToastHelper.showToast(entity.getMessage());
        }
        xListView.stopRefresh();
        xListView.stopLoadMore();
        return true;
    }

    @Override
    public boolean onResponseError(int gact, String response,
                                   VolleyError error, Object... extras) {
        loadingView.postLoadState(LoadingView.State.LOADING_FALIED);
        return true;
    }


    @Override
    public void onRefresh() {
        currentPage = 1;
        requestData();
    }

    @Override
    public void onLoadMore() {
        currentPage++;
        requestData();
    }

    private void requestData(){
        HashMap<String, Object> requestMap = new HashMap<String, Object>();
        requestMap.put("page", currentPage);
        requestMap.put("item",classType);
        post(ProjectConstants.Url.LESSON_LIST_BYID, requestMap);
    }
}
