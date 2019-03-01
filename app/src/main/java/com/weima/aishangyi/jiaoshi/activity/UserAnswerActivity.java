package com.weima.aishangyi.jiaoshi.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.mb.android.utils.Helper;
import com.mb.android.utils.JsonHelper;
import com.mb.android.utils.ToastHelper;
import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.base.BaseActivity;
import com.weima.aishangyi.jiaoshi.constants.ProjectConstants;
import com.weima.aishangyi.jiaoshi.entity.AnswerInoutResp;
import com.weima.aishangyi.jiaoshi.utils.NavigationHelper;

import java.util.HashMap;

/**
 * 问答主页
 */
public class UserAnswerActivity extends BaseActivity {
    private TextView txv_income;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomTitle("问答主页");
        setContentView(R.layout.activity_user_answer);
        initUI();
        requestData();
    }


    private void initUI() {
        txv_income = findView(R.id.txv_income);//总收入
        findView(R.id.btn_paymentdetail).setOnClickListener(mClickListener);
        findView(R.id.btn_question).setOnClickListener(mClickListener);
        findView(R.id.btn_onlookers).setOnClickListener(mClickListener);
    }

    @Override
    public boolean onResponseSuccess(int gact, String response,
                                     Object... extras) {
        AnswerInoutResp entity = JsonHelper.fromJson(response, AnswerInoutResp.class);
        if ("200".equals(entity.getCode())){
            if (Helper.isNotEmpty(entity.getData())){
                txv_income.setText("￥"+entity.getData().getIn());
            }
        }else{
            ToastHelper.showToast(entity.getMessage());
        }
        return true;
    }

    @Override
    public boolean onResponseError(int gact, String response,
                                   VolleyError error, Object... extras) {
        return true;
    }

    private void requestData(){
        HashMap<String, Object> requestMap = new HashMap<String, Object>();
        post(ProjectConstants.Url.CASH_ANWSER_INOUT, requestMap);
    }



    /**
     * 点击事件
     */
    private View.OnClickListener mClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_paymentdetail:
                    NavigationHelper.startActivity(UserAnswerActivity.this, UserPaymentDetailActivity.class, null, false);
                    break;

                case R.id.btn_question:
                    NavigationHelper.startActivity(UserAnswerActivity.this, UserQuestionActivity.class, null, false);
                    break;

                case R.id.btn_onlookers:
                    NavigationHelper.startActivity(UserAnswerActivity.this, AnswerSettingAcitity.class, null, false);
                    break;
                default:
                    break;
            }
        }
    };


}
