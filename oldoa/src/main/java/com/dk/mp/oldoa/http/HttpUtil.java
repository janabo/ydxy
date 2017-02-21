package com.dk.mp.oldoa.http;

import com.dk.mp.core.util.Logger;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.oldoa.entity.Department;
import com.dk.mp.oldoa.entity.Operate;
import com.dk.mp.oldoa.entity.Person;
import com.dk.mp.oldoa.utils.http.HttpClientUtil;
import com.lidroid.xutils.http.ResponseInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：janabo on 2017/2/20 16:56
 */
public class HttpUtil {

    public static List<Operate> getOperate(ResponseInfo<String> responseInfo) {
        List<Operate> operatelist = new ArrayList<Operate>();
        JSONObject object = HttpClientUtil.getJSONObject(responseInfo);
        if (object != null) {
            try {
                JSONArray array = object.getJSONObject("data").getJSONArray("nodeList");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject json = array.getJSONObject(i);
                    Operate o = new Operate();
                    o.setId(json.getString("nodeId"));
                    o.setName(json.getString("nodeName"));
                    o.setUrl(json.getString("url"));
                    operatelist.add(o);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Logger.info(operatelist + "");
        return operatelist;
    }

    public static List<Department> getPersonCanSelect(ResponseInfo<String> responseInfo) {
        List<Department> dList = new ArrayList<Department>();
        JSONObject object = HttpClientUtil.getJSONObject(responseInfo);
        if (object != null) {
            try {
                JSONArray array = object.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject json = array.getJSONObject(i);
                    List<Person> pList = new ArrayList<Person>();
                    JSONArray userArray = json.getJSONArray("userList");
                    Department d = new Department();
                    d.setParentPosition(i);
                    d.setValidate(json.getString("validate"));
                    d.setName(json.getString("name"));
                    d.setParentId(String.valueOf(i));
                    for (int j = 0; j < userArray.length(); j++) {
                        JSONObject userjson = userArray.getJSONObject(j);
                        Person p = new Person();
                        p.setParentPosition(i);
                        p.setId(userjson.getString("userId"));
                        p.setName(userjson.getString("userName"));
                        if ("0".equals(d.getValidate())) {
                            p.setMoreSelect(true);
                        } else {
                            p.setMoreSelect(false);
                        }
                        pList.add(p);
                    }
                    d.setList(pList);
                    dList.add(d);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return dList;
    }

    public static String UploadCommit(ResponseInfo<String> responseInfo) {
        String msg = "没有条件";
        JSONObject object = HttpClientUtil.getJSONObject(responseInfo);
        if (object != null) {
            try {
                String data = object.getString("data");
                if (StringUtils.isNotEmpty(data)) {
                    if ("success".equals(data)) {
                        msg = "操作成功";
                    } else if ("fail".equals(data)) {

                        msg = "操作失败";
                    } else if ("flowEnd".equals(data)) {

                        msg = "流程已经结束";
                    } else if ("expiryData".equals(data)) {

                        msg = "数据已经失效";
                    } else if ("noCondition".equals(data)) {

                        msg = "没有条件";
                    }
                } else {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return msg;
    }
}
