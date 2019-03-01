package com.weima.aishangyi.jiaoshi.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.adapter.PlayAdapter;

/**
 * 教师简介
 * Created by cgy on 16/7/18.
 */
public class DetailDescFragment extends Fragment {
    private ListView play_list;
    private PlayAdapter playAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frg_detail_desc, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
    }

    private void initUI(View view) {
        play_list = (ListView) view.findViewById(R.id.play_list);
        playAdapter = new PlayAdapter(getActivity());
        play_list.setAdapter(playAdapter);
    }

}
