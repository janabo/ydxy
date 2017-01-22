package com.dk.mp.xg.wsjc.entity;

import com.dk.mp.core.util.StringUtils;

/**
 * 打分信息
 * 作者：janabo on 2017/1/9 16:16
 */
public class Dfxx{
    private String id;
    private String mc;//名称
    private String fs;//最高分

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

    public String getFs() {
        if(StringUtils.isNotEmpty(fs))
            return fs;
        else
            return "0";
    }

    public void setFs(String fs) {
        this.fs = fs;
    }
}
