package com.weima.aishangyi.jiaoshi.pop;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.adapter.FilterConditionAdapter;

/**
 * Created by cgy
 */
public class SelectFilterConditionPop extends PopupWindow implements View.OnClickListener {
    private View rootView;
    private ListView listView;
    private Context mContext;
    private FilterConditionAdapter adapter;

    private SelectListener selectListener;
    public interface SelectListener {
        public void onSelected(int type);
    }

    public SelectFilterConditionPop(Context context, SelectListener listener) {
        super(context);
        this.mContext = context;
        this.selectListener = listener;
        rootView = View.inflate(mContext, R.layout.pop_filter_sort, null);
        setFocusable(true);
        setOutsideTouchable(true);
        setWidth(ViewPager.LayoutParams.MATCH_PARENT);
        setHeight(ViewPager.LayoutParams.WRAP_CONTENT);
        setContentView(rootView);
        setBackgroundDrawable(new BitmapDrawable());
        findComponent();
        initComponent();
    }

    private void findComponent() {
        listView = (ListView) rootView.findViewById(R.id.listView);
    }

    private void initComponent() {
        rootView.findViewById(R.id.outView).setOnClickListener(this);
        adapter = new FilterConditionAdapter(mContext, null);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setTempIndex(position);
                selectListener.onSelected(0);
                dismiss();
            }
        });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.outView){
            selectListener.onSelected(-1);
            dismiss();
        }
    }

    public void show(View v) {
        showAsDropDown(v);
    }
}
