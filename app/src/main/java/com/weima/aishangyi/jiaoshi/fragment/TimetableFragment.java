package com.weima.aishangyi.jiaoshi.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mb.android.utils.view.LoadingView;
import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.xlistview.XListView;

/**
 * 联系人
 */
public class TimetableFragment extends Fragment implements XListView.IXListViewListener {
    private XListView xListView;
    private LinearLayout emptyView;
    private LoadingView loadingView;

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
        emptyView = (LinearLayout) view.findViewById(R.id.emptyview);
        loadingView = (LoadingView) view.findViewById(R.id.loadingView);

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
