package com.dk.mp.core.util;

import android.content.Context;
import android.content.Intent;

import com.dk.mp.core.entity.App;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class AppUtil {
	private Context context;

	public AppUtil(Context context) {
		this.context = context;
	}

	/**
	 * 打开activity.
	 */
	public void startActivity(App app) {
		if (!app.getName().equals("显示应用")) {
			CoreSharedPreferencesHelper helper = new CoreSharedPreferencesHelper(context);
			Gson gson = new Gson();
			List<App> apps = gson.fromJson(helper.getValue("preferenceItem"),new TypeToken<ArrayList<App>>() {}.getType());
			if (apps == null) apps = new ArrayList<App>();
			if (apps.size() != 0) {
				for (App p : apps) {
					if (p.getId().equals(app.getId())){
						apps.remove(p);
						break;
					}
				}
				apps.add(0,app);
				int length = apps.size() > 6 ? 6 : apps.size();
				helper.setValue("preferenceItem",gson.toJson(apps.subList(0,length)));
			} else {
				apps.add(0,app);
				int length = apps.size() > 6 ? 6 : apps.size();
				helper.setValue("preferenceItem",gson.toJson(apps.subList(0,length)));
			}
		}

		Intent intent = new Intent();
		intent.putExtra("title", app.getName());

		intent.putExtra("x",app.getX());
		intent.putExtra("y",app.getY());

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
