package com.dk.mp.core.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.dk.mp.core.R;
import com.dk.mp.core.application.MyApplication;
import com.dk.mp.core.entity.App;
import com.dk.mp.core.entity.LoginMsg;
import com.dk.mp.core.entity.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 本地简单的数据存储工具类
 * 作者：janabo on 2016/12/14 16:22
 */
public class CoreSharedPreferencesHelper {
    private static final String PREFS_NAME = "com.dk.mp.db";
    private Context context;
    private Gson gson = new Gson();

    /**
     * 构造方法.
     * @param context Context
     */
    public CoreSharedPreferencesHelper(Context context) {
        this.context = context;
    }

    /**
     * 保存数据.
     * @param key key
     * @param value 值
     */
    public void setValue(String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 获取数据.
     * @param key key
     * @return  值
     */
    public String getValue(String key) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        String hello = settings.getString(key, null);
        return hello;
    }

    public LoginMsg getLoginMsg() {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        String hello = settings.getString("loginMsg", null);
        LoginMsg l = null;
        if (hello != null) {
            l = new LoginMsg();
            String[] lo = hello.split(",");
            l.setUid(lo[0]);
            l.setPsw(lo[1]);
            l.setEncpsw(lo[2]);
        }
        return l;
    }

    /**
     * 設置登錄信息
     * @param user
     * @param psw
     */
    public void setLoginMsg(String user, String psw,String encpsw) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("loginMsg", user + "," + psw+ "," + encpsw);
        editor.commit();
    }

    /**
     * 填充个人信息到界面.
     */
    public void setUserInfo(String json) {
        setValue("user_info", json);
    }


    /**
     * 解析json获取个人信息.
     * @return User
     */
    public User getUser() {
        Gson gson = new Gson();
        String str = getValue("user_info");
        return gson.fromJson(str,User.class);

    }

    /**
     * 清除用戶信息
     */
    public void cleanUser() {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        try {
            JSONObject s = new JSONObject(settings.getString("user_info", ""));
            if (s != null) {
                JSONArray array = s.getJSONObject("jsonp").getJSONObject("data").getJSONArray("apps");
                String userid = s.getJSONObject("jsonp").getJSONObject("data").getJSONObject("userId").toString();
                editor.putString(userid+"oldapp", array.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        editor.putString("user_info", null);
        editor.putString("user", null);
        editor.putString("loginMsg", null);
        editor.putString("preferenceItem", null);
        editor.commit();
    }

    /**
     * 保存数据.
     *
     * @param key
     *            key
     * @param value
     *            值
     */
    public void setInt(String key, int value) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * 获取数据.
     *
     * @param key
     *            key
     * @return 值
     */
    public int getInt(String key) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        int hello = settings.getInt(key, -1);
        return hello;
    }

    /**
     * 保存数据.
     *
     * @param key
     *            key
     * @param value
     *            值
     */
    public void setBoolean(String key, boolean value) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 获取数据.
     *
     * @param key
     *            key
     * @return 值
     */
    public boolean getBoolean(String key) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        boolean hello = settings.getBoolean(key, true);
        return hello;
    }

    /**
     * 获取全部应用图标
     * @return
     */
    public List<App> getAllAppList(){
        String str = getValue("user_info");
        String appindex;
        List<App> list = new ArrayList<>();
        if (str != null) {//根据登录的信息获取用户的全部图标信息
            User user = gson.fromJson(str,User.class);
            list = user.getApps();
            appindex = getValue(user.getUserId()+"appindex");
        }else{
            String deful = MyApplication.getContext().getString(R.string.defulicon);
            list = gson.fromJson(deful, new TypeToken<ArrayList<App>>() {}.getType());
            appindex = getValue("appindex");
        }

        if (appindex == null || appindex.equals("")){
            return list;
        }

        List<App> appindexlist = gson.fromJson(appindex,new TypeToken<ArrayList<App>>() {}.getType());
        List<App> returnlist = new ArrayList<App>();
        for (App app : appindexlist) {
            for (App reapp : list) {
                if (app.getId().equals(reapp.getId())){
                    returnlist.add(reapp);
                    list.remove(reapp);
                    break;
                }
            }
        }

        returnlist.addAll(list);

        return returnlist;
    }

    /**
     * 获取全部影藏的应用图标
     * @return
     */
    public List<App> getNotinList(){
        String str = getValue("user_info");
        String notinstr = null;
        if(getLoginMsg()!=null){
            notinstr = getValue(getLoginMsg().getUid()+"notinlist");
        }else{
            notinstr = getValue("notinlist");
        }
        if(notinstr != null){//根据本地存储的信息获取用户影藏的图标信息
            return gson.fromJson(notinstr, new TypeToken<ArrayList<App>>() {}.getType());
        }else{
            return new ArrayList<App>();
        }
    }
}
