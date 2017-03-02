package com.dk.mp.xg.wsjc.ui.zssgl;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.xg.R;

/**
 * 住宿生统计首页
 * 作者：janabo on 2017/1/18 10:56
 */
public class ZsstjMainActivity extends MyActivity{
    private ErrorLayout mError;
    private LinearLayout bzr,xb,xgc;//调宿统计,退宿统计,停宿统计
    private TextView bzr_s,bzr_name,xb_s,xb_name,xgc_s,xgc_name;//调宿统计名称第一个字,调宿统计名称,退宿统计名称第一个字,退宿统计名称

    @Override
    protected int getLayoutID() {
        return R.layout.app_zsstj_main;
    }

    @Override
    protected void initView() {
        super.initView();
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        bzr = (LinearLayout) findViewById(R.id.bzr);
        xb = (LinearLayout) findViewById(R.id.xb);
        xgc = (LinearLayout) findViewById(R.id.xgc);
        bzr_s = (TextView) findViewById(R.id.bzr_s);
        bzr_name = (TextView) findViewById(R.id.bzr_name);
        xb_s = (TextView) findViewById(R.id.xb_s);
        xb_name = (TextView) findViewById(R.id.xb_name);
        xgc_s = (TextView) findViewById(R.id.xgc_s);
        xgc_name = (TextView) findViewById(R.id.xgc_name);
        bzr_s.setText("调");
        bzr_name.setText("调宿统计");
        xb_s.setText("退");
        xb_name.setText("退宿统计");
        xgc_s.setText("停");
        xgc_name.setText("停宿统计");
        mError.setVisibility(View.GONE);
        bzr.setVisibility(View.VISIBLE);
        xb.setVisibility(View.VISIBLE);
        xgc.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initialize() {
        super.initialize();
        setTitle(getIntent().getStringExtra("title"));
    }

    /**
     * 调宿统计
     * @param v
     */
    public void tobzr(View v){
        toDetail(v,"2");
    }

    /**
     * 退宿统计
     * @param v
     */
    public void toxb(View v){
        toDetail(v,"3");
    }

    /**
     * 停宿统计
     * @param v
     */
    public void toxgc(View v){
        toDetail(v,"1");
    }

    /**
     * 跳转界面
     * @param role
     */
    public void toDetail(View view,String role){
        Intent intent = new Intent(mContext,ZsstjTabActivity.class);
        intent.putExtra("role","1");
        intent.putExtra("lx",role);
        intent.putExtra("title",getIntent().getStringExtra("title"));
        intent.putExtra("x",(view.getLeft() + view.getRight()) / 2);
        intent.putExtra("y",(view.getTop() + view.getBottom()) / 2 + StringUtils.dip2px(mContext,40));
        startActivity(intent);
    }

}
