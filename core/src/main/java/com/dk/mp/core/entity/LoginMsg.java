package com.dk.mp.core.entity;

public class LoginMsg {
  private String uid;
  private String psw;
	private String encpsw;
public String getUid() {
	return uid;
}
public void setUid(String uid) {
	this.uid = uid;
}
public String getPsw() {
	return psw;
}
public void setPsw(String psw) {
	this.psw = psw;
}

	public String getEncpsw() {
		return encpsw;
	}

	public void setEncpsw(String encpsw) {
		this.encpsw = encpsw;
	}
}
