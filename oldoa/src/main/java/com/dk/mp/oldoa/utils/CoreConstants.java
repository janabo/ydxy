package com.dk.mp.oldoa.utils;

import android.os.Environment;

import com.dk.mp.oldoa.R;

import java.io.File;

/**
 * 作者：janabo on 2017/2/20 17:25
 */
public class CoreConstants {

    public static final String DATABASE_NAME = "com.dk.mp.db"; // 数据库名
    public static final int DATABASE_VERSION = 1; // 版本号

    public static final boolean DEBUG = true;// 是否打印debug日志
    public static final String BASEPICPATH = Environment.getExternalStorageDirectory() + "/mobileschool/cache/";
    public static final String DOWNLOADPATH = Environment.getExternalStorageDirectory() + "/mobileschool/download/";

    public static final String REFRESH_APP = "action.refreshApp";
    public static final String REFRESH_APPTAB = "action.refreshAppTab";
    public static final String REFRESH_MSGCOUNT = "action.refreshCount";
    public static final String REFRESH_MSG = "action.refreshMsm";
    public static final String REFRESH_MSG_HISTORY = "action.refreshMsmHis";

    public static final String APKIMAGE = Environment.getExternalStorageDirectory() + "/mobileschool/cache/apk.png";

    public static String idSession;
    public static final String LOG_PATH = Environment.getExternalStorageDirectory() + "/AppLog/log/";
    static {
        File cfile = new File(BASEPICPATH);
        if (!cfile.exists()) {
            cfile.mkdirs();
        }

        File dfile = new File(DOWNLOADPATH);
        if (!dfile.exists()) {
            dfile.mkdirs();
        }

    }

    public static final int ANIM_BACK_IN=0;
    public static final int ANIM_BACK_OUT= R.anim.push_down_out;

    public static final int ANIM_TO_IN=R.anim.push_up_in;
    public static final int ANIM_TO_OUT=R.anim.push_up_out;
}
