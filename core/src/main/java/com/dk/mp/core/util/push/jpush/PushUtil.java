package com.dk.mp.core.util.push.jpush;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

import com.dk.mp.core.util.Logger;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 推送工具类.
 * @version 2013-3-22
 * @author wangw
 */
public class PushUtil {
	public static final String PREFS_NAME = "JPUSH_EXAMPLE";
	public static final String PREFS_DAYS = "JPUSH_EXAMPLE_DAYS";
	public static final String PREFS_START_TIME = "PREFS_START_TIME";
	public static final String PREFS_OPEN = "PREFS_START_OPEN";
	public static final String PREFS_END_TIME = "PREFS_END_TIME";
	public static final String KEY_APP_KEY = "JPUSH_APPKEY";

	/**
	 * 校验Tag Alias 只能是数字,英文字母和中文.
	 * @param s Tag Alias
	 * @return 符合：true，不符合：false
	 */
	public static boolean isValidTagAndAlias(String s) {
		Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]{0,}$");
		Matcher m = p.matcher(s);
		return m.matches();
	}

	/**
	 * 取得AppKey.
	 * @param context Context
	 * @return AppKey
	 */
	public static String getAppKey(Context context) {
		Bundle metaData = null;
		String appKey = null;
		try {
			ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),
					PackageManager.GET_META_DATA);
			if (null != ai)
				metaData = ai.metaData;
			if (null != metaData) {
				appKey = metaData.getString(KEY_APP_KEY);
				if ((null == appKey) || appKey.length() != 24) {
					appKey = null;
				}
			}
		} catch (NameNotFoundException e) {

		}
		return appKey;
	}

	/**
	 * 取得版本号.
	 * @param context Context
	 * @return 版本号
	 */
	public static String getVersion(Context context) {
		try {
			PackageInfo manager = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return manager.versionName;
		} catch (NameNotFoundException e) {
			return "Unknown";
		}
	}

	/**
	 * 设置Tag.
	 * @param context Context
	 * @param tag tag
	 */
	public static void setTag(Context context, String tag) {
		Set<String> tagSet = new LinkedHashSet<String>();
		tagSet.add(tag);
		//调用JPush API设置Tag
		Logger.info("setTag"+tag);
//		JPushInterface.setAliasAndTags(context, "", tagSet);
	}

}
