package com.dk.mp.oldoa.entity;

public class FaWenDoc extends Doc {

	private String fwFileType;// 文件类型
	private String fwJGwz;// 机关代字
	private String fwNum;// 发文编号
	private String zhengwen;// 正文
	private String jinjCD;// 紧急程度
	private String jmcd;//机密程度
	//	private String dqbz;//当前步骤
	//	private String yclry;//已处理人员
	//	private String dclry;//待处理人员

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

	public String getJinjCD() {
		return jinjCD;
	}

	public String getJmcd() {
		return jmcd;
	}

	public void setJmcd(String jmcd) {
		this.jmcd = jmcd;
	}

	public void setJinjCD(String jinjCD) {
		this.jinjCD = jinjCD;
	}

	public String getFwFileType() {
		return fwFileType;
	}

	public void setFwFileType(String fwFileType) {
		this.fwFileType = fwFileType;
	}

	public String getFwJGwz() {
		return fwJGwz;
	}

	public void setFwJGwz(String fwJGwz) {
		this.fwJGwz = fwJGwz;
	}

	public String getFwNum() {
		return fwNum;
	}

	public void setFwNum(String fwNum) {
		this.fwNum = fwNum;
	}

	public String getZhengwen() {
		return zhengwen;
	}

	public void setZhengwen(String zhengwen) {
		this.zhengwen = zhengwen;
	}

}
