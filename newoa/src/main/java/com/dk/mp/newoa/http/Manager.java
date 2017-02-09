package com.dk.mp.newoa.http;


import com.dk.mp.newoa.entity.Detail;
import com.dk.mp.newoa.entity.Jbxx;
import com.dk.mp.newoa.entity.OASubmit;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 作者：janabo on 2016/12/24 14:44
 */
public class Manager {

    public static Detail getOaDetail(JSONObject json) {
        Detail detail = new Detail();
        try {
            List<Jbxx> operations = new ArrayList<Jbxx>();
            JSONArray oArray = json.optJSONArray("operation");
            if(oArray != null){
                for (int i = 0; i < oArray.length(); i++) {
                    Jbxx operation = new Jbxx();
                    JSONObject jo = oArray.getJSONObject(i);
                    operation.setKey(jo.getString("key"));
                    operation.setValue(jo.getString("value"));
                    operations.add(operation);
                }
            }
            detail.setJbxxs(operations);
            String html = json.optString("html");
            detail.setHtml(html);
            OASubmit submit = new OASubmit();
            JSONObject sJson = json.optJSONObject("submit");
            if(sJson != null){
                submit.setNgSuggestion(sJson.optString("ngSuggestion"));
                submit.setSxNgSuggestion(sJson.optString("sxNgSuggestion"));
                submit.setSxSuggestion(sJson.optString("sxSuggestion"));
                submit.setSuggestion(sJson.optString("suggestion"));
                submit.setUsers(sJson.optString("users"));
            }
            detail.setSubmit(submit);
            JSONObject jsonObj = json.optJSONObject("param");
            Map<String, String> pMap = new HashMap<String, String>();
            if(jsonObj != null){
                Iterator<String> nameItr = jsonObj.keys();
                String name;
                while (nameItr.hasNext()) {
                    name = nameItr.next();
                    pMap.put(name, jsonObj.getString(name));
                }
            }
            detail.setParams(pMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

}
