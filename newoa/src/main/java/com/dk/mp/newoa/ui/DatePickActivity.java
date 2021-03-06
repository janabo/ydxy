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
import com.dk.mp.core.widget.NumericWheelAdapter;
import com.dk.mp.core.widget.OnWheelScrollListener;
import com.dk.mp.core.widget.WheelView;
import com.dk.mp.newoa.R;

import java.util.Calendar;
import java.util.Date;

/**
 * 作者：janabo on 2017/3/22 15:37
 */
public class DatePickActivity extends Activity {
    private Context mContext = this;
    private WheelView year;
    private WheelView month;
    private WheelView day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pick_date);
        Intent intent = getIntent();
        String rq = intent.getStringExtra("rq");
        getDatePick();
    }

    private void getDatePick(){
        Calendar c = Calendar.getInstance();
        final int curYear = c.get(Calendar.YEAR);
        int curMonth = c.get(Calendar.MONTH) + 1;
        int curDate = c.get(Calendar.DATE);

        year = (WheelView) findViewById(R.id.year);
        month = (WheelView) findViewById(R.id.month);
        day = (WheelView) findViewById(R.id.day);

        year.setAdapter(new NumericWheelAdapter(curYear, curYear+5,""));
        year.setLabel("年");
        year.setCyclic(true);
        year.addScrollingListener(scrollListener);

        month.setAdapter(new NumericWheelAdapter(1, 12,""));
        month.setLabel("月");
        month.setCyclic(true);
        month.addScrollingListener(scrollListener);

        initDay(curYear,curMonth);
        day.setLabel("日");
        day.setCyclic(true);

        year.setCurrentItem(curYear - 1950);
        month.setCurrentItem(curMonth - 1);
        day.setCurrentItem(curDate - 1);

        Button bt = (Button)findViewById(R.id.set);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mon = month.getCurrentItem()+1+"";
                if(mon.length()<2){
                    mon = "0"+mon;
                }
                String d = day.getCurrentItem()+1+"";
                if(d.length()<2){
                    d = "0"+d;
                }

                String str = (year.getCurrentItem()+curYear) + "-"+ mon +"-"+d;
                if(TimeUtils.comparedDateS(str, TimeUtils.parseDate(new Date()))){
                    new AlertDialog(mContext).show(null,"输入的会议日期小于当前日期请重新输入");
                }else{
                    Intent in = new Intent();
                    in.putExtra("date", str);
                    setResult(1, in);
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

    OnWheelScrollListener scrollListener = new OnWheelScrollListener() {

        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            int n_year = year.getCurrentItem() + 1950;//
            int n_month = month.getCurrentItem() + 1;//
            initDay(n_year,n_month);
        }
    };


    /**
     */
    private void initDay(int arg1, int arg2) {
        day.setAdapter(new NumericWheelAdapter(1, getDay(arg1, arg2), "%02d",""));
    }

    /**
     *
     * @param year
     * @param month
     * @return
     */
    private int getDay(int year, int month) {
        int day = 30;
        boolean flag = false;
        switch (year % 4) {
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                day = flag ? 29 : 28;
                break;
            default:
                day = 30;
                break;
        }
        return day;
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
