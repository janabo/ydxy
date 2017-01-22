package com.dk.mp.xg.wsjc.ui.zssgl;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.xg.R;

/**
 * 住宿生统计首页
 * 作者：janabo on 2017/1/18 10:56
 */
public class ZsstjMainActivity extends MyActivity{
    private LinearLayout bzr,xb,xgc,fdy;//调宿统计,退宿统计,停宿统计
    private TextView bzr_s,bzr_name,xb_s,xb_name,xgc_s,xgc_name;//调宿统计名称第一个字,调宿统计名称,退宿统计名称第一个字,退宿统计名称

    @Override
    protected int getLayoutID() {
        return R.layout.app_wsjctj_main;
    }

    @Override
    protected void initView() {
        super.initView();
        bzr = (LinearLayout) findViewById(R.id.bzr);
        xb = (LinearLayout) findViewById(R.id.xb);
        xgc = (LinearLayout) findViewById(R.id.xgc);
        fdy = (LinearLayout) findViewById(R.id.fdy);
        bzr_s = (TextView) findViewById(R.id.bzr_s);
        bzr_name = (TextView) findViewById(R.id.bzr_name);
        xb_s = (TextView) findViewById(R.id.xb_s);
        xb_name = (TextView) findViewById(R.id.xb_name);
        xgc_s = (TextView) findViewById(R.id.xgc_s);
        xgc_name = (TextView) findViewById(R.id.xgc_name);
        fdy.setVisibility(View.GONE);
        bzr_s.setText("调");
        bzr_name.setText("调宿统计");
        xb_s.setText("退");
        xb_name.setText("退宿统计");
        xgc_s.setText("停");
        xgc_name.setText("停宿统计");
    }

    @Override
    protected void initialize() {
        super.initialize();
        setTitle("在校生住宿管理");
    }

    /**
     * 调宿统计
     * @param v
     */
    public void tobzr(View v){
        Intent intent = new Intent(mContext,ZsstjTabActivity.class);
        intent.putExtra("role","1");
        intent.putExtra("lx","1");
        startActivity(intent);
    }

    /**
     * 退宿统计
     * @param v
     */
    public void toxb(View v){
        Intent intent = new Intent(mContext,ZsstjTabActivity.class);
        intent.putExtra("role","1");
        intent.putExtra("lx","2");
        startActivity(intent);
    }

    /**
     * 停宿统计
     * @param v
     */
    public void toxgc(View v){
        Intent intent = new Intent(mContext,ZsstjTabActivity.class);
        intent.putExtra("role","1");
        intent.putExtra("lx","3");
        startActivity(intent);
    }

}
