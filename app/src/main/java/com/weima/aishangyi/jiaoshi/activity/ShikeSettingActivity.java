package com.weima.aishangyi.jiaoshi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.mb.android.utils.Helper;
import com.mb.android.utils.JsonHelper;
import com.mb.android.utils.ProgressDialogHelper;
import com.mb.android.utils.ToastHelper;
import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.base.BaseActivity;
import com.weima.aishangyi.jiaoshi.constants.ProjectConstants;
import com.weima.aishangyi.jiaoshi.entity.CommonEntity;
import com.weima.aishangyi.jiaoshi.entity.CurrentUser;
import com.weima.aishangyi.jiaoshi.utils.NavigationHelper;

import java.util.HashMap;

/**
 * 试课设置
 */
public class ShikeSettingActivity extends BaseActivity {
    private ImageView btn_voiceremind;
    private EditText edit_test_price;
    private int is_test=1;//1支持试课
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomTitle("试课设置");
        setContentView(R.layout.activity_shike_setting);
        initUI();
        initData();
    }


    private void initUI() {
        btn_voiceremind = findView(R.id.btn_voiceremind);
        edit_test_price = findView(R.id.edit_test_price);
        btn_voiceremind.setOnClickListener(mClickListener);
        findView(R.id.btn_save).setOnClickListener(mClickListener);
    }

    private void initData() {
        CurrentUser currentUser = CurrentUser.getInstance();
        if (Helper.isNotEmpty(currentUser)){
            is_test = currentUser.getIs_test();
            if (is_test==0){
                btn_voiceremind.setImageResource(R.drawable.ic_sound_close);
            }else{
                btn_voiceremind.setImageResource(R.drawable.ic_sound_open);
            }
            edit_test_price.setText(currentUser.getTest_price()==null?"":currentUser.getTest_price());
            edit_test_price.setSelection(currentUser.getTest_price()==null?0:currentUser.getTest_price().length());
        }
    }

    @Override
    public boolean onResponseSuccess(int gact, String response,
                                     Object... extras) {
        super.onResponseSuccess(gact, response, extras);
        ProgressDialogHelper.dismissProgressDialog();
        CommonEntity entity = JsonHelper.fromJson(response, CommonEntity.class);
        if ("200".equals(entity.getCode())){
            ToastHelper.showToast("保存成功");
            LocalBroadcastManager.getInstance(ShikeSettingActivity.this).sendBroadcast(new Intent(ProjectConstants.BroadCastAction.UPDATE_USER_INFO));
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

    /**
     * 点击事件
     */
    private View.OnClickListener mClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_save:
                    doSaveInfo();
                    break;
                case R.id.btn_voiceremind:
                    if (is_test == 0){
                        is_test = 1;
                        btn_voiceremind.setImageResource(R.drawable.ic_sound_open);
                    }else{
                        is_test = 0;
                        btn_voiceremind.setImageResource(R.drawable.ic_sound_close);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void doSaveInfo(){
        HashMap<String, Object> requestMap = new HashMap<String, Object>();
        String test_price = edit_test_price.getText().toString();
        if (Helper.isEmpty(test_price)){
            ToastHelper.showToast("请输入试课价格");
            return;
        }
        requestMap.put("test_price",test_price);
        requestMap.put("is_test", is_test);
        ProgressDialogHelper.showProgressDialog(ShikeSettingActivity.this, "提交中...");
        post(ProjectConstants.Url.ACCOUNT_EDIT_INFO, requestMap);
    }


}
