package com.dk.mp.oldoa.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者：janabo on 2017/4/1 15:06
 */
public class OATab implements Parcelable {
    private String title;//待办 已办
    private String tag;//db ,yb
    private String state;//OA_GZAP
    private String url;//工作周报

    public OATab(String title, String tag, String state, String url) {
        this.title = title;
        this.tag = tag;
        this.state = state;
        this.url = url;
    }

    protected OATab(Parcel in) {
        title = in.readString();
        tag = in.readString();
        state = in.readString();
        url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(tag);
        dest.writeString(state);
        dest.writeString(url);
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
