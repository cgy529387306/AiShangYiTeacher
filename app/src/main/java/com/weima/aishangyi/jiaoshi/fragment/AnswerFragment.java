package com.weima.aishangyi.jiaoshi.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.mb.android.utils.Helper;
import com.mb.android.utils.JsonHelper;
import com.mb.android.utils.PreferencesHelper;
import com.mb.android.utils.ToastHelper;
import com.mb.android.utils.view.LoadingView;
import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.activity.AnswerSearchActivity;
import com.weima.aishangyi.jiaoshi.activity.UserAnswerActivity;
import com.weima.aishangyi.jiaoshi.adapter.AnswerAdapter;
import com.weima.aishangyi.jiaoshi.constants.ProjectConstants;
import com.weima.aishangyi.jiaoshi.entity.ClassTypeResp;
import com.weima.aishangyi.jiaoshi.entity.QuestionResp;
import com.weima.aishangyi.jiaoshi.http.RequestEntity;
import com.weima.aishangyi.jiaoshi.http.RequestHelper;
import com.weima.aishangyi.jiaoshi.http.ResponseListener;
import com.weima.aishangyi.jiaoshi.pop.AnswerHelpPop;
import com.weima.aishangyi.jiaoshi.pop.SelectAnswerTypePop;
import com.weima.aishangyi.jiaoshi.utils.LogHelper;
import com.weima.aishangyi.jiaoshi.utils.NavigationHelper;
import com.weima.aishangyi.jiaoshi.utils.ProjectHelper;
import com.weima.aishangyi.jiaoshi.xlistview.XListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 问答
 * Created by cgy on 16/7/18.
 */
