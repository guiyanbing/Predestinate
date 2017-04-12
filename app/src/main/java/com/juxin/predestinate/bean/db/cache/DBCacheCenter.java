package com.juxin.predestinate.bean.db.cache;

import com.squareup.sqlbrite.BriteDatabase;

/**
 * Created by Kind on 2017/4/7.
 */

public class DBCacheCenter {

    private BriteDatabase mDatabase;
    private DBCacheCenterFProfile cacheCenterFProfile;

    public DBCacheCenter(BriteDatabase database) {
        this.mDatabase = database;
        cacheCenterFProfile = new DBCacheCenterFProfile(database);
    }
}
