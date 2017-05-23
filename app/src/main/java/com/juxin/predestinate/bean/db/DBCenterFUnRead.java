package com.juxin.predestinate.bean.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.juxin.predestinate.bean.db.cache.FProfileCache;
import com.juxin.predestinate.bean.db.utils.CloseUtil;
import com.juxin.predestinate.bean.db.utils.CursorUtil;
import com.juxin.predestinate.bean.db.utils.DBConstant;
import com.juxin.predestinate.module.util.ByteUtil;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import java.util.HashMap;
import java.util.Map;
import rx.Observable;
import rx.functions.Func1;

/**
 * funread 处理
 * Created by Kind on 2017/3/28.
 */

public class DBCenterFUnRead {
    private BriteDatabase mDatabase;

    public DBCenterFUnRead(BriteDatabase database) {
        this.mDatabase = database;
    }

    public long storageData(String key, String content){
        if (!isExist(key)) {//没有数据
            return insertUnRead(key, content);
        } else {
            return updateUnRead(key, content);
        }
    }

    /**
     * 多条消息插入
     * @param stringMap
     */
    public void insertLetter(Map<String, String> stringMap){
        BriteDatabase.Transaction transaction = mDatabase.newTransaction();
        try {
            for (Map.Entry<String, String> entry : stringMap.entrySet()) {
                insertUnRead(entry.getKey(), entry.getValue());
            }

            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }


    /**
     * 单条插入
     * @param key
     * @param content
     * @return
     */
    public long insertUnRead(String key, String content) {
        try {
            final ContentValues values = new ContentValues();
            values.put(FUnRead.COLUMN_KEY, key);

            if(TextUtils.isEmpty(content))
                values.put(FUnRead.COLUMN_KEY, ByteUtil.toBytesUTF(content));

            return mDatabase.insert(FUnRead.FUNREAD_TABLE, values);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return DBConstant.ERROR;
    }

    /**
     * 更新
     * @param key
     * @param content
     * @return
     */
    public int updateUnRead(String key, String content){
        try {
            final ContentValues values = new ContentValues();
            values.put(FUnRead.COLUMN_CONTENT, ByteUtil.toBytesUTF(content));

            return mDatabase.update(FUnRead.FUNREAD_TABLE, values, FUnRead.COLUMN_KEY +  " = ? ", key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DBConstant.ERROR;
    }

    /**
     * 是否存在
     * @param key
     * @return
     */
    private boolean isExist(String key) {
        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(FUnRead.FUNREAD_TABLE)
                .append(" WHERE ")
                .append(FUnRead.COLUMN_KEY + " = ?");
        Cursor cursor = mDatabase.query(sql.toString(), key);
        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else {
            if (cursor != null) cursor.close();
            return false;
        }
    }

    /**
     * 查询所有
     *
     * @return
     */
    public Observable<Map<String, String>> queryUnReadList() {
        String sqlStr = "SELECT * FROM " + FUnRead.FUNREAD_TABLE;
        return mDatabase.createQuery(FUnRead.FUNREAD_TABLE, sqlStr).map(new Func1<SqlBrite.Query, Map<String, String>>() {
            @Override
            public Map<String, String> call(SqlBrite.Query query) {
                Map<String, String> map = new HashMap<>();
                Cursor cursor = query.run();
                if (null == cursor) {
                    return map;
                }
                try {
                    while (cursor.moveToNext()) {
                        map.put(CursorUtil.getString(cursor, FUnRead.COLUMN_KEY), CursorUtil.getBlobToString(cursor, FLetter.COLUMN_CONTENT));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    CloseUtil.close(cursor);
                }
                return map;
            }
        });
    }

    public Observable<String> queryUnRead(String key) {
        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(FUnRead.FUNREAD_TABLE)
                .append(" WHERE ")
                .append(FUnRead.COLUMN_KEY + " = ")
                .append(key);

        return mDatabase.createQuery(FUnRead.FUNREAD_TABLE, sql.toString()).map(new Func1<SqlBrite.Query, String>() {
            @Override
            public String call(SqlBrite.Query query) {
                Cursor cursor = query.run();
                try {
                    if (cursor == null || !cursor.moveToNext()) return null;

                    return CursorUtil.getBlobToString(cursor, FUnRead.COLUMN_CONTENT);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    CloseUtil.close(cursor);
                }
                return null;
            }
        });
    }

    /**
     * 删除
     * @param key
     * @return
     */
    public int delete(String key){
        return mDatabase.delete(FUnRead.FUNREAD_TABLE, FUnRead.COLUMN_KEY + " = ? ", key);
    }
}