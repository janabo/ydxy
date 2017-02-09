package com.dk.mp.newoa.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者：janabo on 2017/1/4 14:57
 */
public class OATab implements Parcelable {
    private String title;//待办 已办
    private String process;//db ,yb
    private String type;//OA_GZAP
    private String typename;//工作周报

    public OATab(String title, String process, String type, String typename) {
        this.title = title;
        this.process = process;
        this.type = type;
        this.typename = typename;
    }

    protected OATab(Parcel in) {
        title = in.readString();
        process = in.readString();
        type = in.readString();
        typename = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(process);
        dest.writeString(type);
        dest.writeString(typename);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OATab> CREATOR = new Creator<OATab>() {
        @Override
        public OATab createFromParcel(Parcel in) {
            return new OATab(in);
        }

        @Override
        public OATab[] newArray(int size) {
            return new OATab[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }
}
