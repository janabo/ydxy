package com.dk.mp.core.db;

import android.content.Context;

import com.dk.mp.core.application.MyApplication;
import com.dk.mp.core.entity.App;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.google.gson.Gson;

import java.util.Iterator;
import java.util.List;

/**
 * 
 * 本地简单数据保存工具类.
 * @version 2013-3-22
 * @author dongqs
 */
public class AppManager {

	/**
	 * 获取需要展示的数据.
	 * 
	 * @return 值
	 */
	public static List<App> getMyAppList(Context context) {
		Gson gson = new Gson();
		CoreSharedPreferencesHelper helper = new CoreSharedPreferencesHelper(context);
		List<App> list = helper.getAllAppList();
		List<App> notinlist = helper.getNotinList();

		//循环删除已经不再权限范围内的图标
		Iterator<App> iter = list.iterator();
		while(iter.hasNext()){
			App app = iter.next();
			for (App noapp : notinlist){
				if(app.getId().equals(noapp.getId())){
					iter.remove();
				}
			}
		}

		if (notinlist.size() != 0){
			list.add(MyApplication.app);
		}

		return list;
	}

	/**
	 * 更新不显示的图标
	 * @param appid
	 * @param context
	 * @param add   true	----新增不显示的图标
	 *              false	----删除不显示的图标
     */
	public static void updateNotin(Context context, String appid, boolean add) {
		Gson gson = new Gson();
		CoreSharedPreferencesHelper helper = new CoreSharedPreferencesHelper(context);
		List<App> notinlist = helper.getNotinList();

		if (add){
			List<App> list = helper.getAllAppList();
			for (App app : list){
				if (app.getId().equals(appid)){
					notinlist.add(app);
					break;
				}
			}
		}else{
			Iterator<App> iter = notinlist.iterator();
			while(iter.hasNext()){
				App item = iter.next();
				if(appid.equals(item.getId())){
					iter.remove();
					break;
				}
			}
		}
		if(helper.getLoginMsg()!=null){
			helper.setValue(helper.getLoginMsg().getUid()+"notinlist",gson.toJson(notinlist));
		}else{
			helper.setValue("notinlist",gson.toJson(notinlist));
		}
	}

	/**
	 * 更新调整后的app图标顺序
	 */
	public static void saveAppIndex(Context context, List<App> apps){
		Gson gson = new Gson();
		CoreSharedPreferencesHelper helper = new CoreSharedPreferencesHelper(context);
		if(helper.getLoginMsg()!=null){
			helper.setValue(helper.getLoginMsg().getUid()+"appindex",gson.toJson(apps));
		}else{
			helper.setValue("appindex",gson.toJson(apps));
		}
	}
}
