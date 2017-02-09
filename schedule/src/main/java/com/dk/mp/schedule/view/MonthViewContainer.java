package com.dk.mp.schedule.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

/**
 * 月历视图的容器
 * 作者：janabo on 2016/12/29 09:55
 */
public class MonthViewContainer extends RelativeLayout {
    @SuppressWarnings("unused")
    private final static String TAG = "MonthViewContainer";

    private MonthViewSwitcher monthViewSwitcher;
    private float touchSlop;
    private MainLayoutContainer mainLayoutContainer;
    public MonthViewContainer(Context context) {
        super(context);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        monthViewSwitcher = new MonthViewSwitcher(context);
        RelativeLayout.LayoutParams monthViewParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(monthViewSwitcher, monthViewParams);
        setGravity(Gravity.BOTTOM);
        setBackgroundColor(Color.WHITE);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mainLayoutContainer = (MainLayoutContainer) getParent();
    }

    public void setOnCellSelectListener(MonthView.OnCellSelectListener listener){
        monthViewSwitcher.setOnCellSelectListener(listener);
    }

    private float touchLastY, touchLastX, firstTouchLastY;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        int action = ev.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                touchLastX = ev.getX();
                touchLastY = ev.getY();
                firstTouchLastY = touchLastY;
                return false;
            case MotionEvent.ACTION_MOVE:
                float dx = ev.getX() - touchLastX;
                float dy = ev.getY() - touchLastY;
                if(Math.abs(dx) < Math.abs(dy) && Math.abs(dy) > touchSlop){
                    Log.v(TAG, "onInterceptTouchEvent, and return true");
                    return true;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                Log.v(TAG, "pointer up");
                break;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "onTouchEvent, action down");
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = event.getY() - touchLastY;
                onMove((int)dy);
                touchLastY = event.getY() + (int)dy - dy;
                return true;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                Log.v(TAG, "onTouchEvent pointer up");
                onPointerUpAnimation(event.getY() - firstTouchLastY);
                return true;
        }
        return super.onTouchEvent(event);
    }

    private int fullHeight = -1;
    int getFullHeight(){
        if(fullHeight == -1){
            fullHeight = 600;
        }
        return fullHeight;
    }

    float getCellHeight(){
        return monthViewSwitcher.getCurrentCellHeight();
    }


    boolean isInAnimation = false;
    void onPointerUpAnimation(float dy){
        ValueAnimator valueAnimator;
        if(dy >= 0){
            valueAnimator = ValueAnimator.ofFloat(getMeasuredHeight(), getFullHeight() + 1);
        }else{
            valueAnimator = ValueAnimator.ofFloat(getMeasuredHeight(), getCellHeight());
        }

        valueAnimator.setDuration(500);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            float measureHeight = getMeasuredHeight();

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (float) animation.getAnimatedValue();
//                Log.v(TAG, "onAnimationUpdate, and currentValue is " + currentValue);
                float tmpDy = currentValue - measureHeight;
                onMove((int) tmpDy);
                measureHeight = currentValue + (int) tmpDy - tmpDy;
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isInAnimation = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isInAnimation = false;
                Log.v(TAG, "onAnimationEnd");
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();
    }

    int status = MainLayoutContainer.MONTHVIEW_MAX;
    void onMove(int dy) {
        ViewGroup.LayoutParams params = getLayoutParams();
        int height = getMeasuredHeight();
        int resultHeight = height + dy;
        dealWithWeekView(height, resultHeight);
        status = MainLayoutContainer.MONTHVIEW_MIDDLE;
        if(resultHeight <= getCellHeight()){
            status = MainLayoutContainer.MONTHVIEW_MIN;
            if(height <= getCellHeight()) return;
            resultHeight = (int) getCellHeight();
        }else if(resultHeight >= getFullHeight()){
            status = MainLayoutContainer.MONTHVIEW_MAX;
            if(height > getFullHeight()) return;
            resultHeight = getFullHeight();
        }
        params.height = resultHeight;
        setLayoutParams(params);
        if(onMonthViewHeightChangedListener != null)
            onMonthViewHeightChangedListener.onMonthViewHeightChanged(this, resultHeight);
        postInvalidate();
    }

    private void dealWithWeekView(int height, int resultHeight) {
        int keyHeight = (int) (getFullHeight() - getCellHeight() * monthViewSwitcher.getSelectRow());
        if(resultHeight <= keyHeight){
            if(height < keyHeight) return;
            mainLayoutContainer.setWeekViewVisibility(VISIBLE);
            Log.v(TAG, "dealWithWeekView, and visible");
        }else{
            if(height > keyHeight) return;
            mainLayoutContainer.setWeekViewVisibility(GONE);
            Log.v(TAG, "dealWithWeekView, and gone");
        }
    }

    interface OnMonthViewHeightChangedListener{
        void onMonthViewHeightChanged(MonthViewContainer monthViewContainer, int height);
    }
    OnMonthViewHeightChangedListener onMonthViewHeightChangedListener;
}
