package com.dk.mp.xg.wsjc.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dk.mp.core.widget.ArrayWheelAdapter;
import com.dk.mp.core.widget.WheelView;
import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.entity.Kf;

import java.util.List;

/**
 * 打分原因选择框
 * 作者：janabo on 2017/1/11 10:27
 */
public class WsjcDfyyPickActivity extends Activity {
    private String[] PLANETM;
    private WheelView mInfo;
    private List<Kf> kfs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_wsjc_df_pk);
        initialize();
    }

    protected void initialize() {
        Bundle bundle = getIntent().getExtras();
        kfs = (List<Kf>) bundle.getSerializable("kfs");
        PLANETM = new String[kfs.size()];
        if(kfs != null && kfs.size()>0){
            int i =0;
            for(Kf j : kfs){
                PLANETM[i] = j.getName();
                i++;
            }
        }
        findView();
    }

    private void findView(){
        mInfo = (WheelView) findViewById(R.id.info);
        mInfo.setAdapter(new ArrayWheelAdapter<>(PLANETM));
        mInfo.setCyclic(true);

        Button bt = (Button)findViewById(R.id.set);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = PLANETM[mInfo.getCurrentItem()];
                Intent in = new Intent();
                in.putExtra("kfs", str);
                in.putExtra("kfsid", kfs.get(mInfo.getCurrentItem()).getId());
                setResult(RESULT_OK, in);
                back();
            }
        });
        Button cancel = (Button)findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }
    public void back() {
        finish();
        overridePendingTransition(0, R.anim.push_down_out);
    }
}
