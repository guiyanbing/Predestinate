package com.juxin.predestinate.bean.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kind on 2017/3/21.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;

    public DBHelper(Context context, String DBNAME) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Fmessage.getCreateTable());
        db.execSQL(FProfile.getCreateTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
