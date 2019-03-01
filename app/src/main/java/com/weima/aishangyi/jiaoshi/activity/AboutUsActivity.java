package com.weima.aishangyi.jiaoshi.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mb.android.utils.AppHelper;
import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.base.BaseActivity;

/**
 * 关于我们
 *
 * @author cgy
 */
public class AboutUsActivity extends BaseActivity {
    private TextView txv_version;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomTitle("关于我们");
        setContentView(R.layout.activity_user_aboutus);
        txv_version = findView(R.id.txv_version);
        txv_version.setText("版本：V"+ AppHelper.getCurrentVersionName());
    }

}