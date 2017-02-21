package com.dk.mp.oldoa.manager;

import com.dk.mp.core.entity.PageMsg;
import com.dk.mp.core.util.Logger;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.oldoa.entity.Attachment;
import com.dk.mp.oldoa.entity.BaoGaoDoc;
import com.dk.mp.oldoa.entity.Doc;
import com.dk.mp.oldoa.entity.FaWenDoc;
import com.dk.mp.oldoa.entity.Opinion;
import com.dk.mp.oldoa.entity.ShouWenDoc;
import com.dk.mp.oldoa.utils.http.HttpClientUtil;
import com.lidroid.xutils.http.ResponseInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：janabo on 2017/2/20 16:23
 */
public class OAManager {
    public final static OAManager oaManger = new OAManager();

    // 限制住不能直接产生一个实例
    private OAManager() {

    };

	/*
	 *
	 * 采用这种单例防止多线程，导致业务逻辑混乱
	 *
	 * @return
	 */

    public synchronized static OAManager getIntence() {

        return oaManger;
    }

    /**
     * 获取OA列表信息
     *
     * @param
     * @return
     */
    public PageMsg getOAListInfos(ResponseInfo<String> responseInfo) {
        PageMsg pageMsg = new PageMsg();
        try {
            JSONObject object = HttpClientUtil.getJSONObject(responseInfo);
            if (object != null) {
                Logger.info("getinterfaces:" + object.toString());
                pageMsg = JsonOAListUtil(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageMsg;
    }

    public PageMsg JsonOAListUtil(JSONObject object) {
        PageMsg pageMsg = new PageMsg();
        List<Doc> oaLists = new ArrayList<Doc>();
        try {
            if (object != null) {
                JSONObject jsono = object.getJSONObject("data");
                JSONArray arrary = jsono.getJSONArray("list");
                for (int i = 0; i < arrary.length(); i++) {
                    JSONObject json = arrary.getJSONObject(i);
                    Doc doc = new Doc();
                    doc.setType(json.getString("type"));
                    doc.setTime(json.getString("time"));
                    doc.setTitle(json.getString("title"));
                    doc.setId(json.getString("bizId"));
                    doc.setOperateId(json.getString("operateId"));
                    doc.setDepartment(json.getString("depart"));
                    doc.setActivityId(json.getString("activityId"));
                    doc.setFlowDetailId(json.getString("flowDetailId"));
                    doc.setUrl(json.getString("url"));
                    oaLists.add(doc);
                }
                pageMsg.setList(oaLists);
                pageMsg.setTotalPages(jsono.getInt("totalPages"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pageMsg;

    }

    /**
     * 获取发文详情
     *
     * @param
     * @return
     */
    public FaWenDoc getOAFWInfos(ResponseInfo<String> responseInfo) {
        FaWenDoc fw = new FaWenDoc();
        try {
            JSONObject object = HttpClientUtil.getJSONObject(responseInfo);
            if (object != null) {
                Logger.info("getfw:" + object.toString());
                fw = JsonOAFWUtil(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fw;

    }

    public FaWenDoc JsonOAFWUtil(JSONObject object) {
        FaWenDoc fwEntity = new FaWenDoc();
        List<Opinion> bossList = new ArrayList<Opinion>();
        List<Opinion> opinionList = new ArrayList<Opinion>();
        List<Attachment> attachmentList = new ArrayList<Attachment>();

        try {

            if (object != null) {
                JSONObject data = object.getJSONObject("data");
                JSONObject next;
                if (data.isNull("next")) {

                    fwEntity.setNext(null);
                    fwEntity.setNeedOptiona(null);
                } else {
                    next = data.getJSONObject("next");
                    fwEntity.setNext(next.getString("next"));
                    fwEntity.setNeedOptiona(next.getString("needOptiona"));
                    fwEntity.setRetrieveNumber(next.getString("retrieveNumber"));

                    if (next.isNull("flowEnd")) {
                        fwEntity.setFlowend(null);
                    } else {
                        fwEntity.setFlowend(next.getString("flowEnd"));
                    }
                }

                //设置领导意见数据
                JSONArray arry = data.optJSONArray("boss");
                if(arry != null){
                    for (int i = 0; i < arry.length(); i++) {
                        JSONObject json = arry.getJSONObject(i);
                        Opinion boss = new Opinion();
                        boss.setContent(json.getString("content"));
                        boss.setName(json.getString("name"));
                        boss.setTime(json.getString("time"));
                        bossList.add(boss);
                    }
                }
                fwEntity.setBoss(bossList);
                //设置发文详情
                fwEntity.setZhengwen(data.getString("zw"));
                fwEntity.setFwJGwz(data.getString("jgdz"));
                fwEntity.setFwFileType(data.getString("wjlx"));
                fwEntity.setDepartment(data.getString("ngbm"));
                fwEntity.setTime(data.getString("ngrq"));
                fwEntity.setJinjCD(data.getString("jjcd"));
                fwEntity.setFwNum(data.getString("fwbh"));
                fwEntity.setDqbz(data.getString("dqbz"));
                fwEntity.setYclry(data.getString("yclry"));
                fwEntity.setDclry(data.getString("dclry"));
                fwEntity.setUrl(data.getString("url"));
                fwEntity.setMap(getMap(data.getString("url")));
                fwEntity.setNgr(data.optString("ngr"));

                //设置附件
                JSONArray arryFj = data.optJSONArray("fj");
                if(arryFj != null){
                    for (int i = 0; i < arryFj.length(); i++) {
                        JSONObject json = arryFj.getJSONObject(i);
                        Attachment att = new Attachment();
                        att.setId(json.getString("id"));
                        att.setTitle(json.getString("title"));
                        att.setUrl(json.getString("url"));
                        attachmentList.add(att);
                    }
                }
                fwEntity.setFujian(attachmentList);

                //设置意见
                JSONArray arryYj = data.optJSONArray("yj");
                if(arryYj != null){
                    for (int i = 0; i < arryYj.length(); i++) {
                        JSONObject json = arryYj.getJSONObject(i);
                        Opinion opinion = new Opinion();
                        opinion.setName(json.getString("name"));
                        opinion.setContent(json.getString("content"));
                        opinion.setTime(json.getString("time"));
                        opinionList.add(opinion);
                    }
                }
                fwEntity.setOpinions(opinionList);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fwEntity;

    }

    /**
     * 获取收文详情
     *
     * @param
     * @return
     */
    public ShouWenDoc getOASWInfos(ResponseInfo<String> responseInfo) {
        ShouWenDoc sw = new ShouWenDoc();
        try {
            JSONObject object = HttpClientUtil.getJSONObject(responseInfo);
            if (object != null) {
                sw = JsonOASWUtil(object);
                Logger.info("object = "+object + "+++++++++++++++++++++++++++++++");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sw;

    }

    public ShouWenDoc JsonOASWUtil(JSONObject object) {
        ShouWenDoc fwEntity = new ShouWenDoc();
        List<Opinion> bossList = new ArrayList<Opinion>();
        List<Opinion> opinionList = new ArrayList<Opinion>();
        List<Attachment> attachmentList = new ArrayList<Attachment>();

        try {

            if (object != null) {
                JSONObject data = object.getJSONObject("data");
                JSONObject next;
                if (data.isNull("next")) {

                    fwEntity.setNext(null);
                    fwEntity.setNeedOptiona(null);
                } else {
                    next = data.getJSONObject("next");
                    fwEntity.setNext(next.getString("next"));
                    fwEntity.setNeedOptiona(next.getString("needOptiona"));
                    fwEntity.setRetrieveNumber(next.getString("retrieveNumber"));

                    if (next.isNull("flowEnd")) {
                        fwEntity.setFlowend(null);
                    } else {
                        fwEntity.setFlowend(next.getString("flowEnd"));
                    }
                }




                //设置领导意见数据
                JSONArray arry = data.optJSONArray("boss");
                if(arry != null){
                    for (int i = 0; i < arry.length(); i++) {
                        JSONObject json = arry.getJSONObject(i);
                        Opinion boss = new Opinion();
                        boss.setContent(json.getString("content"));
                        boss.setName(json.getString("name"));
                        boss.setTime(json.getString("time"));
                        bossList.add(boss);
                    }
                }
                fwEntity.setBoss(bossList);
                //设置发文详情
                fwEntity.setLaiwZH(data.getString("lwzh"));
                fwEntity.setLaiwUnit(data.getString("lwdw"));
                fwEntity.setShouwTime(data.getString("swrq"));
                fwEntity.setShouwWH(data.getString("swh"));
                fwEntity.setJinjCD(data.getString("jjcd"));
                fwEntity.setDqbz(data.getString("dqbz"));
                fwEntity.setYclry(data.getString("yclry"));
                fwEntity.setDclry(data.getString("dclry"));
                fwEntity.setUrl(data.getString("url"));
                fwEntity.setNgr(data.getString("ngr"));
                fwEntity.setDepartment(data.optString("ngbm"));
                Logger.info("I'm in" + data.getString("swrq")+"+++++++++++++++++++++++++++++++");

                fwEntity.setMap(getMap(data.getString("url")));
                //设置附件
                JSONArray arryFj = data.optJSONArray("fj");
                if(arryFj != null){
                    for (int i = 0; i < arryFj.length(); i++) {
                        JSONObject json = arryFj.getJSONObject(i);
                        Attachment att = new Attachment();
                        att.setId(json.getString("id"));
                        att.setTitle(json.getString("title"));
                        att.setUrl(json.getString("url"));
                        attachmentList.add(att);
                    }
                }
                fwEntity.setFujian(attachmentList);

                //设置意见
                JSONArray arryYj = data.optJSONArray("yj");
                if(arryYj != null){
                    for (int i = 0; i < arryYj.length(); i++) {
                        JSONObject json = arryYj.getJSONObject(i);
                        Opinion opinion = new Opinion();
                        opinion.setName(json.getString("name"));
                        opinion.setContent(json.getString("content"));
                        opinion.setTime(json.getString("time"));
                        opinionList.add(opinion);
                    }
                }
                fwEntity.setOpinions(opinionList);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return fwEntity;

    }

    /**
     * 获取报告详情
     *
     * @param
     * @return
     */
    public BaoGaoDoc getOABGInfos(ResponseInfo<String> responseInfo) {
        BaoGaoDoc bg = new BaoGaoDoc();
        try {
            JSONObject object = HttpClientUtil.getJSONObject(responseInfo);
            if (object != null) {
                Logger.info("getbg:" + object.toString());
                bg = JsonOABGUtil(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bg;
    }

    public BaoGaoDoc JsonOABGUtil(JSONObject object) {
        BaoGaoDoc fwEntity = new BaoGaoDoc();
        List<Opinion> bossList = new ArrayList<Opinion>();
        List<Opinion> opinionList = new ArrayList<Opinion>();
        List<Attachment> attachmentList = new ArrayList<Attachment>();

        try {

            if (object != null) {
                JSONObject data = object.getJSONObject("data");
                JSONObject next;
                if (data.isNull("next")) {

                    fwEntity.setNext(null);
                    fwEntity.setNeedOptiona(null);
                } else {
                    next = data.getJSONObject("next");
                    fwEntity.setNext(next.getString("next"));
                    fwEntity.setNeedOptiona(next.getString("needOptiona"));
                    fwEntity.setRetrieveNumber(next.getString("retrieveNumber"));

                    if (next.isNull("flowEnd")) {
                        fwEntity.setFlowend(null);
                    } else {
                        fwEntity.setFlowend(next.getString("flowEnd"));
                    }
                }


                //设置领导意见数据
                JSONArray arry = data.optJSONArray("boss");
                if(arry != null){
                    for (int i = 0; i < arry.length(); i++) {
                        JSONObject json = arry.getJSONObject(i);
                        Opinion boss = new Opinion();
                        boss.setContent(json.getString("content"));
                        boss.setName(json.getString("name"));
                        boss.setTime(json.getString("time"));
                        bossList.add(boss);
                    }
                }
                fwEntity.setBoss(bossList);
                //设置发文详情
                fwEntity.setZw(data.getString("zw"));
                fwEntity.setBgNum(data.getString("bh"));
                fwEntity.setTime(data.getString("ngrq"));
                fwEntity.setJinjCD(data.getString("jjcd"));
                fwEntity.setDqbz(data.getString("dqbz"));
                fwEntity.setYclry(data.getString("yclry"));
                fwEntity.setDclry(data.getString("dclry"));
                fwEntity.setUrl(data.getString("url"));
                fwEntity.setMap(getMap(data.getString("url")));
                fwEntity.setDepartment(data.getString("ngbm"));
                fwEntity.setNgr(data.optString("ngr"));

                //设置附件
                JSONArray arryFj = data.getJSONArray("fj");
                if(arryFj != null){
                    for (int i = 0; i < arryFj.length(); i++) {
                        JSONObject json = arryFj.getJSONObject(i);
                        Attachment att = new Attachment();
                        att.setId(json.getString("id"));
                        att.setTitle(json.getString("title"));
                        att.setUrl(json.getString("url"));
                        attachmentList.add(att);
                    }
                }
                fwEntity.setFujian(attachmentList);

                //设置意见
                JSONArray arryYj = data.getJSONArray("yj");
                if(arryYj != null){
                    for (int i = 0; i < arryYj.length(); i++) {
                        JSONObject json = arryYj.getJSONObject(i);
                        Opinion opinion = new Opinion();
                        opinion.setName(json.getString("name"));
                        opinion.setContent(json.getString("content"));
                        opinion.setTime(json.getString("time"));
                        opinionList.add(opinion);
                    }
                }
                fwEntity.setOpinions(opinionList);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fwEntity;

    }

    /**
     * 发表意见
     *
     * @return
     */
    public String sendOpinions(ResponseInfo<String> responseInfo) {
        String msg = "服务器异常";
        try {
            JSONObject object = HttpClientUtil.getJSONObject(responseInfo);
            if (object != null) {
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
                    msg = "服务器异常";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg;
    }

    /**
     * 分割数值对放到map里面
     * @param url
     * @return
     */
    public Map<String, String> getMap(String url) {

        Map<String, String> m = new HashMap<String, String>();
        if(url!=null){
            String[] urls = url.split("&");
            for (int i = 1; i < urls.length; i++) {
                if (urls[i].split("=").length == 2) {
                    m.put(urls[i].split("=")[0], urls[i].split("=")[1]);
                } else {
                    m.put(urls[i].split("=")[0], "");
                }

            }
        }
        return m;

    }

}
