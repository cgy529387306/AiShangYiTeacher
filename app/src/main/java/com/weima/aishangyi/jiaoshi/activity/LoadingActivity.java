package com.weima.aishangyi.jiaoshi.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps2d.LocationSource;
import com.mb.android.utils.Helper;
import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.utils.NavigationHelper;
import com.weima.aishangyi.jiaoshi.utils.ProjectHelper;
import com.weima.aishangyi.jiaoshi.utils.ServiceHelper;


/**
 * 引导页面
 * @author
 */
public class LoadingActivity extends Activity {
    private static final int LOADING_TIME_OUT = 2 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 去除信号栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        ServiceHelper.startGetClass();
        ServiceHelper.startGetQQ();
        if (ProjectHelper.isLogin()){
            ServiceHelper.startRefreshToken();
        }
        new Handler().postDelayed(new Runnable() {

            public void run() {
                if (ProjectHelper.isLogin()) {
                    NavigationHelper.startActivity(LoadingActivity.this, HomeActivity.class, null, true);
                } else {
                    NavigationHelper.startActivity(LoadingActivity.this, UserLoginActivity.class, null, true);
                }
            }

        }, LOADING_TIME_OUT);
    }

}
