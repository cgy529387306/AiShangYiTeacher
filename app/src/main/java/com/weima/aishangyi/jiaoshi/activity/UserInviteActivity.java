package com.weima.aishangyi.jiaoshi.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.mb.android.utils.JsonHelper;
import com.mb.android.utils.ToastHelper;
import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.base.BaseActivity;
import com.weima.aishangyi.jiaoshi.constants.ProjectConstants;
import com.weima.aishangyi.jiaoshi.entity.BalanceResp;
import com.weima.aishangyi.jiaoshi.utils.NavigationHelper;
import com.weima.aishangyi.jiaoshi.utils.ProjectHelper;

import java.util.HashMap;

/**
 * 邀请好友
 *
 * @author cgy
 */
public class UserInviteActivity extends BaseActivity{
    private TextView txv_invite_code;
    private String title = "爱尚艺教师端";
    private String inviteCode = "eea622";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomTitle("邀请好友");
        setImageRightButton(R.drawable.ic_share, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("title",title);
                NavigationHelper.startActivity(UserInviteActivity.this,ShareActivity.class,bundle,false);
            }
        });
        setContentView(R.layout.activity_user_invite);
        initUI();
        requestData();
    }

    private void initUI() {
        txv_invite_code = findView(R.id.txv_invite_code);
    }


    @Override
    public boolean onResponseSuccess(int gact, String response,
                                     Object... extras) {
        BalanceResp entity = JsonHelper.fromJson(response, BalanceResp.class);
        if ("200".equals(entity.getCode())){
            inviteCode = ProjectHelper.getCommonText(entity.getData());
            txv_invite_code.setText(inviteCode);
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
        post(ProjectConstants.Url.USER_INVITE, requestMap);
    }

}