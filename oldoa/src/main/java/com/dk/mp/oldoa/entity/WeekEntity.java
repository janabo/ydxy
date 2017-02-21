package com.dk.mp.oldoa.entity;

import java.io.Serializable;

/**
 * 周报实体
 * @since 
 * @version 2014-12-9
 * @author lj.zhang
 */
public class WeekEntity implements Serializable {
	private String date;//日期
	private String content;//内容
	private String time;//时间
	private String place;//地点
	private String unit;//责任单位
	private String leader;//出席领导
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getLeader() {
		return leader;
	}
	public void setLeader(String leader) {
		this.leader = leader;
	}

}
