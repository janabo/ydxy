package com.dk.mp.xg.wsjc.ui;

import android.content.Intent;
import android.view.View;

import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.xg.R;

/**
 * 卫生检查扫码
 * 作者：janabo on 2017/1/9 11:22
 */
public class WsjcMainActivity extends MyActivity{
    @Override
    protected int getLayoutID() {
        return R.layout.app_wsjc_sm;
    }

    @Override
    protected void initialize() {
        super.initialize();
        setTitle(getIntent().getStringExtra("title"));
    }

    /**
     * 卫生检查打分
     * @param v
     */
    public void towsjcdetail(View v){
        Intent intent = new Intent(this,WsjcDetailActivity.class);
        startActivity(intent);
    }

    /**
     * 卫生检查记录
     * @param v
     */
    public void toWsjcjl(View v){
        Intent intent = new Intent(this,WsjcRecordMainActivity.class);
        startActivity(intent);
    }

}
