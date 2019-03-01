package com.weima.aishangyi.jiaoshi.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mb.android.utils.Helper;
import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.activity.TeacherDetailAcitity;
import com.weima.aishangyi.jiaoshi.entity.LunboEntity;
import com.weima.aishangyi.jiaoshi.utils.NavigationHelper;

import java.util.ArrayList;
import java.util.List;

public class RecommendTeacherAdapter extends BaseAdapter{
	private Activity activity;
	private List<String> dataList;
	public RecommendTeacherAdapter(Activity act, List<String> list) {
		this.activity = act;
		this.dataList = list == null?getTestData():list;
	}

	private List<String> getTestData(){
		List<String> dataList = new ArrayList<>();
		for (int i=0;i<20;i++){
			dataList.add("test");
		}
		return  dataList;
	}

	public void addMore(){
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
			convertView = LayoutInflater.from(activity).inflate(R.layout.item_recomment_teacher, null);
			holder.star = (RatingBar) convertView.findViewById(R.id.star);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//TODO
				NavigationHelper.startActivity(activity, TeacherDetailAcitity.class,null,false);
			}
		});
		return convertView;
	}
	
	static class ViewHolder{
		TextView txv_title;
		ImageView imv_icon;
		RatingBar star;
	}
}
