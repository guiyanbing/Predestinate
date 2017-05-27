package com.juxin.predestinate.bean.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.juxin.predestinate.ui.utils.Common;

/**
 * Created by siow on 2017/5/19.
 */

public class OldDBHelper extends SQLiteOpenHelper {
    private static final String DBNAME = "weshot.db";
    private static final int VERSION = 9;

    public OldDBHelper(Context context) {
        super(context, Common.getCahceDir() + DBNAME + ".bak", null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
