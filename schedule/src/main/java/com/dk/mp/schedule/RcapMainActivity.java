package com.dk.mp.schedule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CalendarView;

import com.dk.mp.core.entity.RcapDetail;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.BroadcastUtil;
import com.dk.mp.core.util.TimeUtils;
import com.dk.mp.schedule.Adapter.RcapAdapter;
import com.dk.mp.schedule.db.RealmHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：janabo on 2016/12/26 17:41
 */
public class RcapMainActivity extends MyActivity {
    private CalendarView calendarView;
    private RecyclerView mRecyclerView;
    private List<RcapDetail> mData = new ArrayList<>();
    private RealmHelper realmHelper;
    private RcapAdapter mAdapter;
    private String date = TimeUtils.getToday();

    @Override
    protected int getLayoutID() {
        return R.layout.app_rcap_main;
    }

    @Override
    protected void initialize() {
        super.initialize();
        setTitle(TimeUtils.getToday2());
        realmHelper = new RealmHelper(this);
        BroadcastUtil.registerReceiver(this, mRefreshBroadcastReceiver, new String[]{"rcap_refresh"});
        initView();
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

    public void initView(){
        calendarView = (CalendarView) findViewById(R.id.calendarView);
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        mRecyclerView.setHasFixedSize ( true );
        mRecyclerView.setLayoutManager ( new LinearLayoutManager( mContext ) );
        mAdapter = new RcapAdapter (mContext,mData,date);
        mRecyclerView.setAdapter ( mAdapter );
        mRecyclerView.setNestedScrollingEnabled(false);

        getRcapDetials();
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
