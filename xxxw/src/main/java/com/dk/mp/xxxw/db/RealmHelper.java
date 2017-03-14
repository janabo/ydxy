package com.dk.mp.xxxw.db;

import android.content.Context;

import com.dk.mp.core.entity.News;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * 作者：janabo on 2017/3/9 13:55
 */
public class RealmHelper {
    private Realm mRealm;

    public RealmHelper(Context context) {
        mRealm = Realm.getDefaultInstance();
    }

    /**
     * 批量新增新闻
     * @param d
     */
    public void addNews(final List<News> d){
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(d);
        mRealm.commitTransaction();
    }

    /**
     * 查询所有新闻
     * @return
     */
    public List<News> queryAllNews(){
        RealmResults<News> jbxxs = mRealm.where(News.class).findAll();
        return mRealm.copyFromRealm(jbxxs);
    }

    /**
     * delete新闻 （删）
     */
    public void deleteAllNews() {
        RealmResults<News> d = mRealm.where(News.class).findAll();
        mRealm.beginTransaction();
        d.deleteAllFromRealm();
        mRealm.commitTransaction();
    }
}
