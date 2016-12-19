package com.dk.mp.core.util;

import android.annotation.SuppressLint;

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

}
