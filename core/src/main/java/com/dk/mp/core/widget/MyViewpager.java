package com.dk.mp.core.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 自定义支持是否滑动的 Viewpager
 * 作者：janabo on 2017/1/4 11:18
 */
public class MyViewpager extends ViewPager {
    private static final String TAG = "MyViewPager";
    private boolean isEnableScroll = false;

    public MyViewpager ( Context context, boolean isEnableScroll ) {
        super ( context );
        this.isEnableScroll = isEnableScroll;
    }

    public MyViewpager ( Context context, AttributeSet attrs ) {
        super ( context, attrs );
    }

    public void setScrollEnabled ( boolean isEnableScroll ) {
        this.isEnableScroll=isEnableScroll;
    }

    @Override
    public boolean onInterceptTouchEvent ( MotionEvent arg0 ) {
        if ( isEnableScroll ) {
            return super.onInterceptTouchEvent ( arg0 );
        } else
            return false;
    }

    @Override
    public boolean onTouchEvent ( MotionEvent arg0 ) {
        if ( isEnableScroll ) {
            return super.onTouchEvent ( arg0 );
        } else
            return false;
    }
}
