package com.dk.mp.xg.wsjc.ui.zssgl;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.LinearLayout;

import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.widget.MyViewpager;
import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.adapter.MyFragmentPagerAdapter;
import com.dk.mp.xg.wsjc.fragment.ZssglFragment;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * 住宿生管理
 * 作者：janabo on 2017/1/18 15:22
 */
public class ZssglMainActivity extends MyActivity{
    TabLayout mTabLayout;
    MyViewpager mViewpager;
    private LinearLayout dropdown;
    private FloatingActionButton addTiaos,addTuis,addTings;//新增调宿,退宿，停宿
    ZssglFragment fragment1 = new ZssglFragment();
    ZssglFragment fragment2 = new ZssglFragment();
    @Override
    protected int getLayoutID() {
        return R.layout.app_zssgl_main;
    }

    @Override
    protected void initView() {
        super.initView();
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mViewpager = (MyViewpager) findViewById(R.id.viewpager);
        dropdown = (LinearLayout) findViewById(R.id.dropdown);
        addTiaos = (FloatingActionButton) findViewById(R.id.addTiaos);
        addTuis = (FloatingActionButton) findViewById(R.id.addTuis);
        addTings = (FloatingActionButton) findViewById(R.id.addTings);

        dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ZssglMainDialogActivity.class);
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.push_up_in, 0);
            }
        });

        addTiaos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,ZssglAddTiaoSuActivity.class);
//                intent.putExtra("x",(view.getLeft() + view.getRight()) / 2);
//                intent.putExtra("y",(view.getTop() + view.getBottom()) / 2 + StringUtils.dip2px(mContext,40));
                startActivity(intent);
            }
        });

        addTuis.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,ZssglAddTuiSuActivity.class);
//                intent.putExtra("x",(view.getLeft() + view.getRight()) / 2);
//                intent.putExtra("y",(view.getTop() + view.getBottom()) / 2 + StringUtils.dip2px(mContext,40));
                startActivity(intent);
            }
        });

        addTings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,ZsstjAddTingSuActivity.class);
//                intent.putExtra("x",(view.getLeft() + view.getRight()) / 2);
//                intent.putExtra("y",(view.getTop() + view.getBottom()) / 2 + StringUtils.dip2px(mContext,40));
                startActivity(intent);
            }
        });
        setTitle("调宿申请");
    }

    @Override
    protected void initialize() {
        super.initialize();
        initViewPager();
    }

    private void initViewPager() {
        List<String> titles = new ArrayList<>();
        titles.add("待我审核");
        titles.add("我已审核");
        for (int i = 0; i < titles.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(i)));
        }
        List<BaseFragment> fragments = new ArrayList<>();
        fragments.add(fragment1);
        fragments.add(fragment2);
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments, titles);
        mViewpager.setOffscreenPageLimit(fragments.size());
        mViewpager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewpager);
        fragment1.setType("3");
        fragment2.setType("4");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if(resultCode == RESULT_OK){
                    String lmlbname = data.getStringExtra("lmlbname");
                    String lmlb = data.getStringExtra("lmlb");
                    if(!"-1".equals(lmlb)){
                        setTitle(lmlbname);
                        fragment1.setLmlb(lmlb);
                        fragment2.setLmlb(lmlb);
                    }
                }
                break;
        }
    }
}
