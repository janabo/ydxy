package com.dk.mp.lsgl;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.lsgl.entity.RoleEntity;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by dongqs on 2017/1/23.
 */

public class LsglMainActivity extends MyActivity{

    private LinearLayout bzr;
    private LinearLayout xb;
    private LinearLayout xgc;
    private LinearLayout fdy;

    @Override
    protected int getLayoutID() {
        return R.layout.app_lsgl_main;
    }

    @Override
    protected void initialize() {
        super.initialize();
        setTitle("在校生住宿管理");
        initViews();
        loadDatas();
    }

    private void initViews() {
        bzr = (LinearLayout)findViewById(R.id.bzr);
        xb = (LinearLayout)findViewById(R.id.xb);
        xgc = (LinearLayout)findViewById(R.id.xgc);
        fdy = (LinearLayout)findViewById(R.id.fdy);
    }

    private void loadDatas() {
        HttpUtil.getInstance().postJsonObjectRequest("apps/lsxsgl/role", null, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if (result.optInt("code") == 200){//成功返回数据
                    try {
                        List<RoleEntity> roles = getGson().fromJson(result.getJSONArray("data").toString(),new TypeToken<List<RoleEntity>>(){}.getType());
                        if (roles == null){
                            //errory
                        } else if (roles.size() == 0) {
                            //errory
                        } else {
                            for (RoleEntity role : roles) {
                                if (role.getId().equals("1")) {
                                    bzr.setVisibility(View.VISIBLE);
                                } else if (role.getId().equals("3")) {
                                    xb.setVisibility(View.VISIBLE);
                                } else if (role.getId().equals("4")) {
                                    xgc.setVisibility(View.VISIBLE);
                                } else if (role.getId().equals("2")) {
                                    fdy.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    } catch (JSONException e) {

                    }
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    android.os.Handler handler=new android.os.Handler();
    boolean isposting = false;

    public void tobzr(View v){
        doStartActivity("1");
    }
    public void toxb(View v){
        doStartActivity("3");
    }
    public void toxg(View v){
        doStartActivity("4");
    }
    public void tofdy(View v){
        doStartActivity("2");
    }

    private void doStartActivity(final String role){
        if (!isposting) {
            isposting = true;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(LsglMainActivity.this,LsglTabActivity.class);
                    intent.putExtra("role",role);
                    startActivity(intent);
                    isposting = false;
                }
            },500);
        }
    }
}
