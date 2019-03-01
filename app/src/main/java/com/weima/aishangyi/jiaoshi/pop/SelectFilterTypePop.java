package com.weima.aishangyi.jiaoshi.pop;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.adapter.ClassFirstAdapter;
import com.weima.aishangyi.jiaoshi.adapter.ClassSecondAdapter;

/**
 * Created by cgy
 */
public class SelectFilterTypePop extends PopupWindow implements View.OnClickListener {
    private View rootView;
    private Context mContext;
    private ListView listFirst;
    private GridView listSecond;

    private SelectListener selectListener;
    public interface SelectListener {
        public void onSelected(int type);
    }

    public SelectFilterTypePop(Context context, SelectListener listener) {
        super(context);
        this.mContext = context;
        this.selectListener = listener;
        rootView = View.inflate(mContext, R.layout.pop_filter_type, null);
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
        listFirst = (ListView) rootView.findViewById(R.id.listFirst);
        listSecond = (GridView) rootView.findViewById(R.id.listSecond);
    }

    private void initComponent() {
        rootView.findViewById(R.id.outView).setOnClickListener(this);
        listFirst.setAdapter(new ClassFirstAdapter(mContext, null));
        listSecond.setAdapter(new ClassSecondAdapter(mContext,null));
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
