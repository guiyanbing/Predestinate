package com.juxin.predestinate.module.logic.db;

import com.juxin.predestinate.bean.db.DaoMaster;
import com.juxin.predestinate.bean.db.DaoSession;
import com.juxin.predestinate.module.logic.application.App;

/**
 * GreenDao管理类
 * Created by ZRP on 2017/2/7.
 */
public class DBManager {

    private static DBManager mInstance;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    private DBManager() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(App.context, "predestinate-db", null);
        mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
    }

    public static DBManager getInstance() {
        if (mInstance == null) {
            mInstance = new DBManager();
        }
        return mInstance;
    }

    public DaoMaster getMaster() {
        return mDaoMaster;
    }

    public DaoSession getSession() {
        return mDaoSession;
    }

    public DaoSession getNewSession() {
        mDaoSession = mDaoMaster.newSession();
        return mDaoSession;
    }

}
