package com.weima.aishangyi.jiaoshi.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.mb.android.utils.ProgressDialogHelper;
import com.mb.android.utils.ToastHelper;
import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.base.BaseActivity;

/**
 * 课程订单评价
 *
 * @author cgy
 */
public class EvaluateActivity extends BaseActivity {
    private EditText mInputView;
    private Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomTitle("评价");
        setContentView(R.layout.activity_evaluate);
        initUI();
    }

    private void initUI() {
//        mInputView = findView(R.id.add_text_input);
//
//        findView(R.id.btn_submit).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String content = mInputView.getText().toString().trim();
//                if (Helper.isEmpty(content)) {
//                    ToastHelper.showToast("请输入反馈内容");
//                } else {
//                    ProgressDialogHelper.showProgressDialog(UserFeedbackActivity.this, "加载中...");
//                }
//            }
//        });
    }

    @Override
    public boolean onResponseSuccess(int gact, String response,
                                     Object... extras) {
        super.onResponseSuccess(gact, response, extras);
        ProgressDialogHelper.dismissProgressDialog();
//        CommonEntity entity = JsonHelper.fromJson(response, CommonEntity.class);
//		if (Helper.isNotEmpty(entity)) {
//			if ("0".equals(entity.getResuleCode())) {
//				finish();
//			}
//			ToastHelper.showToast(entity.getResultMessage());
//			return true;
//		}
        return true;
    }

    @Override
    public boolean onResponseError(int gact, String response,
                                   VolleyError error, Object... extras) {
        ProgressDialogHelper.dismissProgressDialog();
        ToastHelper.showToast("发送失败");
        return true;
    }

}
