package com.dk.mp.lgj;

import com.dk.mp.core.application.MyApplication;

import cn.jpush.android.api.JPushInterface;

/**
 * 作者：janabo on 2017/3/12 11:23
 */
public class MpApplication extends MyApplication{
    @Override
    public void onCreate() {
        super.onCreate();
        try{
            JPushInterface.init(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
