package com.weima.aishangyi.jiaoshi.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mb.android.utils.Helper;
import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.entity.LunboEntity;

import java.util.ArrayList;
import java.util.List;

public class CouponsAdapter extends BaseAdapter {
    private Activity activity;
    private List<String> dataList;

    public CouponsAdapter(Activity act, List<String> list) {
        this.activity = act;
        this.dataList = list == null ? getTestData() : list;
    }

    private List<String> getTestData() {
        List<String> dataList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            dataList.add("test");
        }
        return dataList;
    }

    public void addMore() {
        dataList.addAll(getTestData());
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public LunboEntity getItem(int position) {
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
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_coupuns, null);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    static class ViewHolder {
    }
}
