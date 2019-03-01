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
import com.weima.aishangyi.jiaoshi.entity.CurrentUser;
import com.weima.aishangyi.jiaoshi.entity.QuestionBean;
import com.weima.aishangyi.jiaoshi.utils.ProjectHelper;

import java.util.HashMap;

/**
 * 作者：cgy on 16/11/28 22:02
 * 邮箱：593960111@qq.com
 * 提问
 */
public class AnswerSettingAcitity extends BaseActivity implements View.OnClickListener{
    private EditText edit_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_setting);
        setCustomTitle("擅长内容");
        initUI();
    }

    private void initUI() {
        edit_content = findView(R.id.edit_content);
        edit_content.setText(ProjectHelper.getCommonText(CurrentUser.getInstance().getGood_at()));
        edit_content.setSelection(ProjectHelper.getCommonSeletion(CurrentUser.getInstance().getGood_at()));
        findView(R.id.btn_save).setOnClickListener(this);
    }

    @Override
    public boolean onResponseSuccess(int gact, String response,
                                     Object... extras) {
        super.onResponseSuccess(gact, response, extras);
        ProgressDialogHelper.dismissProgressDialog();
        CommonEntity entity = JsonHelper.fromJson(response, CommonEntity.class);
        if ("200".equals(entity.getCode())){
            LocalBroadcastManager.getInstance(AnswerSettingAcitity.this).sendBroadcast(new Intent(ProjectConstants.BroadCastAction.UPDATE_USER_INFO));
            ToastHelper.showToast("保存成功");
            finish();
        }else{
            ToastHelper.showToast(entity.getMessage());
        }
        return true;
    }

    @Override
    public boolean onResponseError(int gact, String response,
                                   VolleyError error, Object... extras) {
        ProgressDialogHelper.dismissProgressDialog();
        ToastHelper.showToast("请求失败");
        return true;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_save){
            doSave();
        }
    }

    private void doSave() {
        String good_at = edit_content.getText().toString();
        if (Helper.isEmpty(good_at)){
            ToastHelper.showToast("请输入您擅长回答的内容~");
        }
        ProgressDialogHelper.showProgressDialog(this, "加载中...");
        HashMap<String, Object> requestMap = new HashMap<String, Object>();
        requestMap.put("good_at", good_at);
        post(ProjectConstants.Url.ACCOUNT_EDIT_INFO, requestMap);
    }
}
