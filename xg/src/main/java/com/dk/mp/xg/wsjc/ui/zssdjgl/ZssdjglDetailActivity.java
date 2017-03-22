package com.dk.mp.xg.wsjc.ui.zssdjgl;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.dialog.AlertDialog;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.BroadcastUtil;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.entity.StudentInfo;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：janabo on 2017/2/9 11:33
 */
public class ZssdjglDetailActivity extends MyActivity {
    Button pass,notpass,untread;//通过，不通过，退回
    String detailid,type;
    private ErrorLayout mError;
    private TextView xm,xb,lxdh,bj,fjh,ssl,ssq,xq,lsksrq,lsjsrq,xjrq;
    RelativeLayout mRootView;
    StudentInfo studentInfo =null;
    private LinearLayout xiaojia,xjrq_lin,lsksrq_txt,lsjsrq_txt;

    @Override
    protected int getLayoutID() {
        return R.layout.app_zssdjgl_detail;
    }

    @Override
    protected void initView() {
        super.initView();
        detailid = getIntent().getStringExtra("detailid");
        type = getIntent().getStringExtra("type");
        setTitle("1".equals(type)?"周末留宿学生登记":"日常请假学生登记");
        mRootView = (RelativeLayout) findViewById(R.id.mRootView);
        pass = (Button) findViewById(R.id.pass);
        notpass = (Button) findViewById(R.id.notpass);
        untread = (Button) findViewById(R.id.untread);
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        xm = (TextView) findViewById(R.id.xm);
        xb = (TextView) findViewById(R.id.xb);
        lxdh = (TextView) findViewById(R.id.lxdh);
        bj = (TextView) findViewById(R.id.bj);
        fjh = (TextView) findViewById(R.id.fjh);
        ssl = (TextView) findViewById(R.id.ssl);
        ssq = (TextView) findViewById(R.id.ssq);
        xq = (TextView) findViewById(R.id.xq);
        lsksrq = (TextView) findViewById(R.id.lsksrq);
        lsjsrq = (TextView) findViewById(R.id.lsjsrq);
        xiaojia = (LinearLayout) findViewById(R.id.xiaojia);
        xjrq_lin = (LinearLayout) findViewById(R.id.xjrq_lin);
        xjrq = (TextView) findViewById(R.id.xjrq);
        if("1".equals(type)) {
            xiaojia.setVisibility(View.GONE);
            xjrq_lin.setVisibility(View.GONE);
        }else {
            xiaojia.setVisibility(View.VISIBLE);
            xjrq_lin.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initialize() {
        super.initialize();
    }

    /**
     * 获取数据
     */
    public void getData(){
        if(DeviceUtil.checkNet()){
            getContent();
        }else{
            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
        }
    }

    /**
     * 获取内容
     */
    public void getContent(){
        Map<String,Object> map = new HashMap<>();
        map.put("id",detailid);
        HttpUtil.getInstance().postJsonObjectRequest("apps/zsdjgl/detail", map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result != null) {
                        int code = result.getInt("code");
                        if (code == 200) {
                            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                            studentInfo = new Gson().fromJson(result.get("data").toString(),StudentInfo.class);
                            setStudentInfo(studentInfo);
                        } else {
                            mError.setErrorType(ErrorLayout.DATAFAIL);
                        }
                    }else{
                        mError.setErrorType(ErrorLayout.DATAFAIL);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    mError.setErrorType(ErrorLayout.DATAFAIL);
                }
            }
            @Override
            public void onError(VolleyError error) {
                mError.setErrorType(ErrorLayout.DATAFAIL);
            }
        });
    }

    /**
     * 赋值
     * @param s
     */
    public void setStudentInfo(StudentInfo s){
        xm.setText(s.getXm());
        xb.setText(s.getXb());
        lxdh.setText(s.getDh());
        bj.setText(s.getBj());
        fjh.setText(s.getFjh());
        ssl.setText(s.getSsl());
        ssq.setText(s.getSsq());
        xq.setText(s.getXq());
        lsksrq.setText(s.getKssj());
        lsjsrq.setText(s.getJssj());
        xjrq.setText(s.getXjsj());
        if(!StringUtils.isNotEmpty(s.getXjsj()) && !"1".equals(type)){
            xiaojia.setVisibility(View.VISIBLE);
        }else{
            xiaojia.setVisibility(View.GONE);
        }
    }

    /**
     * 删除
     * @param v
     */
    public void toDelete(View v){
        new AlertDialog(mContext).show(null, "确定删吗？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Map<String,Object> map = new HashMap<>();
                map.put("id",detailid);
                map.put("type",type);
                HttpUtil.getInstance().postJsonObjectRequest("apps/zsdjgl/delete", map, new HttpListener<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                            if(result != null) {
                                int code = result.getInt("code");
                                if (code == 200) {
                                    showErrorMsg(mRootView,"删除成功");
                                    BroadcastUtil.sendBroadcast(mContext, "zssdjgl_refresh");
                                    BroadcastUtil.sendBroadcast(mContext, "zssdjgl_search_refresh");
                                    back();
                                } else {
                                    showErrorMsg(mRootView,result.getString("msg"));
                                }
                            }else{
                                showErrorMsg(mRootView,"删除失败");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            showErrorMsg(mRootView,"删除失败");
                        }
                    }
                    @Override
                    public void onError(VolleyError error) {
                        showErrorMsg(mRootView,"删除失败");
                    }
                });
            }
        });
    }

    /**
     * 修改
     * @param v
     */
    public void toUpdate(View v){
        Intent intent = new Intent(this,ZssdjglEditActivity.class);
        intent.putExtra("detailid",detailid);
        intent.putExtra("type",type);
        if(studentInfo!=null){
            intent.putExtra("ksrq",studentInfo.getKssj());
            intent.putExtra("jsrq",studentInfo.getJssj());
        }
        startActivity(intent);
    }

    /**
     * 销假
     * @param v
     */
    public void toXiaojia(View v){
        Intent intent = new Intent(this,ZssdjglEditActivity.class);
        intent.putExtra("detailid",detailid);
        intent.putExtra("type","3");
        if(studentInfo!=null){
            intent.putExtra("xjrq",studentInfo.getXjsj());
        }
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }
}
