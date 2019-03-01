package com.weima.aishangyi.jiaoshi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.weima.aishangyi.jiaoshi.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 一级分类（即左侧菜单）的adapter
 * Created by hanj on 14-9-25.
 */
public class DetailClassAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;
    private int tempIndex = 0;

    public DetailClassAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list == null?getTestData():list;
    }

    private List<String> getTestData(){
        List<String> dataList = new ArrayList<>();
        dataList.add("舞蹈");
        dataList.add("乐器");
        dataList.add("书画");
        return  dataList;
    }

    private void setTempIndex(int index){
        this.tempIndex = index;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_detail_class, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTempIndex(position);
            }
        });
        return convertView;
    }

    private class ViewHolder {
    }
}
