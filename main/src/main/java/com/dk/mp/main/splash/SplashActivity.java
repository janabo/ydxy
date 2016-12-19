package com.dk.mp.main.splash;

import android.content.Intent;
import android.os.Handler;

import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.main.R;
import com.dk.mp.main.home.ui.HomeActivity;

/**
 * 作者：janabo on 2016/12/14 15:08
 */
public class SplashActivity extends MyActivity {
    private Handler mHandler = new Handler();

    @Override
    protected int getLayoutID() {
        return R.layout.mp_splash;
    }

    @Override
    protected void initialize() {
        super.initialize();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent ( SplashActivity.this, HomeActivity.class );
                startActivity ( intent );
                finish ( );
            }
        },2000);

    }
}
