package com.dk.mp.oldoa.activity;

import android.widget.TextView;

import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.oldoa.R;
import com.dk.mp.oldoa.entity.WeekEventListEntity;

public class WeekDetailsActivity extends MyActivity {

	private TextView dateTxt;//日期
	private TextView placeTxt;//地点
	private TextView reUnitTxt;//责任单位
	private TextView timeTxt;//时间
	private TextView leaderTxt;//出席领导
	private TextView contentTxt;//内容
	private TextView yearTxt;//显示的时间
	private WeekEventListEntity weL;//传递过来的对象数据
	private String date;

	@Override
	protected int getLayoutID() {
		return R.layout.week_item;
	}

	@Override
	protected void initView() {
		super.initView();
		setTitle(getIntent().getStringExtra("title"));
		findUi();
	}

//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.week_item);
//
//	}

	private void findUi() {
		weL = (WeekEventListEntity) getIntent().getSerializableExtra("data");
		date = getIntent().getStringExtra("date");
		yearTxt = (TextView) findViewById(R.id.show_year);
		dateTxt = (TextView) findViewById(R.id.date_txt);
		placeTxt = (TextView) findViewById(R.id.place_txt);
		reUnitTxt = (TextView) findViewById(R.id.unit_txt);
		leaderTxt = (TextView) findViewById(R.id.leader_txt);
		contentTxt = (TextView) findViewById(R.id.content_txt);
		timeTxt = (TextView) findViewById(R.id.time_txt);
		if (StringUtils.isNotEmpty(date)) {

			yearTxt.setText(date);

		}
		if (null != weL) {

			if (StringUtils.isNotEmpty(weL.getAddress())) {
				placeTxt.setText(weL.getAddress());
			}
			if (StringUtils.isNotEmpty(weL.getContent())) {
				contentTxt.setText(weL.getContent());
			}
			if (StringUtils.isNotEmpty(weL.getDate())) {
				dateTxt.setText(weL.getDate());
			}
			if (StringUtils.isNotEmpty(weL.getReponsibleUnit())) {
				reUnitTxt.setText(weL.getReponsibleUnit());
			}
			if (StringUtils.isNotEmpty(weL.getTime())) {
				timeTxt.setText(weL.getTime());
			}
			if (StringUtils.isNotEmpty(weL.getAttendLeader())) {
				leaderTxt.setText(weL.getAttendLeader());
			}

		}

	}

}
