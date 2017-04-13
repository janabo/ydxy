package com.dk.mp.main.home.ui;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dk.mp.core.db.AppManager;
import com.dk.mp.core.entity.App;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.BroadcastUtil;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.view.RecycleViewDivider;
import com.dk.mp.main.R;
import com.dk.mp.main.home.adapter.HpAdapter;
import com.dk.mp.main.home.data.HpDataProvider;
import com.dk.mp.main.home.receiver.NetworkConnectChangedReceiver;
import com.dk.mp.main.message.ui.MessageActivity;
import com.dk.mp.main.setting.ui.SettingActivity;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private LinearLayout warn_main;//是否有网络
    private String theme="标准";


    @Override
    protected int getLayoutID() {
        return R.layout.mp_main;
    }

    @Override
    protected void initView() {
        super.initView();
        theme =  getSharedPreferences().getValue("font_type");
        warn_main = (LinearLayout) findViewById(R.id.warn_main);
        //注册网络状态监听
        BroadcastUtil.registerReceiver(this, mRefreshBroadcastReceiver, new String[]{"checknetwork_true","checknetwork_false"});
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new NetworkConnectChangedReceiver(), filter);
    }

    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
        @SuppressLint("NewApi") @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("checknetwork_true")) {
                warn_main.setVisibility(View.GONE);
                BroadcastUtil.sendBroadcast(context,"ref_headerview");
            }
            if(action.equals("checknetwork_false")){
                warn_main.setVisibility(View.VISIBLE);
            }
        }
    };



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
    public void tomess(View v){
        Intent intent = new Intent(mContext, MessageActivity.class);
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
        if(StringUtils.isNotEmpty(theme) && !theme.equals(getSharedPreferences().getValue("font_type"))){
            reload();
        }else if(!StringUtils.isNotEmpty(theme) && StringUtils.isNotEmpty(getSharedPreferences().getValue("font_type"))){
            reload();
        }
        if(DeviceUtil.checkNet()){
            warn_main.setVisibility(View.GONE);
        }else{
            warn_main.setVisibility(View.VISIBLE);
        }

        apps.clear();
        apps.addAll(AppManager.getMyAppList(HomeActivity.this));
        dataProvider.changeDatas(apps);
        mWrappedAdapter.notifyDataSetChanged();
        //加速度传感器 注册监听
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                //还有SENSOR_DELAY_UI、SENSOR_DELAY_FASTEST、SENSOR_DELAY_GAME，SENSOR_DELAY_NORMAL等，
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
            if ((Math.abs(values[0]) > 20 || Math.abs(values[1]) > 20 || Math.abs(values[2]) > 60)) {
                //摇动手机后，设置button上显示的字为空
                mAdapter.deleteItem();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void showBrirthDayDialog(){
        Intent intent = new Intent(this,BrithdayActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    protected void initBirthTheme() {
        CoreSharedPreferencesHelper helper = new CoreSharedPreferencesHelper(this);
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//可以方便地修改日期格式
        String today = dateFormat.format(now);
        boolean isBrithDay;
        if(helper.getUser() == null){
            isBrithDay = false;
        }else if(StringUtils.isNotEmpty(helper.getUser().getBirthday())){
            String brithday = helper.getUser().getBirthday();
//            if(helper.getUser().getUserId().equals("portal")) {
//                brithday = "2017-03-21";
//            }
            if(brithday.length() == 10){
              isBrithDay = today.substring(5,today.length()).equals(brithday.substring(5,brithday.length()));
            }else{
                isBrithDay = today.substring(6,today.length()).equals(brithday.substring(5,brithday.length()));
            }
        }else{
            isBrithDay = false;
        }
        if (isBrithDay) {
            findViewById(R.id.top_hy).setVisibility(View.VISIBLE);
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(ContextCompat.getColor(this, com.dk.mp.core.R.color.colorPrimaryHy));
            }
            if(helper.getValue(today+helper.getLoginMsg().getUid())==null ||"false".equals(helper.getValue(today+helper.getLoginMsg().getUid()))) {
                helper.setValue(today + helper.getLoginMsg().getUid(), "true");
                showBrirthDayDialog();
            }
        }else{
            findViewById(R.id.top_hy).setVisibility(View.GONE);
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(ContextCompat.getColor(this, com.dk.mp.core.R.color.colorPrimary));
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        initBirthTheme();
    }

    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);//不设置进入退出动画
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }
}
