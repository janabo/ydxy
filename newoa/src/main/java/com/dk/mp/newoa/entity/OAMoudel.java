package com.dk.mp.newoa.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者：janabo on 2017/1/4 14:20
 */
public class OAMoudel implements Parcelable {
    private int icon;
    private String name;
    private String type;
    private String tabs;
    private String process;

    public OAMoudel(){}

    public OAMoudel(int icon,String name,String type,String tabs){
        this.icon = icon;
        this.name = name;
        this.type = type;
        this.tabs = tabs;
    }

    public OAMoudel(String name,String type,String process,String tabs){
        this.name = name;
        this.type = type;
        this.tabs = tabs;
        this.process = process;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTabs() {
        return tabs;
    }

    public void setTabs(String tabs) {
        this.tabs = tabs;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    protected OAMoudel(Parcel in) {
        icon = in.readInt();
        name = in.readString();
        type = in.readString();
        tabs = in.readString();
        process = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(icon);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(tabs);
        dest.writeString(process);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OAMoudel> CREATOR = new Creator<OAMoudel>() {
        @Override
        public OAMoudel createFromParcel(Parcel in) {
            return new OAMoudel(in);
        }

        @Override
        public OAMoudel[] newArray(int size) {
            return new OAMoudel[size];
        }
    };
}
