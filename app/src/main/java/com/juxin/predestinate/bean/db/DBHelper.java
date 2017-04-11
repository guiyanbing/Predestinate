package com.juxin.predestinate.bean.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.juxin.mumu.bean.log.MMLog;
import com.juxin.predestinate.bean.db.cache.FProfile;

/**
 * Created by Kind on 2017/3/21.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;

    public DBHelper(Context context, String DBNAME) {
        super(context, DBNAME +  ".db", null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        MMLog.autoDebug("xxxxx=" + FMessage.getCreateTable());
        db.execSQL(FMessage.getCreateTable());
        MMLog.autoDebug("xxxxx=2" + FProfile.getCreateTable());
        MMLog.autoDebug("xxxxx=3" +FMark.getCreateTable());
        db.execSQL(FProfile.getCreateTable());
        db.execSQL(FMark.getCreateTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
