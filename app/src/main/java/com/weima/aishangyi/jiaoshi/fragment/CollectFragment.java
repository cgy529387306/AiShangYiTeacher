package com.weima.aishangyi.jiaoshi.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.mb.android.utils.Helper;
import com.mb.android.utils.JsonHelper;
import com.mb.android.utils.ToastHelper;
import com.mb.android.utils.view.LoadingView;
import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.adapter.ActivityClassAdapter;
import com.weima.aishangyi.jiaoshi.adapter.RecommendTeacherAdapter;
import com.weima.aishangyi.jiaoshi.adapter.TalentQueryAdapter;
import com.weima.aishangyi.jiaoshi.constants.ProjectConstants;
import com.weima.aishangyi.jiaoshi.entity.ActivityResp;
import com.weima.aishangyi.jiaoshi.entity.InfoResp;
import com.weima.aishangyi.jiaoshi.http.RequestEntity;
import com.weima.aishangyi.jiaoshi.http.RequestHelper;
import com.weima.aishangyi.jiaoshi.http.ResponseListener;
import com.weima.aishangyi.jiaoshi.utils.LogHelper;
import com.weima.aishangyi.jiaoshi.utils.ProjectHelper;
import com.weima.aishangyi.jiaoshi.xlistview.XListView;

import java.util.HashMap;

/**
 * 收藏
 */
public class CollectFragment extends Fragment implements XListView.IXListViewListener,ResponseListener {
    private XListView xListView;
    private LoadingView loadingView;
    private int currentPage = 1;
    private int type = 1;
    private static final String TYPE = "TYPE";
    private ActivityClassAdapter activityClassAdapter;
    private TalentQueryAdapter talentQueryAdapter;
    public static CollectFragment newInstance(int type) { //对外提供创建实例的方法，你给我需要显示的内容，我给你Fragment实例
        CollectFragment fragment = new CollectFragment();
        Bundle b = new Bundle();
        b.putInt(TYPE, type);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getInt(TYPE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.common_listview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        requestData();
    }

    private void initUI(View view) {
        activityClassAdapter = new ActivityClassAdapter(getActivity());
        talentQueryAdapter = new TalentQueryAdapter(getActivity());
        xListView = (XListView) view.findViewById(R.id.xListView);
        xListView.setPullLoadEnable(true);
        xListView.setPullRefreshEnable(true);
        xListView.setXListViewListener(this);
        if (type == 1){
            xListView.setAdapter(talentQueryAdapter);
        }else if (type == 4){
            xListView.setAdapter(activityClassAdapter);
        }
        loadingView = (LoadingView) view.findViewById(R.id.loadingView);
        loadingView.postLoadState(LoadingView.State.LOADING);
    }

    @Override
    public boolean onResponseSuccess(int gact, String response,
                                     Object... extras) {
        loadingView.postLoadState(LoadingView.State.GONE);
        switch (type){
            case 1:
                //资讯
                InfoResp entity = JsonHelper.fromJson(response, InfoResp.class);
                if ("200".equals(entity.getCode())){
                    if (Helper.isNotEmpty(entity.getData())){
                        if (Helper.isNotEmpty(entity.getData().getData())){
                            if (currentPage == 1){
                                talentQueryAdapter.clear();
                            }
                            if (currentPage >= entity.getData().getLast_page()){
                                xListView.setPullLoadEnable(false);
                            }else{
                                xListView.setPullLoadEnable(true);
                            }
                            talentQueryAdapter.addMore(entity.getData().getData());
                        }else{
                            loadingView.postLoadState(LoadingView.State.LOADING_EMPTY);
                            xListView.setPullLoadEnable(false);
                        }
                    }
                }else{
                    ToastHelper.showToast(entity.getMessage());
                }
                break;
            case 4:
                //活动
                ActivityResp activityResp = JsonHelper.fromJson(response, ActivityResp.class);
                if ("200".equals(activityResp.getCode())){
                    if (Helper.isNotEmpty(activityResp.getData())){
                        if (Helper.isNotEmpty(activityResp.getData().getData())){
                            if (currentPage == 1){
                                activityClassAdapter.clear();
                            }
                            if (currentPage >= activityResp.getData().getLast_page()){
                                xListView.setPullLoadEnable(false);
                            }else{
                                xListView.setPullLoadEnable(true);
                            }
                            activityClassAdapter.addMore(activityResp.getData().getData());
                        }else{
                            loadingView.postLoadState(LoadingView.State.LOADING_EMPTY);
                            xListView.setPullLoadEnable(false);
                        }
                    }
                }else{
                    ToastHelper.showToast(activityResp.getMessage());
                }
                break;
            default:
                break;
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
        requestMap.put("type", type);
        requestMap.put("page",currentPage);
        post(ProjectConstants.Url.MY_COLLECT, requestMap);
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
    public void onRefresh() {
        currentPage=1;
        requestData();
    }

    @Override
    public void onLoadMore() {
        currentPage++;
        requestData();
    }


}
