package com.dk.mp.xg.wsjc.ui;

import android.content.Intent;
import android.view.View;

import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.xg.R;

/**
 * 卫生检查统计
 * 作者：janabo on 2017/1/13 10:49
 */
public class WsjcTjMainActivity extends MyActivity{
    @Override
    protected int getLayoutID() {
        return R.layout.app_wsjctj_main;
    }

    @Override
    protected void initView() {
        super.initView();
        setTitle(getIntent().getStringExtra("title"));
    }

    @Override
    protected void initialize() {
        super.initialize();
    }

    /**
     * 班主任统计界面
     * @param v
     */
    public void tobzr(View v){
        Intent intent = new Intent(this,WsjcTjTabActivity.class);
        intent.putExtra("role","1");
        startActivity(intent);
    }

    /**
     * 系部统计界面
     * @param v
     */
    public void toxb(View v){
        Intent intent = new Intent(this,WsjcTjTabActivity.class);
        intent.putExtra("role","3");
        startActivity(intent);
    }

    /**
     * 学工处统计界面
     * @param v
     */
    public void toxgc(View v){
        Intent intent = new Intent(this,WsjcTjTabActivity.class);
        intent.putExtra("role","4");
        startActivity(intent);
    }

    /**
     * 辅导员统计界面
     * @param v
     */
    public void tofdy(View v){

    }

}
