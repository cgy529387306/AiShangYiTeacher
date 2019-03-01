package com.weima.aishangyi.jiaoshi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.mb.android.utils.Helper;
import com.mb.android.utils.JsonHelper;
import com.mb.android.utils.ProgressDialogHelper;
import com.mb.android.utils.ToastHelper;
import com.squareup.picasso.Picasso;
import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.base.BaseActivity;
import com.weima.aishangyi.jiaoshi.constants.ProjectConstants;
import com.weima.aishangyi.jiaoshi.entity.CommonEntity;
import com.weima.aishangyi.jiaoshi.entity.QuestionBean;
import com.weima.aishangyi.jiaoshi.utils.NavigationHelper;
import com.weima.aishangyi.jiaoshi.utils.ProjectHelper;

import java.util.HashMap;

/**
 * 作者：cgy on 16/11/28 22:02
 * 邮箱：593960111@qq.com
 * 提问
 */
public class AnswerQuestionAcitity extends BaseActivity implements View.OnClickListener{
    private ImageView imv_user_avater;
    private TextView txv_user_name,txv_create_time,txv_money;
    private TextView txv_problem,txv_qustion_count;
    private EditText edit_content;
    private QuestionBean questionBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_question);
        setCustomTitle("问题");
        initUI();
        initData();
    }

    private void initUI() {
        imv_user_avater = findView(R.id.imv_user_avater);
        txv_user_name = findView(R.id.txv_user_name);
        txv_create_time = findView(R.id.txv_create_time);
        txv_money = findView(R.id.txv_money);
        txv_problem = findView(R.id.txv_problem);
        txv_qustion_count = findView(R.id.txv_qustion_count);
        edit_content = findView(R.id.edit_content);
        findView(R.id.btn_save).setOnClickListener(this);
    }

    private void initData(){
        questionBean = (QuestionBean) getIntent().getSerializableExtra("question");
        if (Helper.isNotEmpty(questionBean)){
            txv_problem.setText(ProjectHelper.getCommonText(questionBean.getProblem()));
            txv_money.setText(questionBean.getPrice() + "");
            txv_create_time.setText(ProjectHelper.formatLongTime(questionBean.getCreated_at()));
            if (Helper.isNotEmpty(questionBean.getUser())){
                txv_user_name.setText(ProjectHelper.getCommonText(questionBean.getUser().getNickname()));
                if (Helper.isNotEmpty(questionBean.getUser().getIcon())){
                    Picasso.with(AnswerQuestionAcitity.this).load(questionBean.getUser().getIcon()).placeholder(R.drawable.ic_avatar_default).into(imv_user_avater);
                }
            }
            txv_qustion_count.setText("共" + questionBean.getAnswer_count() + "人参与回答");
        }
    }

    @Override
    public boolean onResponseSuccess(int gact, String response,
                                     Object... extras) {
        super.onResponseSuccess(gact, response, extras);
        ProgressDialogHelper.dismissProgressDialog();
        CommonEntity entity = JsonHelper.fromJson(response, CommonEntity.class);
        if ("200".equals(entity.getCode())){
            LocalBroadcastManager.getInstance(AnswerQuestionAcitity.this).sendBroadcast(new Intent(ProjectConstants.BroadCastAction.UPDATE_QUESTION_LIST));
            ToastHelper.showToast("解答成功");
            Bundle bundle = new Bundle();
            bundle.putSerializable("question", questionBean);
            NavigationHelper.startActivity(AnswerQuestionAcitity.this, AnswerDetailAcitity.class, bundle, true);
        }else{
            ToastHelper.showToast(entity.getMessage());
        }
        return true;
    }

    @Override
    public boolean onResponseError(int gact, String response,
                                   VolleyError error, Object... extras) {
        ProgressDialogHelper.dismissProgressDialog();
        ToastHelper.showToast("解答失败");
        return true;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_save){
            doAnswer();
        }
    }

    private void doAnswer() {
        String answer = edit_content.getText().toString();
        if (Helper.isEmpty(answer)){
            ToastHelper.showToast("请输入解答的内容~");
        }
        ProgressDialogHelper.showProgressDialog(this, "加载中...");
        HashMap<String, Object> requestMap = new HashMap<String, Object>();
        requestMap.put("question_id", questionBean.getId());
        requestMap.put("answer", answer);
        post(ProjectConstants.Url.QUESTION_ANSWER, requestMap);
    }
}
