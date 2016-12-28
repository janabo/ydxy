package com.dk.mp.lgj;

import android.content.Intent;
import android.os.Handler;

import com.dk.mp.core.application.MyApplication;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.main.R;
import com.dk.mp.main.home.ui.HomeActivity;
import com.squareup.leakcanary.RefWatcher;

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
        //在自己的应用初始Activity中加入如下两行代码
        RefWatcher refWatcher = MyApplication.getRefWatcher(this);
        refWatcher.watch(this);

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
