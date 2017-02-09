package com.dk.mp.schedule.view;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.dk.mp.schedule.Adapter.RcapAdapter;
import com.dk.mp.schedule.R;
import com.dk.mp.schedule.RcapActivity;

import java.util.Calendar;

/**
 * 主界面的日历主布局
 * 作者：janabo on 2016/12/29 09:54
 */
public class MainLayoutContainer extends RelativeLayout implements MonthView.OnCellSelectListener,
        MonthViewContainer.OnMonthViewHeightChangedListener {
    @SuppressWarnings("unused")
    private final static String TAG = "MainLayoutContainer";

    private MonthViewContainer monthViewContainer;
    private WeekView weekView;
    private float touchSlop;
    private RcapActivity container;
    private final InfoContainer infoContainer;

    public MainLayoutContainer(Context context, final RcapActivity mainList) {
        super(context);
        container = mainList;
        monthViewContainer = new MonthViewContainer(context);
        weekView = new WeekView(context);
        monthViewContainer.setId(R.id.montViewContainer);
        addView(monthViewContainer,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        RelativeLayout.LayoutParams weekParams
                = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        weekParams.addRule(ALIGN_PARENT_TOP);
        addView(weekView, weekParams);
        weekView.onCellSelect(null, Calendar.getInstance());
        weekView.setHeight(150);
        weekView.setVisibility(GONE);

        infoContainer = new InfoContainer(context);
        RelativeLayout.LayoutParams layoutParams
                = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.BELOW, R.id.montViewContainer);
        addView(infoContainer, layoutParams);

        monthViewContainer.setOnCellSelectListener(this);
        monthViewContainer.onMonthViewHeightChangedListener = this;
        monthViewContainer.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        onMonthViewHeightChanged(monthViewContainer, monthViewContainer.getMeasuredHeight());
                        monthViewContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                });
        infoContainer.setPadding(0,0, 0, 0);
    }

    public void setAdapter(RcapAdapter adapter){
        infoContainer.setAdapter(adapter);
    }


    public void onMonthViewMove(int dy){
        monthViewContainer.onMove(dy);
    }
    public void onMonthViewAnimation(float dy){
        monthViewContainer.onPointerUpAnimation(dy);
    }
    public static int MONTHVIEW_MIDDLE = 0;
    public static int MONTHVIEW_MAX = 1;
    public static int MONTHVIEW_MIN = -1;
    public int monthViewStatus(){
        return monthViewContainer.status;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(monthViewContainer.isInAnimation) return true;
        return super.onInterceptTouchEvent(ev);
    }

    void setWeekViewVisibility(int visibility){
        weekView.setVisibility(visibility);
    }

    private MonthView.OnCellSelectListener onCellSelectListener;

    public void setOnCellSelectListener(MonthView.OnCellSelectListener onCellSelectListener) {
        this.onCellSelectListener = onCellSelectListener;
    }

    @Override
    public void onCellSelect(MonthView monthView, Calendar calendar) {
        Log.v(TAG, "onCellSelect, calendar is " + calendar.get(Calendar.DAY_OF_MONTH));
        weekView.onCellSelect(monthView, calendar);
        if(onCellSelectListener != null)
            onCellSelectListener.onCellSelect(monthView, calendar);
    }

    @Override
    public void onMonthViewHeightChanged(MonthViewContainer monthViewContainer, int height) {
        if(container != null){
            container.onMonthViewHeightChanged(height);
        }
    }
}
