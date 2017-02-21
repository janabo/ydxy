package com.dk.mp.oldoa.entity;

public class ShouWenDoc extends Doc {
	private String laiwUnit;// 来文单位

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
	//
	//	public String getDqbz() {
	//		return dqbz;
	//	}
	//
	//	public void setDqbz(String dqbz) {
	//		this.dqbz = dqbz;
	//	}

	private String laiwZH;// 来文字号
	private String shouwWH;// 收文文号
	private String shouwTime;// 收文时间
	private String jinjCD;// 紧急程度
//	private String yclry;//已处理人员
//	private String dclry;//待处理人员
//	private String dqbz;//当前步骤

	public String getLaiwUnit() {
		return laiwUnit;
	}

	public void setLaiwUnit(String laiwUnit) {
		this.laiwUnit = laiwUnit;
	}

	public String getLaiwZH() {
		return laiwZH;
	}

	public void setLaiwZH(String laiwZH) {
		this.laiwZH = laiwZH;
	}

	public String getShouwWH() {
		return shouwWH;
	}

	public void setShouwWH(String shouwWH) {
		this.shouwWH = shouwWH;
	}

	public String getShouwTime() {
		return shouwTime;
	}

	public void setShouwTime(String shouwTime) {
		this.shouwTime = shouwTime;
	}

	public String getJinjCD() {
		return jinjCD;
	}

	public void setJinjCD(String jinjCD) {
		this.jinjCD = jinjCD;
	}
}
