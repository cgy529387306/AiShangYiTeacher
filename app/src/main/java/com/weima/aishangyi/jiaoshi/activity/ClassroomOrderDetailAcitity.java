package com.weima.aishangyi.jiaoshi.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.hyphenate.easeui.EaseConstant;
import com.mb.android.utils.DialogHelper;
import com.mb.android.utils.Helper;
import com.mb.android.utils.JsonHelper;
import com.mb.android.utils.ProgressDialogHelper;
import com.mb.android.utils.ToastHelper;
import com.mb.android.utils.view.LoadingView;
import com.squareup.picasso.Picasso;
import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.base.BaseActivity;
import com.weima.aishangyi.jiaoshi.chat.MyChatActivity;
import com.weima.aishangyi.jiaoshi.constants.ProjectConstants;
import com.weima.aishangyi.jiaoshi.entity.ClassOrderBean;
import com.weima.aishangyi.jiaoshi.entity.CommonEntity;
import com.weima.aishangyi.jiaoshi.entity.OrderDetailResp;
import com.weima.aishangyi.jiaoshi.entity.TeacherBean;
import com.weima.aishangyi.jiaoshi.http.RequestEntity;
import com.weima.aishangyi.jiaoshi.http.RequestHelper;
import com.weima.aishangyi.jiaoshi.http.ResponseListener;
import com.weima.aishangyi.jiaoshi.utils.LogHelper;
import com.weima.aishangyi.jiaoshi.utils.NavigationHelper;
import com.weima.aishangyi.jiaoshi.utils.ProjectHelper;

import java.util.HashMap;

/**
 * 作者：cgy on 16/11/28 22:02
 * 邮箱：593960111@qq.com
 * 课程订单详情
 */
