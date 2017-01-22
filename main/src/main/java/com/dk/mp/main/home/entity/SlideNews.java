package com.dk.mp.main.home.entity;

/**
 * 幻灯新闻
 * @author dake
 *
 */
public class SlideNews {
	private String id;
	private String content;//内容
	private String name;//标题
	private String image;//图片地址
	private String url;

	public SlideNews(String id, String content, String name, String image, String url){
		this.id = id;
		this.content = content;
		this.name = name;
		this.image = image;
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
