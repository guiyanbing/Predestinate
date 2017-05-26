package com.juxin.predestinate.bean.db.cache;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.bean.db.FLetter;
import com.juxin.predestinate.bean.db.utils.CloseUtil;
import com.juxin.predestinate.bean.db.utils.CursorUtil;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.local.chat.utils.MessageConstant;
import com.juxin.predestinate.module.util.ByteUtil;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Kind on 2017/4/7.
 */

public class DBCacheCenter {

    private BriteDatabase mDatabase;

    public DBCacheCenter(BriteDatabase database) {
        this.mDatabase = database;
    }

    /******************** FProfileCache **************************/

    /**
     * 批量更新个人资料
     *
     * @param lightweights
     * @return
     */
    public boolean storageProfileData(List<UserInfoLightweight> lightweights) {
        if (lightweights == null || lightweights.size() <= 0) {
            return false;
        }
        BriteDatabase.Transaction transaction = mDatabase.newTransaction();
        try {
            for (UserInfoLightweight temp : lightweights) {
                storageProfileData(temp);
            }
            transaction.markSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            transaction.end();
        }
        return false;
    }


    /**
     * 插入或更新内容
     *
     * @param lightweight
     * @return
     */
    public void storageProfileData(final UserInfoLightweight lightweight) {
        Observable<Boolean> observable = isProfileExist(lightweight.getUid());
        observable.subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                try {
                    final ContentValues values = new ContentValues();
                    values.put(FProfileCache.COLUMN_USERID, lightweight.getUid());
                    values.put(FProfileCache.COLUMN_TIME, lightweight.getTime());

                    if (lightweight.getInfoJson() != null)
                        values.put(FProfileCache.COLUMN_INFOJSON, ByteUtil.toBytesUTF(lightweight.getInfoJson()));

                    if (!aBoolean) {
                        mDatabase.insert(FProfileCache.FPROFILE_TABLE, values);
                    } else {
                        mDatabase.update(FProfileCache.FPROFILE_TABLE, values, FProfileCache.COLUMN_USERID + " = ? ", String.valueOf(lightweight.getUid()));
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 是否存在
     *
     * @param userID
     * @return
     */
    public Observable<Boolean> isProfileExist(long userID) {
        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(FProfileCache.FPROFILE_TABLE)
                .append(" WHERE ")
                .append(FProfileCache.COLUMN_USERID + " = ")
                .append(userID);

        return mDatabase.createQuery(FProfileCache.FPROFILE_TABLE, sql.toString())
                .map(new Func1<SqlBrite.Query, Boolean>() {
                    @Override
                    public Boolean call(SqlBrite.Query query) {
                        Cursor cursor = null;
                        try {
                            cursor = query.run();
                            if (cursor != null && cursor.moveToFirst()) {
                                cursor.close();
                                return true;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            CloseUtil.close(cursor);
                        }
                        return false;
                    }
                }).unsubscribeOn(Schedulers.io());
    }


    public Observable<UserInfoLightweight> queryProfile(long userID) {
        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(FProfileCache.FPROFILE_TABLE)
                .append(" WHERE ").append(FProfileCache.COLUMN_USERID + " = ").append(userID);

        return mDatabase.createQuery(FProfileCache.FPROFILE_TABLE, sql.toString()).map(new Func1<SqlBrite.Query, UserInfoLightweight>() {
            @Override
            public UserInfoLightweight call(SqlBrite.Query query) {
                UserInfoLightweight lightweight = new UserInfoLightweight();
                Cursor cursor = query.run();
                try {
                    if (cursor == null || !cursor.moveToNext()) {
                        return lightweight;
                    }

                    lightweight.parseUserInfoLightweight(
                            CursorUtil.getLong(cursor, FProfileCache.COLUMN_USERID),
                            CursorUtil.getBlobToString(cursor, FProfileCache.COLUMN_INFOJSON),
                            CursorUtil.getLong(cursor, FProfileCache.COLUMN_TIME)
                    );
                    return lightweight;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    CloseUtil.close(cursor);
                }
                return lightweight;
            }
        });
    }

    /******************** FHttpCache **************************/

    /**
     * 插入或更新内容
     *
     * @param key
     * @param content
     * @return
     */
    public long storageDataCache(String key, String content) {
        try {
            boolean ret = isExist(key);
            final ContentValues values = new ContentValues();
            if (!ret) {
                values.put(FHttpCache.COLUMN_KEY, key);

                if (TextUtils.isEmpty(content))
                    values.put(FHttpCache.COLUMN_KEY, ByteUtil.toBytesUTF(content));

                return mDatabase.insert(FHttpCache.FHTTPCACHE_TABLE, values);
            } else {

                values.put(FHttpCache.COLUMN_KEY, ByteUtil.toBytesUTF(content));
                return mDatabase.update(FHttpCache.FHTTPCACHE_TABLE, values, FHttpCache.COLUMN_KEY + " = ? ", key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MessageConstant.ERROR;

    }

    /**
     * 是否存在
     *
     * @param key
     * @return
     */
    private boolean isExist(String key) {
        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(FHttpCache.FHTTPCACHE_TABLE)
                .append(" WHERE ")
                .append(FHttpCache.COLUMN_KEY + " = ?");
        Cursor cursor = mDatabase.query(sql.toString(), key);
        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else {
            if (cursor != null) cursor.close();
            return false;
        }
    }

    public Observable<String> queryHttpCache(String key) {
        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(FHttpCache.FHTTPCACHE_TABLE)
                .append(" WHERE ")
                .append(FHttpCache.COLUMN_KEY + " = ")
                .append(key);


        return mDatabase.createQuery(FHttpCache.FHTTPCACHE_TABLE, sql.toString()).map(new Func1<SqlBrite.Query, String>() {
            @Override
            public String call(SqlBrite.Query query) {
                String str = null;
                Cursor cursor = query.run();
                if (null == cursor) {
                    return str;
                }
                try {
                    while (cursor.moveToNext()) {
                        str = CursorUtil.getBlobToString(cursor, FHttpCache.COLUMN_CONTENT);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    CloseUtil.close(cursor);
                }
                return str;
            }
        });
    }

    /**
     * 删除
     *
     * @param key
     * @return
     */
    public int delete(String key) {
        return mDatabase.delete(FHttpCache.FHTTPCACHE_TABLE, FHttpCache.COLUMN_KEY + " = ? ", key);
    }

    /**
     * 删除所有
     *
     * @return
     */
    public int deleteAll() {
        return mDatabase.delete(FHttpCache.FHTTPCACHE_TABLE, "", "");
    }
}
