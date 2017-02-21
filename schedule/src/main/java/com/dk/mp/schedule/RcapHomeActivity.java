package com.dk.mp.schedule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dk.mp.core.entity.RcapDetail;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.BroadcastUtil;
import com.dk.mp.core.util.TimeUtils;
import com.dk.mp.schedule.Adapter.RcapAdapter;
import com.dk.mp.schedule.db.RealmHelper;
import com.dk.mp.schedule.manager.CalendarManager;
import com.dk.mp.schedule.widget.CollapseCalendarView;

import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 作者：janabo on 2017/1/6 14:07
 */
public class RcapHomeActivity extends MyActivity{
    private CollapseCalendarView calendarView;
    private CalendarManager mManager;
    private SimpleDateFormat sdf;
    private RecyclerView mRecyclerView;
    private List<RcapDetail> mData = new ArrayList<>();
    private RealmHelper realmHelper;
    private RcapAdapter mAdapter;
    private String mDate=TimeUtils.getToday();

    @Override
    protected int getLayoutID() {
        return R.layout.app_rcap_home;
    }

    @Override
    protected void initialize() {
        super.initialize();
        setTitle(TimeUtils.getToday2());
        realmHelper = new RealmHelper(this);
        sdf = new SimpleDateFormat("yyyy.MM.dd");
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        mRecyclerView.setHasFixedSize ( true );
        mRecyclerView.setLayoutManager ( new LinearLayoutManager( mContext ) );
        mAdapter = new RcapAdapter(mContext,mData,TimeUtils.getToday());
        mRecyclerView.setAdapter ( mAdapter );
        mRecyclerView.setNestedScrollingEnabled(false);
        getRcapDetials();
        calendarView = (CollapseCalendarView) findViewById(R.id.calendar);
        calendarView.showChinaDay(false);
        calendarView.hideHeader();
        BroadcastUtil.registerReceiver(this, mRefreshBroadcastReceiver, new String[]{"rcap_refresh"});
        mManager = new CalendarManager(LocalDate.now(),
                CalendarManager.State.MONTH, LocalDate.now().withYear(100),
                LocalDate.now().plusYears(100));
        /**
         * 日期选中监听器
         */
        calendarView.setDateSelectListener(new CollapseCalendarView.OnDateSelect() {

            @Override
            public void onDateSelected(LocalDate date) {
                setTitle(TimeUtils.formatDateTime(date.toString()));
                mDate = date.toString();
                getRcapDetials();
            }
        });
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, 9);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        try {
            for (int i = 0; i < 30; i++) {
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //初始化日历管理器
        calendarView.init(mManager);
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

    /**
     * 新增日程
     */
    public void toAdd(View v){
        Intent intent = new Intent(this,RcapDetailEditActivity.class);
        intent.putExtra("title","新建日程");
        startActivity(intent);
        overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
    }

    /**
     * 获取详细日程
     */
    public void getRcapDetials(){
        mData.clear();
        List<RcapDetail> rds = realmHelper.queryRcap(mDate);
        mData.addAll(rds);
        mAdapter.setDate(mDate);
        mAdapter.notifyDataSetChanged();
    }
}
