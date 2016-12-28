package com.dk.mp.core.entity;

import java.util.List;

/**
 * 作者：janabo on 2016/12/24 11:09
 */
public class GsonData<T> {
    private List<T> data;
    private int status;
    private String msg;
    private int code;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
