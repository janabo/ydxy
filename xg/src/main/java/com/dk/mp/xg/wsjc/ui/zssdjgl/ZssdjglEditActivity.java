package com.dk.mp.xg.wsjc.ui.zssdjgl;

import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.JsonData;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.BroadcastUtil;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.ui.Sswz.SswzWjrqPickActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 住宿生登记管理修改
 * 作者：janabo on 2017/2/9 14:08
 */
public class ZssdjglEditActivity extends MyActivity{
    private TextView ksrq_pick,jsrq_pick,xjrq_pick,back,title,ok;
    private String ksrq,jsrq,detailid,type,xjrq;
    private TextView ksrq_bt,jsrq_bt;
    private LinearLayout xiaojia,edit;
    private ErrorLayout mError;


    @Override
    protected int getLayoutID() {
        return R.layout.app_zssdjgl_edit;
    }

    @Override
    protected void initView() {
        super.initView();
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.select_title));
        }
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
        xiaojia = (LinearLayout) findViewById(R.id.xiaojia);
        edit = (LinearLayout) findViewById(R.id.edit);
        ksrq_pick = (TextView) findViewById(R.id.ksrq_pick);
        jsrq_pick = (TextView) findViewById(R.id.jsrq_pick);
        xjrq_pick = (TextView) findViewById(R.id.xjrq_pick);
        back = (TextView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        ok = (TextView) findViewById(R.id.ok);
        ksrq_bt = (TextView) findViewById(R.id.ksrq_bt);
        jsrq_bt = (TextView) findViewById(R.id.jsrq_bt);
        title.setText("修改");
        ksrq = getIntent().getStringExtra("ksrq");
        jsrq = getIntent().getStringExtra("jsrq");
        detailid = getIntent().getStringExtra("detailid");
        type = getIntent().getStringExtra("type");
        xjrq = getIntent().getStringExtra("xjrq");
        if("3".equals(type)) {
            xiaojia.setVisibility(View.VISIBLE);
            edit.setVisibility(View.GONE);
            xjrq_pick.setText(xjrq);
        }else{
            xiaojia.setVisibility(View.GONE);
            edit.setVisibility(View.VISIBLE);
            if ("1".equals(type)) {
                ksrq_bt.setText("留宿开始时间");
                jsrq_bt.setText("留宿结束时间");
            } else {
                ksrq_bt.setText("请假开始时间");
                jsrq_bt.setText("请假结束时间");
            }
            ksrq_pick.setText(ksrq);
            jsrq_pick.setText(jsrq);
            if(ksrq_pick.getText().toString().length()>0 && jsrq_pick.getText().toString().length()>0){
                ok.setEnabled(true);
                ok.setTextColor(getResources().getColor(R.color.colorPrimary));
            }else{
                ok.setEnabled(false);
                ok.setTextColor(getResources().getColor(R.color.colorPrimary50));
            }
        }
    }

    @Override
    protected void initialize() {
        super.initialize();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mError.setErrorType(ErrorLayout.LOADDATA);
                ok.setEnabled(false);
                if("3".equals(type)){
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", detailid);
                    map.put("time", xjrq_pick.getText().toString());
                    HttpUtil.getInstance().postJsonObjectRequest("apps/zsdjgl/xiaojia", map, new HttpListener<JSONObject>() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                            ok.setEnabled(true);
                            try {
                                JsonData jd = getGson().fromJson(result.toString(),JsonData.class);
                                if (jd.getCode() == 200 && (Boolean) jd.getData()) {
                                    BroadcastUtil.sendBroadcast(mContext, "zssdjgl_refresh");
                                    BroadcastUtil.sendBroadcast(mContext, "zssdjgl_search_refresh");
                                    back();
                                } else {
                                    showErrorMsg(jd.getMsg());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onError(VolleyError error) {
                            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                            ok.setEnabled(true);
                        }
                    });
                }else {
                    Map<String, Object> map = new HashMap<>();
                    map.put("startTime", ksrq_pick.getText().toString());
                    map.put("endTime", jsrq_pick.getText().toString());
                    map.put("id", detailid);
                    map.put("type", type);
                    HttpUtil.getInstance().postJsonObjectRequest("apps/zsdjgl/update", map, new HttpListener<JSONObject>() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                            ok.setEnabled(true);
                            try {
                                JsonData jd = getGson().fromJson(result.toString(),JsonData.class);
                                if (jd.getCode() == 200 && (Boolean) jd.getData()) {
                                    BroadcastUtil.sendBroadcast(mContext, "zssdjgl_refresh");
                                    BroadcastUtil.sendBroadcast(mContext, "zssdjgl_search_refresh");
                                    back();
                                } else {
                                    showErrorMsg(jd.getMsg());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(VolleyError error) {
                            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                            ok.setEnabled(true);
                        }
                    });
                }
            }
        });
    }

    /**
     * 停宿开始日期
     * @param v
     */
    public void toPickKsrq(View v){
        Intent intent = new Intent(mContext, SswzWjrqPickActivity.class);
        startActivityForResult(intent, 1);
        intent.putExtra("date",ksrq);
        overridePendingTransition(R.anim.push_up_in, 0);
    }

    /**
     * 停宿结束日期
     * @param v
     */
    public void toPickJsrq(View v){
        Intent intent = new Intent(mContext, SswzWjrqPickActivity.class);
        startActivityForResult(intent, 2);
        intent.putExtra("date",jsrq);
        overridePendingTransition(R.anim.push_up_in, 0);
    }

    /**
     * 销假日期
     * @param v
     */
    public void toPickXjrq(View v){
        Intent intent = new Intent(mContext, SswzWjrqPickActivity.class);
        startActivityForResult(intent, 3);
        intent.putExtra("date",xjrq);
        overridePendingTransition(R.anim.push_up_in, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    String mWjrq = data.getStringExtra("date");
                    ksrq_pick.setText(mWjrq);
                    if(jsrq_pick.getText().toString().length()>0){
                        ok.setEnabled(true);
                        ok.setTextColor(getResources().getColor(R.color.colorPrimary));
                    }else{
                        ok.setEnabled(false);
                        ok.setTextColor(getResources().getColor(R.color.colorPrimary50));
                    }
                }
                break;
            case 2:
                if(resultCode == RESULT_OK){
                    String mWjrq = data.getStringExtra("date");
                    jsrq_pick.setText(mWjrq);
                    if(ksrq_pick.getText().toString().length()>0){
                        ok.setEnabled(true);
                        ok.setTextColor(getResources().getColor(R.color.colorPrimary));
                    }else{
                        ok.setEnabled(false);
                        ok.setTextColor(getResources().getColor(R.color.colorPrimary50));
                    }
                }
                break;
            case 3:
                if(resultCode == RESULT_OK){
                    String mWjrq = data.getStringExtra("date");
                    xjrq_pick.setText(mWjrq);
                    ok.setEnabled(true);
                    ok.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
                break;
        }
    }


}
