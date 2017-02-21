package com.dk.mp.oldoa.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.dk.mp.oldoa.R;
import com.dk.mp.oldoa.entity.CalendarEntity;
import com.dk.mp.oldoa.entity.Event;
import com.dk.mp.oldoa.utils.CalendarUtil;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class CalendarGridViewAdapter extends BaseAdapter {

	private Calendar calStartDate = Calendar.getInstance();// 当前显示的日历
	private Calendar calSelected = Calendar.getInstance(); // 选择的日历
	private List<CalendarEntity> list = new ArrayList<CalendarEntity>();
	//	Handler handler;
	private Context mContext;
	int widths;
	private Handler handler = null;

	public void setSelectedDate(Calendar cal) {
		calSelected = cal;
	}

	private Calendar calToday = Calendar.getInstance(); // 今日
	private int iMonthViewCurrentMonth = 0; // 当前视图月

	// 根据改变的日期更新日历
	// 填充日历控件用
	private void UpdateStartDateForMonth() {
		calStartDate.set(Calendar.DATE, 1); // 设置成当月第一天
		iMonthViewCurrentMonth = calStartDate.get(Calendar.MONTH);// 得到当前日历显示的月
		// 星期一是2 星期天是1 填充剩余天数
		int iDay = 0;
		int iFirstDayOfWeek = Calendar.MONDAY;
		int iStartDay = iFirstDayOfWeek;
		if (iStartDay == Calendar.MONDAY) {
			iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
			if (iDay < 0)
				iDay = 6;
		}
		if (iStartDay == Calendar.SUNDAY) {
			iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
			if (iDay < 0)
				iDay = 6;
		}
		calStartDate.add(Calendar.DAY_OF_WEEK, -iDay);
		calStartDate.add(Calendar.DAY_OF_MONTH, 0);// 周一第一位

	}

	public void setList(List<CalendarEntity> l) {
		list = l;
	}

	public void setCalendar(Calendar l) {
		calStartDate = l;
	}

	public List<CalendarEntity> getDates() {
		UpdateStartDateForMonth();
		list = new ArrayList<CalendarEntity>();
		SimpleDateFormat formatYMD = new SimpleDateFormat("yyyy-MM-dd");// formatYMD表示的是yyyy-MM-dd格式

		for (int i = 1; i <= 42; i++) {
			CalendarEntity calendarE = new CalendarEntity();
			calendarE.setDate(calStartDate.getTime());
			calendarE.setFlag(5);
			list.add(calendarE);
			calStartDate.add(Calendar.DAY_OF_MONTH, 1);
		}
		updateView();
		return list;

	}

	private Activity activity;
	Resources resources;

	// construct
	public CalendarGridViewAdapter(Activity a, Calendar cal, List<Event> eList, Handler h, int width) {
		calStartDate = cal;
		activity = a;
		this.widths = width;
		resources = activity.getResources();
		this.mContext = a;
		this.handler = h;
		list = getDates();

	}

	public CalendarGridViewAdapter(Activity a) {
		activity = a;
		resources = activity.getResources();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		WindowManager windowManager = ((Activity) mContext).getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int height = display.getWidth() / 7;
		CalendarEntity item = (CalendarEntity) getItem(position);
		LinearLayout iv = new LinearLayout(activity);
		android.widget.AbsListView.LayoutParams params = new android.widget.AbsListView.LayoutParams(height, height);
		iv.setLayoutParams(params);
		iv.setId(position + 5000);
		LinearLayout imageLayout = new LinearLayout(activity);
		imageLayout.setOrientation(LinearLayout.HORIZONTAL);
		iv.setGravity(Gravity.CENTER);
		iv.setOrientation(LinearLayout.VERTICAL);
		iv.setBackgroundColor(resources.getColor(R.color.white));
		//		Date myDate = (Date) getItem(position);
		Calendar calCalendar = Calendar.getInstance();
		calCalendar.setTime(item.getDate());

		final int iMonth = calCalendar.get(Calendar.MONTH);
		final int iDay = calCalendar.get(Calendar.DAY_OF_WEEK);

		// 判断周六周日
		iv.setBackgroundColor(resources.getColor(R.color.white));
		//		if (iDay == 7) {
		//			// 周六
		//			iv.setBackgroundColor(resources.getColor(R.color.text_6));
		//		} else if (iDay == 1) {
		//			// 周日
		//			iv.setBackgroundColor(resources.getColor(R.color.text_7));
		//		} else {
		//
		//		}
		// 判断周六周日结束

		TextView txtToDay = new TextView(activity);// 日本老黄历
		txtToDay.setGravity(Gravity.CENTER_HORIZONTAL);
		if (widths <= 480) {
			txtToDay.setTextSize(6);
		} else if (widths > 480) {
			txtToDay.setTextSize(10);
		}

		txtToDay.setPadding(0, 0, 0, (int) resources.getDimension(R.dimen.point_padding));
		CalendarUtil calendarUtil = new CalendarUtil(calCalendar);

		// 日期开始
		TextView txtDay = new TextView(activity);// 日期
		txtDay.setGravity(Gravity.CENTER);
		txtDay.setTextSize(16);
		int day = item.getDate().getDate(); // 日期
		txtDay.setText(String.valueOf(day));
		txtDay.setId(position + 500);
		iv.setTag(item.getDate());
		txtToDay.setText(calendarUtil.toString());
		if (item.isMark()) {

			iv.setBackgroundResource(R.mipmap.calendar_center_bg);
			txtDay.setTextColor(mContext.getResources().getColor(R.color.white));
			txtToDay.setTextColor(resources.getColor(R.color.white));
		} else {
			if (equalsDate(calToday.getTime(), item.getDate())) {
				// 当前日期
				txtDay.setTextColor(mContext.getResources().getColor(R.color.white));
				txtToDay.setTextColor(resources.getColor(R.color.white));
				iv.setBackgroundResource(R.mipmap.day_pressed);

			} else {
				if (iMonth == iMonthViewCurrentMonth) {
					txtDay.setTextColor(mContext.getResources().getColor(R.color.black));
					txtToDay.setTextColor(resources.getColor(R.color.ToDayText));
					iv.setBackgroundColor(mContext.getResources().getColor(R.color.white));
				} else {
					txtToDay.setTextColor(resources.getColor(R.color.noMonth));
					txtDay.setTextColor(mContext.getResources().getColor(R.color.noMonth));
					iv.setBackgroundColor(mContext.getResources().getColor(R.color.white));
				}
				txtToDay.setText(calendarUtil.toString());

			}

		}
		LayoutParams lp = new LayoutParams(
				(int) resources.getDimension(R.dimen.day_txt_height), (int) resources.getDimension(R.dimen.date_height));
		iv.addView(txtDay, lp);
		LayoutParams lp1 = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		lp1.bottomMargin = 5;
		iv.addView(txtToDay, lp1);
		return iv;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	private Boolean equalsDate(Date date1, Date date2) {

		if (date1.getYear() == date2.getYear() && date1.getMonth() == date2.getMonth()
				&& date1.getDate() == date2.getDate()) {
			return true;
		} else {
			return false;
		}

	}

	public void updateView() {
		Message msg = new Message();
		msg.what = 5;
		Bundle b = new Bundle();
		b.putSerializable("lists", (Serializable) list);
		msg.setData(b);
		handler.sendMessage(msg);

	}

	//	public void getData() {
	//
	//		SimpleDateFormat formatYMD = new SimpleDateFormat("yyyy-MM-dd");// formatYMD表示的是yyyy-MM-dd格式
	//		for (int i = 0; i < list.size(); i++) {
	//
	//			Logger.error("------date-------" + formatYMD.format(list.get(i).getDate()));
	//
	//		}
	//
	//	}

}
