package com.weima.aishangyi.jiaoshi.pop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mb.android.utils.Helper;
import com.mb.android.utils.ToastHelper;
import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.adapter.ShareAdapter;
import com.weima.aishangyi.jiaoshi.entity.QQBean;
import com.weima.aishangyi.jiaoshi.utils.ProjectHelper;

/**
 * Created by cgy
 */
public class QQServicePop extends PopupWindow implements View.OnClickListener {
    private View rootView;
    private Context mContext;
    private ShareAdapter adapter;
    private TextView qq1,qq2,qq3,qq4;
    private TextView qqGroup1,qqGroup2;
    private TextView phone;
    private QQBean qqInfo;
    private SelectListener selectListener;

    public interface SelectListener {
        public void onSelected(int type);
    }

    public QQServicePop(Context context, SelectListener listener) {
        super(context);
        this.mContext = context;
        this.selectListener = listener;
        rootView = View.inflate(mContext, R.layout.pop_qqservice, null);
        setFocusable(true);
        setOutsideTouchable(true);
        setWidth(ViewPager.LayoutParams.MATCH_PARENT);
        setHeight(ViewPager.LayoutParams.MATCH_PARENT);
        setContentView(rootView);
        setBackgroundDrawable(new BitmapDrawable());
        findComponent();
        initComponent();
    }

    private void findComponent() {
        qq1 = (TextView) rootView.findViewById(R.id.qq1);
        qq2 = (TextView) rootView.findViewById(R.id.qq2);
        qq3 = (TextView) rootView.findViewById(R.id.qq3);
        qq4 = (TextView) rootView.findViewById(R.id.qq4);
        qqGroup1 = (TextView) rootView.findViewById(R.id.qqGroup1);
        qqGroup2 = (TextView) rootView.findViewById(R.id.qqGroup2);
        phone = (TextView) rootView.findViewById(R.id.phone);
    }

    public void initData(QQBean qqBean){
        if (Helper.isNotEmpty(qqBean)){
            qqInfo = qqBean;
            qq1.setText(ProjectHelper.getCommonText(qqBean.getQq1()));
            qq2.setText(ProjectHelper.getCommonText(qqBean.getQq2()));
            qq3.setText(ProjectHelper.getCommonText(qqBean.getQq3()));
            qq4.setText(ProjectHelper.getCommonText(qqBean.getQq4()));
            phone.setText(ProjectHelper.getCommonText(qqBean.getPhone()));
        }
    }

    private void initComponent() {
        rootView.findViewById(R.id.btn_close).setOnClickListener(this);
        qq1.setOnClickListener(this);
        qq2.setOnClickListener(this);
        qq3.setOnClickListener(this);
        qq4.setOnClickListener(this);
        qqGroup1.setOnClickListener(this);
        qqGroup2.setOnClickListener(this);
        phone.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_close) {
            dismiss();
        }else if (id == R.id.qq1){
            if (Helper.isNotEmpty(qqInfo) && Helper.isNotEmpty(qqInfo.getQq1())){
                ProjectHelper.chatQQ(qqInfo.getQq1(),mContext);
            }else{
                ToastHelper.showToast("客服不在");
            }
        }else if (id == R.id.qq2){
            if (Helper.isNotEmpty(qqInfo) && Helper.isNotEmpty(qqInfo.getQq2())){
                ProjectHelper.chatQQ(qqInfo.getQq2(),mContext);
            }else{
                ToastHelper.showToast("客服不在");
            }
        }else if (id == R.id.qq3){
            if (Helper.isNotEmpty(qqInfo) && Helper.isNotEmpty(qqInfo.getQq3())){
                ProjectHelper.chatQQ(qqInfo.getQq3(),mContext);
            }else{
                ToastHelper.showToast("客服不在");
            }
        }else if (id == R.id.qq4){
            if (Helper.isNotEmpty(qqInfo) && Helper.isNotEmpty(qqInfo.getQq4())){
                ProjectHelper.chatQQ(qqInfo.getQq4(),mContext);
            }else{
                ToastHelper.showToast("客服不在");
            }
        }else if (id == R.id.qqGroup1){
            ToastHelper.showToast("客服不在");
        }else if (id == R.id.qqGroup2){
            ToastHelper.showToast("客服不在");
        }else if (id == R.id.phone){
            if (Helper.isNotEmpty(qqInfo) && Helper.isNotEmpty(qqInfo.getPhone())){
                ProjectHelper.callTel((Activity) mContext,qqInfo.getPhone());
            }
        }
    }

    public void show(View v) {
        showAtLocation(v, Gravity.CENTER, 0, 0);
    }
}
