package com.dk.mp.core.application;

import android.app.Application;
import android.content.Context;

import com.dk.mp.core.util.CrashHandler;

/**
 * 作者：janabo on 2016/12/14 14:53
 */
public class MyApplication extends Application{

    private static Application mApplication;

    public static Context getContext ( ) {
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;

        //初始化全局异常
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }
}
