package com.dk.mp.lsgl;

import com.android.volley.VolleyError;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dongqs on 2017/2/6.
 */

public class LsglInfoActivity extends MyActivity{

    @Override
    protected int getLayoutID() {
        return R.layout.app_lsgl_info;
    }

    @Override
    protected void initialize() {
        super.initialize();
        setTitle("留宿学生管理");

        initDatas();
    }

    private void initDatas() {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("id",getIntent().getStringExtra("id"));
        map.put("type",getIntent().getStringExtra("type"));
        HttpUtil.getInstance().postJsonObjectRequest("apps/lsxsgl/detail", map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if (result.optInt("code") == 200){//成功返回数据
                }
            }
            @Override
            public void onError(VolleyError error) {
            }
        });
    }
}
