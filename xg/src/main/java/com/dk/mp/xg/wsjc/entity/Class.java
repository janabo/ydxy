package com.dk.mp.xg.wsjc.entity;

import java.util.List;

/**
 * 班级
 * 作者：janabo on 2017/2/8 14:24
 */
public class Class {
    private String className;//班级名
    private List<Room> rooms;//房间

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
}
