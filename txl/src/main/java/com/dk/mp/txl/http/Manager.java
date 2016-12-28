package com.dk.mp.txl.http;


import com.dk.mp.core.entity.Jbxx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：janabo on 2016/12/24 14:44
 */
public class Manager {

    /**
     * 获取人员
     * @param result json对象
     * @param bmid 部门id
     * @param bmname 部门姓名
     * @return
     * @throws JSONException
     */
    public static List<Jbxx> getPeoples(JSONObject result,String bmid,String bmname) throws JSONException {
        List<Jbxx> departments = new ArrayList<>();
        JSONArray ja = result.getJSONArray("data");
        if(ja != null){
            for (int i = 0; i < ja.length(); i++) {
                JSONObject object2 = ja.getJSONObject(i);
                Jbxx p = new Jbxx();
                String pid = object2.optString("id");
                p.setId(pid);
                p.setName(object2.getString("xm"));
                p.setPrikey("bm"+bmid+"p"+pid);
                p.setDepartmentid(bmid);
                p.setDepartmentname(bmname);
                String hm="";
                JSONArray values = object2.getJSONArray("values");
                for (int index = 0; index < values.length(); index++) {
                    if(index==0){
                        hm=	values.getString(index);
                    }else{
                        hm+="/"+values.getString(index);
                    }
                }
                p.setPhones(hm);
                departments.add(p);
            }
        }
        return departments;
    }


    public static List<Jbxx> getPeoples(JSONObject result) throws JSONException {
        List<Jbxx> departments = new ArrayList<>();
        JSONArray ja = result.getJSONArray("data");
        if(ja != null){
            for (int i = 0; i < ja.length(); i++) {
                JSONObject object2 = ja.getJSONObject(i);
                Jbxx p = new Jbxx();
                String pid = object2.optString("id");
                p.setId(pid);
                p.setName(object2.getString("xm"));
                p.setDepartmentname("测试测试测试");
                String hm="";
                JSONArray values = object2.getJSONArray("values");
                for (int index = 0; index < values.length(); index++) {
                    if(index==0){
                        hm=	values.getString(index);
                    }else{
                        hm+="/"+values.getString(index);
                    }
                }
                p.setPhones(hm);
                departments.add(p);
            }
        }
        return departments;
    }


}
