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
import com.weima.aishangyi.jiaoshi.activity.ConcernInfoActivity;
import com.weima.aishangyi.jiaoshi.constants.ProjectConstants;
import com.weima.aishangyi.jiaoshi.entity.FollowBean;
import com.weima.aishangyi.jiaoshi.entity.User;
import com.weima.aishangyi.jiaoshi.utils.NavigationHelper;

import java.util.ArrayList;
import java.util.List;

public class VisitorAdapter extends BaseAdapter {
    private Activity activity;
    private List<FollowBean> dataList = new ArrayList<FollowBean>();

    public VisitorAdapter(Activity act) {
        this.activity = act;
    }

    public void addMore(List<FollowBean> list){
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
        return dataList.size();
    }

    @Override
    public FollowBean getItem(int position) {
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
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_visitor, null);
            holder.imv_user_avater = (ImageView) convertView.findViewById(R.id.imv_user_avater);
            holder.txv_user_name = (TextView) convertView.findViewById(R.id.txv_user_name);
            holder.txv_time = (TextView) convertView.findViewById(R.id.txv_time);
            holder.txv_count = (TextView) convertView.findViewById(R.id.txv_count);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        FollowBean followBean = getItem(position);
        final User entity = followBean.getUser();
        if (Helper.isNotEmpty(entity)){
            convertView.setVisibility(View.VISIBLE);
            if (Helper.isNotEmpty(entity.getIcon())){
                Picasso.with(activity).load(entity.getIcon()).placeholder(R.drawable.ic_avatar_default).into(holder.imv_user_avater);
            }
            if (Helper.isNotEmpty(entity.getNickname())){
                holder.txv_user_name.setText(entity.getNickname());
            }else{
                holder.txv_user_name.setText(entity.getPhone());
            }
            holder.txv_time.setText(Helper.long2DateString(Long.parseLong(entity.getCreated_at())*1000,"yyyy-MM-dd HH:mm"));
            holder.txv_count.setText("访问"+followBean.getAccess_num()+"次");
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ProjectConstants.BundleExtra.KEY_USER, entity);
                    NavigationHelper.startActivity(activity, ConcernInfoActivity.class, bundle, false);
                }
            });
        }
        return convertView;
    }




    static class ViewHolder {
        ImageView imv_user_avater;
        TextView txv_user_name;
        TextView txv_time;
        TextView txv_count;
    }
}
