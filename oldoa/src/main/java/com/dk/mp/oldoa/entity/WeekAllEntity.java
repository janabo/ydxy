package com.dk.mp.oldoa.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 周报最外一层集合
 * @since 
 * @version 2014-12-10
 * @author lj.zhang
 */
public class WeekAllEntity implements Serializable {

	private String title;
	private String remark;
	private List<WeekEventEntity> list = new ArrayList<WeekEventEntity>();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<WeekEventEntity> getList() {
		return list;
	}

	public void setList(List<WeekEventEntity> list) {
		this.list = list;
	}

}
