package com.weima.aishangyi.jiaoshi.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.mb.android.utils.Helper;
import com.mb.android.utils.JsonHelper;
import com.mb.android.utils.ToastHelper;
import com.mb.android.utils.view.LoadingView;
import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.adapter.ActivityClassAdapter;
import com.weima.aishangyi.jiaoshi.adapter.ContactAdapter;
import com.weima.aishangyi.jiaoshi.adapter.VisitorAdapter;
import com.weima.aishangyi.jiaoshi.base.BaseActivity;
import com.weima.aishangyi.jiaoshi.constants.ProjectConstants;
import com.weima.aishangyi.jiaoshi.entity.ActivityResp;
import com.weima.aishangyi.jiaoshi.entity.CurrentUser;
import com.weima.aishangyi.jiaoshi.entity.FollowResp;
import com.weima.aishangyi.jiaoshi.entity.UserListResp;
import com.weima.aishangyi.jiaoshi.pop.FunSortPop;
import com.weima.aishangyi.jiaoshi.pop.FunTypePop;
import com.weima.aishangyi.jiaoshi.xlistview.XListView;

import java.util.HashMap;

/**
 * 今日访问
 */
public class NewVisitorActivity extends BaseActivity implements View.OnClickListener, XListView.IXListViewListener {
    private XListView xListView;
    private VisitorAdapter mAdapter;
    private int currentPage = 1;
    private LoadingView loadingView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_listview);
        setCustomTitle("今日访问");
        initUI();
        requestData();
    }

    private void initUI() {
        loadingView = findView(R.id.loadingView);
        loadingView.postLoadState(LoadingView.State.LOADING);
        xListView = findView(R.id.xListView);
        xListView.setPullLoadEnable(true);
        xListView.setPullRefreshEnable(true);
        xListView.setXListViewListener(this);
        mAdapter = new VisitorAdapter(NewVisitorActivity.this);
        xListView.setAdapter(mAdapter);
    }

    @Override
    public boolean onResponseSuccess(int gact, String response,
                                     Object... extras) {
        super.onResponseSuccess(gact, response, extras);
        loadingView.postLoadState(LoadingView.State.GONE);
        UserListResp entity = JsonHelper.fromJson(response,UserListResp.class);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
    }


    @Override
    public void onRefresh() {
        currentPage=1;
        requestData();
    }

    @Override
    public void onLoadMore() {
        currentPage++;
        requestData();
    }

    private void requestData(){
        HashMap<String, Object> requestMap = new HashMap<String, Object>();
        post(ProjectConstants.Url.ACCESS_LIST, requestMap);
    }
}
