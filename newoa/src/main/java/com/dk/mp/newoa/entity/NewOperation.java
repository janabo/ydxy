package com.dk.mp.newoa.entity;

/**
 * 操作列表 
 * @version 2016年1月15日
 * @author abc
 */
public class NewOperation {
	private String nodeId;//操作节点id
	private String nodeName;//节点名称
	private String choose;//0:无需选人 1：选一个人  2：选多个人

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getChoose() {
		return choose;
	}

	public void setChoose(String choose) {
		this.choose = choose;
	}



}
