package com.dk.mp.xg.wsjc.ui;

import android.support.v4.view.ViewPager;

import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.adapter.ImagePagerAdapter;

import java.util.List;

/**
 * 作者：janabo on 2017/3/7 10:04
 */
public class ImagePreviewActivity extends MyActivity{
    private ViewPager mViewPager;
    private List<String> mList;
    private int index;
    private ImagePagerAdapter mAdapter;

    @Override
    protected int getLayoutID() {
        return R.layout.image_preview;
    }

    @Override
    protected void initView() {
        super.initView();
        setTitle("查看图片");
        mViewPager = (ViewPager) findViewById(R.id.viewpage);
    }

    @Override
    protected void initialize() {
        super.initialize();
        mList = getIntent().getStringArrayListExtra("list");
        index = getIntent().getIntExtra("index", 0);
        mAdapter = new ImagePagerAdapter(ImagePreviewActivity.this, mList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int arg0) {
            }
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        mViewPager.setCurrentItem(index);
    }
}
