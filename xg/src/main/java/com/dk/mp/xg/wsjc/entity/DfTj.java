package com.dk.mp.xg.wsjc.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 打分统计
 * 作者：janabo on 2017/1/13 17:29
 */
public class DfTj implements Parcelable {
    private String id;
    private String type;//(week:按周，month：按月，year：学期)
    private String key;//周次/月份/学期
    private String role;//角色（1:班主任，2:辅导员，3:系部，4:学工/宿管）
    private String pfmb;//评分模板id
    private String name;//下拉选择的name（第几周，几月，第几学期）

    protected DfTj(Parcel in) {
        id = in.readString();
        type = in.readString();
        key = in.readString();
        role = in.readString();
        pfmb = in.readString();
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(type);
        dest.writeString(key);
        dest.writeString(role);
        dest.writeString(pfmb);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DfTj> CREATOR = new Creator<DfTj>() {
        @Override
        public DfTj createFromParcel(Parcel in) {
            return new DfTj(in);
        }

        @Override
        public DfTj[] newArray(int size) {
            return new DfTj[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPfmb() {
        return pfmb;
    }

    public void setPfmb(String pfmb) {
        this.pfmb = pfmb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
