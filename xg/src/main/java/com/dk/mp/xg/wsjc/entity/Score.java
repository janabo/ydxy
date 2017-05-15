package com.dk.mp.xg.wsjc.entity;

/**
 * 成绩实体
 * 作者：janabo on 2017/5/8 13:35
 */
public class Score {
    private String kcmc;
    private String cj;

    public Score() {
    }

    public Score(String kcmc, String cj) {
        this.kcmc = kcmc;
        this.cj = cj;
    }

    public String getKcmc() {
        return kcmc;
    }

    public void setKcmc(String kcmc) {
        this.kcmc = kcmc;
    }

    public String getCj() {
        return cj;
    }

    public void setCj(String cj) {
        this.cj = cj;
    }
}
