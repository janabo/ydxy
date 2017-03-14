package com.dk.mp.lsgl.entity;

/**
 * Created by dongqs on 2017/2/4.
 */

public class PersonEntity {
    private String room;//房间号
    private String className;//班级
    private String name;//姓名
    private String id;//主键
    private String Zymc;// 专业名称
    private String msg;//反馈信息或者门禁记录
    private boolean canFk;//代表该用户有填写反馈信息的权限且必填
    private boolean canBz;// 代表有该用户有填写备注的权限且必填

    private String zy;

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getZymc() {
        return Zymc;
    }

    public void setZymc(String zymc) {
        Zymc = zymc;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isCanFk() {
        return canFk;
    }

    public void setCanFk(boolean canFk) {
        this.canFk = canFk;
    }

    public boolean isCanBz() {
        return canBz;
    }

    public void setCanBz(boolean canBz) {
        this.canBz = canBz;
    }

    public String getZy() {
        return zy;
    }

    public void setZy(String zy) {
        this.zy = zy;
    }
}
