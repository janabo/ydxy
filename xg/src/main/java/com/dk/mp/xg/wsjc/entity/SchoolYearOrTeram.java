package com.dk.mp.xg.wsjc.entity;

import java.io.Serializable;

/**
 * 学年或学期实体
 * 作者：janabo on 2017/5/8 15:31
 */
public class SchoolYearOrTeram implements Serializable{

    private String id;
    private String mc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    public SchoolYearOrTeram(String id, String mc) {
        this.id = id;
        this.mc = mc;
    }

    public SchoolYearOrTeram() {
        super();
    }
}
