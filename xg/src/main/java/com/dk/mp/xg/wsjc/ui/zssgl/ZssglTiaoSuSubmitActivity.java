package com.dk.mp.xg.wsjc.ui.zssgl;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.dialog.AlertDialog;
import com.dk.mp.core.entity.GsonData;
import com.dk.mp.core.entity.JsonData;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.BroadcastUtil;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.SnackBarUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.view.DrawCheckMarkView;
import com.dk.mp.core.view.DrawCrossMarkView;
import com.dk.mp.core.view.DrawHookView;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.entity.Common;
import com.dk.mp.xg.wsjc.entity.Zxzssgl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 调宿提交
 * 作者：janabo on 2017/2/22 13:43
 */
public class ZssglTiaoSuSubmitActivity extends MyActivity{
    private static final int INIT_GETDATA = 1;
    private static final int PICK_GETDATA = 2;
    private DrawHookView progress;
    private DrawCheckMarkView progress_check;
    private DrawCrossMarkView progress_cross;
    private ScrollView mRootView;
    private LinearLayout ok;
    private TextView xq_pick,ssq_pick,ssl_pick,lc_pick,fjh_pick,cwh_pick,fx_pick,zsf_pick,cws_pick,tzyy_pick;
    private List<Common> xqs = new ArrayList<>();//存放校区信息
    private List<Common> ssqs = new ArrayList<>();//存放宿舍区信息
    private List<Common> ssls = new ArrayList<>();//存放宿舍楼信息
    private List<Common> lcs = new ArrayList<>();//存放楼层信息
    private List<Common> fjhs = new ArrayList<>();//存放房间号信息
    private List<Common> cwhs = new ArrayList<>();//存放床位号信息
    private List<Common> tzyys = new ArrayList<>();//存放调整原因信息
    private String sslid;//宿舍楼id
    private String lcid,xqid,ssqid,fjhid,cwhid,tzyyid;//楼层id,校区id,宿舍区id,房间号,床位号,调整原因id
    private EditText bz,shyj;//申请理由
    private TextView ok_text;
    private String mId,flag,lmlb,xb;
    private ErrorLayout mError;


    @Override
    protected int getLayoutID() {
        return R.layout.app_zssgl_tiaosu_submit;
    }

    @Override
    protected void initView() {
        super.initView();
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        ok_text = (TextView) findViewById(R.id.ok_text);
        progress = (DrawHookView) findViewById(R.id.progress);
        progress_check = (DrawCheckMarkView) findViewById(R.id.progress_check);
        progress_cross = (DrawCrossMarkView) findViewById(R.id.progress_cross);
        ok = (LinearLayout) findViewById(R.id.ok);
        bz = (EditText) findViewById(R.id.bz);
        shyj = (EditText) findViewById(R.id.shyj);
        mRootView = (ScrollView) findViewById(R.id.mRootView);
        xq_pick= (TextView) findViewById(R.id.xq_pick);
        ssq_pick= (TextView) findViewById(R.id.ssq_pick);
        ssl_pick= (TextView) findViewById(R.id.ssl_pick);
        lc_pick= (TextView) findViewById(R.id.lc_pick);
        fjh_pick= (TextView) findViewById(R.id.fjh_pick);
        cwh_pick= (TextView) findViewById(R.id.cwh_pick);
        fx_pick= (TextView) findViewById(R.id.fx_pick);
        zsf_pick= (TextView) findViewById(R.id.zsf_pick);
        cws_pick= (TextView) findViewById(R.id.cws_pick);
        tzyy_pick= (TextView) findViewById(R.id.tzyy_pick);
        xq_pick.addTextChangedListener(mTextWatcher);
        ssq_pick.addTextChangedListener(mTextWatcher);
        ssl_pick.addTextChangedListener(mTextWatcher);
        lc_pick.addTextChangedListener(mTextWatcher);
        fjh_pick.addTextChangedListener(mTextWatcher);
        cwh_pick.addTextChangedListener(mTextWatcher);
        tzyy_pick.addTextChangedListener(mTextWatcher);
        shyj.addTextChangedListener(mTextWatcher);
    }

