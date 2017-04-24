package com.dk.mp.xg.wsjc.entity;

import java.io.Serializable;

/**
 * 作者：janabo on 2017/1/16 10:53
 */
public class Common implements Serializable{
    private String id;//id
    private String name;//名称

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
