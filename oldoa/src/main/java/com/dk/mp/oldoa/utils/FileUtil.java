package com.dk.mp.oldoa.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.dk.mp.core.util.Logger;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.util.TimeUtils;
import com.dk.mp.oldoa.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 作者：janabo on 2017/2/20 17:24
 */
public class FileUtil {

    private static String BASEFILEPATH = CoreConstants.BASEPICPATH;

    /**
     * 加成sdcard卡是否存在.
     *
     * @return 存在：true；不存在：false
     */
    public static boolean checkSdcard() {
        return Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * 检查根路径是否存在.
     *
     * @return 存在：true；不存在：false
     */
    public static boolean checkBaseFath() {
        if (!checkSdcard()) {
            return false;
        }
        File file = new File(BASEFILEPATH);
        return file.exists();
    }

    /**
     * 在sdcard卡上创建文件
     *
     * @param fileName
     *            文件名
     * @param content
     *            文件内容
     */
    public static void saveFile(String fileName, String content) {
        if (checkSdcard()) {
            File file = new File(BASEFILEPATH);
            if (!file.exists()) {
                file.mkdirs();
            }

            char[] c = new char[content.length()];
            for (int i = 0; i < content.length(); i++) {
                c[i] = content.charAt(i);
            }
            FileWriter fw = null;
            try {
                fw = new FileWriter(BASEFILEPATH + fileName);
                fw.write(c);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fw != null) {
                        fw.close();
                    }
                } catch (IOException e) {
                }
            }
        }
    }

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
     * 删除下载文件.
     *
     * @param path
     *            文件下载链接
     */
    public static void deleteDownLoadFile(String path) {
        File f = new File(CoreConstants.DOWNLOADPATH + path);
        if (f.exists()) {
            f.delete();
        }
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

    /**
     * 下砸文件.
     *
     * @param url
     *            下载链接
     * @param fileName
     *            文件名
     * @throws IOException
     *             IOException
     */
    public static void downFile(String url, String fileName) throws IOException {
        // 下载函数
        // 获取文件名
        URL myURL = new URL(url);
        URLConnection conn = myURL.openConnection();
        conn.connect();
        InputStream is = conn.getInputStream();
        try {
            if (conn.getContentLength() > 0 && null != is) {
                FileOutputStream fos = new FileOutputStream(CoreConstants.DOWNLOADPATH + fileName);
                // 把数据存入路径+文件名
                byte buf[] = new byte[1024];
                do {
                    // 循环读取
                    int numread = is.read(buf);
                    if (numread == -1) {
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (true);
                try {
                    is.close();
                } catch (Exception ex) {
                    Log.e("tag", "error: " + ex.getMessage(), ex);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 下砸文件.
     *
     * @param url
     *            下载链接
     * @param fileName
     *            文件名
     * @throws IOException
     *             IOException
     */
    public static void downFile(String url, String fileName, Handler downHandler) throws IOException {
        // 下载函数
        // 获取文件名
        URL myURL = new URL(url);
        URLConnection conn = myURL.openConnection();
        conn.connect();
        InputStream is = conn.getInputStream();
        try {
            if (null != is) {

                FileOutputStream fos = new FileOutputStream(CoreConstants.DOWNLOADPATH + fileName);
                // 把数据存入路径+文件名
                byte buf[] = new byte[1024];
                do {
                    // 循环读取
                    int numread = is.read(buf);
                    if (numread == -1) {
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (true);
                try {
                    is.close();
                    Logger.error("------下载成功-------");
                    downHandler.sendEmptyMessage(0);// 下载成功
                } catch (Exception ex) {
                    Log.e("tag", "error: " + ex.getMessage(), ex);
                }
            } else {
                Logger.error("------下载失败-------");
                downHandler.sendEmptyMessage(1);// 下载失败
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 下载apk文件.
     *
     * @param url
     *            下载路径
     * @return 本地存储路径
     * @throws IOException
     *             IOException
     */
    public static boolean downApk(String url, String path) throws IOException {
        try {
            Logger.info("path:" + path);
            // 获取文件名
            URL myURL = new URL(url);
            URLConnection conn = myURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            int fileSize = conn.getContentLength();// 根据响应获取文件大小
            if (fileSize <= 0)
                throw new RuntimeException("无法获知文件大小 ");
            if (is == null)
                throw new RuntimeException("stream is null");
            FileOutputStream fos = new FileOutputStream(path);
            // 把数据存入路径+文件名
            byte buf[] = new byte[1024];
            do {
                // 循环读取
                int numread = is.read(buf);
                if (numread == -1) {
                    break;
                }
                fos.write(buf, 0, numread);
            } while (true);
            try {
                is.close();
            } catch (Exception ex) {
                Log.e("tag", "error: " + ex.getMessage(), ex);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 过滤http链接字符.
     *
     * @param str
     *            http链接
     * @return 过滤后的http链接
     */
    public static String filterPath(String str) {
        return str.replace("\\", "/").replace("?", "").replace("/", "").replace(":", "");
    }

    /**
     * 判断该视频是否已经下载到本地，是则播放本地视频，否则播放网络视频.
     *
     * @param path
     *            视频网络地址
     * @return 处理后的视频来源
     */
    public static String getVideoPath(String path, String url) {
        if (StringUtils.isNotEmpty(path)) {
            if (new File(path).exists()) {
                return path;
            } else {
                return url;
            }
        } else {
            return url;
        }
    }

    /**
     * 判断文件是否存在.
     *
     * @param fielUrl
     *            文件网络路径.
     * @return boolean
     */
    public static boolean checkExists(Context context, String fielUrl) {
        String fileName = FileUtil.filterPath(getUrl(context, fielUrl));
        if (!new File(CoreConstants.BASEPICPATH + fileName).exists()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 处理请求链接.
     *
     * @param action
     *            请求链接
     * @return 处理后的请求链接
     */
    private static String getUrl(Context context, String action) {
        if (action.startsWith("http://")) {
            return action;
        } else {
            return context.getResources().getString(R.string.rootUrl) + action;
        }
    }

    /**
     * 读文件内容.
     *
     * @param fileName
     *            文件路径
     * @return 文件内容
     */
    public static String readFile(String fileName) {
        StringBuffer s = new StringBuffer();
        Reader reader = null;
        try {
            char[] tempchars = new char[30];
            int charread = 0;
            String encoding = "gbk"; // 字符编码(可解决中文乱码问题 )
            reader = new InputStreamReader(new FileInputStream(fileName), encoding);
            // 读入多个字符到字符数组中，charread为一次读取字符数
            while ((charread = reader.read(tempchars)) != -1) {
                // 同样屏蔽掉\r不显示
                if ((charread == tempchars.length) && (tempchars[tempchars.length - 1] != '\r')) {
                    s.append(tempchars);
                } else {
                    for (int i = 0; i < charread; i++) {
                        if (tempchars[i] == '\r') {
                            continue;
                        } else {
                            s.append(tempchars[i]);
                        }
                    }
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return s.toString();
    }

    /**
     * 调用系统功能打开文件.
     *
     * @param filePath
     *            文件路径
     * @return Intent
     */
    public static Intent openFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists())
            return null;
		/* 取得扩展名 */
        String end = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase();
		/* 依扩展名的类型决定MimeType */
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            return getAudioFileIntent(filePath);
        } else if (end.equals("3gp") || end.equals("mp4")) {
            return getAudioFileIntent(filePath);
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp")) {
            return getImageFileIntent(filePath);
        } else if (end.equals("apk")) {
            return getApkFileIntent(filePath);
        } else if (end.equals("ppt") || end.equals("pptx")) {
            return getPptFileIntent(filePath);
        } else if (end.equals("xls") || end.equals("xlsx")) {
            return getExcelFileIntent(filePath);
        } else if (end.equals("doc") || end.equals("docx")) {
            return getWordFileIntent(filePath);
        } else if (end.equals("pdf") || end.equals("pdfx") ) {
            return getPdfFileIntent(filePath);
        } else if (end.equals("chm")) {
            return getChmFileIntent(filePath);
        } else if (end.equals("txt")) {
            return getTextFileIntent(filePath, false);
        } else {
            return getAllIntent(filePath);
        }
    }

    // Android获取一个用于打开APK文件的intent
    public static Intent getAllIntent(String param) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "*/*");
        return intent;
    }

    // Android获取一个用于打开APK文件的intent
    public static Intent getApkFileIntent(String param) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }

    // Android获取一个用于打开VIDEO文件的intent
    public static Intent getVideoFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "video/*");
        return intent;
    }

    // Android获取一个用于打开AUDIO文件的intent
    public static Intent getAudioFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }

    // Android获取一个用于打开Html文件的intent
    public static Intent getHtmlFileIntent(String param) {
        Uri uri = Uri.parse(param).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    // Android获取一个用于打开图片文件的intent
    public static Intent getImageFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    // Android获取一个用于打开PPT文件的intent
    public static Intent getPptFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    // Android获取一个用于打开Excel文件的intent
    public static Intent getExcelFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    // Android获取一个用于打开Word文件的intent
    public static Intent getWordFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    // Android获取一个用于打开CHM文件的intent
    public static Intent getChmFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    // Android获取一个用于打开文本文件的intent
    public static Intent getTextFileIntent(String param, boolean paramBoolean) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (paramBoolean) {
            Uri uri1 = Uri.parse(param);
            intent.setDataAndType(uri1, "text/plain");
        } else {
            Uri uri2 = Uri.fromFile(new File(param));
            intent.setDataAndType(uri2, "text/plain");
        }
        return intent;
    }

    // Android获取一个用于打开PDF文件的intent
    public static Intent getPdfFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }


    public static void downFile(final String url, final String fileName, final OnDownloadListener listener) {
        // 下载函数
        final Handler handler = new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    URL myURL = new URL(url);
                    URLConnection conn = myURL.openConnection();
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    if (null != is) {

                        FileOutputStream fos = new FileOutputStream(CoreConstants.DOWNLOADPATH + fileName);
                        // 把数据存入路径+文件名
                        byte buf[] = new byte[1024];
                        do {
                            // 循环读取
                            int numread = is.read(buf);
                            if (numread == -1) {
                                break;
                            }
                            fos.write(buf, 0, numread);
                        } while (true);
                        is.close();
                        fos.flush();
                        fos.close();
                        if (listener != null) {
                            handler.post(new Runnable() {

                                @Override
                                public void run() {
                                    listener.OnSucess(url, fileName);
                                }
                            });
                        }
                    } else {
                        if (listener != null) {
                            handler.post(new Runnable() {

                                @Override
                                public void run() {
                                    listener.OnFailure(500, "获取数据失败");
                                }
                            });
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if (listener != null) {
                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                listener.OnFailure(500, "IO异常");
                            }
                        });
                    }
                }
            }
        }).start();
    }

    public interface OnDownloadListener {
        void OnSucess(String url, String fileName);

        void OnFailure(int errorCode, String errorMsg);
    }

}
