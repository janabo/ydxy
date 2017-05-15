package com.dk.mp.xg.wsjc.entity;

import java.io.Serializable;

/**
 * Created by cobb on 2017/5/9.
 */

public class InformationQuery implements Serializable{

    /**
     * bjid : 班级id
     * bjmc : 班级名称
     * xm : 姓名
     * lxdh : 联系电话
     * ssq : 宿舍区
     * ssl : 宿舍楼
     * fjh : 房间号
     * bzr : 班主任
     * bzrlxdh : 班主任联系电话
     */

    private String bjid;
    private String bjmc;
    private String xm;
    private String lxdh;
    private String ssq;
    private String ssl;
    private String fjh;
    private String bzr;
    private String bzrlxdh;

    public InformationQuery(String bjid, String bjmc, String xm, String lxdh, String ssq, String ssl, String fjh, String bzr, String bzrlxdh) {
        this.bjid = bjid;
        this.bjmc = bjmc;
        this.xm = xm;
        this.lxdh = lxdh;
        this.ssq = ssq;
        this.ssl = ssl;
        this.fjh = fjh;
        this.bzr = bzr;
        this.bzrlxdh = bzrlxdh;
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

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getLxdh() {
        return lxdh;
    }

    public void setLxdh(String lxdh) {
        this.lxdh = lxdh;
    }

    public String getSsq() {
        return ssq;
    }

    public void setSsq(String ssq) {
        this.ssq = ssq;
    }

    public String getSsl() {
        return ssl;
    }

    public void setSsl(String ssl) {
        this.ssl = ssl;
    }

    public String getFjh() {
        return fjh;
    }

    public void setFjh(String fjh) {
        this.fjh = fjh;
    }

    public String getBzr() {
        return bzr;
    }

    public void setBzr(String bzr) {
        this.bzr = bzr;
    }

    public String getBzrlxdh() {
        return bzrlxdh;
    }

    public void setBzrlxdh(String bzrlxdh) {
        this.bzrlxdh = bzrlxdh;
    }
}
