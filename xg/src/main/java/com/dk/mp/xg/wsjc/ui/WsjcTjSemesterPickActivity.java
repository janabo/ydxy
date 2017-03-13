package com.dk.mp.xg.wsjc.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.dk.mp.core.widget.ArrayWheelAdapter;
import com.dk.mp.core.widget.WheelView;
import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.entity.Common;

import java.util.List;

/**
 * 按学期统计 选择学期和模板
 * 作者：janabo on 2017/1/16 14:17
 */
public class WsjcTjSemesterPickActivity extends Activity {
    private WheelView semester;
    private WheelView template;
    private String[] PLANETM,PLANEMB;
    private List<Common> mSemester,mTemplate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_wsjctj_month);
        initialize();
    }

    protected void initialize() {
        Bundle bundle = getIntent().getExtras();
        mSemester = (List<Common>) bundle.getSerializable("xq");
        mTemplate = (List<Common>) bundle.getSerializable("mb");
        PLANETM = new String[mSemester.size()];
        if(mSemester != null && mSemester.size()>0){
            int i =0;
            for(Common j : mSemester){
                PLANETM[i] = j.getName();
                i++;
            }
        }
        PLANEMB = new String[mTemplate.size()];
        if(mTemplate != null && mTemplate.size()>0){
            int i =0;
            for(Common j : mTemplate){
                PLANEMB[i] = j.getName();
                i++;
            }
        }
        findView();
    }

    private void findView(){
        semester = (WheelView) findViewById(R.id.year);
        semester.setAdapter(new ArrayWheelAdapter<>(PLANETM,10));
        semester.setCyclic(false);
        template = (WheelView) findViewById(R.id.month);
        template.setAdapter(new ArrayWheelAdapter<>(PLANEMB));
        template.setCyclic(false);

        Button bt = (Button)findViewById(R.id.set);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = PLANETM[semester.getCurrentItem()];
                String mbs = PLANEMB[template.getCurrentItem()];
                Intent in = new Intent();
                in.putExtra("xqs", str);
                in.putExtra("xqsid", mSemester.get(semester.getCurrentItem()).getId());
                in.putExtra("mbs",mbs);
                in.putExtra("mbsid", mTemplate.get(template.getCurrentItem()).getId());
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
