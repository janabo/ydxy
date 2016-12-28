package com.dk.mp.core.util;

import android.content.Context;
import android.content.Intent;

import com.dk.mp.core.entity.App;

public class AppUtil {
	private Context context;

	public AppUtil(Context context) {
		this.context = context;
	}

	/**
	 * 打开activity.
	 */
	public void startActivity(App app) {
		Intent intent = new Intent();
		intent.putExtra("title", app.getName());
		intent.setAction(app.getAction());
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		context.startActivity(intent);
	}

	/**
	 * 判断应用类型并打开.
	 * @param app   App动类
	 */
	public void checkApp(App app) {
		try {
			if (StringUtils.isNotEmpty(app.getPackageName())) {//第三方应用
//				DeviceUtil.openApk(context, app.getPackageName(), new HashMap<String, String>());
			} else {
				if (StringUtils.isNotEmpty(app.getUrl())) {//网页应用
//					startUrlActivity(app.getUrl(), app.getName());
				} else {
					startActivity(app);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 打开网页.
	 * @param url 网页url
	 * @param title 应用标题
	 */
//	public void startUrlActivity(String url, String title) {
//		Intent intent = new Intent(context, HttpWebActivity.class);
//		intent.putExtra("title", title);
//		intent.putExtra("url", url);
//		context.startActivity(intent);
//	}

}
