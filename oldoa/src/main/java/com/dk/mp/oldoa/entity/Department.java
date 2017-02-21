package com.dk.mp.oldoa.entity;

import java.util.List;

public class Department {


	private String id;
	private String name;
	private String validate;//是否多选
	private List<Person> list;
	private int parentPosition;
	private String parentId;

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public int getParentPosition() {
		return parentPosition;
	}

	public void setParentPosition(int parentPosition) {
		this.parentPosition = parentPosition;
	}

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

	public String getValidate() {
		return validate;
	}

	public void setValidate(String validate) {
		this.validate = validate;
	}

	public List<Person> getList() {
		return list;
	}

	public void setList(List<Person> list) {
		this.list = list;
	}

}
