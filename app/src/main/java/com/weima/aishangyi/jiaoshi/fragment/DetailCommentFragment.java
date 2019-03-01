package com.weima.aishangyi.jiaoshi.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.adapter.TalentCircleAdapter;
import com.weima.aishangyi.jiaoshi.xlistview.XListView;

/**
 * 教师简介
 * Created by cgy on 16/7/18.
 */
public class DetailCommentFragment extends Fragment implements XListView.IXListViewListener{

    private XListView xListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frg_detail_comment, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        initData();
    }

    private void initUI(View view) {
        xListView = (XListView) view.findViewById(R.id.xListView);
        xListView.setPullLoadEnable(true);
        xListView.setPullRefreshEnable(true);
        xListView.setXListViewListener(this);
        xListView.addHeaderView(getHeaderView());
        xListView.setAdapter(new TalentCircleAdapter(getActivity()));
    }

    private View getHeaderView(){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.header_comment, null);
        return view;
    }


    private void initData() {
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    /**
     * 点击事件
     */
    private View.OnClickListener mClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                default:
                    break;
            }
        }
    };
}
