package com.dk.mp.core.util;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 常用关于时间的工具.
 * 比如格式化时间等.
 * @since jdk1.5
 * @version 2012-3-5
 
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtils {
	/**
	 * 计算两日期之间相差的天数.
	 * @param startTime     开始时间
	 * @param endTime  结束时间
	 * @return 返回相差天数.
	 */
	public static long getDaysBetween(final String startTime, final String endTime) {
		long days = 0;
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		Date timeStart;
		try {
			timeStart = millisToDate(startTime);
			Date timeEnd = millisToDate(endTime);
			start.setTime(timeStart);
			end.setTime(timeEnd);
		} catch (Exception e) {
			Logger.error("计算两日期之间相差的天数"+ e);
		}
		days = (end.getTimeInMillis() - start.getTimeInMillis()) / (24 * 60 * 60 * 1000);
		return days;
	}

	/**
	 * 将毫秒转换为日期.
	 * @param millis 毫秒
	 * @return 日期
	 * @throws Exception Exception
	 */
	public static Date millisToDate(String millis) throws Exception {
		if (millis.contains("-")) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			return format.parse(millis);
		} else {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(Long.parseLong(millis));
			return c.getTime();
		}
	}

	/**
	 * 获取当天日期.
	 * @return String(yyyy-mm-dd)
	 */
	public static String getToday() {
		Date now = new Date();
		String hehe = new SimpleDateFormat("yyyy-MM-dd").format(now);
		return hehe;
	}

	public static String getToday2(){
		Date now = new Date();
		String hehe = new SimpleDateFormat("yyyy.MM.dd").format(now);
		return hehe;
	}

	/**
	 * 获取当天日期.
	 * @return String(yyyy-mm-dd HH:mm)
	 */
	public static String getCurrTime() {
		Date now = new Date();
		String hehe = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(now);
		return hehe;
	}

	/**
	 * 当前年份.
	 * @return  yyyy
	 */
	public static String getNowyear() {
		SimpleDateFormat formatYMD = new SimpleDateFormat("yyyy");
		Date now = new Date();
		String hehe = formatYMD.format(now);
		return hehe;
	}

	/**
	 * 当前时间多少分钟之后
	 * @param mins
	 * @return
     */
	public static String getTimeAfterMins(int mins){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date();
		String time_now = format.format(date);
		System.out.println(time_now);
		long time = date.getTime() + mins * 60 * 1000;
		Date date1 = new Date(time);
		String time_after = format.format(date1);
		return time_after;
	}

	/**
	 * 判断时间先后.
	 * @param startTime   日期
	 * @param endTime  日期
	 * @return 是或否
	 */
	public static boolean comparedTime2(String startTime, String endTime) {
		SimpleDateFormat formatYMD = new SimpleDateFormat("yyyy-MM-dd HH:mm");// formatYMD表示的是yyyy-MM-dd格式
		Date d1 = null;
		Date d2 = null;
		boolean bool = true;
		try {
			d1 = formatYMD.parse(startTime);
			d2 = formatYMD.parse(endTime);
			if (d1.before(d2)) {
				bool = true;
			} else if (d1.after(d2)) {
				bool = false;
			}
		} catch (ParseException e) {
		}
		return bool;
	}


	/**
	 * 格式化日期.
	 * @param dateTime 日期 yyyy-MM-dd HH:mm
	 * @return 格式化后的日期 HH:mm
	 */
	public static String getTimeByDateTime(String dateTime) {
		if(!StringUtils.isNotEmpty(dateTime)){
			return "";
		}
		SimpleDateFormat formatYMD = new SimpleDateFormat("yyyy-MM-dd HH:mm");// formatYMD表示的是yyyy-MM-dd格式
		SimpleDateFormat formatYMD1 = new SimpleDateFormat("HH:mm");// formatYMD表示的是yyyy-MM-dd格式
		Date d = null;
		try {
			d = formatYMD.parse(dateTime);
		} catch (ParseException e) {
			Logger.error("格式化日期"+e);
		}
		return formatYMD1.format(d);
	}

	/**
	 * 给传入的日期加 day 天.
	 * @param time  日期
	 * @param days 要加的天数
	 * @return 传入日期加 day 天的日期 （String）
	 */
	public static String getAfterDate(String time, int days) {
		SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		Date date = new Date();
		try {
			date = sm.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + days);
		return sm.format(calendar.getTime());
	}
}
