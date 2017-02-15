package com.dk.mp.core.application;

import android.app.Application;
import android.content.Context;

import com.dk.mp.core.db.RealmHelper;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.CrashHandler;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * 作者：janabo on 2016/12/14 14:53
 */
public class MyApplication extends Application{

    private static Application mApplication;


    private CoreSharedPreferencesHelper preference;

    public static Context getContext ( ) {
        return mApplication;
    }

    private static MyApplication instance;

    public static MyApplication newInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        instance = this;
        preference = new CoreSharedPreferencesHelper(this);

        //初始化全局异常
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());

        Realm.init(this);
        RealmConfiguration configuration=new RealmConfiguration.Builder()
                .name(RealmHelper.DB_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);

        //在自己的Application中添加如下代码
        refWatcher = LeakCanary.install(this);
    }

    //在自己的Application中添加如下代码
    private RefWatcher refWatcher;

    //在自己的Application中添加如下代码
    public static RefWatcher getRefWatcher(Context context) {
        MyApplication application = (MyApplication) context
                .getApplicationContext();
        return application.refWatcher;
    }

}
