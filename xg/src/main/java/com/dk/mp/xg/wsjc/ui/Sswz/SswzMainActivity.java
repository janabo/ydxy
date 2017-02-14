package com.dk.mp.xg.wsjc.ui.Sswz;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.xg.R;

/**
 * 宿舍违章登记
 * 作者：janabo on 2017/1/16 16:50
 */
public class SswzMainActivity extends MyActivity {
    TextView jcrecord;

    @Override
    protected int getLayoutID() {
        return R.layout.app_wsjc_sm;
    }

    @Override
    protected void initialize() {
        super.initialize();
        setTitle(getIntent().getStringExtra("title"));
        jcrecord = (TextView) findViewById(R.id.jcrecord);
        jcrecord.setText("宿舍违章登记记录");
    }

    /**
     * 宿舍违章登记
     * @param v
     */
    public void towsjcdetail(View v){
        Intent intent = new Intent(this,SswzDjMainActivity.class);
        startActivity(intent);
    }

    /**
     * 宿舍违章登记记录
     * @param v
     */
    public void toWsjcjl(View v){
        Intent intent = new Intent(this,SswzRecordListActivity.class);
        startActivity(intent);
    }
}
