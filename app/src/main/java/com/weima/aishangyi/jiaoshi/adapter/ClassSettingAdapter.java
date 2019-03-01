package com.weima.aishangyi.jiaoshi.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mb.android.utils.Helper;
import com.squareup.picasso.Picasso;
import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.activity.ConcernInfoActivity;
import com.weima.aishangyi.jiaoshi.activity.EditClassActivity;
import com.weima.aishangyi.jiaoshi.activity.PostclassActivity;
import com.weima.aishangyi.jiaoshi.constants.ProjectConstants;
import com.weima.aishangyi.jiaoshi.entity.ClassBean;
import com.weima.aishangyi.jiaoshi.entity.LunboEntity;
import com.weima.aishangyi.jiaoshi.entity.TalentBean;
import com.weima.aishangyi.jiaoshi.utils.NavigationHelper;
import com.weima.aishangyi.jiaoshi.utils.ProjectHelper;
import com.weima.aishangyi.jiaoshi.widget.CircleTransform;
import com.weima.aishangyi.jiaoshi.widget.NestListView;

import java.util.ArrayList;
import java.util.List;

public class ClassSettingAdapter extends BaseAdapter {
    private Activity activity;
    private List<ClassBean> dataList = new ArrayList<>();

    public ClassSettingAdapter(Activity act) {
        this.activity = act;
    }

    public void addMore(List<ClassBean> list){
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
    public String getItem(int position) {
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
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_classsetting, null);
            holder.typeItem = (LinearLayout) convertView.findViewById(R.id.typeItem);
            holder.imv_icon = (ImageView) convertView.findViewById(R.id.imv_icon);
            holder.txv_title = (TextView) convertView.findViewById(R.id.txv_title);
            holder.txv_content = (TextView) convertView.findViewById(R.id.txv_content);
            holder.listView = (NestListView) convertView.findViewById(R.id.listView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ClassBean entity = dataList.get(position);
        final ClassBean.ItemBean item = entity.getItem();
        if (Helper.isNotEmpty(item)){
            if (Helper.isNotEmpty(item.getIcon())){
                Picasso.with(activity).load(item.getIcon()).placeholder(R.drawable.img_default).into(holder.imv_icon);
            }
            holder.txv_title.setText(ProjectHelper.getCommonText(item.getName()));
            int count = item.getLesson()==null?0:item.getLesson().size();
            holder.txv_content.setText("共" + count+"门自定义课程");
            if (Helper.isNotEmpty(item.getLesson())){
                holder.listView.setAdapter(new ClassSetting2Adapter(activity,item.getLesson()));
            }
        }
        holder.typeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item!=null){
                    Bundle bundle = new Bundle();
                    bundle.putLong(ProjectConstants.BundleExtra.KEY_CLASS_TYPE_ID,item.getId());
                    bundle.putString(ProjectConstants.BundleExtra.KEY_CLASS_TYPE_NAME,item.getName());
                    NavigationHelper.startActivity(activity, PostclassActivity.class, bundle, false);
                }
            }
        });
        return convertView;
    }

    static class ViewHolder {
        LinearLayout typeItem;
        ImageView imv_icon;
        TextView txv_title;
        TextView txv_content;
        NestListView listView;
    }
}
