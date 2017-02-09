package com.dk.mp.schedule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dk.mp.core.entity.RcapDetail;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.BroadcastUtil;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.TimeUtils;
import com.dk.mp.schedule.Adapter.RcapAdapter;
import com.dk.mp.schedule.db.RealmHelper;
import com.dk.mp.schedule.utils.CalendarUtil;
import com.dk.mp.schedule.view.MainLayoutContainer;
import com.dk.mp.schedule.view.MonthView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * 日程安排
 * 作者：janabo on 2016/12/29 09:34
 */
public class RcapActivity extends MyActivity implements MonthView.OnCellSelectListener{
    FrameLayout calendarLayout;
    private RcapAdapter mAdapter;
    private List<RcapDetail> mData = new ArrayList<>();
    private String date = TimeUtils.getToday();
    private RealmHelper realmHelper;

    @Override
    protected int getLayoutID() {
        return R.layout.app_rcap;
    }

    @Override
    protected void initialize() {
        super.initialize();
        initTitle();
        realmHelper = new RealmHelper(this);
        calendarLayout = (FrameLayout)findViewById(R.id.content);
        initCalendarLayout();
        initInfoAdapter();
        BroadcastUtil.registerReceiver(this, mRefreshBroadcastReceiver, new String[]{"rcap_refresh"});
    }

    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("rcap_refresh")) {
                getRcapDetials();
            }
        }
    };

    public void onMonthViewHeightChanged(int height){

    }

    private void initInfoAdapter() {
        mAdapter = new RcapAdapter(mContext,mData,date);
        mainLayoutContainer.setAdapter(mAdapter);
    }

    MainLayoutContainer mainLayoutContainer;
    private void initCalendarLayout() {
        mainLayoutContainer = new MainLayoutContainer(mContext, this);
        mainLayoutContainer.setOnCellSelectListener(this);
        calendarLayout.addView(mainLayoutContainer, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /**
     * 初始化标题
     */
    private void initTitle() {
        LinearLayout weekLayout = (LinearLayout)findViewById(R.id.weeks);
        String[] weekNames = getResources().getStringArray(R.array.week_names);
        for(int i = 0; i < 7; i++){
            TextView textView = new TextView(mContext);
            textView.setText(weekNames[i]);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(DeviceUtil.dip2px(mContext,4));
            textView.setTypeface(Typeface.SANS_SERIF);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            params.weight = 1;
            weekLayout.addView(textView, params);
        }
    }

    @Override
    public void onCellSelect(MonthView monthView, Calendar calendar) {
        date = CalendarUtil.formatTime(calendar.getTimeInMillis(), CalendarUtil.DATE);
        setTitle(date);
        getRcapDetials();//重新获取日历数据
    }

    /**
     * 获取详细日程
     */
    public void getRcapDetials(){
        mData.clear();
        List<RcapDetail> rds = realmHelper.queryRcap(date);
        mData.addAll(rds);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 新增日程
     */
    public void toAdd(View v){
        Intent intent = new Intent(this,RcapDetailEditActivity.class);
        intent.putExtra("title","新建日程");
        startActivity(intent);
        overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
    }

}
