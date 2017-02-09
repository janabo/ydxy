package com.dk.mp.xg.wsjc.entity;

import java.io.Serializable;

/**
 * 住宿生登记管理
 * 作者：janabo on 2017/2/8 09:29
 */
public class Zssdjgl implements Serializable{
    private String id;
    private String name;//学生姓名
    private String className;//班级名称
    private String time;//日期
    private String msg;//
    private String room;//房间号

    public Zssdjgl(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