    @Override
    protected void initialize() {
        super.initialize();
        setTitle("在校生住宿管理");
        mId = getIntent().getStringExtra("detailid");
        flag = getIntent().getStringExtra("flag");
        lmlb = getIntent().getStringExtra("lmlb");
        xb = getIntent().getStringExtra("xb");
        getData();
        getXqs(INIT_GETDATA);
    }

    /**
     * 获取楼层列表
     */
    public void getLcs(String sslid,final int param){
        lcs.clear();
        Map<String,Object> map = new HashMap<>();
        map.put("ssl",sslid);
        HttpUtil.getInstance().postJsonObjectRequest("apps/zxzssgl/lcList", map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result != null) {
                        GsonData<Common> gsonData = new Gson().fromJson(result.toString(), new TypeToken<GsonData<Common>>() {
                        }.getType());
                        if (gsonData.getCode() == 200) {
                            List<Common> dfxxes = gsonData.getData();
                            if(dfxxes.size()>0){//获取数据不为空
                                lcs.addAll(dfxxes);
                                toPickActivity(lcs,5,param);
                            }else{
                                errorMsg(param,"未获取到楼层");
                            }
                        } else {
                            errorMsg(param,"获取楼层失败");
                        }
                    }else{
                        errorMsg(param,"获取楼层失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    errorMsg(param,"获取楼层失败");
                }
            }
            @Override
            public void onError(VolleyError error) {
                errorMsg(param,"获取楼层失败");
            }
        });
    }

    /**
     * 获取房间号列表
     */
    public void getFjhs(String ssl,String lc,final int param){
        fjhs.clear();
        Map<String,Object> map = new HashMap<>();
        map.put("ssl",ssl);
        map.put("lc",lc);
        map.put("xb",xb);
        HttpUtil.getInstance().postJsonObjectRequest("apps/zxzssgl/fjList", map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result != null) {
                        GsonData<Common> gsonData = new Gson().fromJson(result.toString(), new TypeToken<GsonData<Common>>() {
                        }.getType());
                        if (gsonData.getCode() == 200) {
                            List<Common> dfxxes = gsonData.getData();
                            if(dfxxes.size()>0){//获取数据不为空
                                fjhs.addAll(dfxxes);
                                toPickActivity(fjhs,6,param);
                            }else{
                                errorMsg(param,"未获取到房间号");
                            }
                        } else {
                            errorMsg(param,"获取房间号失败");
                        }
                    }else{
                        errorMsg(param,"获取房间号失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    errorMsg(param,"获取房间号失败");
                }
            }
            @Override
            public void onError(VolleyError error) {
                errorMsg(param,"获取房间号失败");
            }
        });
    }

    /**
     * 获取床位号列表
     */
    public void getCwhs(String fjhid,final int param){
        cwhs.clear();
        Map<String,Object> map = new HashMap<>();
        map.put("fjh",fjhid);
        HttpUtil.getInstance().postJsonObjectRequest("apps/zxzssgl/cwhList", map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result != null) {
                        GsonData<Common> gsonData = new Gson().fromJson(result.toString(), new TypeToken<GsonData<Common>>() {
                        }.getType());
                        if (gsonData.getCode() == 200) {
                            List<Common> dfxxes = gsonData.getData();
                            if(dfxxes.size()>0){//获取数据不为空
                                cwhs.addAll(dfxxes);
                                toPickActivity(cwhs,7,param);
                            }else{
                                errorMsg(param,"未获取到床位号");
                            }
                        } else {
                            errorMsg(param,"获取床位号失败");
                        }
                    }else{
                        errorMsg(param,"获取床位号失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    errorMsg(param,"获取床位号失败");
                }
            }
            @Override
            public void onError(VolleyError error) {
                errorMsg(param,"获取床位号失败");
            }
        });
    }

    /**
     * 获取校区列表
     */
    public void getXqs(final int param){
        xqs.clear();
        HttpUtil.getInstance().postJsonObjectRequest("apps/zxzssgl/xqList", null, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result != null) {
                        GsonData<Common> gsonData = new Gson().fromJson(result.toString(), new TypeToken<GsonData<Common>>() {
                        }.getType());
                        if (gsonData.getCode() == 200) {
                            List<Common> dfxxes = gsonData.getData();
                            if(dfxxes.size()>0){//获取数据不为空
                                xqs.addAll(dfxxes);
                                toPickActivity(xqs,2,param);
                            }else{
                                errorMsg(param,"未获取到校区");
                            }
                        } else {
                            errorMsg(param,"获取校区失败");
                        }
                    }else{
                        errorMsg(param,"获取校区失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    errorMsg(param,"获取校区失败");
                }
            }
            @Override
            public void onError(VolleyError error) {
                errorMsg(param,"获取校区失败");
            }
        });
    }

    /**
     * 获取宿舍区列表
     */
    public void getSsqs(String xqid, final int param){
        ssqs.clear();
        Map<String,Object> map = new HashMap<>();
        map.put("xq",xqid);
        HttpUtil.getInstance().postJsonObjectRequest("apps/zxzssgl/ssqList", map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result != null) {
                        GsonData<Common> gsonData = new Gson().fromJson(result.toString(), new TypeToken<GsonData<Common>>() {
                        }.getType());
                        if (gsonData.getCode() == 200) {
                            List<Common> dfxxes = gsonData.getData();
                            if(dfxxes.size()>0){//获取数据不为空
                                ssqs.addAll(dfxxes);
                                toPickActivity(ssqs,3,param);
                            }else{
                                errorMsg(param,"未获取到宿舍区");
                            }
                        } else {
                            errorMsg(param,"获取宿舍区失败");
                        }
                    }else{
                        errorMsg(param,"获取宿舍区失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    errorMsg(param,"获取宿舍区失败");
                }
            }
            @Override
            public void onError(VolleyError error) {
                errorMsg(param,"获取宿舍区失败");
            }
        });
    }

    /**
     * 获取宿舍楼列表
     */
    public void getSsls(String ssqid, final int param){
        ssls.clear();
        Map<String,Object> map = new HashMap<>();
        map.put("ssq",ssqid);
        HttpUtil.getInstance().postJsonObjectRequest("apps/zxzssgl/sslList", map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result != null) {
                        GsonData<Common> gsonData = new Gson().fromJson(result.toString(), new TypeToken<GsonData<Common>>() {
                        }.getType());
                        if (gsonData.getCode() == 200) {
                            List<Common> dfxxes = gsonData.getData();
                            if(dfxxes.size()>0){//获取数据不为空
                                ssls.addAll(dfxxes);
                                toPickActivity(ssls,4,param);
                            }else{
                                errorMsg(param,"未获取到宿舍楼");
                            }
                        } else {
                            errorMsg(param,"获取宿舍楼失败");
                        }
                    }else{
                        errorMsg(param,"获取宿舍楼失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    errorMsg(param,"获取宿舍楼失败");
                }
            }
            @Override
            public void onError(VolleyError error) {
                errorMsg(param,"获取宿舍楼失败");
            }
        });
    }

