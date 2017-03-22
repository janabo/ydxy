package com.dk.mp.newoa.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.BroadcastUtil;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.newoa.R;
import com.dk.mp.newoa.entity.SeriMap;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 会场管理提交
 * 作者：janabo on 2017/3/22 15:23
 */
public class HcglSubmitActivity extends MyActivity implements View.OnClickListener {
    private String submitType;//提交类型从上一个界面传过来  (flowRepeal:流程撤销,flowWithDraw:流程取回,giveBack:打回,flowEnd:办结,pass:通过,notPass:不通过,next:下一步)
    private Map<String, String> params;
    private RelativeLayout tjlxr_view;
    private LinearLayout sfytz;//是否已调整本次安排
    private TextView tjlxr_xt_tip,spyj_edit;
    private String targetNodeId;
    private String hysid="",stime="",etime="",rq="",hysdd="";
    private Button submit;
    @Override
    protected int getLayoutID() {
        return R.layout.oa_hcgl;
    }

    @Override
    protected void initView() {
        super.initView();
        setTitle(getIntent().getStringExtra("title"));
        findView();
        setUI();
    }

    private void findView() {
        tjlxr_view = (RelativeLayout) findViewById(R.id.tjlxr_view);
        tjlxr_xt_tip = (TextView) findViewById(R.id.tjlxr_xt_tip);
        sfytz = (LinearLayout) findViewById(R.id.sfytz);
        spyj_edit = (TextView)findViewById(R.id.spyj_edit);
        tjlxr_view.setOnClickListener(this);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);
    }

    @SuppressWarnings("unchecked")
    private void setUI() {
        Bundle bundle = getIntent().getExtras();
        targetNodeId=getIntent().getStringExtra("nodeId");
        submitType = getIntent().getStringExtra("submitType");
        SeriMap sMap = (SeriMap) bundle.getSerializable("map");
        params = sMap.getMap();

        if("btg".equals(submitType)){
            tjlxr_view.setVisibility(View.GONE);
        }else{
            tjlxr_view.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        if(v.getId() == R.id.tjlxr_view){
            intent = new Intent(mContext,HcglSubmitSelectActivity.class);
            intent.putExtra("hyrq", rq);
            intent.putExtra("stime", stime);
            intent.putExtra("etime", etime);
            intent.putExtra("hysid", hysid);
            intent.putExtra("hysdd", hysdd);
            startActivityForResult(intent, 1);
            overridePendingTransition(R.anim.push_down_in, R.anim.push_bottom_out);
        }else if(v.getId() == R.id.submit){
            if (spyj_edit.getText().length() >200) {
                showErrorMsg("审批意见不能大于200字");
            } else {
                if (DeviceUtil.checkNet()) {
                    submit();
                }else{
                    showErrorMsg(getReString(R.string.net_no2));
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1:
                sfytz.setVisibility(View.VISIBLE);
                tjlxr_xt_tip.setVisibility(View.GONE);
                rq = data.getStringExtra("hyrq");
                stime = data.getStringExtra("stime");
                etime = data.getStringExtra("etime");
                hysid = data.getStringExtra("hysid");
                hysdd = data.getStringExtra("hysdd");
                break;
            default:
                break;
        }
    }

    /**
     * 提交
     */
    public void submit() {
        params.put("suggestion", spyj_edit.getText().toString());
        params.put("type", submitType);
        params.put("usedDate", rq);//会议日期
        params.put("beginTime", stime);
        params.put("endTime", etime);
        params.put("meetingRoomId", hysid);

        if(targetNodeId!=null){
            params.put("targetNodeId", targetNodeId);
        }
        Map<String,Object> mObject = new HashMap<>();
        for (String key : params.keySet()) {
            System.out.println(key + "=" + params.get(key));
            mObject.put(key,params.get(key));
        }

        HttpUtil.getInstance().postJsonObjectRequest("apps/office/subOpinion", mObject, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if(result != null){
                    try {
                        boolean bool = result.optBoolean("data");
                        String msg = result.optString("msg");
                        if(bool){
                            showErrorMsg(msg);
                            BroadcastUtil.sendBroadcast(mContext, "com.test.action.refresh");
                            OADetailActivity.instance.finish();
                            if (OperationActivity.instance != null) {
                                OperationActivity.instance.finish();
                            }
                            finish();
                        }else{
                            showErrorMsg(msg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onError(VolleyError error) {
                showErrorMsg(getReString(R.string.data_fail));
            }
        });
    }
}
