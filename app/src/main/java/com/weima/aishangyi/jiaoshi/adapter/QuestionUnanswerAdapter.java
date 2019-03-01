package com.weima.aishangyi.jiaoshi.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mb.android.utils.Helper;
import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.entity.LunboEntity;

import java.util.ArrayList;
import java.util.List;

public class QuestionUnanswerAdapter extends BaseAdapter {
    private Activity activity;
    private List<LunboEntity> dataList = new ArrayList<LunboEntity>();

    public QuestionUnanswerAdapter(Activity act, List<LunboEntity> list) {
        this.activity = act;
        this.dataList = list;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public LunboEntity getItem(int position) {
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
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_question_unanswer, null);
            holder.txv_content = (TextView) convertView.findViewById(R.id.txv_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        LunboEntity entity = getItem(position);
        holder.txv_content.setText(entity.getTitle());
//		Picasso.with(activity).load(entity.getImageUrl()).placeholder(R.drawable.img_default).into(holder.imv_icon);
        return convertView;
    }

    static class ViewHolder {
        TextView txv_content;
    }
}
