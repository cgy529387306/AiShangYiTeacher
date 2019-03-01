package com.weima.aishangyi.jiaoshi.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.mb.android.utils.Helper;
import com.mb.android.utils.JsonHelper;
import com.mb.android.utils.ToastHelper;
import com.mb.android.utils.view.LoadingView;
import com.squareup.picasso.Picasso;
import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.adapter.ActivityClassAdapter;
import com.weima.aishangyi.jiaoshi.adapter.AnswerDetailAdapter;
import com.weima.aishangyi.jiaoshi.base.BaseActivity;
import com.weima.aishangyi.jiaoshi.constants.ProjectConstants;
import com.weima.aishangyi.jiaoshi.entity.AnswerDetailResp;
import com.weima.aishangyi.jiaoshi.entity.QuestionBean;
import com.weima.aishangyi.jiaoshi.utils.ProjectHelper;
import com.weima.aishangyi.jiaoshi.xlistview.XListView;

import java.util.HashMap;

/**
 * 作者：cgy on 16/11/28 22:02
 * 邮箱：593960111@qq.com
 * 问题详情
 */
public class AnswerDetailAcitity extends BaseActivity implements View.OnClickListener{
    private XListView xListView;
    private LoadingView loadingView;
    private AnswerDetailAdapter adapter;
    private ImageView imv_user_avater;
    private TextView txv_user_name,txv_create_time,txv_money;
    private TextView txv_problem,txv_qustion_count;
    private QuestionBean question;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_listview);
        setCustomTitle("问题");
        initUI();
        requestData();
    }

    private void initUI() {
        xListView = findView(R.id.xListView);
        xListView.setPullLoadEnable(false);
        xListView.setPullRefreshEnable(false);
        xListView.addHeaderView(getHeaderView());
        adapter = new AnswerDetailAdapter(AnswerDetailAcitity.this);
        xListView.setAdapter(adapter);
        xListView.setDivider(new ColorDrawable(0xfff8f8f8));
        xListView.setDividerHeight(20);
        loadingView = findView(R.id.loadingView);
        loadingView.postLoadState(LoadingView.State.LOADING);
    }

    private View getHeaderView(){
        View view = LayoutInflater.from(this).inflate(R.layout.header_answer, null);
        imv_user_avater = (ImageView) view.findViewById(R.id.imv_user_avater);
        txv_user_name = (TextView) view.findViewById(R.id.txv_user_name);
        txv_create_time = (TextView) view.findViewById(R.id.txv_create_time);
        txv_money = (TextView) view.findViewById(R.id.txv_money);
        txv_problem = (TextView) view.findViewById(R.id.txv_problem);
        txv_qustion_count = (TextView) view.findViewById(R.id.txv_qustion_count);
        return view;
    }

    private void initData(QuestionBean questionBean){
        if (Helper.isNotEmpty(questionBean)){
            txv_problem.setText(ProjectHelper.getCommonText(questionBean.getProblem()));
            txv_money.setText(questionBean.getPrice() + "");
            txv_create_time.setText(ProjectHelper.formatLongTime(questionBean.getCreated_at()));
            if (Helper.isNotEmpty(questionBean.getUser())){
                txv_user_name.setText(ProjectHelper.getCommonText(questionBean.getUser().getNickname()));
                if (Helper.isNotEmpty(questionBean.getUser().getIcon())){
                    Picasso.with(AnswerDetailAcitity.this).load(questionBean.getUser().getIcon()).placeholder(R.drawable.ic_avatar_default).into(imv_user_avater);
                }
            }
            txv_qustion_count.setText("共" + questionBean.getAnswer_count() + "人参与回答");
            if (Helper.isNotEmpty(questionBean.getAnswer())){
                adapter.addMore(questionBean.getAnswer());
            }
        }
    }

    @Override
    public boolean onResponseSuccess(int gact, String response,
                                     Object... extras) {
        loadingView.postLoadState(LoadingView.State.GONE);
        AnswerDetailResp entity = JsonHelper.fromJson(response, AnswerDetailResp.class);
        if ("200".equals(entity.getCode())){
            initData(entity.getData());
        }else{
            ToastHelper.showToast(entity.getMessage());
        }
        return true;
    }

    @Override
    public boolean onResponseError(int gact, String response,
                                   VolleyError error, Object... extras) {
        loadingView.postLoadState(LoadingView.State.LOADING_FALIED);
        return true;
    }

    private void requestData(){
        question = (QuestionBean) getIntent().getSerializableExtra("question");
        HashMap<String, Object> requestMap = new HashMap<String, Object>();
        requestMap.put("question_id",question.getId());
        post(ProjectConstants.Url.QUESTION_DETAIL, requestMap);
    }



    @Override
    public void onClick(View v) {
        int id = v.getId();
    }
}
