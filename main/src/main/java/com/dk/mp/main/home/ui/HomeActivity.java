package com.dk.mp.main.home.ui;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.dk.mp.core.db.AppManager;
import com.dk.mp.core.entity.App;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.view.RecycleViewDivider;
import com.dk.mp.main.R;
import com.dk.mp.main.home.adapter.HpAdapter;
import com.dk.mp.main.home.data.HpDataProvider;
import com.dk.mp.main.setting.ui.SettingActivity;
import com.dk.mp.txl.ui.TxlDepartActivity;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：janabo on 2016/12/14 15:25
 */
public class HomeActivity extends MyActivity implements SensorEventListener {

    private RecyclerView mRecyclerView;
    private ImageView main_setting;
    private ImageView msgCount;

    private RecyclerViewDragDropManager mRecyclerViewDragDropManager;
    private HpAdapter mAdapter;
    private RecyclerView.Adapter mWrappedAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<App> apps = new ArrayList<App>();
    private HpDataProvider dataProvider;

    private SensorManager mSensorManager;//定义sensor管理器

    @Override
    protected int getLayoutID() {
        return R.layout.mp_main;
    }

    @Override
    protected void initialize() {
        super.initialize();
//初始化可拖拽列表
//        apps.addAll(AppManager.getMyAppList(this));
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        main_setting = (ImageView)findViewById(R.id.main_setting);
        msgCount = (ImageView)findViewById(R.id.msgcount);
        // drag & drop manager
        mRecyclerViewDragDropManager = new RecyclerViewDragDropManager();
//        mRecyclerViewDragDropManager.setDraggingItemShadowDrawable((NinePatchDrawable) ContextCompat.getDrawable(this, R.drawable.material_shadow_z3));
        mLayoutManager = new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
        // Start dragging after long press
        mRecyclerViewDragDropManager.setInitiateOnLongPress(true);
        mRecyclerViewDragDropManager.setInitiateOnMove(false);
        mRecyclerViewDragDropManager.setLongPressTimeout(750);
        //adapter
        dataProvider = new HpDataProvider(apps);
        mAdapter = new HpAdapter(dataProvider,this);
//        mAdapter = myItemAdapter;
        mWrappedAdapter = mRecyclerViewDragDropManager.createWrappedAdapter(mAdapter);      // wrap for dragging
        final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mWrappedAdapter);  // requires *wrapped* adapter
        mRecyclerView.setItemAnimator(animator);
        mRecyclerViewDragDropManager.attachRecyclerView(mRecyclerView);
        //登录跟新界面图标
//        getRxSharedPreferences().getString("user_info").asObservable().subscribe(new Action1<String>() {
//            @Override
//            public void call(String s) {
//                apps.clear();
//                apps.addAll(AppManager.getMyAppList(MainActivity.this));
//                dataProvider.changeDatas(apps);
//                mWrappedAdapter.notifyDataSetChanged();
//            }
//        });
        mRecyclerView.addItemDecoration(new RecycleViewDivider(HomeActivity.this, GridLayoutManager.VERTICAL, 1, Color.rgb(201, 201, 201)));//添加分割线
        mRecyclerView.addItemDecoration(new RecycleViewDivider(HomeActivity.this, GridLayoutManager.HORIZONTAL, 1, Color.rgb(201, 201, 201)));//添加分割线

        //获取传感器管理服务
        mSensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
    }

    /**
     * 设置
     * @param v
     */
    public void tosetting(View v){
        Intent intent = new Intent(mContext, SettingActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转学校新闻
     * @param v
     */
    public void toxxxw(View v){
//        Intent intent = new Intent(mContext, NewsListActivity.class);
//        intent.putExtra("title","校园新闻");
//        startActivity(intent);

        Intent intent = new Intent(mContext, TxlDepartActivity.class);
        intent.putExtra("title","通讯录");
        startActivity(intent);

    }

    @Override
    public void onPause() {
        mRecyclerViewDragDropManager.cancelDrag();
        super.onPause();
        AppManager.saveAppIndex(this,dataProvider.getmData());
    }

    @Override
    protected void onDestroy() {
        {
            if (mRecyclerViewDragDropManager != null) {
                mRecyclerViewDragDropManager.release();
                mRecyclerViewDragDropManager = null;
            }

            if (mRecyclerView != null) {
                mRecyclerView.setItemAnimator(null);
                mRecyclerView.setAdapter(null);
                mRecyclerView = null;
            }

            if (mWrappedAdapter != null) {
                WrapperAdapterUtils.releaseAll(mWrappedAdapter);
                mWrappedAdapter = null;
            }
            mAdapter = null;
            mLayoutManager = null;

            super.onDestroy();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        apps.clear();
        apps.addAll(AppManager.getMyAppList(HomeActivity.this));
        dataProvider.changeDatas(apps);
        mWrappedAdapter.notifyDataSetChanged();
        //加速度传感器 注册监听
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                //还有SENSOR_DELAY_UI、SENSOR_DELAY_FASTEST、SENSOR_DELAY_GAME等，
                //根据不同应用，需要的反应速率不同，具体根据实际情况设定
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onStop() {
        super.onStop();
        //取消注册
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();
        //values[0]:X轴，values[1]：Y轴，values[2]：Z轴
        float[] values = sensorEvent.values;
        if (sensorType == Sensor.TYPE_ACCELEROMETER) {
               /*因为一般正常情况下，任意轴数值最大就在9.8~10之间，只有在你突然摇动手机
              *的时候，瞬时加速度才会突然增大或减少，所以，经过实际测试，只需监听任一轴的
              * 加速度大于14的时候，改变你需要的设置就OK了
              */
            if ((Math.abs(values[0]) > 20 || Math.abs(values[1]) > 20 || Math.abs(values[2]) > 20)) {
                //摇动手机后，设置button上显示的字为空
                mAdapter.deleteItem();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