public class AnswerFragment extends Fragment implements XListView.IXListViewListener, View.OnClickListener , ResponseListener {
    private TextView txv_answer_type;
    private XListView xListView;
    private AnswerAdapter mAdapter;
    private LoadingView loadingView;
    private int currentPage = 1;
    private SelectAnswerTypePop selectAnswerTypePop;
    private TextView btn_answering,btn_answered;
    private long item = 0;
    private LinearLayout linType;
    private int type = 0;
    private AnswerHelpPop answerHelpPop;
    private LocalBroadcastManager mLocalBroadcastManager;
    private BroadcastReceiver mUpdateUserInfoReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            requestData();
        }
    };

    private BroadcastReceiver mAnsweredReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            toggleType();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        mLocalBroadcastManager.registerReceiver(mUpdateUserInfoReceiver, new IntentFilter(ProjectConstants.BroadCastAction.UPDATE_QUESTION_LIST));
        mLocalBroadcastManager.registerReceiver(mAnsweredReceiver, new IntentFilter(ProjectConstants.BroadCastAction.UPDATE_ANSWER_STATUS));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocalBroadcastManager.unregisterReceiver(mUpdateUserInfoReceiver);
        mLocalBroadcastManager.unregisterReceiver(mAnsweredReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frg_answer, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        initType();
        requestData();
    }

    private void initUI(View view) {
        linType = (LinearLayout) view.findViewById(R.id.linType);
        btn_answering = (TextView) view.findViewById(R.id.btn_answering);
        btn_answered = (TextView) view.findViewById(R.id.btn_answered);
        txv_answer_type = (TextView) view.findViewById(R.id.txv_answer_type);
        xListView = (XListView) view.findViewById(R.id.xListView);
        xListView.setPullLoadEnable(true);
        xListView.setPullRefreshEnable(true);
        xListView.setXListViewListener(this);
        mAdapter = new AnswerAdapter(getActivity());
        xListView.setAdapter(mAdapter);
        xListView.setDivider(new ColorDrawable(0xfff8f8f8));
        xListView.setDividerHeight(20);
        loadingView = (LoadingView) view.findViewById(R.id.loadingView);
        loadingView.postLoadState(LoadingView.State.LOADING);
        txv_answer_type.setOnClickListener(this);
        btn_answering.setOnClickListener(this);
        btn_answered.setOnClickListener(this);
        view.findViewById(R.id.imv_answer_chat).setOnClickListener(this);
        view.findViewById(R.id.imv_answer_search).setOnClickListener(this);
        view.findViewById(R.id.btn_answer_help).setOnClickListener(this);
        answerHelpPop = new AnswerHelpPop(getActivity(), new AnswerHelpPop.SelectListener() {
            @Override
            public void onSelected(int type) {
            }
        });
    }

    private void initType(){
        try {
            List<ClassTypeResp.DataBean> classTypeList = new ArrayList<>();
            ClassTypeResp.DataBean  dataBean = new ClassTypeResp.DataBean();
            dataBean.setName("所有");
            dataBean.setId(-1);
            classTypeList.add(dataBean);
            String response = PreferencesHelper.getInstance().getString(ProjectConstants.Preferences.KEY_CLASS_TYPE);
            if (Helper.isNotEmpty(response)){
                ClassTypeResp entity = JsonHelper.fromJson(response, ClassTypeResp.class);
                if ("200".equals(entity.getCode())) {
                    if (Helper.isNotEmpty(entity.getData())){
                        classTypeList.addAll(entity.getData());
                    }
                } else {
                    ToastHelper.showToast(entity.getMessage());
                }
            }
            item = classTypeList.get(0).getId();
            txv_answer_type.setText(ProjectHelper.getCommonText(classTypeList.get(0).getName()));
            selectAnswerTypePop = new SelectAnswerTypePop(getActivity(), classTypeList, new SelectAnswerTypePop.SelectListener() {
                @Override
                public void onSelected(ClassTypeResp.DataBean typeData) {
                    if (Helper.isNotEmpty(typeData)){
                        item = typeData.getId();
                        txv_answer_type.setText(ProjectHelper.getCommonText(typeData.getName()));
                        requestData();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onResponseSuccess(int gact, String response,
                                     Object... extras) {
        loadingView.postLoadState(LoadingView.State.GONE);
        QuestionResp entity = JsonHelper.fromJson(response, QuestionResp.class);
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
                    mAdapter.addMore(entity.getData().getData(),type);
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
        if (id == R.id.imv_answer_chat) {
            NavigationHelper.startActivity(getActivity(), UserAnswerActivity.class, null, false);
        } else if (id == R.id.imv_answer_search) {
            Bundle bundle = new Bundle();
            bundle.putInt("type",type);
            NavigationHelper.startActivity(getActivity(), AnswerSearchActivity.class, bundle, false);
        } else if (id == R.id.txv_answer_type) {
            if (selectAnswerTypePop.isShowing()) {
                selectAnswerTypePop.dismiss();
            } else {
                selectAnswerTypePop.show(v);
            }
        }else if (id == R.id.btn_answering){
            toggleType();
        }else if (id == R.id.btn_answered){
            toggleType();
        }else if (id == R.id.btn_answer_help){
            if (answerHelpPop.isShowing()) {
                answerHelpPop.dismiss();
            } else {
                answerHelpPop.show(v);
            }
        }
    }

    private void toggleType(){
        type = type==0?1:0;
        btn_answering.setTextColor(type == 0?getActivity().getResources().getColor(R.color.base_orange):getActivity().getResources().getColor(R.color.white));
        btn_answering.setBackgroundColor(type == 0 ? getActivity().getResources().getColor(R.color.white) : getActivity().getResources().getColor(R.color.base_orange));
        btn_answered.setTextColor(type == 0 ? getActivity().getResources().getColor(R.color.white) : getActivity().getResources().getColor(R.color.base_orange));
        btn_answered.setBackgroundColor(type == 0 ? getActivity().getResources().getColor(R.color.base_orange) : getActivity().getResources().getColor(R.color.white));
        currentPage = 1;
        requestData();
    }

    private void requestData(){
        HashMap<String, Object> requestMap = new HashMap<String, Object>();
        if (item != -1){
            requestMap.put("item",item);
        }
        requestMap.put("type",type);
        requestMap.put("page",currentPage);
        post(ProjectConstants.Url.QUESTION_SEARCH, requestMap);
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
