package com.dk.mp.xg.wsjc.entity;

/**
 * 住宿生管理
 * 作者：janabo on 2017/1/19 09:59
 */
public class Zssgl {
    private String id;
    private String sqrq;//申请日期
    private String xm;//姓名
    private String lx;//类型
    private String shzt;//审核状态
    private String sfksh;//是否可审核
    private String xb;//性别
    private String rzzt;//入住状态

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSqrq() {
        return sqrq;
    }

    public void setSqrq(String sqrq) {
        this.sqrq = sqrq;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getLx() {
        return lx;
    }

    public void setLx(String lx) {
        this.lx = lx;
    }

    public String getShzt() {
        return shzt;
    }

    public void setShzt(String shzt) {
        this.shzt = shzt;
    }

    public String getSfksh() {
        return sfksh;
    }

    public void setSfksh(String sfksh) {
        this.sfksh = sfksh;
    }

    public String getXb() {
        return xb;
    }

    public void setXb(String xb) {
        this.xb = xb;
    }

    public String getRzzt() {
        return rzzt;
    }

    public void setRzzt(String rzzt) {
        this.rzzt = rzzt;
    }
}
