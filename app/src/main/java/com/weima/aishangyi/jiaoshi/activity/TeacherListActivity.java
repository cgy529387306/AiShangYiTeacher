package com.weima.aishangyi.jiaoshi.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.adapter.RecommendTeacherAdapter;
import com.weima.aishangyi.jiaoshi.base.BaseActivity;
import com.weima.aishangyi.jiaoshi.pop.SelectFilterConditionPop;
import com.weima.aishangyi.jiaoshi.pop.SelectFilterSortPop;
import com.weima.aishangyi.jiaoshi.pop.SelectFilterTypePop;
import com.weima.aishangyi.jiaoshi.utils.NavigationHelper;
import com.weima.aishangyi.jiaoshi.xlistview.XListView;

/**
 * 作者：cgy on 16/11/26 15:21
 * 邮箱：593960111@qq.com
 * 老师列表
 */
public class TeacherListActivity extends BaseActivity implements View.OnClickListener, XListView.IXListViewListener {
    private LinearLayout lin_tab;
    private SelectFilterTypePop selectFilterTypePop;
    private SelectFilterSortPop selectFilterSortPop;
    private SelectFilterConditionPop selectFilterConditionPop;
    private TextView btn_filter_type, btn_filter_sort, btn_filter_condition;
    private XListView xListView;
    private RecommendTeacherAdapter adapter;
    private int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list);
        setCustomTitle("老师列表");
        setImageRightButton(R.drawable.ic_search_white, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationHelper.startActivity(TeacherListActivity.this, HomeSearchActivity.class, null, false);
            }
        });
        initUI();
    }

    private void initUI() {
        xListView = findView(R.id.xListView);
        xListView.setPullLoadEnable(true);
        xListView.setPullRefreshEnable(true);
        xListView.setXListViewListener(this);
        adapter = new RecommendTeacherAdapter(TeacherListActivity.this, null);
        xListView.setAdapter(adapter);
        selectFilterTypePop = new SelectFilterTypePop(this, new SelectFilterTypePop.SelectListener() {
            @Override
            public void onSelected(int type) {
                setTabType(-1);
            }
        });
        selectFilterSortPop = new SelectFilterSortPop(this, new SelectFilterSortPop.SelectListener() {
            @Override
            public void onSelected(int type) {
                setTabType(-1);
            }
        });
        selectFilterConditionPop = new SelectFilterConditionPop(this, new SelectFilterConditionPop.SelectListener() {
            @Override
            public void onSelected(int type) {
                setTabType(-1);
            }
        });

        lin_tab = findView(R.id.lin_tab);
        btn_filter_type = findView(R.id.btn_filter_type);
        btn_filter_sort = findView(R.id.btn_filter_sort);
        btn_filter_condition = findView(R.id.btn_filter_condition);
        btn_filter_type.setOnClickListener(this);
        btn_filter_sort.setOnClickListener(this);
        btn_filter_condition.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_filter_type) {
            setTabType(0);
        } else if (id == R.id.btn_filter_sort) {
            setTabType(1);
        } else if (id == R.id.btn_filter_condition) {
            setTabType(2);
        }
    }

    private void setTabType(int type) {
        btn_filter_type.setSelected(type == 0 && !btn_filter_type.isSelected());
        btn_filter_sort.setSelected(type == 1 && !btn_filter_sort.isSelected());
        btn_filter_condition.setSelected(type == 2 && !btn_filter_condition.isSelected());
        if (btn_filter_type.isSelected()) {
            selectFilterTypePop.show(lin_tab);
        } else {
            selectFilterTypePop.dismiss();
        }
        if (btn_filter_sort.isSelected()) {
            selectFilterSortPop.show(lin_tab);
        } else {
            selectFilterSortPop.dismiss();
        }
        if (btn_filter_condition.isSelected()) {
            selectFilterConditionPop.show(lin_tab);
        } else {
            selectFilterConditionPop.dismiss();
        }
    }


    @Override
    public void onRefresh() {
        currentPage = 1;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finshLoad();
            }
        },1000);
    }

    @Override
    public void onLoadMore() {
        currentPage++;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.addMore();
                finshLoad();
            }
        },1000);
    }

    private void finshLoad() {
        xListView.stopRefresh();
        xListView.stopLoadMore();
    }
}
