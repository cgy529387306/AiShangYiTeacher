package com.weima.aishangyi.jiaoshi.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mb.android.utils.view.LoadingView;
import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.adapter.CouponsAdapter;
import com.weima.aishangyi.jiaoshi.xlistview.XListView;

/**
 * 我的优惠券
 */
public class CouponsFragment extends Fragment implements XListView.IXListViewListener {
    private XListView xListView;
    private LinearLayout emptyView;
    private LoadingView loadingView;
    private CouponsAdapter adapter;
    private int currentPage = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.common_listview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
    }

    private void initUI(View view) {
        xListView = (XListView) view.findViewById(R.id.xListView);
        xListView.setPullLoadEnable(true);
        xListView.setPullRefreshEnable(true);
        xListView.setXListViewListener(this);
        adapter = new CouponsAdapter(getActivity(), null);
        xListView.setAdapter(adapter);
        emptyView = (LinearLayout) view.findViewById(R.id.emptyview);
        loadingView = (LoadingView) view.findViewById(R.id.loadingView);

    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finshLoad();
            }
        }, 1000);
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
        }, 1000);
    }

    private void finshLoad() {
        xListView.stopRefresh();
        xListView.stopLoadMore();
    }
}
