package com.weima.aishangyi.jiaoshi.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;

import com.mb.android.utils.Helper;
import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.adapter.DetailClassAdapter;
import com.weima.aishangyi.jiaoshi.base.BaseActivity;
import com.weima.aishangyi.jiaoshi.fragment.DetailCommentFragment;
import com.weima.aishangyi.jiaoshi.fragment.DetailDescFragment;
import com.weima.aishangyi.jiaoshi.tabstrip.PagerSlidingTabStrip;
import com.weima.aishangyi.jiaoshi.utils.NavigationHelper;
import com.weima.aishangyi.jiaoshi.widget.NestListView;

/**
 * 作者：cgy on 16/11/28 22:02
 * 邮箱：593960111@qq.com
 */
public class TeacherDetailAcitity extends BaseActivity implements View.OnClickListener {
    private NestListView listClass;
    private PagerSlidingTabStrip tabstrip;
    private ViewPager viewpager;
    private RelativeLayout rel_actionbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_detail);
        setCustomTitle("详情");
        setImageRightButton(R.drawable.ic_share, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        setImageRightButton2(R.drawable.ic_collect, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        initUI();
    }

    private void initUI() {
        rel_actionbar = (RelativeLayout) findViewById(R.id.rel_actionbar);
        listClass = findView(R.id.listClass);

        listClass.setAdapter(new DetailClassAdapter(TeacherDetailAcitity.this, null));

        tabstrip = (PagerSlidingTabStrip) findViewById(R.id.tabstrip);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        viewpager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        tabstrip.setViewPager(viewpager);
        viewpager.setCurrentItem(0); //初始化显示0
        tabstrip.setTextColor(getResources().getColor(R.color.text_color));//未选中字体的颜色
        tabstrip.setSelectedTextColor(getResources().getColor(R.color.base_orange));//选中选项中字体的颜色
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);//获取屏幕宽度
        int width = dm.widthPixels;//宽度
        tabstrip.setTextSize(width / 28);//字体的大小
    }

    public class MyAdapter extends FragmentStatePagerAdapter {
        private final String[] titles = {"简介", "评价"};


        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new DetailDescFragment();
            } else {
                return new DetailCommentFragment();
            }
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
    }
}
