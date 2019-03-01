package com.weima.aishangyi.jiaoshi.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.mb.android.utils.DialogHelper;
import com.mb.android.utils.Helper;
import com.mb.android.utils.JsonHelper;
import com.mb.android.utils.PreferencesHelper;
import com.mb.android.utils.ProgressDialogHelper;
import com.mb.android.utils.ToastHelper;
import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.adapter.TimeableAdapter;
import com.weima.aishangyi.jiaoshi.base.BaseActivity;
import com.weima.aishangyi.jiaoshi.constants.ProjectConstants;
import com.weima.aishangyi.jiaoshi.entity.CommonEntity;
import com.weima.aishangyi.jiaoshi.entity.CurrentUser;
import com.weima.aishangyi.jiaoshi.entity.TimeBean;
import com.weima.aishangyi.jiaoshi.utils.ProjectHelper;
import com.weima.aishangyi.jiaoshi.widget.LineGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/12/13 0013.
 */
public class TimeSettingActivity extends BaseActivity implements View.OnClickListener{
    private LineGridView timeableGrid;
    private TimeableAdapter timeableAdapter;
    private EditText add_text_input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomTitle("授课时间设置");
        setRightButton("重置", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogHelper.showConfirmDialog(TimeSettingActivity.this, "重置", "确定要重置授课时间？", true,
                        R.string.dialog_positive, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                timeableAdapter.clearList();
                            }

                        }, R.string.dialog_negative, null);
            }
        });
        setContentView(R.layout.activity_time_setting);
        initUI();
        initData();
    }

    private void initUI() {
        add_text_input = findView(R.id.add_text_input);
        timeableGrid = findView(R.id.timeableGrid);
        timeableAdapter = new TimeableAdapter(this);
        timeableGrid.setAdapter(timeableAdapter);
        findView(R.id.btn_save).setOnClickListener(this);
        findView(R.id.btn_add).setOnClickListener(this);
    }


    private void initData() {
        CurrentUser currentUser = CurrentUser.getInstance();
        if (Helper.isNotEmpty(currentUser)){
            if (Helper.isNotEmpty(currentUser.getLesson_time())){
                timeableAdapter.clearList();
                timeableAdapter.addList(currentUser.getLesson_time());
            }
            add_text_input.setText(ProjectHelper.getCommonText(currentUser.getMessage()));
            add_text_input.setSelection(add_text_input.getText().toString().length());
        }
    }

    private List<TimeBean> addTimeList(){
        List<TimeBean> data = new ArrayList<>();
        for (int i=0;i<7;i++){
            int count = timeableAdapter.getSelectTime().size();
            TimeBean timeBean = new TimeBean();
            timeBean.setId(count+i);
            timeBean.setWeek(i);
            timeBean.setStatus(1);
            timeBean.setNumber(1);
            data.add(timeBean);
        }
        return data;
    }

    @Override
    public boolean onResponseSuccess(int gact, String response,
                                     Object... extras) {
        super.onResponseSuccess(gact, response, extras);
        ProgressDialogHelper.dismissProgressDialog();
        CommonEntity entity = JsonHelper.fromJson(response, CommonEntity.class);
        if ("200".equals(entity.getCode())){
            LocalBroadcastManager.getInstance(TimeSettingActivity.this).sendBroadcast(new Intent(ProjectConstants.BroadCastAction.UPDATE_USER_INFO));
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
        ToastHelper.showToast("保存失败");
        return true;
    }

    private void doSaveInfo(){
        String leftMessage = add_text_input.getText().toString();
        ProgressDialogHelper.showProgressDialog(TimeSettingActivity.this, "保存中...");
        HashMap<String, Object> requestMap = new HashMap<String, Object>();
        if (Helper.isNotEmpty(timeableAdapter.getSelectTime())){
            requestMap.put("lesson_time", timeableAdapter.getSelectTime());
        }else{
            ToastHelper.showToast("请添加可授课时间！");
            return;
        }
        if (Helper.isNotEmpty(leftMessage)) {
            requestMap.put("message", leftMessage);
        }
        post(ProjectConstants.Url.ACCOUNT_EDIT_INFO, requestMap);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btn_save){
            doSaveInfo();
        }else if (id == R.id.btn_add){
            timeableAdapter.addList(addTimeList());
        }
    }
}
