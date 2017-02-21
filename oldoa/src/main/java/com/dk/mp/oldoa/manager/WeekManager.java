package com.dk.mp.oldoa.manager;

import android.content.Context;

import com.dk.mp.core.util.Logger;
import com.dk.mp.oldoa.entity.WeekAllEntity;
import com.dk.mp.oldoa.entity.WeekEventEntity;
import com.dk.mp.oldoa.entity.WeekEventListEntity;
import com.dk.mp.oldoa.utils.CalendarUtil;
import com.dk.mp.oldoa.utils.http.HttpClientUtil;
import com.lidroid.xutils.http.ResponseInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeekManager {
	public final static WeekManager scheduleManger = new WeekManager();

	// 限制住不能直接产生一个实例
	private WeekManager() {

	};

	/*
	 * 
	 * 采用这种单例防止多线程，导致业务逻辑混乱
	 * 
	 * @return
	 */

	public synchronized static WeekManager getIntence() {

		return scheduleManger;
	}
	
	/**
	 * 获取周报列表信息
	 * @param responseInfo
	 * @return
	 */
	public WeekAllEntity getWeekListInfos(ResponseInfo<String> responseInfo) {
		List<WeekEventEntity> listMiddle = new ArrayList<WeekEventEntity>();//中间那层集合
		WeekAllEntity we = null;
		WeekEventEntity middleEntity = null;
		try {
			JSONObject object = HttpClientUtil.getJSONObject(responseInfo);
			String date = "";
			if (object != null) {
				Logger.info("getinterfaces:" + object.toString());
				JSONObject data = object.getJSONObject("data");
				we = new WeekAllEntity();
				JSONArray arrary = data.getJSONArray("list");
				for (int i = 0; i < arrary.length(); i++) {
					JSONObject jsonObj = arrary.getJSONObject(i);
					date = jsonObj.getString("date");
					middleEntity = new WeekEventEntity();
					middleEntity.setDateTime(date);
					JSONArray eventJson = jsonObj.getJSONArray("list");
					List<WeekEventListEntity> sideLists = new ArrayList<WeekEventListEntity>();//里面那层集合;
					for (int j = 0; j < eventJson.length(); j++) {
						JSONObject json = eventJson.getJSONObject(j);
						WeekEventListEntity wee = new WeekEventListEntity();
						wee.setAddress(json.getString("address"));
						wee.setContent(json.getString("content"));
						wee.setTime(json.getString("time"));
						wee.setAttendLeader(json.getString("attendLeader"));
						wee.setReponsibleUnit(json.getString("responsibleDw"));
						wee.setCjry(json.getString("cjry"));
						wee.setDate(CalendarUtil.getDate(date));
						sideLists.add(wee);
					}
					middleEntity.setWeekL(sideLists);
					listMiddle.add(middleEntity);
				}
				we.setList(listMiddle);
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return we;
	}
	

	/**
	 * 获取周报列表信息
	 * 
	 * @param context
	 * @return
	 */
	public WeekAllEntity getWeekListInfos(Context context, String Time) {
		List<WeekEventEntity> listMiddle = new ArrayList<WeekEventEntity>();//中间那层集合
		WeekAllEntity we = null;
		WeekEventEntity middleEntity = null;
//		try {
//			JSONObject object = HttpClientUtil.getJsonByGet(context, "apps/oa/getWeek?date=" + Time);
//			String date = "";
//			if (object != null) {
//				Logger.info("getinterfaces:" + object.toString());
//				JSONObject data = object.getJSONObject("data");
//				we = new WeekAllEntity();
//				JSONArray arrary = data.getJSONArray("list");
//				for (int i = 0; i < arrary.length(); i++) {
//					JSONObject jsonObj = arrary.getJSONObject(i);
//					date = jsonObj.getString("date");
//					middleEntity = new WeekEventEntity();
//					middleEntity.setDateTime(date);
//					JSONArray eventJson = jsonObj.getJSONArray("list");
//					List<WeekEventListEntity> sideLists = new ArrayList<WeekEventListEntity>();//里面那层集合;
//					for (int j = 0; j < eventJson.length(); j++) {
//						JSONObject json = eventJson.getJSONObject(j);
//						WeekEventListEntity wee = new WeekEventListEntity();
//						wee.setAddress(json.getString("address"));
//						wee.setContent(json.getString("content"));
//						wee.setTime(json.getString("time"));
//						wee.setAttendLeader(json.getString("attendLeader"));
//						wee.setReponsibleUnit(json.getString("responsibleDw"));
//						wee.setCjry(json.getString("cjry"));
//						wee.setDate(CalendarUtil.getDate(date));
//						sideLists.add(wee);
//					}
//					middleEntity.setWeekL(sideLists);
//					listMiddle.add(middleEntity);
//				}
//				we.setList(listMiddle);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		}
		return we;

	}
}