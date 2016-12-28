package com.dk.mp.core.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.view.WindowManager;

import com.dk.mp.core.application.MyApplication;

/**
 * 作者：janabo on 2016/12/14 16:28
 */
public class DeviceUtil {
    private static int width;

    /**
     * 获取网络状态.
     * @return boolean
     */
    public static boolean checkNet() {
        Context context = MyApplication.getContext();
        boolean flag = false;
        ConnectivityManager cwjManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cwjManager.getActiveNetworkInfo() != null) {
            flag = cwjManager.getActiveNetworkInfo().isAvailable();
        }
        return flag;
    }

    /**
     * 功能:获得版本号.
     *
     * @param context
     *            Context
     * @return 版本号
     * @throws Exception
     */
    public static String getVersionName(Context context) {
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            String version = packInfo.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取屏幕宽度。
     *
     * @param context
     *            Context
     * @return 宽度
     */
    public static int getScreenWidth(Context context) {
        if (width == 0) {
            WindowManager wm = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            width = wm.getDefaultDisplay().getWidth();
        }
        return width;
    }
}
