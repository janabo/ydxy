package com.dk.mp.newoa.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.util.TimeUtils;
import com.dk.mp.newoa.R;
import com.dk.mp.newoa.entity.Jbxx;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 作者：janabo on 2017/3/22 15:30
 */
public class HcglSubmitSelectActivity extends MyActivity implements View.OnClickListener {
    private RelativeLayout hyrq,hysj,hydd;
    private TextView hyrq_text,hysj_text,hydd_text,right_txt;
    private String rq ="";
    private List<Jbxx> hys;
    private String hysid="";
    private String stime="",etime = "",hysdd="";

    @Override
    protected int getLayoutID() {
        return R.layout.oa_hcgl_select;
    }

    @Override
    protected void initView() {
        super.initView();
        setTitle("调整本次申请安排");
        findView();
        setRightText("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(compareTime()){
                    showErrorMsg("会议开始时间不能小于或者等于当前时间");
                }else{
                    Intent intent = new Intent();
                    intent.putExtra("hyrq", rq);
                    intent.putExtra("stime", stime);
                    intent.putExtra("etime", etime);
                    intent.putExtra("hysid", hysid);
                    intent.putExtra("hysdd", hysdd);
                    setResult(1, intent);
                    back();
                }
            }
        });
        if (DeviceUtil.checkNet()) {
            getHys();
        }else{
            showErrorMsg(getReString(R.string.net_no2));
        }
    }

    public void findView(){
        hyrq = (RelativeLayout) findViewById(R.id.hyrq);
        hysj = (RelativeLayout) findViewById(R.id.hysj);
        hydd = (RelativeLayout) findViewById(R.id.hydd);
        hyrq_text = (TextView) findViewById(R.id.hyrq_text);
        hysj_text = (TextView) findViewById(R.id.hysj_text);
        hydd_text = (TextView) findViewById(R.id.hydd_text);
        right_txt = (TextView) findViewById(R.id.right_txt);
        right_txt.setEnabled(false);
        hyrq.setOnClickListener(this);
        hysj.setOnClickListener(this);
        hydd.setOnClickListener(this);

        Intent intent = getIntent();
        rq = intent.getStringExtra("hyrq");
        stime = intent.getStringExtra("stime");
        etime = intent.getStringExtra("etime");
        hysid = intent.getStringExtra("hysid");
        hysdd = intent.getStringExtra("hysdd");
        if(StringUtils.isNotEmpty(rq)){
            hyrq_text.setText(rq);
            hyrq_text.setTextSize(15);
            hyrq_text.setTextColor(getResources().getColor(R.color.rcap_txt));
        }
        if(StringUtils.isNotEmpty(stime) && StringUtils.isNotEmpty(etime)){
            hysj_text.setText(stime+"~"+etime);
            hysj_text.setTextSize(15);
            hysj_text.setTextColor(getResources().getColor(R.color.rcap_txt));
        }
        if(StringUtils.isNotEmpty(hysdd)){
            hydd_text.setText(hysdd);
            hydd_text.setTextSize(15);
            hydd_text.setTextColor(getResources().getColor(R.color.rcap_txt));
            right_txt.setTextColor(getResources().getColor(R.color.rcap_txt));
            right_txt.setEnabled(true);
        }
    }

    @Override
    public void back() {
        finish();
        overridePendingTransition(0, R.anim.push_down_out);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        if(v.getId() == R.id.hyrq){
            intent = new Intent(mContext,DatePickActivity.class);
            intent.putExtra("rq", rq);
            startActivityForResult(intent, 1);
            overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        }else if(v.getId() == R.id.hysj){
            intent = new Intent(mContext,TimePickActivity.class);
            startActivityForResult(intent, 2);
            overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        }else if(v.getId() == R.id.hydd){
            intent = new Intent(mContext,HysPickActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("hys", (Serializable) hys);
            intent.putExtras(bundle);
            startActivityForResult(intent, 3);
            overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1:
                String date = data.getStringExtra("date");
                rq = date;
                hyrq_text.setTextSize(15);
                hyrq_text.setTextColor(getResources().getColor(R.color.rcap_txt));
                hyrq_text.setText(date);
                checkForm();
                break;
            case 2:
                stime = data.getStringExtra("stime");
                etime = data.getStringExtra("etime");
                hysj_text.setTextSize(15);
                hysj_text.setTextColor(getResources().getColor(R.color.rcap_txt));
                hysj_text.setText(stime+"~"+etime);
                checkForm();
                break;
            case 3:
                String hys = data.getStringExtra("hys");
                hysid = data.getStringExtra("hysid");
                hysdd = hys;
                hydd_text.setTextSize(15);
                hydd_text.setTextColor(getResources().getColor(R.color.rcap_txt));
                hydd_text.setText(hys);
                checkForm();
                break;
        }
    }

    public void checkForm(){
        if(rq.length()>0 && hysj_text.getText().toString().length()>0 && hysid.length()>0 ){
            right_txt.setTextColor(getResources().getColor(R.color.rcap_txt));
            right_txt.setEnabled(true);
        }else{
            right_txt.setTextColor(getResources().getColor(R.color.rcap_txt_no));
            right_txt.setEnabled(false);
        }
    }

    public void getHys(){
        HttpUtil.getInstance().postJsonObjectRequest("apps/office/getKsList", null,new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if(result != null) {
                    try {
                        hys = new ArrayList<Jbxx>();
                        JSONArray array = result.getJSONArray("data");
                        for(int i =0;i<array.length();i++){
                            Jbxx jbxx = new Jbxx();
                            JSONObject jo = array.getJSONObject(i);
                            jbxx.setKey(jo.getString("id"));
                            jbxx.setValue(jo.getString("name"));
                            hys.add(jbxx);
                        }
                        if(hys.size()<=0){
                            showErrorMsg("未获取到会议地点");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        showErrorMsg("获取会议地点失败");
                        hys = new ArrayList<Jbxx>();
                    }
                }else{
                    showErrorMsg("获取会议地点失败");
                    hys = new ArrayList<Jbxx>();
                }
            }
            @Override
            public void onError(VolleyError error) {
                showErrorMsg("获取会议地点失败");
                hys = new ArrayList<Jbxx>();
            }
        });
    }

    /**
     * 比较会议开始时间和当前时间
     * @return
     */
    public boolean compareTime(){
        String currtime = TimeUtils.dateMinuteToStr(new Date());
        String starttime = rq+" "+stime;
        if(TimeUtils.comparedTime2(starttime, currtime)){
            return true;
        }
        return false;
    }

}
