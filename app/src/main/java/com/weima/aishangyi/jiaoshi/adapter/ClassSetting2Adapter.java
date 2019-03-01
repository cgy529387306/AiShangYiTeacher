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
import com.mb.android.utils.ToastHelper;
import com.squareup.picasso.Picasso;
import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.activity.EditClassActivity;
import com.weima.aishangyi.jiaoshi.constants.ProjectConstants;
import com.weima.aishangyi.jiaoshi.entity.LessonBean;
import com.weima.aishangyi.jiaoshi.utils.NavigationHelper;
import com.weima.aishangyi.jiaoshi.utils.ProjectHelper;
import com.weima.aishangyi.jiaoshi.widget.NestListView;

import java.util.ArrayList;
import java.util.List;

public class ClassSetting2Adapter extends BaseAdapter {
    private Activity activity;
    private List<LessonBean> dataList = new ArrayList<>();

    public ClassSetting2Adapter(Activity act,List<LessonBean> list) {
        this.activity = act;
        this.dataList = list;
    }

    public void addMore(List<LessonBean> list){
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
    public LessonBean getItem(int position) {
        return dataList.get(position);
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
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_classsetting2, null);
            holder.imv_icon = (ImageView) convertView.findViewById(R.id.imv_icon);
            holder.txv_title = (TextView) convertView.findViewById(R.id.txv_title);
            holder.txv_content = (TextView) convertView.findViewById(R.id.txv_content);
            holder.txv_number = (TextView) convertView.findViewById(R.id.txv_number);
            holder.txv_status = (TextView) convertView.findViewById(R.id.txv_status);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final LessonBean entity = dataList.get(position);
        if (Helper.isNotEmpty(entity.getIcon())){
            Picasso.with(activity).load(entity.getIcon()).into(holder.imv_icon);
        }
        holder.txv_title.setText(ProjectHelper.getCommonText(entity.getName()));
        holder.txv_content.setText(ProjectHelper.getCommonText(entity.getLesson_brief()));
        holder.txv_number.setText(entity.getNumber()+"课时");
        if (entity.getStatus()==0){
            holder.txv_status.setText("待审核");
        }else if (entity.getStatus()==1){
            holder.txv_status.setText("审核通过");
        }else if (entity.getStatus()==2){
            holder.txv_status.setText("审核不通过");
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (entity.getStatus()==0){
                    ToastHelper.showToast("课程正在审核中...");
                }else{
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ProjectConstants.BundleExtra.KEY_CLASS,entity);
                    NavigationHelper.startActivity(activity, EditClassActivity.class, bundle, false);
                }
            }
        });
        return convertView;
    }

    static class ViewHolder {
        ImageView imv_icon;
        TextView txv_title;
        TextView txv_content;
        TextView txv_number;
        TextView txv_status;
    }
}
