package com.dk.mp.core.util;

import android.util.Log;

/**
 * 日志工具类
 * 作者：janabo on 2016/12/14 17:30
 */
public class Logger {
    private static final boolean DEBUG = true;//正式发布是否打印日志

    /**
     * 打印信息.
     * @param msg 信息
     */
    public static void info(String msg) {
        if (DEBUG) {
            Log.i("INFO", msg);
        }
    }

    /**
     * 打印错误信息.
     * @param msg 信息
     */
    public static void error(String msg) {
        if (DEBUG) {
            Log.e("ERROR", msg);
        }
    }

    /**
     * 打印错误信息.
     * @param msg 信息
     */
    public static void debug(String msg,Exception e) {
        if (DEBUG) {
            Log.i("DEBUG", msg);
        }
    }
}
