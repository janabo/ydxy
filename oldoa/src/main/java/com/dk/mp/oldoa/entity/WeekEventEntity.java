package com.dk.mp.oldoa.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 周报外面包装实体
 * @since 
 * @version 2014-12-10
 * @author lj.zhang
 */
public class WeekEventEntity implements Serializable {

	private String dateTime;//日期
	private List<WeekEventListEntity> weekL = new ArrayList<WeekEventListEntity>();

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public List<WeekEventListEntity> getWeekL() {
		return weekL;
	}

	public void setWeekL(List<WeekEventListEntity> weekL) {
		this.weekL = weekL;
	}

}
