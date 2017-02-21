package com.dk.mp.oldoa.entity;

import java.util.List;

public class Person {
	private String id;//相关联系人的ID
	private String name;//相关意见人姓名
	private String content;//相关意见人的意见内容
	private String time;//相关意见人发表时间
	private boolean check;//选中状态
	private int csIndex;//可以被选中的颜色
	private int parentPosition;
	private String parentName;

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	private boolean isMoreSelect;//单选还是多选

	public boolean isMoreSelect() {
		return isMoreSelect;
	}

	public void setMoreSelect(boolean isMoreSelect) {
		this.isMoreSelect = isMoreSelect;
	}

	public int getParentPosition() {
		return parentPosition;
	}

	public void setParentPosition(int parentPosition) {
		this.parentPosition = parentPosition;
	}

	private List<Person> list;

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

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public int getCsIndex() {
		return csIndex;
	}

	public void setCsIndex(int csIndex) {
		this.csIndex = csIndex;
	}

	public List<Person> getList() {
		return list;
	}

	public void setList(List<Person> list) {
		this.list = list;
	}

}
