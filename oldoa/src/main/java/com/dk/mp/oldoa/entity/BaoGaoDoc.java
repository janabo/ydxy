package com.dk.mp.oldoa.entity;

import java.util.Map;

public class BaoGaoDoc extends Doc {

	private String bgNum;// 编号
	private String ngTime;//拟稿时间
	private String contant;// 内容
	private String jinjCD;// 紧急程度
	//	private String yclry;//已处理人员
	//	private String dclry;//待处理人员
	//	private String dqbz;//当前步骤

	private String zw;

	public String getZw() {
		return zw;
	}

	public void setZw(String zw) {
		this.zw = zw;
	}

	//	public String getYclry() {
	//		return yclry;
	//	}
	//
	//	public void setYclry(String yclry) {
	//		this.yclry = yclry;
	//	}
	//
	//	public String getDclry() {
	//		return dclry;
	//	}
	//
	//	public void setDclry(String dclry) {
	//		this.dclry = dclry;
	//	}

	//	public String getDqbz() {
	//		return dqbz;
	//	}
	//
	//	public void setDqbz(String dqbz) {
	//		this.dqbz = dqbz;
	//	}

	public String getJinjCD() {
		return jinjCD;
	}

	public void setJinjCD(String jinjCD) {
		this.jinjCD = jinjCD;
	}

	public String getBgNum() {
		return bgNum;
	}

	public void setBgNum(String bgNum) {
		this.bgNum = bgNum;
	}

	public String getNgTime() {
		return ngTime;
	}

	public void setNgTime(String ngTime) {
		this.ngTime = ngTime;
	}

	public String getContant() {
		return contant;
	}

	public void setContant(String contant) {
		this.contant = contant;
	}
}
