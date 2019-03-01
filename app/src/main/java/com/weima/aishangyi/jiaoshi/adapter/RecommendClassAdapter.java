package com.weima.aishangyi.jiaoshi.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.entity.LunboEntity;

import java.util.ArrayList;
import java.util.List;

public class RecommendClassAdapter extends PagerAdapter {
	private Activity activity;
	private List<LunboEntity> dataList;
	public RecommendClassAdapter(Activity act, List<LunboEntity> list) {
		this.activity = act;
		this.dataList = list == null?getTestData():list;
	}

	private List<LunboEntity> getTestData() {
		List<LunboEntity> list = new ArrayList<>();
		LunboEntity entity1 = new LunboEntity();
		entity1.setImageUrl("http://c.hiphotos.baidu.com/image/pic/item/d009b3de9c82d1585e277e5f840a19d8bd3e42b2.jpg");
		list.add(entity1);

		LunboEntity entity2 = new LunboEntity();
		entity2.setImageUrl("http://d.hiphotos.baidu.com/image/pic/item/b3fb43166d224f4a034954fd0df790529922d10f.jpg");
		list.add(entity2);

		LunboEntity entity3 = new LunboEntity();
		entity3.setImageUrl("http://c.hiphotos.baidu.com/image/pic/item/54fbb2fb43166d226ded327a422309f79152d241.jpg");
		list.add(entity3);

		LunboEntity entity4 = new LunboEntity();
		entity4.setImageUrl("http://g.hiphotos.baidu.com/image/pic/item/8b82b9014a90f6039dd1baa93d12b31bb151edff.jpg");
		list.add(entity4);

		LunboEntity entity5 = new LunboEntity();
		entity5.setImageUrl("http://g.hiphotos.baidu.com/image/pic/item/8b82b9014a90f6039dd1baa93d12b31bb151edff.jpg");
		list.add(entity5);
		return list;
	}

	@Override
	public int getCount() {
		return dataList == null ? 0 : dataList.size();
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		View view = LayoutInflater.from(activity).inflate(R.layout.item_recomment_class, null);
		ImageView imv_image = (ImageView) view.findViewById(R.id.imv_image);
		if (dataList.get(position)!=null){
			Picasso.with(activity).load(dataList.get(position).getImageUrl()).placeholder(R.drawable.img_default).into(imv_image);
		}
		((ViewPager) container).addView(view);
		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}


}
