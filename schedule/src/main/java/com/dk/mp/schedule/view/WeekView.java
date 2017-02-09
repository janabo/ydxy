package com.dk.mp.schedule.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.dk.mp.core.application.MyApplication;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.schedule.R;
import com.dk.mp.schedule.utils.CalendarUtil;
import com.dk.mp.schedule.utils.LunarHelper;

import java.util.Calendar;

/**
 * 周视图
 * 作者：janabo on 2016/12/29 10:13
 */
public class WeekView extends View implements MonthView.OnCellSelectListener {
    @SuppressWarnings("unused")
    private final static String TAG = "WeekView";

    private Calendar selectCalendar, firstDayCalendar;
    private float cellWidth;
    private MonthViewPaint monthViewPaint;
    public WeekView(Context context) {
        super(context);
        monthViewPaint = new MonthViewPaint();
        setMinimumHeight(40);
        setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }

    private boolean isFirstDraw = true;
    @Override
    public void onCellSelect(MonthView monthView, Calendar calendar) {
        selectCalendar = calendar;
        firstDayCalendar = CalendarUtil.getFirstDayCalendarOfWeek(selectCalendar);
        isFirstDraw = true;
        if(monthView != null){
            setHeight((int) monthView.getCellHeight());
        }
    }

    private int getDeltaDay(int firstDay){
        if(firstDay == Calendar.SUNDAY){
            return 6;
        }
        return firstDay - 1;
    }

    void setHeight(int height){
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = height;
        setLayoutParams(params);
    }

    private RectF tmp = new RectF();
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isFirstDraw){
            calculatePosition();
            isFirstDraw = false;
        }

        for(int i = 0; i < 7; i++){
            if(cellSelect == i){
                Log.v(TAG, "this cell select, and i is " + i);
                tmp.set(cellOriginX[i]+DeviceUtil.dip2px(getContext(),12), cellHeightMarginTop+DeviceUtil.dip2px(getContext(),12),
                        cellOriginX[i] + + cellWidth/2+DeviceUtil.dip2px(getContext(),15),
                        cellHeightMarginTop + + cellWidth/2+DeviceUtil.dip2px(getContext(),15));
             //   tmp.set(cellOriginX[i], cellHeightMarginTop, cellOriginX[i] + cellWidth, cellHeightMarginTop + cellWidth);
                canvas.drawArc(tmp, 360, 360, false, monthViewPaint.selectCellBGPaint);
            }

            //判断是否在当月
            if(Integer.parseInt(dateText[i]) <= Integer.parseInt(dateText[cellSelect]) && i<=cellSelect) {
                canvas.drawText(dateText[i], dateTextOrigin[i].x, dateTextOrigin[i].y, monthViewPaint.dateTextPaint);
            }else if(Integer.parseInt(dateText[i]) > Integer.parseInt(dateText[cellSelect]) && i>cellSelect) {
                canvas.drawText(dateText[i], dateTextOrigin[i].x, dateTextOrigin[i].y, monthViewPaint.dateTextPaint);
            }else{
                canvas.drawText(dateText[i], dateTextOrigin[i].x, dateTextOrigin[i].y, monthViewPaint.ndateTextPaint);
            }


            canvas.drawText(cellLunarString[i], lunarTextOrigin[i].x, lunarTextOrigin[i].y, monthViewPaint.lunarTextPaint);
        }
    }

    private String[] cellLunarString = new String[7];
    private String[] dateText = new String[7];
    private PointF[] dateTextOrigin = new PointF[7];
    private PointF[] lunarTextOrigin = new PointF[7];
    private float[] cellOriginX = new float[7];
    private float cellHeightMarginTop;
    private int cellSelect = -1;
    private void calculatePosition() {
        int width = getMeasuredWidth();
        if(width <= 0) return;
        if(selectCalendar == null) return;

        int dayOfWeek = selectCalendar.get(Calendar.DAY_OF_WEEK)-1;
        cellSelect = dayOfWeek == Calendar.SATURDAY ? 6 : dayOfWeek;

        Calendar tmpFirstDay = (Calendar) firstDayCalendar.clone();
        cellWidth = width / 7;
        cellHeightMarginTop = (getMeasuredHeight() - cellWidth)/2;
        for(int i = 0; i < 7; i++){
            LunarHelper helper = new LunarHelper(tmpFirstDay);
            String lunarDay = helper.getLunarDay();
            if("初一".equals(lunarDay)){
                lunarDay = helper.getLunarMonthString();
            }
            cellLunarString[i] =  lunarDay;
            dateText[i] = String.valueOf(tmpFirstDay.get(Calendar.DAY_OF_MONTH));
            tmpFirstDay.add(Calendar.DAY_OF_MONTH, 1);
            cellOriginX[i] = i * cellWidth;

            dateTextOrigin[i] = new PointF();
            lunarTextOrigin[i] = new PointF();

            // 计算日期文字与农历文字的开始位置
            int dateTextHOffset = (int) ((cellWidth - monthViewPaint.dateTextPaint.measureText(dateText[i]))/2);
            int dateTextHeight =  DeviceUtil.getTextHeight(monthViewPaint.dateTextPaint, "10");

            int lunarTextHeight = DeviceUtil.getTextHeight(monthViewPaint.lunarTextPaint, cellLunarString[i]) + DeviceUtil.dip2px(MyApplication.getContext(),6);
            int lunarTextHOffset = (int) ((cellWidth - monthViewPaint.lunarTextPaint.measureText(cellLunarString[i]))/2);

            int topPadding = ((600/5 - dateTextHeight - lunarTextHeight)/2);
//            int topPadding =  ((600/5 - dateTextHeight)/2);
            int dateTextVOffset = topPadding + dateTextHeight;
            int lunarTextVOffset = topPadding + dateTextHeight + lunarTextHeight;
            dateTextOrigin[i].set(cellOriginX[i] + dateTextHOffset, dateTextVOffset);
            lunarTextOrigin[i].set(cellOriginX[i] + lunarTextHOffset, lunarTextVOffset);
        }
    }
}
