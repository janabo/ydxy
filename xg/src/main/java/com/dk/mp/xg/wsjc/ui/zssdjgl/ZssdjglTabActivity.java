package com.dk.mp.xg.wsjc.ui.zssdjgl;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.view.View;

import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.widget.MyViewpager;
import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.adapter.MyFragmentPagerAdapter;
import com.dk.mp.xg.wsjc.fragment.ZssdjglListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 住宿登记管理---周末留宿学生登记与日常请假学生登记
 * 作者：janabo on 2017/2/7 13:43
 */
public class ZssdjglTabActivity extends MyActivity{
    TabLayout mTabLayout;
    MyViewpager mViewpager;
//    LinearLayout search_button;

    @Override
    protected int getLayoutID() {
        return R.layout.app_zssdjgl_tab;
    }

    @Override
    protected void initView() {
        super.initView();
        setTitle(getIntent().getStringExtra("title"));
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mViewpager = (MyViewpager) findViewById(R.id.viewpager);
//        search_button = (LinearLayout) findViewById(R.id.search_button);

    }



    @Override
    protected void initialize() {
        super.initialize();
        initViewPager();
    }

    private void initViewPager(){
        List<String> titles = new ArrayList<>();
        titles.add("周末留宿学生登记");
        titles.add("日常请假学生登记");

        for(int i=0;i<titles.size();i++){
            mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(i)));
        }

        List<BaseFragment> fragments = new ArrayList<>();

        fragments.add(ZssdjglListFragment.newInstance("1"));
        fragments.add(ZssdjglListFragment.newInstance("2"));

        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fragments,titles);
        mViewpager.setOffscreenPageLimit ( fragments.size ( ) );
        mViewpager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewpager);
    }

    /**
     * 新增留宿学生
     * @param
     */
    public void addLiusu(View view){
        Intent intent = new Intent(this,ZssdjglAddLiusuActivity.class);
        intent.putExtra("type","1");
//        intent.putExtra("x",(view.getLeft() + view.getRight()) / 2);
//        intent.putExtra("y",(view.getTop() + view.getBottom()) / 2 + StringUtils.dip2px(mContext,40));
        startActivity(intent);
    }

    /**
     * 新增请假学生
     * @param
     */
    public void addQingjia(View view){
        Intent intent = new Intent(this,ZssdjglAddLiusuActivity.class);
        intent.putExtra("type","2");
//        intent.putExtra("x",(view.getLeft() + view.getRight()) / 2);
//        intent.putExtra("y",(view.getTop() + view.getBottom()) / 2 + StringUtils.dip2px(mContext,40));
        startActivity(intent);
    }
}
