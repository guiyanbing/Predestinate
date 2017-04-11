package com.juxin.predestinate.bean.db.cache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kind on 2017/3/21.
 */

public class DBCacheHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;

    public DBCacheHelper(Context context) {
        super(context, "predestinate.db", null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FProfile.getCreateTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
