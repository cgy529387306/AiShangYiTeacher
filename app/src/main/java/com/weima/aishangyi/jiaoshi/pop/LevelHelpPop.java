package com.weima.aishangyi.jiaoshi.pop;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.adapter.ShareAdapter;

/**
 * Created by cgy
 */
public class LevelHelpPop extends PopupWindow implements View.OnClickListener {
    private View rootView;
    private Context mContext;
    private ShareAdapter adapter;

    private SelectListener selectListener;

    public interface SelectListener {
        public void onSelected(int type);
    }

    public LevelHelpPop(Context context, SelectListener listener) {
        super(context);
        this.mContext = context;
        this.selectListener = listener;
        rootView = View.inflate(mContext, R.layout.pop_qqservice, null);
        setFocusable(true);
        setOutsideTouchable(true);
        setWidth(ViewPager.LayoutParams.MATCH_PARENT);
        setHeight(ViewPager.LayoutParams.MATCH_PARENT);
        setContentView(rootView);
        setBackgroundDrawable(new BitmapDrawable());
        findComponent();
        initComponent();
    }

    private void findComponent() {

//        grd_share = (GridView) rootView.findViewById(R.id.grd_share);
    }

    private void initComponent() {
        rootView.findViewById(R.id.btn_close).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_close) {
            dismiss();
        }
    }

    public void show(View v) {
        showAtLocation(v, Gravity.CENTER, 0, 0);
    }
}
