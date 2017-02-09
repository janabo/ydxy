package com.dk.mp.schedule.view;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.dk.mp.schedule.R;

import java.util.Calendar;

/**
 * 月历视图切换器
 * 作者：janabo on 2016/12/29 10:02
 */
public class MonthViewSwitcher extends FrameLayout {
    @SuppressWarnings("unused")
    private static final String TAG = "MonthViewSwitcher";

    private MonthView[] monthView = new MonthView[2];
    private int currentPosition = 0;
    // 下一个视图是下一月 1 -- 上一月 -1 --- 都不是 0
    private int nextOrLastMonth = 0;
    public MonthViewSwitcher(Context context) {
        super(context);
        monthView[1] = new MonthView(context);
        monthView[0] = new MonthView(context);
        addView(monthView[1], new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(monthView[0], new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    float getCurrentCellHeight(){
        return monthView[currentPosition].getCellHeight();
    }

    int getSelectRow(){
        if(monthView[currentPosition].selectCell == -1){
            return -1;
        }

        int cellIndex = monthView[currentPosition].selectCell + monthView[currentPosition].deltaDay;
        return cellIndex / 7;
    }

    void changeCurrentPosition(){
        currentPosition = currentPosition == 0 ? 1: 0;
        switch (nextOrLastMonth){
            case 1:
                nextOrLastMonth = -1;
                break;
            case 0:
                break;
            case -1:
                nextOrLastMonth = 1;
                break;
            default:
                Log.e(TAG, "nextOrLastMonth status is wrong, and nextOrLastMonth is " + nextOrLastMonth);
        }
        monthView[currentPosition].callListener();
    }
    public void setOnCellSelectListener(MonthView.OnCellSelectListener listener){
        getNextView().setOnCellSelectListener(listener);
        monthView[currentPosition].setOnCellSelectListener(listener);
    }
    public MonthView getNextView(){
        int inPosition = currentPosition == 0 ? 1 : 0;
        return monthView[inPosition];
    }

    public void nextMonth(){
        if(nextOrLastMonth == 1) return;
        Calendar calendar = (Calendar) monthView[currentPosition].getFirstDayCalendar().clone();
        calendar.add(Calendar.MONTH, 1);
        getNextView().setCalendar(calendar);
        nextOrLastMonth = 1;
    }

    public void lastMonth(){
        if(nextOrLastMonth == -1) return;
        Calendar calendar = (Calendar)monthView[currentPosition].getFirstDayCalendar().clone();
        calendar.add(Calendar.MONTH, -1);
        getNextView().setCalendar(calendar);
        nextOrLastMonth = -1;
    }

    // 是否是当前显示视图
    boolean isCurrentView(MonthView month){
        if(month == monthView[currentPosition]){
            return true;
        }
        return false;
    }

    private void anotherMonthViewSetCalendar(Calendar calendar){
        getNextView().setCalendar(calendar);
    }

    public void setOutAnimation(Animation animation){
        Animation tmpAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.left_out_animation);
        monthView[currentPosition].outAnimationStart(tmpAnimation);
    }

    public void setInAnimation(Animation animation){
        getNextView().inAnimationStart(AnimationUtils.loadAnimation(getContext(), R.anim.right_in_animation));

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(heightMeasureSpec);
//        Log.v(TAG, "width is " + width);
        int fakeHeightMeasureSpec = MeasureSpec.makeMeasureSpec(600, widthMode);
        super.onMeasure(widthMeasureSpec, fakeHeightMeasureSpec);
    }
}
