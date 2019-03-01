package com.weima.aishangyi.jiaoshi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mb.android.utils.Helper;
import com.mb.android.utils.JsonHelper;
import com.mb.android.utils.ProgressDialogHelper;
import com.mb.android.utils.ToastHelper;
import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.base.BaseActivity;
import com.weima.aishangyi.jiaoshi.constants.ProjectConstants;
import com.weima.aishangyi.jiaoshi.entity.CertifyInfoResp;
import com.weima.aishangyi.jiaoshi.entity.CommonEntity;
import com.weima.aishangyi.jiaoshi.entity.CurrentUser;
import com.weima.aishangyi.jiaoshi.utils.NavigationHelper;

import java.util.HashMap;

/**
 * 资料认证
 *
 * @author cgy
 */
public class IdentificationActivity extends BaseActivity {
    private TextView txv_id,txv_ed,txv_ql;
    private CertifyInfoResp.CertifyInfo certifyInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomTitle("资料认证");
        setContentView(R.layout.activity_identification);
        initUI();
        initData();
        requestData();
    }

    private void initUI() {
        txv_id = findView(R.id.txv_id);
        txv_ed = findView(R.id.txv_ed);
        txv_ql = findView(R.id.txv_ql);
        findView(R.id.btn_identity).setOnClickListener(mClickListener);
        findView(R.id.btn_education).setOnClickListener(mClickListener);
        findView(R.id.btn_qualification).setOnClickListener(mClickListener);
    }

    private void initData(){
        CurrentUser currentUser = CurrentUser.getInstance();
        if (Helper.isNotEmpty(currentUser)){
            if (currentUser.getIdCertification() == 1){
                txv_id.setText("审核中");
            }else if (currentUser.getIdCertification() == 2){
                txv_id.setText("已认证");
            }else if (currentUser.getIdCertification() == 3){
                txv_id.setText("认证未通过");
            }else {
                txv_id.setText("未认证");
            }

            if (currentUser.getEdCertification() == 1){
                txv_ed.setText("审核中");
            }else if (currentUser.getEdCertification() == 2){
                txv_ed.setText("已认证");
            }else if (currentUser.getEdCertification() == 3){
                txv_ed.setText("认证未通过");
            }else {
                txv_ed.setText("未认证");
            }

            if (currentUser.getQlCertification() == 1){
                txv_ql.setText("审核中");
            }else if (currentUser.getQlCertification() == 2){
                txv_ql.setText("已认证");
            }else if (currentUser.getQlCertification() == 3){
                txv_ql.setText("认证未通过");
            }else {
                txv_ql.setText("未认证");
            }
        }
    }

    @Override
    public boolean onResponseSuccess(int gact, String response,
                                     Object... extras) {
        super.onResponseSuccess(gact, response, extras);
        CertifyInfoResp entity = JsonHelper.fromJson(response, CertifyInfoResp.class);
        if (Helper.isNotEmpty(entity)) {
            if ("200".equals(entity.getCode())) {
                certifyInfo = entity.getData();
            }else{
                ToastHelper.showToast(entity.getMessage());
            }
            return true;
        }
        return true;
    }

    @Override
    public boolean onResponseError(int gact, String response,
                                   VolleyError error, Object... extras) {
        ToastHelper.showToast("请求失败");
        return true;
    }

    private void requestData(){
        HashMap<String, Object> requestMap = new HashMap<String, Object>();
        post(ProjectConstants.Url.ACCOUNT_GET_CERTIFY_INFO, requestMap);
    }


    /**
     * 点击事件
     */
    private OnClickListener mClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            CurrentUser currentUser = CurrentUser.getInstance();
            Bundle bundle = new Bundle();
            bundle.putSerializable(ProjectConstants.BundleExtra.KEY_CERTIFY_INFO,certifyInfo);
            switch (v.getId()) {
                case R.id.btn_identity://身份认证
                    if (currentUser.getIdCertification()==0){
                        bundle.putInt("type",0);
                        NavigationHelper.startActivityForResult(IdentificationActivity.this,IdentityActivity.class,bundle,ProjectConstants.ActivityRequestCode.REQUEST_CERTIFY_INFO);
                    }else if (currentUser.getIdCertification()==1){
                        ToastHelper.showToast("您提交的资料正在审核中...");
                    }else if (currentUser.getIdCertification()==2){
                        ToastHelper.showToast("您提交的资料已经审核通过了");
                    }else if (currentUser.getIdCertification()==3){
                        bundle.putInt("type",1);
                        NavigationHelper.startActivityForResult(IdentificationActivity.this,IdentityActivity.class,bundle,ProjectConstants.ActivityRequestCode.REQUEST_CERTIFY_INFO);
                    }
                    break;
                case R.id.btn_education://学历认证
                    if (currentUser.getEdCertification()==0){
                        bundle.putInt("type",0);
                        NavigationHelper.startActivityForResult(IdentificationActivity.this,EducationActivity.class,bundle,ProjectConstants.ActivityRequestCode.REQUEST_CERTIFY_INFO);
                    }else if (currentUser.getEdCertification()==1){
                        ToastHelper.showToast("您提交的资料正在审核中...");
                    }else if (currentUser.getEdCertification()==2){
                        ToastHelper.showToast("您提交的资料已经审核通过了");
                    }else if (currentUser.getEdCertification()==3){
                        bundle.putInt("type",1);
                        NavigationHelper.startActivityForResult(IdentificationActivity.this,EducationActivity.class,bundle,ProjectConstants.ActivityRequestCode.REQUEST_CERTIFY_INFO);
                    }
                    break;
                case R.id.btn_qualification://资格认证
                    if (currentUser.getQlCertification()==0){
                        bundle.putInt("type",0);
                        NavigationHelper.startActivityForResult(IdentificationActivity.this,QuelifyActivity.class,bundle,ProjectConstants.ActivityRequestCode.REQUEST_CERTIFY_INFO);
                    }else if (currentUser.getQlCertification()==1){
                        ToastHelper.showToast("您提交的资料正在审核中...");
                    }else if (currentUser.getQlCertification()==2){
                        ToastHelper.showToast("您提交的资料已经审核通过了");
                    }else if (currentUser.getQlCertification()==3){
                        bundle.putInt("type",1);
                        NavigationHelper.startActivityForResult(IdentificationActivity.this,QuelifyActivity.class,bundle,ProjectConstants.ActivityRequestCode.REQUEST_CERTIFY_INFO);
                    }
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == ProjectConstants.ActivityRequestCode.REQUEST_CERTIFY_INFO){
            initData();
        }
    }

}
