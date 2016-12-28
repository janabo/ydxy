package com.dk.mp.main.home.ui;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.dk.mp.core.db.AppManager;
import com.dk.mp.core.entity.App;
import com.dk.mp.core.ui.MyActivity;
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
public class HomeActivity extends MyActivity{

    private RecyclerView mRecyclerView;
    private ImageView main_setting;
    private ImageView msgCount;

    private RecyclerViewDragDropManager mRecyclerViewDragDropManager;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter mWrappedAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<App> apps = new ArrayList<App>();
    private HpDataProvider dataProvider;

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
//        mRecyclerView.addItemDecoration(new RecycleViewDivider(MainActivity.this, GridLayoutManager.VERTICAL, 1, Color.rgb(201, 201, 201)));//添加分割线
//        mRecyclerView.addItemDecoration(new RecycleViewDivider(MainActivity.this, GridLayoutManager.HORIZONTAL, 1, Color.rgb(201, 201, 201)));//添加分割线

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
    }

}
