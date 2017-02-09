package com.dk.mp.newoa.entity;

import java.io.Serializable;

/**
 * 基本信息
 * @author dake
 *
 */
public class Jbxx implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String key;
	private String value;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
