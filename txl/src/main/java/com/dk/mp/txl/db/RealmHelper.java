package com.dk.mp.txl.db;

import android.content.Context;

import com.dk.mp.core.entity.Department;
import com.dk.mp.core.entity.Jbxx;
import com.dk.mp.core.entity.XbPersons;

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
    public List<XbPersons> queryALlXb(){
        RealmResults<XbPersons> jbxxs = mRealm.where(XbPersons.class).findAll();
        return mRealm.copyFromRealm(jbxxs);
    }

    /**
     * 新增部门
     * @return
     */
    public List<Department> queryAllDepartment(){
        RealmResults<Department> jbxxs = mRealm.where(Department.class).findAll();
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

    /**
     * delete星标同事 （删）
     */
    public void deleteALlXb() {
        RealmResults<Jbxx> d = mRealm.where(Jbxx.class).findAll();
        mRealm.beginTransaction();
        d.deleteAllFromRealm();
        mRealm.commitTransaction();
    }


    /**
     * 批量新增部门
     * @param d
     */
    public void addPersons(final List<Jbxx> d){
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(d);
        mRealm.commitTransaction();
    }

    /**
     * delete指定部门下的人员（删）
     */
    public void deletePersonsByDepartmentid(String departmentid) {
        RealmResults<Jbxx> d = mRealm.where(Jbxx.class).equalTo("departmentid",departmentid).findAll();
        mRealm.beginTransaction();
        d.deleteAllFromRealm();
        mRealm.commitTransaction();
    }

    /**
     * 查询指定部门下人员
     * @return
     */
    public List<Jbxx> queryPersonsByDepartmentid(String departmentid){
        RealmResults<Jbxx> jbxxs = mRealm.where(Jbxx.class).equalTo("departmentid",departmentid).findAll();
        return mRealm.copyFromRealm(jbxxs);
    }

    /**
     * 通过主键查询数据
     * @param key 主键
     * @return
     */
    public XbPersons queryPersonsByKey(String key){
        XbPersons j=mRealm.where(XbPersons.class).equalTo("prikey",key).findFirst();
        return j;
    }

    /**
     * 删除指定的星标用户
     * @param prikey
     */
    public void deleteXbById(String prikey){
        XbPersons d = mRealm.where(XbPersons.class).equalTo("prikey", prikey).findFirst();
        mRealm.beginTransaction();
        d.deleteFromRealm();
        mRealm.commitTransaction();
    }
    /**
     * 新增星标同事
     * @param j
     */
    public void addXb(final XbPersons j){
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(j);
        mRealm.commitTransaction();
    }

}
