package com.dk.mp.main.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.dk.mp.core.entity.User;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.StringUtils;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import cn.jpush.android.api.JPushInterface;
//import cn.jpush.android.api.TagAliasCallback;

/**
 * 推送工具类.
 * @version 2013-3-22
 * @author wangw
 */
public class PushUtil {
	private Context context;
	
	public PushUtil(Context context){
		this.context=context;
	}

	/**
	 * 开关
	 * @param type 1:开 0：关
	 */
	public static void setStatus(Context context,int type){
//		if(type == 1){
//			JPushInterface.stopPush(context.getApplicationContext());
//		}else{
//			JPushInterface.resumePush(context.getApplicationContext());
//		}
	}
	
	public void setTag(){
		User user=new CoreSharedPreferencesHelper(context).getUser();
		if(user==null){
			return;
		}
		StringBuffer tag = new StringBuffer(user.getUserId());
		if (StringUtils.isNotEmpty(user.getClassId())) {
			tag.append("," + user.getClassId());
		}

		if (StringUtils.isNotEmpty(user.getDepartId())) {
			tag.append("," + user.getDepartId());
		}

		if (StringUtils.isNotEmpty(user.getSpecialtyId())) {
			tag.append("," + user.getSpecialtyId());
		}
		
        // 检查 tag 的有效性
		if (TextUtils.isEmpty(tag)) {
			return;
		}
		// ","隔开的多个 转换成 Set
		String[] sArray = tag.toString().split(",");
		Set<String> tagSet = new LinkedHashSet<String>();
		for (String sTagItme : sArray) {
			if (!PushUtil.isValidTagAndAlias(sTagItme)) {
				return;
			}
			tagSet.add(sTagItme);
		}
		
		//调用JPush API设置Tag
		mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tagSet));

	} 
	
    
	private static final int MSG_SET_TAGS = 1002;
	
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
            case MSG_SET_TAGS:
//                JPushInterface.setAliasAndTags(context, null, (Set<String>) msg.obj, mTagsCallback);
                break;
                
            default:
            }
        }
    };
	
//    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {
//
//        @Override
//        public void gotResult(int code, String alias, Set<String> tags) {
//            String logs="gotResult";
//            switch (code) {
//            case 0:
//                logs = "Set tag and alias success";
//                Logger.info("#######################"+logs);
//                break;
//
//            case 6002:
//                logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
//                Logger.info("#######################"+logs);
//                if (PushUtil.isConnected(context)) {
//                	mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
//                } else {
//                }
//                break;
//
//            default:
//                logs = "Failed with errorCode = " + code;
//                Logger.info("#######################"+logs);
//            }
//        }
//
//    };
	
	public static final String PREFS_NAME = "JPUSH_EXAMPLE";
	public static final String PREFS_DAYS = "JPUSH_EXAMPLE_DAYS";
	public static final String PREFS_START_TIME = "PREFS_START_TIME";
	public static final String PREFS_END_TIME = "PREFS_END_TIME";
	public static final String KEY_APP_KEY = "JPUSH_APPKEY";
	public static final String PREFS_OPEN = "PREFS_OPEN";

	public static boolean isEmpty(String s) {
		if (null == s)
			return true;
		if (s.length() == 0)
			return true;
		if (s.trim().length() == 0)
			return true;
		return false;
	}

	// 校验Tag Alias 只能是数字,英文字母和中文
	public static boolean isValidTagAndAlias(String s) {
		Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]{0,}$");
		Matcher m = p.matcher(s);
		return m.matches();
	}

	// 取得AppKey
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

	// 取得版本号
	public static String GetVersion(Context context) {
		try {
			PackageInfo manager = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return manager.versionName;
		} catch (NameNotFoundException e) {
			return "Unknown";
		}
	}

	public static void showToast(final String toast, final Context context) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
				Looper.loop();
			}
		}).start();
	}

	public static boolean isConnected(Context context) {
		ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = conn.getActiveNetworkInfo();
		return (info != null && info.isConnected());
	}


}
