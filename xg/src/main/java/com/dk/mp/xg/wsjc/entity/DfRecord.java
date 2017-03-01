package com.dk.mp.xg.wsjc.entity;

/**
 * 卫生打分记录实体
 * 作者：janabo on 2017/1/12 14:53
 */
public class DfRecord {
    private String ssl;//宿舍楼
    private String ssq;//宿舍区
    private String fjh;//房间号
    private String fs;//分数
    private String id;//

    public String getSsl() {
        return ssl;
    }

    public void setSsl(String ssl) {
        this.ssl = ssl;
    }

    public String getSsq() {
        return ssq;
    }

    public void setSsq(String ssq) {
        this.ssq = ssq;
    }

    public String getFs() {
        return fs;
    }

    public void setFs(String fs) {
        this.fs = fs;
    }

    public String getFjh() {
        return fjh;
    }

    public void setFjh(String fjh) {
        this.fjh = fjh;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
