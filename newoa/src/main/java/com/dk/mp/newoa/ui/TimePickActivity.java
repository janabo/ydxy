package com.dk.mp.newoa.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.dk.mp.core.dialog.AlertDialog;
import com.dk.mp.core.util.TimeUtils;
import com.dk.mp.core.widget.ArrayWheelAdapter;
import com.dk.mp.core.widget.WheelView;
import com.dk.mp.newoa.R;

/**
 * 作者：janabo on 2017/3/22 15:47
 */
public class TimePickActivity extends Activity{
    private Context mContext = TimePickActivity.this;
    private static final String[] PLANETS = new String[]{"08:00", "08:30", "09:00", "09:30", "10:00", "10:30",
            "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30",
            "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30", "23:00", "23:30"};

    private static final String[] PLANETE = new String[]{"08:30", "09:00", "09:30", "10:00", "10:30",
            "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30",
            "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30", "23:00","23:30","24:00"};

    private static final String[] PLANETM = new String[]{"至"};

    private WheelView stattime,endtime,midtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pick_time);
        findView();
    }

    private void findView(){
        stattime = (WheelView) findViewById(R.id.starttime);
        endtime = (WheelView) findViewById(R.id.endtime);
        midtime = (WheelView) findViewById(R.id.midtime);

        stattime.setAdapter(new ArrayWheelAdapter<String>(PLANETS));
        stattime.setCurrentItem(6);
        stattime.setCyclic(false);

        endtime.setAdapter(new ArrayWheelAdapter<String>(PLANETE));
        endtime.setCurrentItem(6);
        endtime.setCyclic(false);

        midtime.setAdapter(new ArrayWheelAdapter<String>(PLANETM));
        midtime.setCyclic(false);

        Button bt = (Button)findViewById(R.id.set);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sTime = PLANETS[stattime.getCurrentItem()];
                String eTime = PLANETE[endtime.getCurrentItem()];
                if(TimeUtils.comparedTime2("2016-03-24 "+eTime, "2016-03-24 "+sTime)){
                    new AlertDialog(mContext).show(null,"会议开始时间不能大于或者等于结束时间");
                }else{
                    Intent in = new Intent();
                    in.putExtra("stime", sTime);
                    in.putExtra("etime", eTime);
                    setResult(2, in);
                    back();
                }
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
