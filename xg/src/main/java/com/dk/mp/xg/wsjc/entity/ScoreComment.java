package com.dk.mp.xg.wsjc.entity;

import java.util.List;

/**
 * 作者：janabo on 2017/5/8 13:37
 */
public class ScoreComment {
    private List<Score> list;
    private String py;

    public List<Score> getList() {
        return list;
    }

    public void setList(List<Score> list) {
        this.list = list;
    }

    public String getPy() {
        return py;
    }

    public void setPy(String py) {
        this.py = py;
    }
}
