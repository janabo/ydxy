package com.dk.mp.xxxw.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 作者：janabo on 2017/3/9 14:54
 */
public class Xxxw extends RealmObject {
    @PrimaryKey
    private String id;
    private String content; //内容
    private String image;//图片
    private String name;//标题
    private String url;//详情页链接
    private String publishTime;
    private boolean load = false;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public boolean isLoad() {
        return load;
    }

    public void setLoad(boolean load) {
        this.load = load;
    }
}
