package com.dk.mp.lgj;

import android.content.Intent;
import android.os.Handler;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.LoginMsg;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.main.R;
import com.dk.mp.main.home.ui.HomeActivity;
import com.dk.mp.main.util.PushUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * 作者：janabo on 2016/12/14 15:08
 */
public class SplashActivity extends MyActivity {
    private Handler mHandler = new Handler();
    private CoreSharedPreferencesHelper helper;

    @Override
    protected int getLayoutID() {
        return R.layout.mp_splash;
    }

    @Override
    protected void initialize() {
        super.initialize();
        helper = getSharedPreferences();
        //在自己的应用初始Activity中加入如下两行代码 //TODO
//        RefWatcher refWatcher = MyApplication.getRefWatcher(this);
//        refWatcher.watch(this);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent ( SplashActivity.this, HomeActivity.class );
                startActivity ( intent );
                finish ( );
            }
        },2000);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                login();
            }
        },0);

    }

    private void login(){
        final LoginMsg loginMsg = helper.getLoginMsg();
        if (loginMsg == null) return;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", loginMsg.getUid());
        map.put("password", loginMsg.getPsw());
        HttpUtil.getInstance().postJsonObjectRequest("login", map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result)  {
                try {
                    if (result.getInt("code") == 200) {
                        helper.setUserInfo(result.getJSONObject("data").toString());
                        new PushUtil(mContext).setTag();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(mContext);
        MobclickAgent.onPause(mContext);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(mContext);
        MobclickAgent.onResume(getApplicationContext());
    }

}
