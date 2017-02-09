package com.dk.mp.schedule.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.dk.mp.core.entity.RcapDetail;
import com.dk.mp.core.util.TimeUtils;
import com.dk.mp.schedule.Adapter.RcapAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.dk.mp.core.http.HttpUtil.mContext;


/**
 * 日历下边的信息容器
 * 作者：janabo on 2016/12/29 17:41
 */
public class InfoContainer extends RelativeLayout {
    private MainLayoutContainer mainLayoutContainer;
    private RecyclerView mRecyclerView;
    private float touchSlop;
    private RcapAdapter mAdapter;
    private List<RcapDetail> mData = new ArrayList<>();
    private String date = TimeUtils.getToday();
    public InfoContainer(Context context) {
        super(context);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mRecyclerView = new RecyclerView(context);
        addView(mRecyclerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mRecyclerView.setHasFixedSize ( true );
        mRecyclerView.setLayoutManager ( new LinearLayoutManager( mContext ) );
        mAdapter = new RcapAdapter (mContext,mData,date);
        mRecyclerView.setAdapter ( mAdapter );
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mainLayoutContainer = (MainLayoutContainer) getParent();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                touchLastY = ev.getRawY();
                firstTouchY = touchLastY;
                return false;
            case MotionEvent.ACTION_MOVE:
                float dy = ev.getRawY() - touchLastY;
                if(dy < 0 && mainLayoutContainer.monthViewStatus() != MainLayoutContainer.MONTHVIEW_MIN){
                    return true;
                }else if(dy > 0){
                    return true;
                }
        }
        return super.onInterceptTouchEvent(ev);
    }

    float touchLastY, firstTouchY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = event.getRawY() - touchLastY;
                mainLayoutContainer.onMonthViewMove((int) dy);
                touchLastY = event.getRawY() + dy - (int)dy;
//                Log.v(TAG, "onTouchEvent, action move, touchLastY is " + touchLastY);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mainLayoutContainer.onMonthViewAnimation(event.getRawY() - firstTouchY);
                break;
        }
        return true;
    }

    public void setAdapter(RcapAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }
}
