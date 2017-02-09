package com.dk.mp.newoa.entity;

import java.util.List;

/**
 * 审批意见
 * @author dake
 *
 */
public class NewOpinion {
	private String title;//分类标题
	private List<String> list;//意见列表
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<String> getList() {
		return list;
	}
	public void setList(List<String> list) {
		this.list = list;
	}
	
	
}
