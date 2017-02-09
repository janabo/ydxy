package com.dk.mp.schedule.utils;

import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Calendar相关的一些工具类
 * Created by ke on 16-1-19.
 */
public class CalendarUtil {

    private CalendarUtil(){}
    public static final String DATE = "yyyy-MM-dd";
    public static final String TIME = "yyyy-MM-dd HH-mm-ss";
    public static final String TIME_HM = "yyyy-MM-dd HH-mm";
    /***
     * 获取第一天是星期几
     */
    public static int getFirstDayOfMonth(@NonNull Calendar calOld){
        return getFirstDayCalendarOfMonth(calOld).get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 今天周几.
     * @return int
     */
    public static int getNowWeek() {
        Calendar cld = Calendar.getInstance();
        return cld.get(Calendar.DAY_OF_WEEK) - 1;
    }

    public static Calendar getNowDay(){
        Calendar cld = Calendar.getInstance();
        Date date = new Date();
        cld.setTime(date);
        return cld;
    }

    /**
     * 获取这个月第一天的Calendar
     */
    @NonNull
    public static Calendar getFirstDayCalendarOfMonth(@NonNull Calendar calOld) {
        Calendar calendar = (Calendar) calOld.clone();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar;
    }

    /**
     * 获取这一周的第一天
     * @param calOld
     * @return
     */
    @NonNull
    public static Calendar getFirstDayCalendarOfWeek(@NonNull Calendar calOld){
        Calendar calendar = (Calendar) calOld.clone();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        if(calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            calendar.add(Calendar.DAY_OF_WEEK, 1 - calendar.get(Calendar.DAY_OF_WEEK));
        }else{
            calendar.add(Calendar.DAY_OF_MONTH,0);
        }
        return calendar;
    }

    /**
     * 获取这个月最后一天的Calendar
     */
    @NonNull
    public static Calendar getLastDayCalendar(@NonNull Calendar calOld){
        Calendar calendar = (Calendar) calOld.clone();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.roll(Calendar.DAY_OF_MONTH, -1);
        return calendar;
    }

    /**
     * 获取当月的周数, 完整周, 以周一为开始
     * @param calOld
     * @return
     */
    public static int getWeeksOfMonth(@NonNull Calendar calOld){
        Calendar calendar = (Calendar) calOld.clone();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setMinimalDaysInFirstWeek(1);
        calendar = getLastDayCalendar(calendar);
        return  calendar.get(Calendar.WEEK_OF_MONTH);
    }

    /**
     * 获取当月天数
     */
    public static int getDayCount(@NonNull Calendar calOld){
        return getLastDayCalendar(calOld).get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取年月的天数
     * 月份是１－ｂａｓｅｄ
     * @param year     年份
     * @param month    月份 以１开头
     * @return
     */
    public static int getDayCount(int year, int month){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 10);
        return getDayCount(calendar);
    }

    /**
     * 格式化时间
     * @param timeInMill 毫秒
     * @param format     格式化的字符串
     * @return
     */
    public static String formatTime(long timeInMill, @NonNull String format){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(new Date(timeInMill));
    }

    /**
     * 解析时间
     * @throws ParseException
     */
    public static Date parseTime(@NonNull String dateStr, @NonNull String format) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.parse(dateStr);
    }
}
