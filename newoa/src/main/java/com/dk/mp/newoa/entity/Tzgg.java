package com.dk.mp.newoa.entity;

/**
 * 通知公告实体
 * 作者：janabo on 2017/1/3 17:21
 */
public class Tzgg {
    private String time; //时间
    private String url;//链接
    private String title;//标题
    private String subTitle;//副标题
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getSubTitle() {
        return subTitle;
    }
    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
}
