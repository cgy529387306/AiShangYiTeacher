package com.weima.aishangyi.jiaoshi.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mb.android.utils.Helper;
import com.squareup.picasso.Picasso;
import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.activity.AnswerDetailAcitity;
import com.weima.aishangyi.jiaoshi.activity.AnswerQuestionAcitity;
import com.weima.aishangyi.jiaoshi.activity.ClassroomOrderDetailAcitity;
import com.weima.aishangyi.jiaoshi.entity.QuestionBean;
import com.weima.aishangyi.jiaoshi.utils.NavigationHelper;
import com.weima.aishangyi.jiaoshi.utils.ProjectHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 一级分类（即左侧菜单）的adapter
 * Created by hanj on 14-9-25.
 */
public class AnswerAdapter extends BaseAdapter {
    private Activity activity;
    private List<QuestionBean> dataList = new ArrayList<>();
    private int type = 0;
    public AnswerAdapter(Activity act) {
        this.activity = act;
    }

    public void addMore(List<QuestionBean> list,int type){
        this.type = type;
        if (Helper.isNotEmpty(list)){
            dataList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void clear(){
        dataList.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (Helper.isNull(convertView)) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_answer, null);
            holder.imv_user_avater = (ImageView) convertView.findViewById(R.id.imv_user_avater);
            holder.txv_problem = (TextView) convertView.findViewById(R.id.txv_problem);
            holder.txv_user_name = (TextView) convertView.findViewById(R.id.txv_user_name);
            holder.txv_create_time = (TextView) convertView.findViewById(R.id.txv_create_time);
            holder.txv_money = (TextView) convertView.findViewById(R.id.txv_money);
            holder.txv_qustion_count = (TextView) convertView.findViewById(R.id.txv_qustion_count);
            holder.btn_operate = (TextView) convertView.findViewById(R.id.btn_operate);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        final QuestionBean entity = dataList.get(position);
        holder.txv_problem.setText(ProjectHelper.getCommonText(entity.getProblem()));
        holder.txv_money.setText(entity.getPrice() + "");
        holder.txv_create_time.setText(formatTime(entity.getCreated_at()));
        if (Helper.isNotEmpty(entity.getUser())){
            holder.txv_user_name.setText(ProjectHelper.getCommonText(entity.getUser().getNickname()));
            if (Helper.isNotEmpty(entity.getUser().getIcon())){
                Picasso.with(activity).load(entity.getUser().getIcon()).placeholder(R.drawable.ic_avatar_default).into(holder.imv_user_avater);
            }
        }
        holder.txv_qustion_count.setText("共" + entity.getAnswer_count() + "人参与回答");
        holder.btn_operate.setVisibility(type == 0 ? View.VISIBLE : View.GONE);
        holder.btn_operate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("question", entity);
                NavigationHelper.startActivity(activity, AnswerQuestionAcitity.class, bundle, false);
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == 0){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("question", entity);
                    NavigationHelper.startActivity(activity, AnswerQuestionAcitity.class, bundle, false);
                }else{
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("question", entity);
                    NavigationHelper.startActivity(activity, AnswerDetailAcitity.class, bundle, false);
                }
            }
        });
        return convertView;
    }


    private String formatTime(String time){
        String result = "";
        try {
            result = Helper.long2DateString(Long.parseLong(time) * 1000, "MM月dd日");
        }catch (Exception e){
            return "2017-10-10";
        }
        return result;
    }

    static class ViewHolder{
        ImageView imv_user_avater;
        TextView txv_problem;
        TextView txv_user_name;
        TextView txv_create_time;
        TextView txv_money;
        TextView txv_qustion_count;
        TextView btn_operate;
    }
}
