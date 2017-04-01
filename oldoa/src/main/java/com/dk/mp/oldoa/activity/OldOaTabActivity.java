package com.dk.mp.oldoa.activity;


import android.support.design.widget.TabLayout;

import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.widget.MyViewpager;
import com.dk.mp.oldoa.R;
import com.dk.mp.oldoa.adapter.MyFragmentPagerAdapter;
import com.dk.mp.oldoa.entity.OATab;
import com.dk.mp.oldoa.fragment.OAListFragment;
import com.dk.mp.oldoa.utils.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * 老oa
 * 作者：janabo on 2017/4/1 15:00
 */
public class OldOaTabActivity extends com.dk.mp.core.ui.MyActivity {
    TabLayout mTabLayout;
    MyViewpager mViewpager;
    private String title;

    @Override
    protected int getLayoutID() {
        return R.layout.oa_tab_old;
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

        fragments.add(OAListFragment.newInstance(new OATab(titles.get(0),"readnot","0", Constant.DAI_BAN_INTERFACE)));
        fragments.add(OAListFragment.newInstance(new OATab(titles.get(1),"readed","1",Constant.YI_BAN_INTERFACE)));

        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fragments,titles);
        mViewpager.setOffscreenPageLimit ( fragments.size ( ) );
        mViewpager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewpager);
    }
}
