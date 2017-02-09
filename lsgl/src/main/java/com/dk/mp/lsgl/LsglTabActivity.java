package com.dk.mp.lsgl;

import android.support.design.widget.TabLayout;

import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.widget.MyViewpager;
import com.dk.mp.lsgl.adapter.MyFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongqs on 2017/1/23.
 */

public class LsglTabActivity extends MyActivity{

    private TabLayout mTabLayout;
    private MyViewpager mViewpager;
    private LsglListFragment ss = new LsglListFragment();
    private LsglListFragment xb = new LsglListFragment();

    @Override
    protected int getLayoutID() {
        return R.layout.app_lsgl_tab;
    }

    @Override
    protected void initialize() {
        super.initialize();
        initViews();
    }

    private void initViews() {
        setTitle("留宿学生管理");
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        List<String> titles = new ArrayList<>();
        titles.add("宿舍视角");
        titles.add("系部视角");
        for(int i=0;i<titles.size();i++){
            mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(i)));
        }

        ss.setRole(getIntent().getStringExtra("role"));
        ss.setType("1");
        xb.setRole(getIntent().getStringExtra("role"));
        xb.setType("2");
        List<BaseFragment> fragments = new ArrayList<>();
        fragments.add(ss);
        fragments.add(xb);

        mViewpager = (MyViewpager)findViewById(R.id.viewpager);
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fragments,titles);
        mViewpager.setOffscreenPageLimit ( fragments.size ( ) );
        mViewpager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewpager);
    }
}
