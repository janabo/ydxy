package com.dk.mp.oldoa.entity;

import java.io.Serializable;

/**
 * 周报事件
 * @since 
 * @version 2014-12-10
 * @author lj.zhang
 */
public class WeekEventListEntity implements Serializable {
	private String address;//地址
	private String content;//内容
	private String time;//时间
	private String date;//日期
	private String cjry;//参加人员

	public String getCjry() {
		return cjry;
	}

	public void setCjry(String cjry) {
		this.cjry = cjry;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getReponsibleUnit() {
		return reponsibleUnit;
	}

	public void setReponsibleUnit(String reponsibleUnit) {
		this.reponsibleUnit = reponsibleUnit;
	}

	public String getAttendLeader() {
		return attendLeader;
	}

	public void setAttendLeader(String attendLeader) {
		this.attendLeader = attendLeader;
	}

	private String reponsibleUnit;//责任单位
	private String attendLeader;//出席人员

}
