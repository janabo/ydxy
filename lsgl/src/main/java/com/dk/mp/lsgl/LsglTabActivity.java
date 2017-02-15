package com.dk.mp.lsgl;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.Button;

import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.widget.MyViewpager;
import com.dk.mp.lsgl.adapter.MyFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongqs on 2017/1/23.
 */

public class LsglTabActivity extends MyActivity implements View.OnClickListener{

    private TabLayout mTabLayout;
    private MyViewpager mViewpager;
    private Button search;
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

        search = (Button) findViewById(R.id.search);
        search.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        int i = mViewpager.getCurrentItem();

        Intent intent = new Intent(LsglTabActivity.this,SearchActivity.class);
        intent.putExtra("role",getIntent().getStringExtra("role"));
        intent.putExtra("type",(i+1)+"");
        intent.putExtra("x",(view.getLeft() + view.getRight()) / 2);
        intent.putExtra("y",(view.getTop() + view.getBottom()) / 2 + StringUtils.dip2px(LsglTabActivity.this,40));
        startActivity(intent);
    }
}
