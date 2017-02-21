package com.dk.mp.oldoa.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 标记日历上事件标识实体
 * @since 
 * @version 2014-7-18
 * @author lj.zhang
 */
public class CalendarEntity implements Serializable {

	private int isFlag;//是否有提醒事件,0代表过期，1代表未开始，2代表未有事件
	private Date date;
	private boolean mark;//是否选中

	public boolean isMark() {
		return mark;
	}

	public void setMark(boolean mark) {
		this.mark = mark;
	}

	public int getIsFlag() {
		return isFlag;
	}

	public void setIsFlag(int isFlag) {
		this.isFlag = isFlag;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int isFlag() {
		return isFlag;
	}

	public void setFlag(int isFlag) {
		this.isFlag = isFlag;
	}

}
