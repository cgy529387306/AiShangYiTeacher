package com.weima.aishangyi.jiaoshi.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.base.BaseActivity;

/**
 * 爱尚艺客户端
 *
 * @author cgy
 */
public class AiShangYiClientActivity extends BaseActivity {
    private EditText mInputView;
    private Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomTitle("爱尚艺客户端");
        setContentView(R.layout.activity_aishangyi_client);
        initUI();
    }

    private void initUI() {
    }

}
