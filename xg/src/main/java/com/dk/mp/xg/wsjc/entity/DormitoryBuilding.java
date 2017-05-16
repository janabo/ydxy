package com.dk.mp.xg.wsjc.entity;

import java.io.Serializable;

/**
 * 宿舍楼
 * 作者：janabo on 2017/5/12 10:12
 */
public class DormitoryBuilding implements Serializable{
    private String id;
    private String name;

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
}
