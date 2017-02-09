package com.dk.mp.newoa.entity;

import java.util.List;

/**
 * 审批意见
 * @author dake
 *
 */
public class NewDepartment {
	private String title;//分类标题
	private List<People> list;//意见列表
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<People> getList() {
		return list;
	}
	public void setList(List<People> list) {
		this.list = list;
	}
	
	
}
