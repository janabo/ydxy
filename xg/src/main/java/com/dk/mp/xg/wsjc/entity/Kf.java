package com.dk.mp.xg.wsjc.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 扣分原因 和宿舍楼层 更用实体
 * 作者：janabo on 2017/1/11 10:10
 */
public class Kf implements Serializable{
    private String id;
    private String name;//名称
//    private List<Kf> ssls;//宿舍楼
    private List<String> lc;//楼层

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

//    public List<Kf> getSsls() {
//        return ssls;
//    }
//
//    public void setSsls(List<Kf> ssls) {
//        this.ssls = ssls;
//    }

    public List<String> getLc() {
        return lc;
    }

    public void setLc(List<String> lc) {
        this.lc = lc;
    }

    public Kf(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
