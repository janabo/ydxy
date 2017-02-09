package com.dk.mp.newoa.ui;

import android.support.design.widget.TabLayout;

import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.widget.MyViewpager;
import com.dk.mp.newoa.R;
import com.dk.mp.newoa.adapter.MyFragmentPagerAdapter;
import com.dk.mp.newoa.entity.OATab;
import com.dk.mp.newoa.fragment.GzzbListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 工作周报
 * 作者：janabo on 2017/1/4 10:16
 */
public class GzzbTabActivity extends MyActivity{
    TabLayout mTabLayout;
    MyViewpager mViewpager;
    private String title;

    @Override
    protected int getLayoutID() {
        return R.layout.oa_gzzb;
    }

    @Override
    protected void initialize() {
        super.initialize();
        title = getIntent().getStringExtra("title");
        setTitle(title);
        findView();
        initViewPager();
    }

    private void findView(){
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mViewpager = (MyViewpager) findViewById(R.id.viewpager);
    }

    private void initViewPager(){
        List<String> titles = new ArrayList<>();
        titles.add("待办");
        titles.add("已办");

        for(int i=0;i<titles.size();i++){
            mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(i)));
        }

        List<BaseFragment> fragments = new ArrayList<>();

        fragments.add(GzzbListFragment.newInstance(new OATab(titles.get(0),"db","OA_GZAP",title)));
        fragments.add(GzzbListFragment.newInstance(new OATab(titles.get(1),"yb","OA_GZAP",title)));

        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fragments,titles);
        mViewpager.setOffscreenPageLimit ( fragments.size ( ) );
        mViewpager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewpager);
    }

}
