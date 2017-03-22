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
	 * 获取当月.
	 * @return String(yyyy-mm)
	 */
	public static String getCurrMonth() {
		Date now = new Date();
		String hehe = new SimpleDateFormat("yyyy-MM").format(now);
		return hehe;
	}

	/**
	 * 获取当年.
	 * @return String(yyyy)
	 */
	public static int getCurrYear() {
		Date now = new Date();
		String hehe = new SimpleDateFormat("yyyy").format(now);
		return Integer.parseInt(hehe);
	}

	/**
	 * 格式化日期.
	 * @param dateTime  日期
	 * @return 格式化后的日期
	 */
	public static String formatDateTime(String dateTime) {
		SimpleDateFormat formatYMD = new SimpleDateFormat("yyyy-MM-dd");// formatYMD表示的是yyyy-MM-dd格式
		SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");// formatYMD表示的是yyyy-MM-dd格式
		Date d = null;
		try {
			d = formatYMD.parse(dateTime);
		} catch (ParseException e) {
			Logger.error("格式化日期错误");
		}
		return format.format(d);
	}

	/**
	 * 格式化日期.
	 * @param dateTime  日期
	 * @return 格式化后的日期
	 */
	public static String formatDateTime2(String dateTime) {
		SimpleDateFormat formatYMD = new SimpleDateFormat("yyyy-MM-dd");// formatYMD表示的是yyyy-MM-dd格式
		SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");// formatYMD表示的是yyyy-MM-dd格式
		Date d = null;
		try {
			d = format.parse(dateTime);
		} catch (ParseException e) {
			Logger.error("格式化日期错误");
		}
		return formatYMD.format(d);
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
	 * 判断日期先后
	 * @param startTime   日期
	 * @param endTime  日期
	 * @return 是或否
	 */
	public static boolean comparedDate(String startTime, String endTime) {
		SimpleDateFormat formatYMD = new SimpleDateFormat("yyyy-MM-dd");// formatYMD表示的是yyyy-MM-dd格式
		Date d1 = null;
		Date d2 = null;
		boolean bool = true;
		try {
			d1 = formatYMD.parse(startTime);
			d2 = formatYMD.parse(endTime);
			if (d1.after(d2)) {
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

	/**
	 * 是否同一月
	 * @param month1 yyyy-MM-dd
	 * @return 是或否
	 */
	public static boolean sfMonth(String month1, String month2) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		boolean bool = false;
		try {
			Date d1 = sdf.parse(month1);
			Date d2 = sdf.parse(month2);
			if (d1.equals(d2)) {
				bool = true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return bool;
	}

	/**
	 *
	 * @param time  时间
	 * @param format  格式
	 * @return 格式后的时间
	 */
	public static String getFormatTime(String time, SimpleDateFormat format) {
		Date d = null;
		SimpleDateFormat formatYMD = new SimpleDateFormat("yyyy-MM-dd HH:mm");// formatYMD表示的是yyyy-MM-dd格式
		try {
			d = format.parse(time);
		} catch (ParseException e) {
			Logger.error("getFormatTime"+ e);
		}
		return formatYMD.format(d);
	}

	/**
	 *
	 * @param time     时间
	 * @return 格式后的时间
	 */
	public static String getFormatTime(String time) {
		Date d = null;
		SimpleDateFormat formatYMD = new SimpleDateFormat("yyyy-MM-dd");// formatYMD表示的是yyyy-MM-dd格式
		try {
			d = formatYMD.parse(time);
		} catch (ParseException e) {
			Logger.error("getFormatTime"+e);
		}
		return formatYMD.format(d);
	}

	/**
	 * 获取该天所在周的周一的日期.
	 * @param t 日期
	 * @return 周一的日期
	 */
	public static String getMonday(String t) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
		Date time = null;
		try {
			time = sdf.parse(t);
		} catch (ParseException e) {
			Logger.error("获取该天所在周的周一和周日日期"+e);
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		if (1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		cal.setFirstDayOfWeek(Calendar.MONDAY); // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		int day = cal.get(Calendar.DAY_OF_WEEK); // 获得当前日期是一个星期的第几天
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		String imptimeBegin = sdf.format(cal.getTime());
		return imptimeBegin;
	}

	/**
	 *
	 * @param specifiedDay  某天
	 * @return 某天的后一天
	 */
	public static String getDayNext(String specifiedDay) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (Exception e) {
			Logger.error("某天的后一天"+ e);
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + 1);
		String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayBefore;
	}

	/**
	 * 根据日期获取当天周几.
	 * @param date  日期
	 * @return string
	 */
	public static int getWeek(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(format.parse(date));
		} catch (ParseException e) {
			Logger.error("根据日期获取当天周几"+e);
		}
		int dayForWeek = 0;
		if ((int)c.get(Calendar.DAY_OF_WEEK) == 1) {
			dayForWeek = 7;
		} else {
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return dayForWeek;
	}

	/**
	 * @param weekday  数字周几
	 * @return 汉字（一，二，三，四，五，六，日）
	 */
	public static String getWeekDayInt2Str(int weekday) {
		String week = "";
		switch (weekday) {
			case 7:
				week = "日";
				break;
			case 1:
				week = "一";
				break;
			case 2:
				week = "二";
				break;
			case 3:
				week = "三";
				break;
			case 4:
				week = "四";
				break;
			case 5:
				week = "五";
				break;
			case 6:
				week = "六";
				break;
		}
		return "星期" + week;
	}

	/**
	 * 获取每个月的最后一天.
	 * @param time 时间
	 * @return yyyy-MM-dd
	 */
	public static String getLastDayOfMonth(String time) {
		Calendar ca = Calendar.getInstance();
		Date date = null;
		SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
		if (!StringUtils.isNotEmpty(time)) {
			date = new Date();
		} else {
			try {
				date = sm.parse(time);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		ca.setTime(date);
		final int lastDay = ca.getActualMaximum(Calendar.DAY_OF_MONTH);
		Date lastDate = ca.getTime();
		lastDate.setDate(lastDay);
		return sm.format(lastDate);
	}

	/**
	 * 格式化日期精确到分.
	 * @param date  日期
	 * @return yyyy-MM-dd HH:mm
	 */
	public static String dateMinuteToStr(Date date) {
		SimpleDateFormat formatYMD = new SimpleDateFormat("yyyy-MM-dd HH:mm");// formatYMD表示的是yyyy-MM-dd格式
		String ret = null;
		try {
			ret = formatYMD.format(date);

		} catch (Exception e) {
		}
		return ret;
	}

	/**
	 * 判断时间先后.
	 * @param startTime   日期
	 * @param endTime  日期
	 * @return 是或否
	 */
	public static boolean comparedDateS(String startTime, String endTime) {
		SimpleDateFormat formatYMD = new SimpleDateFormat("yyyy-MM-dd");// formatYMD表示的是yyyy-MM-dd格式
		Date d1 = null;
		Date d2 = null;
		boolean bool = true;
		try {
			d1 = formatYMD.parse(startTime);
			d2 = formatYMD.parse(endTime);
			if (d1.before(d2)) {
				bool = true;
			} else{
				bool = false;
			}
		} catch (ParseException e) {
		}
		return bool;
	}

	/**
	 * 格式化日期.
	 * @param date  日期
	 * @return String(yyyy-mm-dd)
	 */
	public static String parseDate(Date date) {
		String hehe = new SimpleDateFormat("yyyy-MM-dd").format(date);
		return hehe;

	}
}
