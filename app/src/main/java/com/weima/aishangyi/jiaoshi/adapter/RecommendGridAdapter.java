package com.weima.aishangyi.jiaoshi.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mb.android.utils.Helper;
import com.squareup.picasso.Picasso;
import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.activity.TeacherListActivity;
import com.weima.aishangyi.jiaoshi.entity.LunboEntity;
import com.weima.aishangyi.jiaoshi.utils.NavigationHelper;
import com.weima.aishangyi.jiaoshi.widget.CircleTransform;

public class RecommendGridAdapter extends BaseAdapter{
	private Activity activity;
	private List<LunboEntity> dataList = new ArrayList<LunboEntity>();
	public RecommendGridAdapter(Activity act,List<LunboEntity> list) {
		this.activity = act;
		this.dataList = list == null?getTestData():list;
	}

	private List<LunboEntity> getTestData() {
		List<LunboEntity> list = new ArrayList<>();
		LunboEntity entity1 = new LunboEntity();
		entity1.setTitle("舞蹈");
		entity1.setImageUrl("http://c.hiphotos.baidu.com/image/pic/item/d009b3de9c82d1585e277e5f840a19d8bd3e42b2.jpg");
		list.add(entity1);

		LunboEntity entity2 = new LunboEntity();
		entity2.setTitle("乐器");
		entity2.setImageUrl("http://d.hiphotos.baidu.com/image/pic/item/b3fb43166d224f4a034954fd0df790529922d10f.jpg");
		list.add(entity2);

		LunboEntity entity3 = new LunboEntity();
		entity3.setTitle("书画");
		entity3.setImageUrl("http://c.hiphotos.baidu.com/image/pic/item/54fbb2fb43166d226ded327a422309f79152d241.jpg");
		list.add(entity3);

		LunboEntity entity4 = new LunboEntity();
		entity4.setTitle("表演");
		entity4.setImageUrl("http://g.hiphotos.baidu.com/image/pic/item/8b82b9014a90f6039dd1baa93d12b31bb151edff.jpg");
		list.add(entity4);

		LunboEntity entity5 = new LunboEntity();
		entity5.setTitle("音乐理论");
		entity5.setImageUrl("http://g.hiphotos.baidu.com/image/pic/item/8b82b9014a90f6039dd1baa93d12b31bb151edff.jpg");
		list.add(entity5);

		LunboEntity entity6 = new LunboEntity();
		entity6.setTitle("语言");
		entity6.setImageUrl("http://tupian.enterdesk.com/2015/lcx/1/21/7/4.jpg");
		list.add(entity6);

		LunboEntity entity7 = new LunboEntity();
		entity7.setTitle("租课室");
		entity7.setImageUrl("http://www.bz55.com/uploads/allimg/150704/139-150F4100Q6.jpg");
		list.add(entity7);
		return list;
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
			convertView = LayoutInflater.from(activity).inflate(R.layout.item_recomment_grid, null);
			holder.txv_title = (TextView) convertView.findViewById(R.id.txv_title);
			holder.imv_icon = (ImageView) convertView.findViewById(R.id.imv_icon);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		LunboEntity entity = dataList.get(position);
		holder.txv_title.setText(entity.getTitle());
		Picasso.with(activity).load(entity.getImageUrl()).placeholder(R.drawable.ic_avatar_default).into(holder.imv_icon);
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//TODO
				NavigationHelper.startActivity(activity, TeacherListActivity.class, null, false);
			}
		});
		return convertView;
	}
	
	static class ViewHolder{
		TextView txv_title;
		ImageView imv_icon;
	}
}
