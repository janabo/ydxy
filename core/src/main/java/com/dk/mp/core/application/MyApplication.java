package com.dk.mp.core.application;

import android.app.Application;
import android.content.Context;

import com.dk.mp.core.db.RealmHelper;
import com.dk.mp.core.entity.App;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.CrashHandler;
import com.dk.mp.core.util.ImagePipelineConfigFactory;
import com.facebook.drawee.backends.pipeline.Fresco;

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

    public static App app = new App("","","","显示应用","-1","","icon_addapp","addapps");

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

        //初始化图片加载框架
        Fresco.initialize(this, ImagePipelineConfigFactory.getImagePipelineConfig(this));
    }

}
