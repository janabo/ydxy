package com.dk.mp.core.entity;

/**
 * 角色对象
 * 作者：janabo on 2017/2/15 16:43
 */
public class Role {
    private String id;//角色id
    private String name;//角色名
    private String type;//角色类型

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
