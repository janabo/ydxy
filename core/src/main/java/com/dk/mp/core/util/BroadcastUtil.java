package com.dk.mp.core.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

public class BroadcastUtil {

	/**
	 * 发送广播.
	 * @param context Context
	 *  REFRESH_APP:更新应用页面<br>
	 *  REFRESH_APPTAB:更新应用页面页签<br>
	 *  REFRESH_MSGCOUNT:更新消息中心未读条数<br>
	 *  REFRESH_MSG:更新消息中心数据列表<br>
	 */
	public static void registerReceiver(Context context, BroadcastReceiver mRefreshBroadcastReceiver, String[] actions) {
		IntentFilter intentFilter2 = new IntentFilter();
		for (int i = 0; i < actions.length; i++) {
			intentFilter2.addAction(actions[i]);
		}
		LocalBroadcastManager.getInstance(context).registerReceiver(mRefreshBroadcastReceiver, intentFilter2);
	}

	/**
	 * 发送广播.
	 * @param context Context
	 * @param action action:<br>
	 *  REFRESH_APP:更新应用页面<br>
	 *  REFRESH_APPTAB:更新应用页面页签<br>
	 *  REFRESH_MSGCOUNT:更新消息中心未读条数<br>
	 *  REFRESH_MSG:更新消息中心数据列表<br>
	 */
	public static void startActivity(Context context, String action) {
		Intent intent = new Intent();
		intent.setAction(action);
		Logger.info("startActivity  action="+action);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		context.startActivity(intent);
	}

	/**
	 * 註銷广播.
	 * @param context Context
	 * @param receiver BroadcastReceiver:<br>
	 */
	public static void unregisterReceiver(Context context, BroadcastReceiver receiver) {
		try {
			LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送广播.
	 * @param context Context
	 *  REFRESH_APP:更新应用页面<br>
	 *  REFRESH_APPTAB:更新应用页面页签<br>
	 *  REFRESH_MSGCOUNT:更新消息中心未读条数<br>
	 *  REFRESH_MSG:更新消息中心数据列表<br>
	 */
	public static void registerReceiver(Context context, BroadcastReceiver mRefreshBroadcastReceiver, String actions) {
		IntentFilter intentFilter2 = new IntentFilter();
		intentFilter2.addAction(actions);
		LocalBroadcastManager.getInstance(context).registerReceiver(mRefreshBroadcastReceiver, intentFilter2);
	}

	/**
	 * 发送广播.
	 * @param context Context
	 * @param action action:<br>
	 *  REFRESH_APP:更新应用页面<br>
	 *  REFRESH_APPTAB:更新应用页面页签<br>
	 *  REFRESH_MSGCOUNT:更新消息中心未读条数<br>
	 *  REFRESH_MSG:更新消息中心数据列表<br>
	 */
	public static void sendBroadcast(Context context, String action) {
		Logger.info("发送广播====:" + action);
		Intent intent = new Intent();
		intent.setAction(action);
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}

	/**
	 * 发送广播.
	 * @param context Context
	 * @param action action:<br>
	 *  REFRESH_APP:更新应用页面<br>
	 *  REFRESH_APPTAB:更新应用页面页签<br>
	 *  REFRESH_MSGCOUNT:更新消息中心未读条数<br>
	 *  REFRESH_MSG:更新消息中心数据列表<br>
	 */
	public static void sendBroadcast(Context context, String action, Intent intent) {
		Logger.info("sendBroadcast:" + action);
		intent.setAction(action);
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}

	public static void sendBroadcast(Context context, Intent intent) {
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}

}
