package com.dk.mp.schedule.db;

import android.content.Context;

import com.dk.mp.core.entity.Rcap;
import com.dk.mp.core.entity.RcapDetail;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * 作者：janabo on 2016/12/27 14:13
 */
public class RealmHelper {

    private Realm mRealm;
    private CoreSharedPreferencesHelper preference;
    private String uid = "";

    public RealmHelper(Context context) {
        mRealm = Realm.getDefaultInstance();
        preference = new CoreSharedPreferencesHelper(context);
        if(preference.getLoginMsg() != null){
            uid = preference.getLoginMsg().getUid();
        }
    }

    /**
     * 新增日程
     * @param rcap
     */
    public void addRcap(Rcap rcap){
        rcap.setUid(uid);
        mRealm.beginTransaction();
        if(!isRcapExist(rcap.getId(),rcap.getUid()))
            mRealm.copyToRealm(rcap);
        else
            mRealm.copyToRealmOrUpdate(rcap);
        insertRcapDetail(rcap);
        mRealm.commitTransaction();
    }

    /**
     * 按天新增日程安排
     * @param rcap
     */
    public void insertRcapDetail(Rcap rcap){
        RealmResults<RcapDetail> rds = mRealm.where(RcapDetail.class).equalTo("rcid",rcap.getId()).findAll();
        rds.deleteAllFromRealm();
        String time1 = rcap.getTime_start().split(" ")[0];
        String time2 = rcap.getTime_end().split(" ")[0];
        long count = TimeUtils.getDaysBetween(time1, time2);
        List<RcapDetail> rcapDetails = new ArrayList<>();
        for (int i = 0; i < count + 1; i++) {
            RcapDetail rd = new RcapDetail();
            rd.setId(UUID.randomUUID().toString());
            String date = TimeUtils.getAfterDate(time1, i);
            rd.setDate(date);
            rd.setStime(rcap.getStime());
            rd.setTitle(rcap.getTitle());
            rd.setContent(rcap.getContent());
            rd.setLocation(rcap.getLocation());
            rd.setTime_start(rcap.getTime_start());
            rd.setTime_end(rcap.getTime_end());
            rd.setRcid(rcap.getId());
            rd.setUid(rcap.getUid());
            rcapDetails.add(rd);
        }
        mRealm.copyToRealm(rcapDetails);
    }

    /**
     * 检查日程是否存在
     * @param id
     * @return
     */
    public boolean isRcapExist(String id,String mUid){
        Rcap rcap=mRealm.where(Rcap.class).equalTo("id",id).equalTo("uid",mUid).findFirst();
        if (rcap==null){
            return false;
        }else {
            return  true;
        }
    }

    /**
     * 查询指定日期人员
     * @param date
     * @return
     */
    public List<RcapDetail> queryRcap(String date){
        RealmResults<RcapDetail> rcaps = mRealm.where(RcapDetail.class).equalTo("date",date).equalTo("uid",uid).findAllSorted("stime");
        return mRealm.copyFromRealm(rcaps);
    }

    /**
     * 查询日程安排
     * @param rcapid
     * @return
     */
    public Rcap qRcap(String rcapid){
        Rcap rcap = mRealm.where(Rcap.class).equalTo("id", rcapid).findFirst();
        return rcap;
    }

    /**
     * 删除日程安排
     * @param id
     */
    public void deleteRcap(String id){
        mRealm.beginTransaction();
        Rcap rcap = mRealm.where(Rcap.class).equalTo("id", id).findFirst();
        if(rcap != null) {
            rcap.deleteFromRealm();
        }
        RealmResults<RcapDetail> rcapDetails = mRealm.where(RcapDetail.class).equalTo("rcid",id).findAll();
        if(rcapDetails!=null && rcapDetails.size()>0) {
            rcapDetails.deleteAllFromRealm();
        }
        mRealm.commitTransaction();
    }


    /**
     * 关闭数据库
     */
    public void closeRealm(){
        mRealm.close();
    }
}
