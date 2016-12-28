package com.dk.mp.schedule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.widget.NumericWheelAdapter;
import com.dk.mp.core.widget.OnWheelScrollListener;
import com.dk.mp.core.widget.WheelView;

import java.util.Calendar;

/**
 * 时间选择
 * @author dake
 *
 */
public class DateAndTimePickActivity extends Activity {
	private WheelView year;
	private WheelView month;
	private WheelView day,hour,min;
	private String rq="";
	private String compRq="";//比较日期
	private int type;//1,开始时间 2，结束时间
	int curYear;
	int curMonth;
	int curDate;
	int curHour;
	int curMin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pick_dateandtime);
		rq = getIntent().getStringExtra("rq");
		type = getIntent().getIntExtra("type", 0);
		getDatePick();
	}

	private void getDatePick() {
		if(StringUtils.isNotEmpty(rq)){
			curYear = Integer.parseInt(rq.substring(0, 4));
			curMonth = Integer.parseInt(rq.substring(5, 7));
			curDate = Integer.parseInt(rq.substring(8, 10));
			curHour = Integer.parseInt(rq.substring(11, 13));
			curMin = Integer.parseInt(rq.substring(14, rq.length()));
		}else{
			Calendar c = Calendar.getInstance();
			curYear = c.get(Calendar.YEAR);
			curMonth = c.get(Calendar.MONTH) + 1;
			curDate = c.get(Calendar.DATE);
			curHour = c.get(Calendar.HOUR_OF_DAY);
			curMin = c.get(Calendar.MINUTE);
		}

		year = (WheelView) findViewById(R.id.year);
		month = (WheelView) findViewById(R.id.month);
		day = (WheelView) findViewById(R.id.day);
		hour = (WheelView) findViewById(R.id.hour);
		min = (WheelView) findViewById(R.id.minute);

		year.setAdapter(new NumericWheelAdapter(curYear, curYear + 5, ""));
		year.setLabel("年");
		year.setCyclic(true);
		year.addScrollingListener(scrollListener);

		month.setAdapter(new NumericWheelAdapter(1, 12, ""));
		month.setLabel("月");
		month.setCyclic(true);
		month.addScrollingListener(scrollListener);

		initDay(curYear, curMonth);
		day.setLabel("日");
		day.setCyclic(true);
		
		hour.setAdapter(new NumericWheelAdapter(1, 24, ""));
		hour.setLabel("时");
		hour.setCyclic(true);
		
		min.setAdapter(new NumericWheelAdapter(0, 59, ""));
		min.setLabel("分");
		min.setCyclic(true);

		year.setCurrentItem(curYear - 1956);
		month.setCurrentItem(curMonth - 1);
		day.setCurrentItem(curDate - 1);
		hour.setCurrentItem(curHour-1);
		min.setCurrentItem(curMin);

		Button bt = (Button) findViewById(R.id.set);
		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			Intent in = new Intent();
			in.putExtra("date", getTime(curYear));
			setResult(RESULT_OK, in);
			back();
			}
		});
		Button cancel = (Button) findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				back();
			}
		});
	}
	
	
	private String getTime(int curYear){
		String mon = month.getCurrentItem() + 1 + "";
		if (mon.length() < 2) {
			mon = "0" + mon;
		}
		
		String d = day.getCurrentItem()+1+"";
		if(d.length()<2){
			d = "0"+d;
		}
		
		String xiaoshi="0";
		String fenzhong="0";
		
		if((hour.getCurrentItem() + 1)<10){
			xiaoshi+=(hour.getCurrentItem() + 1);	
		}else{
			xiaoshi=(hour.getCurrentItem() + 1)+"";
		}
		
		
		if((min.getCurrentItem() )<10){
			fenzhong+=(min.getCurrentItem() );	
		}else{
			fenzhong=(min.getCurrentItem())+"";
		}
		
		String str = (year.getCurrentItem() + curYear) + "-" + mon + "-" + d+" "+xiaoshi+":"+fenzhong;
		return str;
	}
	

	OnWheelScrollListener scrollListener = new OnWheelScrollListener() {

		@Override
		public void onScrollingStarted(WheelView wheel) {

		}

		@Override
		public void onScrollingFinished(WheelView wheel) {
			int n_year = year.getCurrentItem() + 1950;//
			int n_month = month.getCurrentItem() + 1;//
			initDay(n_year, n_month);
		}
	};

	/**
	 */
	private void initDay(int arg1, int arg2) {
		day.setAdapter(new NumericWheelAdapter(1, getDay(arg1, arg2), "%02d", ""));
	}

	/**
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	private int getDay(int year, int month) {
		int day = 30;
		boolean flag = false;
		switch (year % 4) {
		case 0:
			flag = true;
			break;
		default:
			flag = false;
			break;
		}
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			day = 31;
			break;
		case 2:
			day = flag ? 29 : 28;
			break;
		default:
			day = 30;
			break;
		}
		return day;
	}

	public void back() {
		finish();
		overridePendingTransition(0, R.anim.push_down_out);
	}
	
	private boolean checkTime(String starttime,String enttime){
		long a =Long.parseLong(starttime.replace("-", "").replace(" ", "").replace(":", ""));
		long b =Long.parseLong(enttime.replace("-", "").replace(" ", "").replace(":", ""));
		if(a>b){
			return true;
		}else{
			return false;
		}
	}

}
