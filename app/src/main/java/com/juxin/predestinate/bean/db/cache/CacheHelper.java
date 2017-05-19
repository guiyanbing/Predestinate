package com.juxin.predestinate.bean.db.cache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kind on 2017/3/21.
 */

public class CacheHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;

    public CacheHelper(Context context) {
        super(context, "predestinate.db", null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FProfileCache.getCreateTable());
        db.execSQL(FHttpCache.getCreateTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
