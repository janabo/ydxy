package com.dk.mp.schedule;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dk.mp.core.dialog.AlertDialog;
import com.dk.mp.core.entity.Rcap;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.BroadcastUtil;
import com.dk.mp.core.util.SnackBarUtil;
import com.dk.mp.core.util.TimeUtils;
import com.dk.mp.schedule.db.RealmHelper;

import java.util.UUID;

/**
 * 日程安排新增或编辑
 * 作者：janabo on 2016/12/27 09:07
 */
public class RcapDetailEditActivity extends MyActivity implements View.OnClickListener{
    private Rcap rcap;
    private String idRcap;
    private EditText sTitle, content, place;
    private TextView starttime, endtime;
    private TextView cancle,submit;
    private TextView mTitle;
    private LinearLayout main_layout;
    private RealmHelper mRealmHelper;

    @Override
    protected int getLayoutID() {
        return R.layout.app_rcap_detail_edit;
    }

    @Override
    protected void initialize() {
        super.initialize();
        mRealmHelper = new RealmHelper(this);
        Intent intent = getIntent();
        idRcap = getIntent().getStringExtra("idRcap");
        findView();
        mTitle.setText(intent.getStringExtra("title"));
    }

    /**
     * 初始化
     */
    private void findView(){
        mTitle = (TextView) findViewById(R.id.title);
        sTitle = (EditText) findViewById(R.id.schedule_title);
        content = (EditText) findViewById(R.id.schedule_content);
        cancle = (TextView) findViewById(R.id.cancle);
        submit = (TextView) findViewById(R.id.submit);
        place = (EditText) findViewById(R.id.schedule_place);
        starttime = (TextView) findViewById(R.id.show_starttime);
        endtime = (TextView) findViewById(R.id.show_endtime);
        main_layout = (LinearLayout) findViewById(R.id.main_layout);
        starttime.setOnClickListener(this);
        endtime.setOnClickListener(this);
        cancle.setOnClickListener(this);
        submit.setOnClickListener(this);

        mTitle.addTextChangedListener(titleWatcher);
        content.addTextChangedListener(titleWatcher);
        place.addTextChangedListener(titleWatcher);

        if (idRcap != null) {//判断编辑还是新增
            rcap = mRealmHelper.qRcap(idRcap);
            if(rcap != null) {
                sTitle.setText(rcap.getTitle());
                content.setText(rcap.getContent());
                place.setText(rcap.getLocation());
                starttime.setText(rcap.getTime_start());
                endtime.setText(rcap.getTime_end());
            }
        }else{
            starttime.setText(TimeUtils.getTimeAfterMins(30));
            endtime.setText(TimeUtils.getTimeAfterMins(60));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.show_starttime) {//开始时间
            Intent in = new Intent(this, DateAndTimePickActivity.class);
            in.putExtra("rq", starttime.getText());
            in.putExtra("compRq", endtime.getText());
            in.putExtra("type", 1);
            startActivityForResult(in, 1);
        }else if (v.getId() == R.id.show_endtime) {//结束时间
            Intent in = new Intent(this, DateAndTimePickActivity.class);
            in.putExtra("rq", endtime.getText());
            in.putExtra("compRq", starttime.getText());
            in.putExtra("type", 2);
            startActivityForResult(in, 2);
        }else if(v.getId() == R.id.cancle){//取消
            final AlertDialog alert = new AlertDialog(this);
            alert.show(null, "确定删除该日程吗？", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    overridePendingTransition(0, R.anim.push_down_out);
                }
            });
        }else if(v.getId() == R.id.submit){//保存
            InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            if(imm != null) {
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),
                        0);
            }


            insertData();
        }
    }

    private TextWatcher titleWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            check();
        }
    };

    private void  check(){
        if(mTitle.getText().length()!=0 && content.getText().length()!=0 && place.getText().length()!=0){
            submit.setTextColor(getResources().getColor(R.color.colorPrimary));
            submit.setEnabled(true);
        }else{
            submit.setTextColor(getResources().getColor(R.color.colorPrimary50));
            submit.setEnabled(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String date = data.getStringExtra("date");
                    if (date != null) {
                        starttime.setText(date);
                    }
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    String date2 = data.getStringExtra("date");
                    if (date2 != null) {
                        endtime.setText(date2);
                    }
                }
                break;
        }
    }


    /**
     * 插入数据库
     */
    private void insertData() {
        if(mTitle.getText().length()==0){
            SnackBarUtil.showShort(main_layout,"请填写日程主题");
        }else if(place.getText().length()==0){
            SnackBarUtil.showShort(main_layout,"请填写日程地点");
        }else if(content.getText().length()==0){
            SnackBarUtil.showShort(main_layout,"请填写日程描述");
        }else if(!TimeUtils.comparedTime2(starttime.getText().toString(), endtime.getText().toString())){
            SnackBarUtil.showShort(main_layout,"开始时间不能在结束时间之后");
        }else{
            Rcap ev = new Rcap();
            if (idRcap != null) {
                ev.setId(idRcap);
            } else {
                ev.setId(UUID.randomUUID().toString());
            }
            ev.setTitle(sTitle.getText().toString().trim());
            ev.setContent(content.getText().toString().trim());
            ev.setLocation(place.getText().toString().trim());
            ev.setTime_start(starttime.getText().toString());
            ev.setTime_end(endtime.getText().toString());
            ev.setStime(TimeUtils.getTimeByDateTime(starttime.getText().toString()));
            mRealmHelper.addRcap(ev);
            BroadcastUtil.sendBroadcast(mContext, "rcap_refresh");
            finish();
            overridePendingTransition(0, R.anim.push_down_out);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealmHelper.closeRealm();
    }
}
