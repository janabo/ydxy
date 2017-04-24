package com.dk.mp.xg.wsjc.entity;

import java.io.Serializable;

/**
 * 学生住宿信息
 * 作者：janabo on 2017/1/23 14:33
 */
public class Student implements Serializable{
    private String ssl;//宿舍楼
    private String lc;//楼层
    private String xm;//姓名
    private String bj;//班级
    private String nj;//年级
    private String fjh;//房间号
    private String ssq;//宿舍区
    private String xq;//校区
    private String zsf;//住宿费
    private String cws;//床位数
    private String xh;//学号
    private String yx;//院系
    private String xslb;//学生类别
    private String cwh;//床位号
    private String fx;//房型
    private String xb;//性别
    private String xsid;//学生id

    public String getKfyy() {
        return kfyy;
    }

    public void setKfyy(String kfyy) {
        this.kfyy = kfyy;
    }

    private String kfyy; //扣分原因

    public String getSsl() {
        return ssl;
    }

    public void setSsl(String ssl) {
        this.ssl = ssl;
    }

    public String getLc() {
        return lc;
    }

    public void setLc(String lc) {
        this.lc = lc;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getBj() {
        return bj;
    }

    public void setBj(String bj) {
        this.bj = bj;
    }

    public String getNj() {
        return nj;
    }

    public void setNj(String nj) {
        this.nj = nj;
    }

    public String getFjh() {
        return fjh;
    }

    public void setFjh(String fjh) {
        this.fjh = fjh;
    }

    public String getSsq() {
        return ssq;
    }

    public void setSsq(String ssq) {
        this.ssq = ssq;
    }

    public String getXq() {
        return xq;
    }

    public void setXq(String xq) {
        this.xq = xq;
    }

    public String getZsf() {
        return zsf;
    }

    public void setZsf(String zsf) {
        this.zsf = zsf;
    }

    public String getCws() {
        return cws;
    }

    public void setCws(String cws) {
        this.cws = cws;
    }

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    public String getYx() {
        return yx;
    }

    public void setYx(String yx) {
        this.yx = yx;
    }

    public String getXslb() {
        return xslb;
    }

    public void setXslb(String xslb) {
        this.xslb = xslb;
    }

    public String getCwh() {
        return cwh;
    }

    public void setCwh(String cwh) {
        this.cwh = cwh;
    }

    public String getFx() {
        return fx;
    }

    public void setFx(String fx) {
        this.fx = fx;
    }

    public String getXb() {
        return xb;
    }

    public void setXb(String xb) {
        this.xb = xb;
    }

    public String getXsid() {
        return xsid;
    }

    public void setXsid(String xsid) {
        this.xsid = xsid;
    }
}
