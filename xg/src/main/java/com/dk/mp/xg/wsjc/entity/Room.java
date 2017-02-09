package com.dk.mp.xg.wsjc.entity;

import java.util.List;

/**
 * 房间信息
 * 作者：janabo on 2017/2/8 14:11
 */
public class Room {
    private String id;
    private String name;//房间号
    private String className;//班级名
    private List<Zssdjgl> xsxxs;//学生人员

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

    public List<Zssdjgl> getXsxxs() {
        return xsxxs;
    }

    public void setXsxxs(List<Zssdjgl> xsxxs) {
        this.xsxxs = xsxxs;
    }
}
