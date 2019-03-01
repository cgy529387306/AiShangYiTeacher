package com.weima.aishangyi.jiaoshi.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mb.android.utils.Helper;
import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.entity.TimeBean;
import com.weima.aishangyi.jiaoshi.pop.EditTimePop;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/13 0013.
 */
public class TimeableAdapter extends BaseAdapter{
    private Activity activity;
    private List<TimeBean> selectTime = new ArrayList<>();
    public TimeableAdapter(Activity act) {
        this.activity = act;
    }

    public void addList(List<TimeBean> list) {
        if (Helper.isNotEmpty(list)){
            selectTime.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void clearList() {
        selectTime.clear();
        notifyDataSetChanged();
    }

    public List<TimeBean> getSelectTime(){
        return this.selectTime;
    }

    @Override
    public int getCount() {
        return selectTime.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (true) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_timeable, null);
            holder.txv_time = (TextView) convertView.findViewById(R.id.txv_time);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        final TimeBean timeBean = selectTime.get(position);
        if (Helper.isNotEmpty(timeBean.getStart_time()) && Helper.isNotEmpty(timeBean.getEnd_time()) ){
            holder.txv_time.setText(timeBean.getStart_time()+"-"+timeBean.getEnd_time());
        }else{
            holder.txv_time.setText("");
        }
        holder.txv_time.setBackgroundColor(timeBean.getStatus()==1?activity.getResources().getColor(R.color.white)
                :activity.getResources().getColor(R.color.base_gray_light));
        final ViewHolder finalHolder = holder;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditTimePop pop = new EditTimePop(activity, new EditTimePop.SelectListener() {
                    @Override
                    public void onSelected(TimeBean time) {
                        if (Helper.isNotEmpty(time)){
                            selectTime.set(position,time);
                            finalHolder.txv_time.setText(time.getStart_time()+"-"+time.getEnd_time());
                            finalHolder.txv_time.setBackgroundColor(timeBean.getStatus()==1?activity.getResources().getColor(R.color.white)
                                    :activity.getResources().getColor(R.color.base_gray_light));
                        }
                    }
                },timeBean);
                pop.show(view);
            }
        });
        return convertView;
    }

    static class ViewHolder{
        TextView txv_time;
    }

}
