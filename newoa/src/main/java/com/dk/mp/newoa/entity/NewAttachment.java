package com.dk.mp.newoa.entity;

public class NewAttachment {
	
	private String title;
	private String url;
	private String id;
	private String size;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	private boolean downloaded;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isDownloaded() {
		return downloaded;
	}
	public void setDownloaded(boolean downloaded) {
		this.downloaded = downloaded;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	
	

}
