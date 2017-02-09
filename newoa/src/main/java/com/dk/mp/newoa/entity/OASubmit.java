package com.dk.mp.newoa.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class OASubmit implements Parcelable {
	private String suggestion;//填写意见(true必填)
	private String sxSuggestion;//手写审批意见(true有此功能，false无此功能)
	private String ngSuggestion;//填写拟稿意见(true显示，false不显示)
	private String sxNgSuggestion;//手写拟稿意见(true显示，false不显示)
	private String users;//刘国钧 转办 选人 （true必填,false选填，hide隐藏）；
	
	public String getSuggestion() {
		return suggestion;
	}
	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}
	public String getSxSuggestion() {
		return sxSuggestion;
	}
	public void setSxSuggestion(String sxSuggestion) {
		this.sxSuggestion = sxSuggestion;
	}
	public String getNgSuggestion() {
		return ngSuggestion;
	}
	public void setNgSuggestion(String ngSuggestion) {
		this.ngSuggestion = ngSuggestion;
	}
	public String getSxNgSuggestion() {
		return sxNgSuggestion;
	}
	public void setSxNgSuggestion(String sxNgSuggestion) {
		this.sxNgSuggestion = sxNgSuggestion;
	}
	
	public String getUsers() {
		return users;
	}
	public void setUsers(String users) {
		this.users = users;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.suggestion);
		dest.writeString(this.sxSuggestion);
		dest.writeString(this.ngSuggestion);
		dest.writeString(this.sxNgSuggestion);
		dest.writeString(this.users);
	}
	
	
	public static final Creator<OASubmit> CREATOR = new Creator<OASubmit>() {
		@Override
		public OASubmit[] newArray(int size) {
			return new OASubmit[size];
		}

		@Override
		public OASubmit createFromParcel(Parcel source) {
			OASubmit p = new OASubmit();
			p.suggestion = source.readString();
			p.sxSuggestion = source.readString();
			p.ngSuggestion = source.readString();
			p.sxNgSuggestion = source.readString();
			p.users = source.readString();
			return p;
		}
	};
}
