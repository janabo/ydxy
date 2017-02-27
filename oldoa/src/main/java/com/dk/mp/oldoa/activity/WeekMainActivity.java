package com.dk.mp.oldoa.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dk.mp.core.util.TimeUtils;
import com.dk.mp.oldoa.R;
import com.dk.mp.oldoa.adapter.CalendarGridViewAdapter;
import com.dk.mp.oldoa.adapter.HomePageAdapter;
import com.dk.mp.oldoa.entity.CalendarEntity;
import com.dk.mp.oldoa.entity.Event;
import com.dk.mp.oldoa.interfaces.IconPageIndicator;
import com.dk.mp.oldoa.interfaces.PageIndicator;
import com.dk.mp.oldoa.utils.NumberHelper;
import com.dk.mp.oldoa.view.CalendarGridView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.dk.mp.core.ui.MyActivity;


/**
 * 周报
 * @since 
 * @version 2014-7-17
 * @author lj.zhang
 */
public class WeekMainActivity extends MyActivity implements OnTouchListener, OnGestureListener, OnLongClickListener,
		OnClickListener {
	/**
	 * 日历布局ID
	 */
	private static final int CAL_LAYOUT_ID = 55;
	//判断手势用
	private static final int SWIPE_MIN_DISTANCE = 120;
	private ViewPager viewPager;
	ArrayList<View> pageViews = new ArrayList<View>();
	//动画
	private GestureDetector mGestureDetector = null;
	GestureDetector mGesture = null;
	LinearLayout weekLayout = null;

	//	/**
	//	 * 用于显示今天的日期
	//	 */
	private TextView monthTxt;
	private TextView yearTxt;

	// 基本变量
	private Context mContext;
	/**
	 * 当前月View
	 */
	private GridView currentGridView;

	/**
	 * 当前显示的日历
	 * 
	 */
	private Calendar calStartDate = Calendar.getInstance();

	/**
	 * 选择的日历
	 */
	private Calendar calSelected = Calendar.getInstance();

	/**
	 * 今日
	 */
	private Calendar calToday = Calendar.getInstance();

	/**
	 * 当前界面展示的数据源
	 */
	private CalendarGridViewAdapter currentGridAdapter;

	/**
	 * 当前视图月
	 */
	private int mMonthViewCurrentMonth = 0;

	/**
	 * 当前视图年
	 */
	private int mMonthViewCurrentYear = 0;

	/**
	 * 起始周
	 */
	private int iFirstDayOfWeek = Calendar.MONDAY;
	private List<Event> scheduleMonthLists = new ArrayList<Event>();
	//	ScheduleDBHelper dbHelper;
	String beginTime;//开始时间
	String endTime;//结束时间
	RelativeLayout topLayout;
	private PageIndicator mIndicator;
	HomePageAdapter hAdapte = null;
	private List<CalendarEntity> calendarLists;
	long start;
	long end;
	int width = 0;
	DisplayMetrics dm = null;
	private Animation animation = null;
	List<CalendarEntity> saveSelectDays = new ArrayList<CalendarEntity>();//保存选中的天
	private TextView okBtn = null;//确定按钮
	private String date;//日期
	private Handler mHander = new Handler() {//更新翻页数据

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Bundle b = msg.getData();
				int animIndex = b.getInt("anim");
				updateYear();
				if (animIndex == 1) {

					animation = AnimationUtils.loadAnimation(WeekMainActivity.this, R.anim.push_left_in);
					viewPager.setAnimation(animation);
					currentGridAdapter.getDates();
				} else if (animIndex == 2) {
					animation = AnimationUtils.loadAnimation(WeekMainActivity.this, R.anim.push_right_in);
					viewPager.setAnimation(animation);
					currentGridAdapter.getDates();
				}
				currentGridAdapter.notifyDataSetChanged();

				break;

			default:
				break;
			}
		};

	};
	private Handler pageHandler = new Handler() {//初始化数据并过渡
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Bundle b = msg.getData();

				hAdapte = new HomePageAdapter(pageViews);
				viewPager.setAdapter(hAdapte);
				mIndicator.setViewPager(viewPager);
				viewPager.setCurrentItem(b.getInt("index"));
				WindowManager windowManager = ((Activity) mContext).getWindowManager();
				Display display = windowManager.getDefaultDisplay();
				int height = display.getWidth() / 7;
				LinearLayout.LayoutParams params_br = (LinearLayout.LayoutParams) viewPager.getLayoutParams();
				params_br.height = height * 6 + 30;
				params_br.width = display.getWidth();
				viewPager.setLayoutParams(params_br);
				//					currentGridAdapter.updateView();

				break;
			default:
				break;
			}
			Bundle b = msg.getData();
			int animIndex = b.getInt("anim");
			Message mg = new Message();
			mg.what = 0;
			Bundle bd = new Bundle();
			bd.putInt("anim", animIndex);
			mg.setData(bd);
			mHander.sendMessage(mg);
		};
	};

	/**
	 * 获取日历中返回的数据
	 */
	private Handler hander = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 5:
				Bundle b = msg.getData();
				calendarLists = (List<CalendarEntity>) b.getSerializable("lists");
				break;

			default:
				break;
			}

		};

	};

	@Override
	protected int getLayoutID() {
		return R.layout.week_main_view;
	}

	@Override
	protected void initView() {
		super.initView();
		setTitle(getIntent().getStringExtra("title"));
		dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;//宽度
		mContext = WeekMainActivity.this;
		findView();
		updateStartDateForMonth();
		generateContetView(0);
		viewPager.setOnTouchListener(this);
		viewPager.setLongClickable(true);
		mGestureDetector = new GestureDetector(this);
	}

