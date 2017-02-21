package com.dk.mp.oldoa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.Logger;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.util.TimeUtils;
import com.dk.mp.oldoa.R;
import com.dk.mp.oldoa.adapter.HomePageAdapter;
import com.dk.mp.oldoa.adapter.listViewAdapter;
import com.dk.mp.oldoa.entity.CalendarEntity;
import com.dk.mp.oldoa.entity.WeekAllEntity;
import com.dk.mp.oldoa.entity.WeekEventListEntity;
import com.dk.mp.oldoa.interfaces.IconPageIndicator;
import com.dk.mp.oldoa.interfaces.PageIndicator;
import com.dk.mp.oldoa.manager.WeekManager;
import com.dk.mp.oldoa.utils.http.HttpClientUtil;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 周报详情
 * @since 
 * @version 2014-7-17
 * @author lj.zhang
 */
public class WeekEventsActivity extends MyActivity implements OnClickListener {
	private ViewPager viewPager;
	private PageIndicator mIndicator;
	private LinearLayout linearLayout1;
	public int startX = 0;
	String getTime;
	String key = "4";
	int width = 320;
	private TextView yearTxt;

	HomePageAdapter hAdapte = null;
	String[] weekName = { "周一", "周二", "周三", "周四", "周五", "周六", "周日" };
	ArrayList<View> pageViews = new ArrayList<View>();
	WeekAllEntity weeKList = new WeekAllEntity();//列表数据
	DisplayMetrics dm = null;
	private String date;
	private String showYear;
	private TextView lookBtn = null;//查看按钮
	private String initDate;

