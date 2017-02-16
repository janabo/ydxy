package com.dk.mp.main.splash;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.LoginMsg;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.main.R;
import com.dk.mp.main.home.ui.HomeActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：janabo on 2016/12/14 15:08
 */
public class SplashActivity extends MyActivity {
    private Handler mHandler = new Handler();
    private ImageView imageView;
    private RelativeLayout layout;
    private CoreSharedPreferencesHelper helper;

    @Override
    protected int getLayoutID() {
        return R.layout.mp_splash;
    }

    @Override
    protected void initialize() {
        super.initialize();
        helper = getSharedPreferences();
        imageView = (ImageView) findViewById(R.id.splashImageview);
        layout = (RelativeLayout) findViewById(R.id.splash);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent ( SplashActivity.this, HomeActivity.class );
                startActivity ( intent );
                finish();
            }
        },2000);

    }

    public static void releaseImageViewResouce(ImageView imageView) {
        if (imageView == null) return;
        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseImageViewResouce(imageView);
        BitmapDrawable bd = (BitmapDrawable)layout.getBackground();
        layout.setBackgroundResource(0);
        bd.setCallback(null);
        bd.getBitmap().recycle();
        layout = null;
    }

    private void login(){
        final LoginMsg loginMsg = helper.getLoginMsg();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", loginMsg.getUid());
        map.put("password", loginMsg.getPsw());
        HttpUtil.getInstance().postJsonObjectRequest("login", map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result)  {
                try {
                    if (result.getInt("code") == 200) {
                        helper.setUserInfo(result.getJSONObject("data").toString());
//                        new PushUtil(context).setTag();
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


}
