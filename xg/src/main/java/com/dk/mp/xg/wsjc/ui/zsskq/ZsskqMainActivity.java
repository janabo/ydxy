package com.dk.mp.xg.wsjc.ui.zsskq;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.GsonData;
import com.dk.mp.core.entity.Role;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.xg.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.List;

/**
 * 住宿生考勤
 * 作者：janabo on 2017/1/22 09:34
 */
public class ZsskqMainActivity extends MyActivity implements View.OnClickListener{
    private ErrorLayout mError;
    private LinearLayout bzr,xb,xgc,fdy;//调宿统计,退宿统计,停宿统计
    private TextView bzr_s,bzr_name,xb_s,xb_name,xgc_s,xgc_name;//调宿统计名称第一个字,调宿统计名称,退宿统计名称第一个字,退宿统计名称

    @Override
    protected int getLayoutID() {
        return R.layout.app_wsjctj_main;
    }

    @Override
    protected void initView() {
        super.initView();
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        bzr = (LinearLayout) findViewById(R.id.bzr);
        xb = (LinearLayout) findViewById(R.id.xb);
        xgc = (LinearLayout) findViewById(R.id.xgc);
        fdy = (LinearLayout) findViewById(R.id.fdy);
        bzr_s = (TextView) findViewById(R.id.bzr_s);
        bzr_name = (TextView) findViewById(R.id.bzr_name);
        xb_s = (TextView) findViewById(R.id.xb_s);
        xb_name = (TextView) findViewById(R.id.xb_name);
        xgc_s = (TextView) findViewById(R.id.xgc_s);
        xgc_name = (TextView) findViewById(R.id.xgc_name);
        fdy.setVisibility(View.GONE);
        getData();
    }

    @Override
    protected void initialize() {
        super.initialize();
        setTitle("住宿生考勤");
    }

    /**
     * 班主任
     * @param v
     */
    public void tobzr(View v){
        toDetail("1");
    }

    /**
     * 系部
     * @param v
     */
    public void toxb(View v){
        toDetail("3");
    }

    /**
     * 学工处
     * @param v
     */
    public void toxgc(View v){
        toDetail("4");
    }

    /**
     * 辅导员统计界面
     * @param v
     */
    public void tofdy(View v){
        toDetail("2");
    }

    public void getData(){
        if(DeviceUtil.checkNet()){
            getList();
        }else{
            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
        }
    }

    private void getList() {
        HttpUtil.getInstance().postJsonObjectRequest("apps/zsskq/role", null, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result != null) {
                        GsonData<Role> gsonData = new Gson().fromJson(result.toString(), new TypeToken<GsonData<Role>>() {
                        }.getType());
                        if (gsonData.getCode() == 200) {
                            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                            List<Role> dfxxes = gsonData.getData();
                            if(dfxxes.size()>0){//获取数据不为空
                                if(dfxxes.size() == 1) {//如果只有一g个角色数据之间跳转响应界面
                                    toDetail(dfxxes.get(0).getId());
                                    finish();
                                }else{
                                    for(Role r : dfxxes){
                                        if("1".equals(r.getId())){
                                            bzr.setVisibility(View.VISIBLE);
                                            continue;
                                        }else if("2".equals(r.getId())){
                                            fdy.setVisibility(View.VISIBLE);
                                            continue;
                                        }else if("3".equals(r.getId())){
                                            xb.setVisibility(View.VISIBLE);
                                            continue;
                                        }else if("4".equals(r.getId())){
                                            xgc.setVisibility(View.VISIBLE);
                                            continue;
                                        }
                                    }
                                }
                            }else{
                                mError.setErrorType(ErrorLayout.NODATA);
                            }
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

    @Override
    public void onClick(View view) {
        getData();
    }

    /**
     * 跳转界面
     * @param role
     */
    public void toDetail(String role){
        Intent intent = new Intent(mContext,ZsskqTjTabActivity.class);
        intent.putExtra("role",role);
        startActivity(intent);
    }
}
