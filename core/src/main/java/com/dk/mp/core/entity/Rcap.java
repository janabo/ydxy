package com.dk.mp.core.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 作者：janabo on 2016/12/27 16:31
 */
public class Rcap extends RealmObject {
    @PrimaryKey
    private String id;//事件id
    private String title;//标题
    private String content;//内容
    private String time_start;//开始时间
    private String time_end;//结束时间
    private String location;//地点
    private String stime;//开始时间 08:30
    private String uid;//用户id

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