public class ClassroomOrderDetailAcitity extends BaseActivity implements ResponseListener {
    private static final int REQUEST_TYPE_CANCEL_ORDER = 1;
    private static final int REQUEST_TYPE_COMFIRM_ORDER = 2;
    private static final int REQUEST_TYPE_DELETE_ORDER = 3;
    private static final int REQUEST_TYPE_ORDER_DETAIL = 4;
    private ImageView imv_icon, imv_teacher_avater;
    private TextView txv_classname, txv_classdesc, txv_classtype, txv_status, txv_number, txv_time;
    private TextView cancelOrder, comfirmOrder;
    private TextView txv_teacher_name, txv_teacher_goodat,txv_address;
    private long order_id;
    private ClassOrderBean entity;
    private LoadingView loadingView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroomorder_detail);
        setCustomTitle("订单详情");
        initUI();
        requestData();
    }


    private void initUI() {
        order_id = getIntent().getLongExtra("order_id", 0);
        imv_icon = findView(R.id.imv_icon);
        txv_classname = findView(R.id.txv_classname);
        txv_classdesc = findView(R.id.txv_classdesc);
        txv_classtype = findView(R.id.txv_classtype);
        txv_address = findView(R.id.txv_address);
        txv_number = findView(R.id.txv_number);
        txv_time = findView(R.id.txv_time);
        txv_status = findView(R.id.txv_status);
        imv_teacher_avater = findView(R.id.imv_teacher_avater);
        txv_teacher_name = findView(R.id.txv_teacher_name);
        txv_teacher_goodat = findView(R.id.txv_teacher_goodat);
        cancelOrder = findView(R.id.cancelOrder);
        comfirmOrder = findView(R.id.comfirmOrder);
        loadingView = findView(R.id.loadingView);
        loadingView.postLoadState(LoadingView.State.LOADING);
        findView(R.id.imv_chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Helper.isNotEmpty(entity) && Helper.isNotEmpty(entity.getUser())){
                    Bundle bundle=new Bundle();
                    bundle.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
                    bundle.putString(EaseConstant.EXTRA_USER_ID, "stu_"+entity.getUser().getId());
                    bundle.putString(EaseConstant.EXTRA_USER_NAME, entity.getUser().getNickname());
                    NavigationHelper.startActivity(ClassroomOrderDetailAcitity.this, MyChatActivity.class, bundle, false);
                }
            }
        });
    }

    private void initData(ClassOrderBean orderBean) {
        entity = orderBean;
        if (Helper.isNotEmpty(entity.getTeacher())){
            txv_address.setText(ProjectHelper.getCommonText(entity.getTeacher().getAddress()));
        }
        if (Helper.isNotEmpty(entity.getUser())){
            txv_teacher_name.setText(ProjectHelper.getCommonText(entity.getUser().getNickname()));
            txv_teacher_goodat.setText(ProjectHelper.getCommonText(entity.getUser().getSignature()));
            if (Helper.isNotEmpty(entity.getUser().getIcon())) {
                Picasso.with(ClassroomOrderDetailAcitity.this).load(entity.getUser().getIcon()).placeholder(R.drawable.img_default).into(imv_teacher_avater);
            }
        }
        if (Helper.isNotEmpty(entity.getLesson())) {
            txv_classdesc.setText(ProjectHelper.getCommonText(entity.getLesson().getLesson_brief()));
            txv_classname.setText(ProjectHelper.getCommonText(entity.getLesson().getName()));
            if (Helper.isNotEmpty(entity.getLesson().getIcon())) {
                Picasso.with(ClassroomOrderDetailAcitity.this).load(entity.getLesson().getIcon()).placeholder(R.drawable.img_default).into(imv_icon);
            }
            txv_classtype.setText(entity.getLesson().getLesson_item() == 1 ? "一对一课程" : "拼课");
        }
        txv_status.setText(ProjectHelper.getOrderStatus(entity.getStatus()));
        txv_number.setText(ProjectHelper.getCommonText(entity.getOrder_no()));
        if (Helper.isNotEmpty(entity.getAppoint_time())) {
            String time = "";
            if (Helper.isNotEmpty(entity.getAppoint_time().getStart_time()) &&
                    Helper.isNotEmpty(entity.getAppoint_time().getEnd_time())){
                time = entity.getAppoint_time().getStart_time()+"-"+entity.getAppoint_time().getEnd_time();
            }
            txv_time.setText(ProjectHelper.getWeekStr(entity.getAppoint_time().getWeek()) + "  " + time);
        }
        //  "status" : "0", //0待确认，1待授课，2待评价，3已完成，4取消订单
        if (entity.getStatus() == 0) {
            cancelOrder.setVisibility(View.VISIBLE);
            comfirmOrder.setVisibility(View.VISIBLE);
            cancelOrder.setText("取消订单");
            comfirmOrder.setText("确认接单");
            orderCancelEvent(cancelOrder);
            orderComfirmTeacherEvent(comfirmOrder);
        } else if (entity.getStatus() == 1) {
            cancelOrder.setVisibility(View.GONE);
            comfirmOrder.setVisibility(View.VISIBLE);
            comfirmOrder.setText("取消订单");
            orderCancelEvent(comfirmOrder);
        } else if (entity.getStatus() == 2) {
            cancelOrder.setVisibility(View.GONE);
            comfirmOrder.setVisibility(View.GONE);
        } else {
            cancelOrder.setVisibility(View.GONE);
            comfirmOrder.setVisibility(View.VISIBLE);
            comfirmOrder.setText("删除订单");
            orderDeleteEvent(comfirmOrder);
        }
    }

    private void orderCancelEvent(View view){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogHelper.showConfirmDialog(ClassroomOrderDetailAcitity.this, "取消授课", "确定要取消授课吗？", true,
                        R.string.dialog_positive, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestCancelOrder();
                                dialog.dismiss();
                            }

                        }, R.string.dialog_negative, null);
            }
        });
    }

    private void orderDeleteEvent(View view){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogHelper.showConfirmDialog(ClassroomOrderDetailAcitity.this, "删除订单", "确定要删除该订单？", true,
                        R.string.dialog_positive, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestDeleteOrder();
                                dialog.dismiss();
                            }

                        }, R.string.dialog_negative, null);
            }
        });
    }

    private void orderComfirmTeacherEvent(View view){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogHelper.showConfirmDialog(ClassroomOrderDetailAcitity.this, "完成授课", "确定已经完成授课了吗？", true,
                        R.string.dialog_positive, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestComfirmlOrder();
                                dialog.dismiss();
                            }

                        }, R.string.dialog_negative, null);
            }
        });
    }

    @Override
    public boolean onResponseSuccess(int gact, String response,
                                     Object... extras) {
        int requestType = Integer.parseInt(extras[0].toString());
        if (requestType == REQUEST_TYPE_ORDER_DETAIL){
            loadingView.postLoadState(LoadingView.State.GONE);
            OrderDetailResp resp = JsonHelper.fromJson(response, OrderDetailResp.class);
            if(null != resp && Helper.isNotEmpty(resp.getData())){
                initData(resp.getData());
            }
            return true;
        }
        CommonEntity entity = JsonHelper.fromJson(response, CommonEntity.class);
        if ("200".equals(entity.getCode())){
            LocalBroadcastManager.getInstance(ClassroomOrderDetailAcitity.this).sendBroadcast(new Intent("classOrder"));
            if (requestType == REQUEST_TYPE_CANCEL_ORDER){
                ToastHelper.showToast("取消授课成功");
            }else if (requestType == REQUEST_TYPE_COMFIRM_ORDER){
                ToastHelper.showToast("接单成功");
            }else if (requestType == REQUEST_TYPE_DELETE_ORDER){
                ToastHelper.showToast("删除订单成功");
            }
            finish();
        }else{
            ToastHelper.showToast(entity.getMessage());
        }
        return true;
    }

    @Override
    public boolean onResponseError(int gact, String response,
                                   VolleyError error, Object... extras) {
        int requestType = Integer.parseInt(extras[0].toString());
        if (requestType == REQUEST_TYPE_ORDER_DETAIL){
            loadingView.postLoadState(LoadingView.State.LOADING_EMPTY);
            return true;
        }
        ToastHelper.showToast("请求失败");
        ProgressDialogHelper.dismissProgressDialog();
        return true;
    }

    private void requestCancelOrder() {
        HashMap<String, Object> requestMap = new HashMap<String, Object>();
        requestMap.put("order_id", order_id);
        post(ProjectConstants.Url.ORDER_LESSON_CANCEl, requestMap, REQUEST_TYPE_CANCEL_ORDER);
    }

    private void requestComfirmlOrder() {
        HashMap<String, Object> requestMap = new HashMap<String, Object>();
        requestMap.put("order_id", order_id);
        post(ProjectConstants.Url.ORDER_ACCEPT, requestMap, REQUEST_TYPE_COMFIRM_ORDER);
    }

    private void requestDeleteOrder() {
        HashMap<String, Object> requestMap = new HashMap<String, Object>();
        requestMap.put("order_id", order_id);
        post(ProjectConstants.Url.ORDER_LESSON_DELETE, requestMap, REQUEST_TYPE_DELETE_ORDER);
    }

    private void requestData() {
        HashMap<String, Object> requestMap = new HashMap<String, Object>();
        requestMap.put("order_id", order_id);
        post(ProjectConstants.Url.ORDER_DETAIL, requestMap, REQUEST_TYPE_ORDER_DETAIL);
    }


    public void post(String url, HashMap<String, Object> requestParamsMap, Object... extras) {
        LogHelper.i(url);
        RequestEntity entity = new RequestEntity.Builder().setTimeoutMs(20000).setUrl(url).setRequestHeader(ProjectHelper.getUserAgent1()).setRequestParamsMap(requestParamsMap).getRequestEntity();
        RequestHelper.post(entity, this, extras);
    }

}
