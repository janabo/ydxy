package com.dk.mp.schedule.view;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.dk.mp.core.application.MyApplication;
import com.dk.mp.core.util.DeviceUtil;

/**
 * 作者：janabo on 2016/12/29 10:03
 */
public class MonthViewPaint {
    Paint circlePaint;
    Paint dateTextPaint;
    Paint lunarTextPaint;
    Paint selectCellBGPaint;

    Paint ndateTextPaint;

    public MonthViewPaint(){
        dateTextPaint = new Paint();
        dateTextPaint.setAntiAlias(true);
        dateTextPaint.setColor(Color.WHITE);
        dateTextPaint.setTextSize(DeviceUtil.dip2px(MyApplication.getContext(),14));

        ndateTextPaint = new Paint();
        ndateTextPaint.setAntiAlias(true);
        ndateTextPaint.setColor(Color.rgb(166,205,249));
        ndateTextPaint.setTextSize(DeviceUtil.dip2px(MyApplication.getContext(),14));


        selectCellBGPaint = new Paint();
        selectCellBGPaint.setColor(Color.rgb(166,213,250));

        lunarTextPaint = new Paint();
        lunarTextPaint.setColor(Color.GRAY);
        lunarTextPaint.setTextSize(DeviceUtil.dip2px(MyApplication.getContext(),10));
        lunarTextPaint.setTypeface(Typeface.SANS_SERIF);
        lunarTextPaint.setAntiAlias(true);

        circlePaint = new Paint();
        circlePaint.setColor(Color.WHITE);
    }
}
