package com.dk.mp.main.setting.ui.entity;

/**
 * 作者：janabo on 2017/2/20 10:15
 */
public class Version {
    private String url;//更新链接
    private String desc;//更新信息
    private String versionId;//版本号
    private String versionName;

    public Version(String url, String desc, String versionId, String versionName) {
        this.url = url;
        this.desc = desc;
        this.versionId = versionId;
        this.versionName = versionName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
}
