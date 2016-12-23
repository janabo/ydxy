package com.dk.mp.txl.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 部门或人员实体
 * 作者：janabo on 2016/12/22 15:54
 */
public class Jbxx extends RealmObject {
    @PrimaryKey
    private String id;//id
    private String name;//部门,姓名


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
