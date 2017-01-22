package com.dk.mp.xg.wsjc.entity;

/**
 * 宿舍违章实体
 * 作者：janabo on 2017/1/16 17:09
 */
public class Sswz {
    private String id;
    private String name;//姓名
    private String className;//班级
    private String time;//时间
    private String msg;//违章信息
    private String room;//房间号

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
