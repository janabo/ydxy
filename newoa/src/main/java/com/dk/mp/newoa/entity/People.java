package com.dk.mp.newoa.entity;


/**
 * 审批意见
 * @author dake
 *
 */
public class People {
	private String title;//分类标题
	private String id;//操作节点id
	private String name;//节点名称

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	

}
