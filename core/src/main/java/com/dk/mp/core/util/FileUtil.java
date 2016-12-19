package com.dk.mp.core.util;

import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 作者：janabo on 2016/12/14 17:29
 */
public class FileUtil {

    private static String BASEFILEPATH = Environment.getExternalStorageDirectory() + "/mobileschool/cache/";

    /**
     * 删除文件.
     *
     * @param days
     *            文件创建时间与当前时间相差天数
     */
    public static void delete(int days) {
        deleteDirectory(BASEFILEPATH, days);
    }

    /**
     * 删除文件.
     *
     * @param absolutePath
     *            文件夹路径
     * @param days
     *            文件创建时间与当前时间相差天数
     */
    public static void deleteDirectory(String absolutePath, int days) {
        File file = new File(absolutePath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (final File f : files) {
                if (f.isDirectory()) {
                    deleteDirectory(f.getPath(), days);
                } else if (f.isFile()) {
                    if (f.exists() && checkFile(f.getAbsolutePath(), days)) {
                        f.delete();
                    }
                }
            }
        }
    }

    /**
     * 判断文件最后修改时间和当前时间是否相差 days 天.
     *
     * @param path
     *            文件路径
     * @param days
     *            相差天数
     * @return boolean true: 相差days以上;false:相差days天以内
     */
    public static boolean checkFile(String path, int days) {
        try {
            Logger.info("checkFile days:" + days);
            File file = new File(path);
            Long time = file.lastModified();
            Calendar cd = Calendar.getInstance();
            cd.setTimeInMillis(time);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// 可以方便地修改日期格式 = new SimpleDateFormat("yyyy-MM-dd");// 可以方便地修改日期格式
            long day = TimeUtils.getDaysBetween(dateFormat.format(cd.getTime()), TimeUtils.getToday());
            Logger.info("checkFile day:" + day);
            if (day >= days) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
