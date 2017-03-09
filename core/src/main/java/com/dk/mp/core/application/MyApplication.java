package com.dk.mp.core.application;

import android.animation.Animator;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.LinearInterpolator;

import com.dk.mp.core.db.RealmHelper;
import com.dk.mp.core.entity.App;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.CrashHandler;
import com.dk.mp.core.util.ImagePipelineConfigFactory;
import com.dk.mp.core.util.Logger;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * 作者：janabo on 2016/12/14 14:53
 */
public class MyApplication extends Application{
    public static App app = new App("","","","显示应用","-1","","icon_addapp","addapps");

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

        //在自己的Application中添加如下代码TODO
//        refWatcher = LeakCanary.install(this);

        //初始化图片加载框架
        Fresco.initialize(this, ImagePipelineConfigFactory.getImagePipelineConfig(this));
    }

    //在自己的Application中添加如下代码
//    private RefWatcher refWatcher;

    //在自己的Application中添加如下代码
//    public static RefWatcher getRefWatcher(Context context) {
//        MyApplication application = (MyApplication) context
//                .getApplicationContext();
//        return application.refWatcher;
//    }

    private static Animator animator;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static Animator createRevealAnimator(boolean reversed, int x, int y, final View view, final Activity mContext) {
        float hypot = (float) Math.hypot(view.getHeight(), view.getWidth());
        float startRadius = reversed ? hypot : 0;
        float endRadius = reversed ? 0 : hypot;

        Logger.info("###########hypot="+hypot);
//        animator.cancel();
        animator = ViewAnimationUtils.createCircularReveal(
                view, x, y,
                startRadius,
                endRadius);
        animator.setDuration(400);
//        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setInterpolator(new LinearInterpolator());
//        Logger.info("###########duration="+animator.getDuration());
       Logger.info("##########startime="+ new Date().getTime());
        if (reversed)
//            Logger.info("##########startime2="+ new Date().getTime());
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }
                @Override
                public void onAnimationEnd(Animator animation) {
                    Logger.info("##########endtime="+ new Date().getTime());
                    view.setVisibility(View.INVISIBLE);
                    mContext.finish();
                    animator.cancel();
                }
                @Override
                public void onAnimationCancel(Animator animation) {}
                @Override
                public void onAnimationRepeat(Animator animation) {}
            });
        return animator;
    }


}
