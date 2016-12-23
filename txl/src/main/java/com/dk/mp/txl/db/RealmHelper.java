package com.dk.mp.txl.db;

import android.content.Context;

import com.dk.mp.txl.entity.Department;
import com.dk.mp.txl.entity.Jbxx;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * 通讯录
 * 作者：janabo on 2016/12/23 10:46
 */
public class RealmHelper {

    private Realm mRealm;

    public RealmHelper(Context context) {
        mRealm = Realm.getDefaultInstance();
    }

    /**
     * 新增星标同事
     * @param j
     */
    public void addXb(final Jbxx j){
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(j);
        mRealm.commitTransaction();
    }

    /**
     * 批量新增部门
     * @param d
     */
    public void addDepartment(final List<Department> d){
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(d);
        mRealm.commitTransaction();
    }

    /**
     * 新增星标同事
     * @return
     */
    public List<Jbxx> queryALlXb(){
        RealmResults<Jbxx> jbxxs = mRealm.where(Jbxx.class).findAll();
        jbxxs = jbxxs.sort("id");
        return mRealm.copyFromRealm(jbxxs);
    }

    /**
     * 新增部门
     * @return
     */
    public List<Department> queryAllDepartment(){
        RealmResults<Department> jbxxs = mRealm.where(Department.class).findAll();
        jbxxs = jbxxs.sort("id");
        return mRealm.copyFromRealm(jbxxs);
    }

    /**
     * delete部门 （删）
     */
    public void deleteDepartment() {
        RealmResults<Department> d = mRealm.where(Department.class).findAll();
        mRealm.beginTransaction();
        d.deleteAllFromRealm();
        mRealm.commitTransaction();

    }

}