//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.week_main_view);
//		setTitle(getIntent().getStringExtra("title"));
//		dm = new DisplayMetrics();
//		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
//		width = dm.widthPixels;//宽度
//		mContext = WeekMainActivity.this;
//		findView();
//		updateStartDateForMonth();
//		generateContetView(0);
//		viewPager.setOnTouchListener(this);
//		viewPager.setLongClickable(true);
//		mGestureDetector = new GestureDetector(this);
//	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}

	/**
	 * 用于初始化控件
	 */
	private void findView() {
		yearTxt = (TextView) findViewById(R.id.show_year);
		monthTxt = (TextView) findViewById(R.id.month_txt);
		weekLayout = (LinearLayout) findViewById(R.id.week_layout);
		topLayout = (RelativeLayout) findViewById(R.id.message_layout);
		viewPager = (ViewPager) findViewById(R.id.pager);
		mIndicator = (IconPageIndicator) findViewById(R.id.indicator);
		okBtn = (TextView) findViewById(R.id.right_txt);
		okBtn.setText(getResources().getString(R.string.ok));
		okBtn.setOnClickListener(this);
	}

	/**
	 * 主要用于生成发前展示的日历View
	 *
	 * @param
	 */
	private void generateContetView(int current) {
		calStartDate = getCalendarStartDate();
		initCreateGirdView(current, 0);
	}

	/**
	 * 用于创建当前将要用于展示的View
	 */
	private void initCreateGirdView(int current, int l) {
		pageViews.clear();
		viewPager.removeAllViews();
		currentGridView = new CalendarGridView(mContext);
		currentGridAdapter = new CalendarGridViewAdapter(this, calStartDate, scheduleMonthLists, hander, width);
		currentGridView.setAdapter(currentGridAdapter);// 设置菜单Adapter
		currentGridView.setId(CAL_LAYOUT_ID);
		currentGridView.setOnTouchListener(this);
		pageViews.add(currentGridView);
		Message msg = new Message();
		msg.what = 0;
		Bundle b = new Bundle();
		b.putInt("index", current);
		b.putInt("anim", l);
		msg.setData(b);
		pageHandler.sendMessage(msg);
	}

	/**
	 * 用于创建当前将要用于展示的View
	 */
	private void CreateGirdView(int current, int l) {
		currentGridAdapter.setCalendar(calStartDate);
		Message msg = new Message();
		msg.what = 0;
		Bundle b = new Bundle();
		b.putInt("index", current);
		b.putInt("anim", l);
		msg.setData(b);
		pageHandler.sendMessage(msg);
	}

	/**
	 * 上一个月
	 */
	private void setPrevViewItem(int current, int l) {
		mMonthViewCurrentMonth--;// 当前选择月--
		// 如果当前月为负数的话显示上一年
		if (mMonthViewCurrentMonth == -1) {
			mMonthViewCurrentMonth = 11;
			mMonthViewCurrentYear--;
		}
		calStartDate.set(Calendar.DAY_OF_MONTH, 1); // 设置日为当月1日
		calStartDate.set(Calendar.MONTH, mMonthViewCurrentMonth); // 设置月
		calStartDate.set(Calendar.YEAR, mMonthViewCurrentYear); // 设置年
		String time;
		if (mMonthViewCurrentMonth == 11) {

			time = (mMonthViewCurrentYear + 1) + "-" + (mMonthViewCurrentMonth + 1) + "-"
					+ calStartDate.get(Calendar.DAY_OF_MONTH);
		} else {

			time = (mMonthViewCurrentYear) + "-" + (mMonthViewCurrentMonth + 1) + "-"
					+ calStartDate.get(Calendar.DAY_OF_MONTH);
		}

		beginTime = calSelected.get(Calendar.YEAR) + "-"
				+ NumberHelper.LeftPad_Tow_Zero((calStartDate.get(Calendar.MONTH) + 1)) + "-" + "01";
		endTime = TimeUtils.getLastDayOfMonth(time);
		CreateGirdView(current, l);
	}

	/**
	 * 下一个月
	 */
	private void setNextViewItem(int current, int l) {
		mMonthViewCurrentMonth++;
		if (mMonthViewCurrentMonth == 12) {
			mMonthViewCurrentMonth = 0;
			mMonthViewCurrentYear++;
		}
		calStartDate.set(Calendar.DAY_OF_MONTH, 1);
		calStartDate.set(Calendar.MONTH, mMonthViewCurrentMonth);
		calStartDate.set(Calendar.YEAR, mMonthViewCurrentYear);
		String time = mMonthViewCurrentYear + "-" + (mMonthViewCurrentMonth + 1) + "-"
				+ calStartDate.get(Calendar.DAY_OF_MONTH);
		beginTime = calStartDate.get(Calendar.YEAR) + "-"
				+ NumberHelper.LeftPad_Tow_Zero((calStartDate.get(Calendar.MONTH) + 1)) + "-" + "01";
		endTime = TimeUtils.getLastDayOfMonth(time);
		CreateGirdView(current, l);
	}

	/**
	 * 根据改变的日期更新日历
	 * 填充日历控件用
	 */
	private void updateStartDateForMonth() {
		calStartDate.set(Calendar.DATE, 1); // 设置成当月第一天
		mMonthViewCurrentMonth = calStartDate.get(Calendar.MONTH);// 得到当前日历显示的月
		mMonthViewCurrentYear = calStartDate.get(Calendar.YEAR);// 得到当前日历显示的年
		//		mDayMessage.setText(s);
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
		//		yearTxt.setText(showYear());
		String time = calSelected.get(Calendar.YEAR) + "-" + (calSelected.get(Calendar.MONTH) + 1) + "-"
				+ calSelected.get(Calendar.DAY_OF_MONTH);

		beginTime = calSelected.get(Calendar.YEAR) + "-"
				+ NumberHelper.LeftPad_Tow_Zero((calSelected.get(Calendar.MONTH) + 1)) + "-" + "01";
		endTime = TimeUtils.getLastDayOfMonth(time);
	}

	/**
	 * 用于获取当前显示月份的时间
	 *
	 * @return 当前显示月份的时间
	 */
	private Calendar getCalendarStartDate() {
		calToday.setTimeInMillis(System.currentTimeMillis());
		calToday.setFirstDayOfWeek(iFirstDayOfWeek);

		if (calSelected.getTimeInMillis() == 0) {
			calStartDate.setTimeInMillis(System.currentTimeMillis());
			calStartDate.setFirstDayOfWeek(iFirstDayOfWeek);
		} else {
			calStartDate.setTimeInMillis(calSelected.getTimeInMillis());
			calStartDate.setFirstDayOfWeek(iFirstDayOfWeek);
		}

		return calStartDate;
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		//得到当前选中的是第几个单元格
		String s = "";
		int pos = currentGridView.pointToPosition((int) e.getX(), (int) e.getY());
		LinearLayout txtDay = (LinearLayout) currentGridView.findViewById(pos + 5000);
		if (txtDay != null) {
			if (txtDay.getTag() != null) {
				Date date = (Date) txtDay.getTag();
				calSelected.setTime(date);
				s = calSelected.get(Calendar.YEAR) + "-"
						+ NumberHelper.LeftPad_Tow_Zero((calSelected.get(Calendar.MONTH) + 1)) + "-"
						+ NumberHelper.LeftPad_Tow_Zero(calSelected.get(Calendar.DAY_OF_MONTH));
				String title = calSelected.get(Calendar.YEAR) + "-"
						+ NumberHelper.LeftPad_Tow_Zero((calSelected.get(Calendar.MONTH) + 1)) + "-"
						+ NumberHelper.LeftPad_Tow_Zero(calSelected.get(Calendar.DAY_OF_MONTH));
				updateCalendar(getMarkLists(s));
				yearTxt.setText(showYear());
				okBtn.setVisibility(View.VISIBLE);

			}
		}

		Log.i("TEST", "onSingleTapUp -  pos=" + pos);

		return false;
		//		}
	}

	@Override
	public void onResume() {

		super.onResume();

	};

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public List<CalendarEntity> getSchedulePosition() {
		return calendarLists;
	}

	@Override
	public boolean onLongClick(View v) {
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		try {

			// right to left swipe
			if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE) {
				saveSelectDays.clear();
				setNextViewItem(1, 1);

				return true;

			} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE) {
				saveSelectDays.clear();
				setPrevViewItem(1, 2);
				return true;

			}
		} catch (Exception e) {
			// nothing
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	/**
	 * 获取一周数据
	 * @param date 传入日期
	 * @return
	 */
	private List<CalendarEntity> getMarkLists(String date) {
		List<CalendarEntity> lists = new ArrayList<CalendarEntity>();
		String monday = TimeUtils.getMonday(date);
		String nextDay = null;
		CalendarEntity cale = null;
		SimpleDateFormat formatYMD = new SimpleDateFormat("yyyy-MM-dd");// formatYMD表示的是yyyy-MM-dd格式
		Date d = null;
		for (int i = 0; i < 7; i++) {
			if (i == 0) {
				cale = new CalendarEntity();
				try {
					nextDay = TimeUtils.getMonday(monday);
					d = formatYMD.parse(monday);
					cale.setDate(d);
					cale.setMark(true);
					cale.setFlag(0);//0是开头
				} catch (ParseException e) {
					e.printStackTrace();
				}

			} else if (i == 6) {
				nextDay = TimeUtils.getDayNext(nextDay);
				cale = new CalendarEntity();
				try {
					d = formatYMD.parse(nextDay);
					cale.setDate(d);
					cale.setMark(true);
					cale.setFlag(2);//是结尾
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} else {
				nextDay = TimeUtils.getDayNext(nextDay);
				cale = new CalendarEntity();
				try {
					d = formatYMD.parse(nextDay);
					cale.setDate(d);
					cale.setMark(true);
					cale.setFlag(1);//是中间
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			lists.add(cale);
		}
		saveSelectDays.clear();
		saveSelectDays.addAll(lists);
		return lists;

	}

	private Boolean equalsDate(Date date1, Date date2) {

		if (date1.getYear() == date2.getYear() && date1.getMonth() == date2.getMonth()
				&& date1.getDate() == date2.getDate()) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 更新选中操作
	 * @param list
	 */
	private void updateCalendar(List<CalendarEntity> list) {
		clearCalendar();
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < calendarLists.size(); j++) {
				if (equalsDate(list.get(i).getDate(), calendarLists.get(j).getDate())) {
					calendarLists.set(j, list.get(i));
					break;
				}

			}
		}

		currentGridAdapter.setList(calendarLists);
		currentGridAdapter.notifyDataSetChanged();
	}

	/**
	 * 清除日历背景
	 */
	private void clearCalendar() {

		for (int j = 0; j < calendarLists.size(); j++) {
			CalendarEntity ce = new CalendarEntity();
			ce.setDate(calendarLists.get(j).getDate());
			ce.setFlag(5);
			ce.setMark(false);
			calendarLists.set(j, ce);

		}

	}

	/**
	 * 截取选中周的字符串
	 * @return
	 */
	private String showYear() {
		String yearTxt = "";
		SimpleDateFormat formatYMD = new SimpleDateFormat("yyyy年MM月dd");// formatYMD表示的是yyyy-MM-dd格式
		String month = "";
		if (saveSelectDays.size() > 0) {
			String Start = formatYMD.format(saveSelectDays.get(0).getDate());
			String end = formatYMD.format(saveSelectDays.get(6).getDate());
			yearTxt = Start + "-" + end;
		} else {
			if (mMonthViewCurrentMonth == 11) {

				yearTxt = (mMonthViewCurrentYear) + "年" + NumberHelper.LeftPad_Tow_Zero((mMonthViewCurrentMonth + 1))
						+ "月" + NumberHelper.LeftPad_Tow_Zero(calToday.get(Calendar.DAY_OF_MONTH)) + "日";
			} else {

				yearTxt = (mMonthViewCurrentYear) + "年" + NumberHelper.LeftPad_Tow_Zero((mMonthViewCurrentMonth + 1))
						+ "月" + NumberHelper.LeftPad_Tow_Zero(calToday.get(Calendar.DAY_OF_MONTH)) + "日";
			}
		}
		return yearTxt;

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.right_txt) {
			if (saveSelectDays.size() > 0) {
				SimpleDateFormat formatYMD = new SimpleDateFormat("yyyy-MM-dd");// formatYMD表示的是yyyy-MM-dd格式
				date = formatYMD.format(saveSelectDays.get(0).getDate());
				Intent intent = new Intent(this, WeekEventsActivity.class);
				intent.putExtra("date", date);
				intent.putExtra("show_year", yearTxt.getText().toString().trim());
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			}

		}

	}

	/**
	 * 更新显示月份等数据
	 */
	private void updateYear() {

		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				yearTxt.setText(showYear());
				monthTxt.setText(NumberHelper.LeftPad_Tow_Zero((mMonthViewCurrentMonth + 1)) + "月");

			}
		});

	}
}