//    /**
//     * 退宿原因和调整原因
//     */
//    public void getTsyys(){
//        HttpUtil.getInstance().postJsonObjectRequest("apps/zxzssgl/tzyyList", null, new HttpListener<JSONObject>() {
//            @Override
//            public void onSuccess(JSONObject result) {
//                try {
//                    if(result != null) {
//                        GsonData<Common> gsonData = new Gson().fromJson(result.toString(), new TypeToken<GsonData<Common>>() {
//                        }.getType());
//                        if (gsonData.getCode() == 200) {
//                            List<Common> dfxxes = gsonData.getData();
//                            if(dfxxes.size()>0){//获取数据不为空
//                                tzyys.addAll(dfxxes);
//                            }else{
//                            }
//                        } else {
//                        }
//                    }else{
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            @Override
//            public void onError(VolleyError error) {
//            }
//        });
//    }

    /**
     * 获取房间详情
     * @param fjh 房间号
     */
    public void getFjDetail(String fjh){
        fjhs.clear();
        Map<String,Object> map = new HashMap<>();
        map.put("fjh",fjh);
        HttpUtil.getInstance().postJsonObjectRequest("apps/zxzssgl/fjDetail", map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result != null) {
                        JsonData jsonData = new Gson().fromJson(result.toString(),JsonData.class);
                        if (jsonData.getCode() == 200) {
                            Map map = (Map) jsonData.getData();
                            if(map != null && !map.isEmpty()){
                                fx_pick.setText((String)map.get("ssfx"));
                                zsf_pick.setText((String)map.get("zsf"));
                                cws_pick.setText((String)map.get("cws"));
                            }

                        } else {
                        }
                    }else{
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(VolleyError error) {
            }
        });
    }

    /**
     * 选择校区
     * @param v
     */
    public void toPickXq(View v){
        mError.setErrorType(ErrorLayout.LOADDATA);
        if (xqs.size() > 0) {
            toPickActivity(xqs,2,PICK_GETDATA);
        } else {
            if(DeviceUtil.checkNet()) {
                getXqs(PICK_GETDATA);
            }else{
                errorMsg(PICK_GETDATA,getReString(R.string.net_no2));
            }
        }
    }

    /**
     * 选择宿舍区
     * @param v
     */
    public void toPickSsq(View v){
        mError.setErrorType(ErrorLayout.LOADDATA);
        if (ssqs.size() > 0) {
            toPickActivity(ssqs,3,PICK_GETDATA);
        } else {
            if(StringUtils.isNotEmpty(xqid)) {
                if(DeviceUtil.checkNet()) {
                    getSsqs(xqid, PICK_GETDATA);
                }else{
                    errorMsg(PICK_GETDATA,getReString(R.string.net_no2));
                }
            }else{
                errorMsg(PICK_GETDATA,"请先选择校区");
            }
        }
    }

    /**
     * 选择宿舍楼
     * @param v
     */
    public void toPickSsl(View v){
        mError.setErrorType(ErrorLayout.LOADDATA);
        if (ssls.size() > 0) {
            toPickActivity(ssls,4,PICK_GETDATA);
        } else {
            if(StringUtils.isNotEmpty(ssqid)) {
                if(DeviceUtil.checkNet()) {
                    getSsls(ssqid,PICK_GETDATA);
                }else{
                    errorMsg(PICK_GETDATA,getReString(R.string.net_no2));
                }
            }else{
                errorMsg(PICK_GETDATA,"请先选择宿舍区");
            }
        }
    }

    /**
     * 选择楼层
     * @param v
     */
    public void toPickLc(View v){
        mError.setErrorType(ErrorLayout.LOADDATA);
        if (lcs.size() > 0) {
            toPickActivity(lcs,5,PICK_GETDATA);
        } else {
            if(StringUtils.isNotEmpty(sslid)) {
                if(DeviceUtil.checkNet()) {
                    getLcs(sslid,PICK_GETDATA);
                }else{
                    errorMsg(PICK_GETDATA,getReString(R.string.net_no2));
                }
            }else{
                errorMsg(PICK_GETDATA,"请先选择宿舍楼");
            }
        }
    }

    /**
     * 选择房间号
     * @param v
     */
    public void toPickFjh(View v){
        mError.setErrorType(ErrorLayout.LOADDATA);
        if (fjhs.size() > 0) {
            toPickActivity(fjhs,6,PICK_GETDATA);
        } else {
            if(!StringUtils.isNotEmpty(sslid)){
                errorMsg(PICK_GETDATA,"请先选择宿舍楼");
            }else if(!StringUtils.isNotEmpty(lcid)){
                errorMsg(PICK_GETDATA,"请先选择楼层");
            }else {
                if(DeviceUtil.checkNet()) {
                    getFjhs(sslid, lcid, PICK_GETDATA);
                }else{
                    errorMsg(PICK_GETDATA,getReString(R.string.net_no2));
                }
            }
        }
    }

    /**
     * 选择床位号
     * @param v
     */
    public void toPickCwh(View v){
        mError.setErrorType(ErrorLayout.LOADDATA);
        if (cwhs.size() > 0) {
            toPickActivity(cwhs,7,PICK_GETDATA);
        } else {
            if(StringUtils.isNotEmpty(fjhid)) {
                if(DeviceUtil.checkNet()) {
                    getCwhs(fjhid,PICK_GETDATA);
                }else{
                    errorMsg(PICK_GETDATA,getReString(R.string.net_no2));
                }
            }else{
                errorMsg(PICK_GETDATA,"请先选择房间号");
            }
        }
    }

    /**
     * 选择调整原因
     * @param v
     */
    public void toPickTzyy(View v){
        if(StringUtils.isNotEmpty(ssq_pick.getText().toString())) {
            Intent intent = new Intent(mContext, ZssglSelectTzyyActivity.class);
            intent.putExtra("ssqid", ssqid);
            intent.putExtra("tzyyid",tzyyid);
            startActivityForResult(intent, 8);
            overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
        }else{
            showErrorMsg(mRootView, "请先选择宿舍区");
        }
    }

    /**
     * 提交停宿
     * @param v
     */
    public void submitTuisu(View v){
        if(xq_pick.getText().toString().length()<=0){
            showErrorMsg(mRootView,"请选择校区");
            return;
        }
        if(ssq_pick.getText().toString().length()<=0){
            showErrorMsg(mRootView,"请选择宿舍区");
            return;
        }
        if(ssl_pick.getText().toString().length()<=0){
            showErrorMsg(mRootView,"请选择宿舍楼");
            return;
        }
        if(lc_pick.getText().toString().length()<=0){
            showErrorMsg(mRootView,"请选择楼层");
            return;
        }
        if(fjh_pick.getText().toString().length()<=0){
            showErrorMsg(mRootView,"请选择房间号");
            return;
        }
        if(cwh_pick.getText().toString().length()<=0){
            showErrorMsg(mRootView,"请选择床位号");
            return;
        }
        if(tzyy_pick.getText().toString().length()<=0){
            showErrorMsg(mRootView,"请选择调整原因");
            return;
        }
        if(shyj.getText().toString().length()<=0){
            showErrorMsg(mRootView,"请填写审核意见");
            return;
        }

        Map<String,Object> map = new HashMap<>();
        map.put("bz",bz.getText().toString());//备注
        map.put("id",mId);//学生id
        map.put("xq",xqid);//校区id
        map.put("ssq",ssqid);//校区id
        map.put("ssl",sslid);//校区id
        map.put("lc",lcid);//校区id
        map.put("fjh",fjhid);//校区id
        map.put("cwh",cwhid);//床位号id
        map.put("fx",fx_pick.getText().toString());//房型
        map.put("zsf",zsf_pick.getText().toString());//住宿费
        map.put("cws",cws_pick.getText().toString());//床位数
        map.put("tzyy",tzyyid);//调整原因id
        map.put("shyj",shyj.getText().toString());//审核意见
        map.put("flag",flag);
        ok_text.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        HttpUtil.getInstance().postJsonObjectRequest("apps/zxzssgl/tiaosush", map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result)  {
                try {
                    JsonData jd = getGson().fromJson(result.toString(),JsonData.class);
                    if (jd.getCode() == 200 && (Boolean) jd.getData()) {
                        progress.setVisibility(View.GONE);
                        progress_check.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {//等待成功动画结束
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void run() {
                                ok.setEnabled(true);
                                BroadcastUtil.sendBroadcast(mContext, "zssgl_refresh");
                                if(ZssglDetailActivity.instance!= null){
                                    ZssglDetailActivity.instance.finish();
                                }
                                finish();
                            }
                        },1000);
                    }else{
                        SnackBarUtil.showShort(mRootView,jd.getMsg());
                        errorInfo();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    errorInfo();
                }
            }

            @Override
            public void onError(VolleyError error) {
                errorInfo();
            }
        });

    }

    private void errorInfo(){
        progress.setVisibility(View.GONE);
        progress_cross.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {//等待成功动画结束
            @Override
            public void run() {
                progress_cross.setVisibility(View.GONE);
                ok_text.setVisibility(View.VISIBLE);
                ok.setEnabled(true);
            }
        },1000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 2:
                if (resultCode == RESULT_OK) {
                    String mXqid = data.getStringExtra("kfsid");
                    String xqname = data.getStringExtra("kfs");
                    if(StringUtils.isNotEmpty(xqid) && !xqid.equals(mXqid)){
                        ssqid ="";sslid="";lcid="";fjhid="";cwhid="";tzyyid="";
                        ssq_pick.setText("");ssl_pick.setText("");lc_pick.setText("");
                        fjh_pick.setText("");cwh_pick.setText("");fx_pick.setText("");
                        zsf_pick.setText("");cws_pick.setText("");tzyy_pick.setText("");
                    }
                    xqid = mXqid;
                    xq_pick.setText(xqname);
                    getSsqs(xqid,INIT_GETDATA);
                }
                break;
            case 3:
                if (resultCode == RESULT_OK) {
                    String mSsqid = data.getStringExtra("kfsid");
                    String ssqname = data.getStringExtra("kfs");
                    if(StringUtils.isNotEmpty(ssqid) && !ssqid.equals(mSsqid)){
                        sslid="";lcid="";fjhid="";cwhid="";tzyyid="";
                        ssl_pick.setText("");lc_pick.setText("");
                        fjh_pick.setText("");cwh_pick.setText("");fx_pick.setText("");
                        zsf_pick.setText("");cws_pick.setText("");tzyy_pick.setText("");
                    }
                    ssqid = mSsqid;
                    ssq_pick.setText(ssqname);
                    getSsls(ssqid,INIT_GETDATA);
                }
                break;
            case 4:
                if (resultCode == RESULT_OK) {
                    String mSslid = data.getStringExtra("kfsid");
                    String sslname = data.getStringExtra("kfs");
                    if(StringUtils.isNotEmpty(sslid) && !ssqid.equals(mSslid)){
                        lcid="";fjhid="";cwhid="";tzyyid="";
                        lc_pick.setText("");
                        fjh_pick.setText("");cwh_pick.setText("");fx_pick.setText("");
                        zsf_pick.setText("");cws_pick.setText("");tzyy_pick.setText("");
                    }
                    sslid = mSslid;
                    ssl_pick.setText(sslname);
                    getLcs(sslid,INIT_GETDATA);
                }
                break;
            case 5:
                if (resultCode == RESULT_OK) {
                    String mLcid = data.getStringExtra("kfsid");
                    String lcname = data.getStringExtra("kfs");
                    if(StringUtils.isNotEmpty(lcid) && !ssqid.equals(mLcid)){
                        fjhid="";cwhid="";tzyyid="";
                        fjh_pick.setText("");cwh_pick.setText("");fx_pick.setText("");
                        zsf_pick.setText("");cws_pick.setText("");tzyy_pick.setText("");
                    }
                    lcid = mLcid;
                    lc_pick.setText(lcname);
                    getFjhs(sslid, lcid,INIT_GETDATA);
                }
                break;
            case 6:
                if (resultCode == RESULT_OK) {
                    String mFjhid = data.getStringExtra("kfsid");
                    String fjhname = data.getStringExtra("kfs");
                    if(StringUtils.isNotEmpty(fjhid) && !ssqid.equals(mFjhid)){
                        cwhid="";tzyyid="";
                        cwh_pick.setText("");fx_pick.setText("");
                        zsf_pick.setText("");cws_pick.setText("");tzyy_pick.setText("");
                    }
                    fjhid = mFjhid;
                    fjh_pick.setText(fjhname);
                    getCwhs(fjhid,INIT_GETDATA);
                    getFjDetail(fjhid);
                }
                break;
            case 7:
                if (resultCode == RESULT_OK) {
                    cwhid = data.getStringExtra("kfsid");
                    String cwhname = data.getStringExtra("kfs");
                    cwh_pick.setText(cwhname);
                }
                break;
            case 8:
                if (resultCode ==RESULT_OK) {
                    tzyyid = data.getStringExtra("tzyyid");
                    String tzyyname = data.getStringExtra("tzyyname");
                    tzyy_pick.setText(tzyyname);
                }
                break;
        }
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            dealOkButton();
        }
    };

    /**
     * 设置ok按钮的样式
     */
    public void dealOkButton(){
        if(StringUtils.isNotEmpty(xqid) && StringUtils.isNotEmpty(ssqid) && StringUtils.isNotEmpty(sslid)
                && StringUtils.isNotEmpty(lcid) && StringUtils.isNotEmpty(fjhid) && StringUtils.isNotEmpty(cwhid)
                && StringUtils.isNotEmpty(tzyyid) && shyj.getText().toString().length()>0){
            ok.setBackground(getResources().getDrawable(R.drawable.ripple_bg));
            ok.setEnabled(true);
        }else{
            ok.setBackgroundColor(getResources().getColor(R.color.rcap_gray));
            ok.setEnabled(false);
        }
    }


    //apps/zxzssgl/detail?id="+detailid+"&lmlb="+lmlb+"&uid=
    public void getData(){
        Map<String,Object> map = new HashMap<>();
        map.put("id",mId);
        map.put("lmlb",lmlb);
        HttpUtil.getInstance().postJsonObjectRequest("apps/zxzssgl/getDetail", map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    JSONObject jo = result.getJSONObject("data");
                    if(jo != null) {
                        Zxzssgl z = getGson().fromJson(jo.toString(),Zxzssgl.class);
                        lcid = z.getLc();
                        xqid = z.getXqid();
                        ssqid = z.getSsqid();
                        fjhid = z.getFjhid();
                        cwhid = z.getCwhid();
                        tzyyid = z.getTzyyid();
                        sslid = z.getSslid();
//                        TextView xq_pick,ssq_pick,ssl_pick,lc_pick,fjh_pick,cwh_pick,fx_pick,zsf_pick,cws_pick,tzyy_pick;
                        xq_pick.setText(z.getXq());
                        ssq_pick.setText(z.getSsq());
                        ssl_pick.setText(z.getSsl());
                        lc_pick.setText(z.getLc());
                        fjh_pick.setText(z.getFjh());
                        cwh_pick.setText(z.getCwh());
                        fx_pick.setText(z.getFx());
                        zsf_pick.setText(z.getZsf());
                        cws_pick.setText(z.getCws());
                        tzyy_pick.setText(z.getTzyy());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(VolleyError error) {
            }
        });
    }

    @Override
    public void back() {
        new AlertDialog(mContext).show(null, "确定退出调宿审核？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ZssglTiaoSuSubmitActivity.super.back();
            }
        });
    }

    /**
     * 跳转到选择页面
     * @param picks
     * @param result
     */
    public void toPickActivity(List<Common> picks,int result,int param){
        if(param == PICK_GETDATA) {
            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
            Intent intent = new Intent(mContext, ZsstjPickActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("kfs", (Serializable) picks);
            intent.putExtras(bundle);
            startActivityForResult(intent, result);
            overridePendingTransition(R.anim.push_up_in, 0);
        }
    }

    /**
     * 展示错误信息
     * @param param
     * @param msg
     */
    private void errorMsg(int param,String msg){
        if(param == PICK_GETDATA){
            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
            showErrorMsg(mRootView, msg);
        }
    }
}
