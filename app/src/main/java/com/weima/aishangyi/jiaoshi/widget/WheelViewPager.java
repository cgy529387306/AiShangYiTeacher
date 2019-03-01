package com.weima.aishangyi.jiaoshi.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.weima.aishangyi.jiaoshi.utils.ProjectHelper;


/**
 * 适配列表冲突问题
 *
 */
public class WheelViewPager extends ViewPager {

	protected static final String TAG = WheelViewPager.class.getSimpleName();
	
	private Float mDownX;
	private Float mDownY;
	
	public WheelViewPager(Context context) {
		super(context);
	}
	
	public WheelViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
    	switch(ev.getAction()) {
    	case MotionEvent.ACTION_DOWN:
    		mDownX = ev.getX();
    		mDownY = ev.getY();
    		ProjectHelper.changeParentDisallowInterceptState(getParent(), false);
    		break;
    	case MotionEvent.ACTION_MOVE:
    		if(Math.abs(ev.getX() - mDownX) > Math.abs(ev.getY() - mDownY)) {
    			ProjectHelper.changeParentDisallowInterceptState(getParent(), true);
    		}
    		break;
    	case MotionEvent.ACTION_UP:
    	case MotionEvent.ACTION_CANCEL:
    		ProjectHelper.changeParentDisallowInterceptState(getParent(), false);
    		break;
    	}
        return super.dispatchTouchEvent(ev);
    }
}
