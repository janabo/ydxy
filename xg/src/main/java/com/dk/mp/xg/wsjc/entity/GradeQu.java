package com.dk.mp.xg.wsjc.entity;

import java.io.Serializable;

/**
 * Created by cobb on 2017/5/9.
 */

public class GradeQu implements Serializable{

    private String bjid;
    private String bjmc;

    public GradeQu(String bjid, String bjmc) {
        this.bjid = bjid;
        this.bjmc = bjmc;
    }

    public String getBjid() {
        return bjid;
    }

    public void setBjid(String bjid) {
        this.bjid = bjid;
    }

    public String getBjmc() {
        return bjmc;
    }

    public void setBjmc(String bjmc) {
        this.bjmc = bjmc;
    }
}
