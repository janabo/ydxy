package com.dk.mp.xg.wsjc.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.dk.mp.core.widget.NumericWheelAdapter;
import com.dk.mp.core.widget.WheelView;
import com.dk.mp.xg.R;

import java.util.Calendar;

/**
 * 月选择器
 * 作者：janabo on 2017/1/16 13:50
 */
public class WsjcTjMonthPickActivity extends Activity{
    private WheelView year;
    private WheelView month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_wsjctj_month);
        getDatePick();
    }

    private void getDatePick(){
        Calendar c = Calendar.getInstance();
        final int curYear = c.get(Calendar.YEAR);
        int curMonth = c.get(Calendar.MONTH) + 1;

        year = (WheelView) findViewById(R.id.year);
        month = (WheelView) findViewById(R.id.month);

        year.setAdapter(new NumericWheelAdapter(curYear, curYear+49,""));
        year.setLabel("年");
        year.setCyclic(false);

        month.setAdapter(new NumericWheelAdapter(1, 12,""));
        month.setLabel("月");
        month.setCyclic(false);


        year.setCurrentItem(curYear - 1950);
        month.setCurrentItem(curMonth - 1);

        Button bt = (Button)findViewById(R.id.set);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mon = month.getCurrentItem()+1+"";
                if(mon.length()<2){
                    mon = "0"+mon;
                }

                String str = (year.getCurrentItem()+curYear) + "-"+ mon;
                Intent in = new Intent();
                in.putExtra("date", str);
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
