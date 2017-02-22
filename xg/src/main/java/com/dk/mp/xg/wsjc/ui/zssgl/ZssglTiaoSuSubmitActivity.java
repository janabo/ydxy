package com.dk.mp.xg.wsjc.ui.zssgl;

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
import com.dk.mp.core.entity.GsonData;
import com.dk.mp.core.entity.JsonData;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.BroadcastUtil;
import com.dk.mp.core.util.SnackBarUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.view.DrawCheckMarkView;
import com.dk.mp.core.view.DrawCrossMarkView;
import com.dk.mp.core.view.DrawHookView;
import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.entity.Common;
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
    private String mId,flag;


    @Override
    protected int getLayoutID() {
        return R.layout.app_zssgl_tiaosu_submit;
    }

    @Override
    protected void initView() {
        super.initView();
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
        getXqs();
        getSsqs();
        getSsls();
        getTsyys();
    }

    /**
     * 获取楼层列表
     */
    public void getLcs(String sslid){
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
                            }else{
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
     * 获取房间号列表
     */
    public void getFjhs(String ssl,String lc){
        fjhs.clear();
        Map<String,Object> map = new HashMap<>();
        map.put("ssl",ssl);
        map.put("lc",lc);
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
                            }else{
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
     * 获取床位号列表
     */
    public void getCwhs(String fjhid){
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
                            }else{
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
     * 获取校区列表
     */
    public void getXqs(){
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
                            }else{
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
     * 获取宿舍区列表
     */
    public void getSsqs(){
        HttpUtil.getInstance().postJsonObjectRequest("apps/zxzssgl/ssqList", null, new HttpListener<JSONObject>() {
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
                            }else{
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
     * 获取宿舍楼列表
     */
    public void getSsls(){
        HttpUtil.getInstance().postJsonObjectRequest("apps/zxzssgl/sslList", null, new HttpListener<JSONObject>() {
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
                            }else{
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
     * 退宿原因和调整原因
     */
    public void getTsyys(){
        HttpUtil.getInstance().postJsonObjectRequest("apps/zxzssgl/tzyyList", null, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result != null) {
                        GsonData<Common> gsonData = new Gson().fromJson(result.toString(), new TypeToken<GsonData<Common>>() {
                        }.getType());
                        if (gsonData.getCode() == 200) {
                            List<Common> dfxxes = gsonData.getData();
                            if(dfxxes.size()>0){//获取数据不为空
                                tzyys.addAll(dfxxes);
                            }else{
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
     * 获取房间详情
     * @param fjh 房间号
     */
    public void getFjDetail(String fjh){
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
        if (xqs.size() > 0) {
            Intent intent = new Intent(mContext, ZsstjPickActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("kfs", (Serializable) xqs);
            intent.putExtras(bundle);
            startActivityForResult(intent, 2);
            overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        } else {
            getXqs();
            if (xqs.size() > 0) {
                Intent intent = new Intent(mContext, ZsstjPickActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("kfs", (Serializable) xqs);
                intent.putExtras(bundle);
                startActivityForResult(intent, 2);
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            }else {
                showErrorMsg(mRootView, "未获取到校区选项");
            }
        }
    }

    /**
     * 选择宿舍区
     * @param v
     */
    public void toPickSsq(View v){
        if (ssqs.size() > 0) {
            Intent intent = new Intent(mContext, ZsstjPickActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("kfs", (Serializable) ssqs);
            intent.putExtras(bundle);
            startActivityForResult(intent, 3);
            overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        } else {
            getSsqs();
            if (ssqs.size() > 0) {
                Intent intent = new Intent(mContext, ZsstjPickActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("kfs", (Serializable) ssqs);
                intent.putExtras(bundle);
                startActivityForResult(intent, 3);
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            } else {
                showErrorMsg(mRootView, "未获取到宿舍区选项");
            }
        }
    }

    /**
     * 选择宿舍楼
     * @param v
     */
    public void toPickSsl(View v){
        if (ssls.size() > 0) {
            Intent intent = new Intent(mContext, ZsstjPickActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("kfs", (Serializable) ssls);
            intent.putExtras(bundle);
            startActivityForResult(intent, 4);
            overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        } else {
            getSsls();
            if (ssls.size() > 0) {
                Intent intent = new Intent(mContext, ZsstjPickActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("kfs", (Serializable) ssls);
                intent.putExtras(bundle);
                startActivityForResult(intent, 4);
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            } else {
                showErrorMsg(mRootView, "未获取到宿舍楼选项");
            }
        }
    }

    /**
     * 选择楼层
     * @param v
     */
    public void toPickLc(View v){
        if (lcs.size() > 0) {
            Intent intent = new Intent(mContext, ZsstjPickActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("kfs", (Serializable) lcs);
            intent.putExtras(bundle);
            startActivityForResult(intent, 5);
            overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        } else {
            getLcs(sslid);
            if (lcs.size() > 0) {
                Intent intent = new Intent(mContext, ZsstjPickActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("kfs", (Serializable) lcs);
                intent.putExtras(bundle);
                startActivityForResult(intent, 5);
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            }else {
                showErrorMsg(mRootView, "未获取到楼层选项");
            }
        }
    }

    /**
     * 选择房间号
     * @param v
     */
    public void toPickFjh(View v){
        if (fjhs.size() > 0) {
            Intent intent = new Intent(mContext, ZsstjPickActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("kfs", (Serializable) fjhs);
            intent.putExtras(bundle);
            startActivityForResult(intent, 6);
            overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        } else {
            getFjhs(sslid,lcid);
            if (fjhs.size() > 0) {
                Intent intent = new Intent(mContext, ZsstjPickActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("kfs", (Serializable) fjhs);
                intent.putExtras(bundle);
                startActivityForResult(intent, 6);
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            } else {
                showErrorMsg(mRootView, "未获取到房间号选项");
            }
        }
    }

    /**
     * 选择床位号
     * @param v
     */
    public void toPickCwh(View v){
        if (cwhs.size() > 0) {
            Intent intent = new Intent(mContext, ZsstjPickActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("kfs", (Serializable) cwhs);
            intent.putExtras(bundle);
            startActivityForResult(intent, 7);
            overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        } else {
            getCwhs(fjhid);
            if (cwhs.size() > 0) {
                Intent intent = new Intent(mContext, ZsstjPickActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("kfs", (Serializable) cwhs);
                intent.putExtras(bundle);
                startActivityForResult(intent, 7);
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            } else {
                showErrorMsg(mRootView, "未获取到床位号选项");
            }
        }
    }

    /**
     * 选择调整原因
     * @param v
     */
    public void toPickTzyy(View v){
        if (tzyys.size() > 0) {
            Intent intent = new Intent(mContext, ZsstjPickActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("kfs", (Serializable) tzyys);
            intent.putExtras(bundle);
            startActivityForResult(intent, 8);
            overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        } else {
            showErrorMsg(mRootView, "未获取到调整原因选项");
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
                    if (jd.getCode() != 200 && !(Boolean) jd.getData()) {
                        SnackBarUtil.showShort(mRootView,jd.getMsg());
                        errorInfo();
                    }else{
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
                                back();
                            }
                        },1000);
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
                    xqid = data.getStringExtra("kfsid");
                    String xqname = data.getStringExtra("kfs");
                    xq_pick.setText(xqname);
                }
                break;
            case 3:
                if (resultCode == RESULT_OK) {
                    ssqid = data.getStringExtra("kfsid");
                    String ssqname = data.getStringExtra("kfs");
                    ssq_pick.setText(ssqname);
                }
                break;
            case 4:
                if (resultCode == RESULT_OK) {
                    sslid = data.getStringExtra("kfsid");
                    String sslname = data.getStringExtra("kfs");
                    ssl_pick.setText(sslname);
                    getLcs(sslid);
                }
                break;
            case 5:
                if (resultCode == RESULT_OK) {
                    lcid = data.getStringExtra("kfsid");
                    String lcname = data.getStringExtra("kfs");
                    lc_pick.setText(lcname);
                    getFjhs(sslid,lcid);
                }
                break;
            case 6:
                if (resultCode == RESULT_OK) {
                    fjhid = data.getStringExtra("kfsid");
                    String fjhname = data.getStringExtra("kfs");
                    fjh_pick.setText(fjhname);
                    getCwhs(fjhid);
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
                    tzyyid = data.getStringExtra("kfsid");
                    String tzyyname = data.getStringExtra("kfs");
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
}
