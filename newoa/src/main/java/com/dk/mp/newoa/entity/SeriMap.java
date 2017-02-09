package com.dk.mp.newoa.entity;

import java.io.Serializable;
import java.util.Map;

/**
 * 序列化map
 * @author dake
 *
 */
public class SeriMap implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8433615557712922564L;
	private Map map;

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}
	
}
