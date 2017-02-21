package com.dk.mp.oldoa.entity;

import java.io.Serializable;

public class Event implements Serializable {

	private String id;//事件id
	private String title;//标题
	private String content;//内容
	private String time_start;//开始时间
	private String time_end;//结束时间
	private String space;//提醒间隔
	private String location;//地点
	private int tempId;//新建时存放日程的新id

	public int getTempId() {
		return tempId;
	}

	public void setTempId(int tempId) {
		this.tempId = tempId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getMentionTime() {
		return mentionTime;
	}

	public void setMentionTime(String mentionTime) {
		this.mentionTime = mentionTime;
	}

	public String getAttendLeader() {
		return attendLeader;
	}

	public void setAttendLeader(String attendLeader) {
		this.attendLeader = attendLeader;
	}

	private String mentionTime;//提醒时间
	private String attendLeader;//出席人员

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime_start() {
		return time_start;
	}

	public void setTime_start(String time_start) {
		this.time_start = time_start;
	}

	public String getTime_end() {
		return time_end;
	}

	public void setTime_end(String time_end) {
		this.time_end = time_end;
	}

	public String getSpace() {
		return space;
	}

	public void setSpace(String space) {
		this.space = space;
	}

}
