package com.weima.aishangyi.jiaoshi.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;

import com.android.volley.VolleyError;
import com.mb.android.utils.Helper;
import com.mb.android.utils.JsonHelper;
import com.mb.android.utils.ProgressDialogHelper;
import com.mb.android.utils.ToastHelper;
import com.mb.android.utils.view.LoadingView;
import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.adapter.ClassSettingAdapter;
import com.weima.aishangyi.jiaoshi.base.BaseActivity;
import com.weima.aishangyi.jiaoshi.constants.ProjectConstants;
import com.weima.aishangyi.jiaoshi.entity.CommonEntity;
import com.weima.aishangyi.jiaoshi.entity.LessonResp;
import com.weima.aishangyi.jiaoshi.entity.TalentResp;
import com.weima.aishangyi.jiaoshi.utils.NavigationHelper;
import com.weima.aishangyi.jiaoshi.xlistview.XListView;

import java.util.HashMap;

/**
 * 课程设置
 */
public class ClassSettingActivity extends BaseActivity implements XListView.IXListViewListener {
    private XListView xListView;
    private ClassSettingAdapter mAdapter;
    private int currentPage = 1;
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
        setContentView(R.layout.activity_classsetting);
        setCustomTitle("课程设置");
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(ClassSettingActivity.this);
        mLocalBroadcastManager.registerReceiver(mUpdateUserInfoReceiver, new IntentFilter(ProjectConstants.BroadCastAction.UPDATE_LESSON_LIST));
        setImageRightButton(R.drawable.bg_add, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationHelper.startActivity(ClassSettingActivity.this, PostclassActivity.class, null, false);
            }
        });
        initUI();
        loadingView.postLoadState(LoadingView.State.LOADING);
        requestData();
    }


    private void initUI() {
        xListView = findView(R.id.xListView);
        xListView.setPullLoadEnable(true);
        xListView.setPullRefreshEnable(true);
        xListView.setXListViewListener(this);
        loadingView = findView(R.id.loadingView);
        mAdapter = new ClassSettingAdapter(this);
        xListView.setAdapter(mAdapter);
        findView(R.id.btn_shike).setOnClickListener(mClickListener);
        findView(R.id.btn_teachtime).setOnClickListener(mClickListener);
    }

    @Override
    public boolean onResponseSuccess(int gact, String response,
                                     Object... extras) {
        super.onResponseSuccess(gact, response, extras);
        loadingView.postLoadState(LoadingView.State.GONE);
        LessonResp entity = JsonHelper.fromJson(response,LessonResp.class);
        if ("200".equals(entity.getCode())){
            if (Helper.isNotEmpty(entity.getData())){
                if (Helper.isNotEmpty(entity.getData().getData())){
                    if (currentPage == 1){
                        mAdapter.clear();
                    }
                    if (currentPage >= entity.getData().getLast_page()){
                        xListView.setPullLoadEnable(false);
                    }else{
                        xListView.setPullLoadEnable(true);
                    }
                    mAdapter.addMore(entity.getData().getData());
                }else{
                    loadingView.postLoadState(LoadingView.State.LOADING_EMPTY);
                    xListView.setPullLoadEnable(false);
                }
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

    private void requestData(){
        HashMap<String, Object> requestMap = new HashMap<String, Object>();
        requestMap.put("page",currentPage);
        post(ProjectConstants.Url.LESSON_LIST, requestMap);
    }


    private View.OnClickListener mClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_shike://试课设置
                    NavigationHelper.startActivity(ClassSettingActivity.this, ShikeSettingActivity.class, null, false);
                    break;

                case R.id.btn_teachtime://授课时间设置
                    NavigationHelper.startActivity(ClassSettingActivity.this, TimeSettingActivity.class, null, false);
                    break;
                default:
                    break;
            }
        }
    };


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
}
