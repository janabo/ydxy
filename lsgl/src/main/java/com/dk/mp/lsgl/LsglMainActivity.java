package com.dk.mp.lsgl;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.lsgl.entity.RoleEntity;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by dongqs on 2017/1/23.
 */

public class LsglMainActivity extends MyActivity implements View.OnClickListener{

    private LinearLayout bzr;
    private LinearLayout xb;
    private LinearLayout xgc;
    private LinearLayout fdy;

    private ErrorLayout mError;

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
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        mError.setOnLayoutClickListener(this);
    }

    private void loadDatas() {
        mError.setErrorType(ErrorLayout.LOADDATA);
        HttpUtil.getInstance().postJsonObjectRequest("apps/lsxsgl/role", null, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if (result.optInt("code") == 200){//成功返回数据
                    try {
                        List<RoleEntity> roles = getGson().fromJson(result.getJSONArray("data").toString(),new TypeToken<List<RoleEntity>>(){}.getType());
                        if (roles == null){
                            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
                        } else if (roles.size() == 0) {
                            mError.setErrorType(ErrorLayout.NODATA);
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
                            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                        }
                    } catch (JSONException e) {
                        mError.setErrorType(ErrorLayout.NETWORK_ERROR);
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {
                mError.setErrorType(ErrorLayout.NETWORK_ERROR);
            }
        });
    }

    android.os.Handler handler=new android.os.Handler();
    boolean isposting = false;

    public void tobzr(View v){
        doStartActivity("1",v);
    }
    public void toxb(View v){
        doStartActivity("3",v);
    }
    public void toxg(View v){
        doStartActivity("4",v);
    }
    public void tofdy(View v){
        doStartActivity("2",v);
    }

    private void doStartActivity(final String role , final View view){
        if (!isposting) {
            isposting = true;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(LsglMainActivity.this,LsglTabActivity.class);
                    intent.putExtra("role",role);
                    intent.putExtra("x",(view.getLeft() + view.getRight()) / 2);
                    intent.putExtra("y",(view.getTop() + view.getBottom()) / 2 + StringUtils.dip2px(LsglMainActivity.this,40));
                    startActivity(intent);
                    isposting = false;
                }
            },500);
        }
    }

    @Override
    public void onClick(View view) {
        loadDatas();
    }
}