	private Handler handler = new Handler() {//更新数据处理
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				hAdapte = new HomePageAdapter(getView(weeKList));
				viewPager.setAdapter(hAdapte);
				mIndicator.setViewPager(viewPager);
				viewPager.setCurrentItem(0);
				break;

			default:
				break;
			}
			hideProgressDialog();
		};
	};

	/**
	 * 初始化数据
	 */
	private void init() {
		date = getIntent().getStringExtra("date");
		if (date == null || !StringUtils.isNotEmpty(date)) {

			date = TimeUtils.getToday();
			if (getMarkLists(date).size() > 0) {
				SimpleDateFormat formatYMD = new SimpleDateFormat("yyyy年MM月dd");// formatYMD表示的是yyyy-MM-dd格式
				String start = formatYMD.format(getMarkLists(date).get(0).getDate());
				String end = formatYMD.format(getMarkLists(date).get(6).getDate());
				initDate = start + "-" + end;
			}

		}
		Logger.error("-------date--------" + date);
		showYear = getIntent().getStringExtra("show_year");
		dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;//宽度
		linearLayout1 = (LinearLayout) findViewById(R.id.linearlayout1);
		viewPager = (ViewPager) findViewById(R.id.pager);
		mIndicator = (IconPageIndicator) findViewById(R.id.indicator);
		yearTxt = (TextView) findViewById(R.id.show_year);
		if (StringUtils.isNotEmpty(showYear)) {

			yearTxt.setText(showYear);

		} else {
			yearTxt.setText(initDate);

		}

		lookBtn = (TextView) findViewById(R.id.right_txt);
		lookBtn.setText(getResources().getString(R.string.look_txt));
		lookBtn.setVisibility(View.VISIBLE);
		lookBtn.setOnClickListener(this);
		mIndicator.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				changeTab(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.week_detail_view);
		setTitle(getIntent().getStringExtra("title"));
		init();
		initTitle(0);
		getList(date);
	};

	/**
	 * 获取周报列表 
	 */
	private void getList(final String date) {
		showProgressDialog();
		try {
			if (DeviceUtil.checkNet()) {
				Map<String,String> map = new HashMap<String,String>();
				map.put("date", date);
				HttpClientUtil.post("apps/oa/getWeek", map, new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						weeKList = WeekManager.getIntence().getWeekListInfos(responseInfo);
						handler.sendEmptyMessage(0);
					}
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						handler.sendEmptyMessage(0);
					}
				});
			} else {
				handler.sendEmptyMessage(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 生成每一天view
	 * @param week
	 * @return
	 */
	private ArrayList<View> getView(final WeekAllEntity week) {
		try {
			pageViews.clear();
			viewPager.removeAllViews();
			if (null != week && week.getList().size() > 0) {
				for (int i = 0; i < week.getList().size(); i++) {
					final String date = week.getList().get(i).getDateTime();
					if (week.getList().get(i).getWeekL().size() > 0) {
						ListView listView = new ListView(WeekEventsActivity.this);
						for (int j = 0; j < week.getList().get(i).getWeekL().size(); j++) {
							listView.setCacheColorHint(getResources().getColor(R.color.transparent));
							listView.setDividerHeight(1);
							listView.setDivider(getResources().getDrawable(R.mipmap.view_divice));
							listView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
									LayoutParams.FILL_PARENT));
							listViewAdapter adapter = new listViewAdapter(WeekEventsActivity.this, week.getList()
									.get(i).getWeekL());
							listView.setAdapter(adapter);
							listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
									WeekEventListEntity list = (WeekEventListEntity) adapter
											.getItemAtPosition(position);
									Intent intent = new Intent(WeekEventsActivity.this, WeekDetailsActivity.class);
									intent.putExtra("data", (Serializable) list);
									intent.putExtra("date", date);
									intent.putExtra("title", getIntent().getStringExtra("title"));
									startActivity(intent);

								}
							});
						}
						pageViews.add(listView);
					}
				}
			} else {
				for (int i = 0; i < 7; i++) {
					TextView textView = new TextView(WeekEventsActivity.this);
					textView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
							LayoutParams.FILL_PARENT));
					textView.setGravity(Gravity.CENTER);
					textView.setText("暂无周报查询");
					textView.setTextSize(16);
					textView.setTextColor(getResources().getColor(R.color.no_data_color));
					pageViews.add(textView);

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return pageViews;

	}

	/**
	 * 获取一周数据
	 * @param date 传人日期
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
		return lists;

	}

	/**
	 * 更改选中的星期下标
	 * @param arg0 选中的星期下标
	 */
	private void changeTab(int arg0) {
		int dd = arg0 % 7;
		for (int i = 0; i < linearLayout1.getChildCount(); i++) {
			LinearLayout parent = (LinearLayout) linearLayout1.getChildAt(i);
			TextView text = (TextView) parent.getChildAt(0);
			TextView textD = (TextView) parent.getChildAt(1);
			if (i == dd) {
				text.setTextColor(getResources().getColor(R.color.week_txt_bg));
				text.setTextSize(18);
				textD.setBackgroundColor(getResources().getColor(R.color.week_txt_bg));
			} else {
				text.setBackgroundColor(getResources().getColor(R.color.white));
				text.setTextSize(16);
				text.setTextColor(getResources().getColor(R.color.black));
				textD.setBackgroundColor(getResources().getColor(R.color.white));
			}
		}
	}

	/**
	 * 生成顶部星期栏
	 * @param tag
	 */
	private void initTitle(int tag) {
		linearLayout1.removeAllViews();
		LinearLayout l = new LinearLayout(this);
		l.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1.0f));
		l.setOrientation(LinearLayout.VERTICAL);
		for (int i = 0; i < 7; i++) {
			LayoutInflater mInflater = LayoutInflater.from(this);
			View view = mInflater.inflate(R.layout.week_top_title, null);
			final TextView title = (TextView) view.findViewById(R.id.week_title);
			final TextView titleDivice = (TextView) view.findViewById(R.id.week_divice);
			if (i == tag) {
				title.setTextColor(getResources().getColor(R.color.week_txt_bg));
				title.setTextSize(18);
				title.setBackgroundColor(getResources().getColor(R.color.white));
				titleDivice.setBackgroundColor(getResources().getColor(R.color.week_txt_bg));
			} else {
				title.setTextColor(getResources().getColor(R.color.black));
				title.setBackgroundColor(getResources().getColor(R.color.white));
				titleDivice.setBackgroundColor(getResources().getColor(R.color.white));
				title.setTextSize(16);
			}
			title.setText(weekName[i]);
			view.setLayoutParams(new LayoutParams(width / 7, LayoutParams.MATCH_PARENT));
			view.setOnClickListener(new MyOnClickListener(i));
			linearLayout1.addView(view);
		}

	}

	/**
	 * 
	 * @since 栏目的点击效果
	 * @version 2014-12-12
	 * @author lj.zhang
	 */
	private class MyOnClickListener implements OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			viewPager.setCurrentItem(index);
			changeTab(index);
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.right_txt) {
			Intent intent = new Intent(this, WeekMainActivity.class);
			intent.putExtra("title", getIntent().getStringExtra("title"));
			startActivity(intent);
		}

	}

}
