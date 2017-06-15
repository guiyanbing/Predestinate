package com.juxin.predestinate.bean.db.cache;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Handler;
import android.text.TextUtils;

import com.juxin.library.log.PLogger;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.bean.db.utils.CloseUtil;
import com.juxin.predestinate.bean.db.utils.CursorUtil;
import com.juxin.predestinate.module.local.chat.utils.MessageConstant;
import com.juxin.predestinate.module.util.ByteUtil;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import rx.Observable;
import rx.Observer;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Kind on 2017/4/7.
 */

public class DBCacheCenter {

    private BriteDatabase mDatabase;
    private Handler handler;

    public DBCacheCenter(BriteDatabase database, Handler handler) {
        this.mDatabase = database;
        this.handler = handler;
    }

    /******************** FProfileCache **************************/

    /**
     * 批量更新个人资料
     *
     * @param lightweights
     * @return
     */
    public boolean storageProfileData(final List<UserInfoLightweight> lightweights) {
        if (lightweights == null || lightweights.size() <= 0) {
            return false;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                BriteDatabase.Transaction transaction = mDatabase.newTransaction();
                try {
                    for (UserInfoLightweight temp : lightweights) {
                        storageOneData(temp, isInfoExist(temp.getUid()));
                    }
                    transaction.markSuccessful();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    transaction.end();
                }
            }
        });

        return true;
    }


    /**
     * 是否存在
     *
     * @param userID
     * @return
     */
    private boolean isInfoExist(long userID) {
        Cursor cursor = null;
        try {
            StringBuilder sql = new StringBuilder("SELECT * FROM ").append(FProfileCache.FPROFILE_TABLE)
                    .append(" WHERE ")
                    .append(FProfileCache.COLUMN_USERID + " = ?");
            cursor = mDatabase.query(sql.toString(), String.valueOf(userID));
            if (cursor != null && cursor.moveToFirst()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(cursor);
        }
        return false;
    }

    private void storageOneData(UserInfoLightweight lightweight, boolean aBoolean) {
        try {
            long uid = lightweight.getUid();
            PLogger.printObject("storageProfileData==" + aBoolean);
            final ContentValues values = new ContentValues();
            values.put(FProfileCache.COLUMN_USERID, uid);
            values.put(FProfileCache.COLUMN_TIME, lightweight.getTime());

            if (lightweight.getInfoJson() != null)
                values.put(FProfileCache.COLUMN_INFOJSON, ByteUtil.toBytesUTF(lightweight.getInfoJson()));

            if (aBoolean) {
                mDatabase.update(FProfileCache.FPROFILE_TABLE, values, FProfileCache.COLUMN_USERID + " = ? ", String.valueOf(uid));
            } else {
                mDatabase.insert(FProfileCache.FPROFILE_TABLE, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 插入或更新内容
     *
     * @param lightweight
     * @return
     */
    public void storageProfileData(final UserInfoLightweight lightweight) {
        PLogger.d("storageProfileData==" + "111111");

        handler.post(new Runnable() {
            @Override
            public void run() {
                final long uid = lightweight.getUid();
                Observable<Boolean> observable = isProfileExist(uid);
                observable.subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        PLogger.d("storageProfileData==" + aBoolean);
                        storageOneData(lightweight, aBoolean);
                    }
                }).unsubscribe();
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
                });
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

    public Observable<List<UserInfoLightweight>> queryProfile(List<Long> userIDs) {
        if (userIDs.size() < 1) return null;

        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(FProfileCache.FPROFILE_TABLE)
                .append(" WHERE ");
        if (userIDs.size() == 1) {
            sql.append(FProfileCache.COLUMN_USERID + " = ").append(userIDs.get(0));
        } else {
            sql.append(FProfileCache.COLUMN_USERID + " in ").append("( ");
            for (int i = 0; i < userIDs.size(); i++) {
                sql.append(userIDs.get(i));
                if ((i + 1) < userIDs.size()) {
                    sql.append(",");
                }
            }
            sql.append(" )");
        }

        PLogger.printObject("sql.to=" + sql.toString());
        return mDatabase.createQuery(FProfileCache.FPROFILE_TABLE, sql.toString())
                .map(new Func1<SqlBrite.Query, List<UserInfoLightweight>>() {
                    @Override
                    public List<UserInfoLightweight> call(SqlBrite.Query query) {
                        Cursor cursor = query.run();
                        if (null == cursor) return null;

                        List<UserInfoLightweight> infoLightweights = new ArrayList<UserInfoLightweight>();
                        try {
                            while (cursor.moveToNext()) {
                                UserInfoLightweight temp = new UserInfoLightweight();
                                temp.parseUserInfoLightweight(
                                        CursorUtil.getLong(cursor, FProfileCache.COLUMN_USERID),
                                        CursorUtil.getBlobToString(cursor, FProfileCache.COLUMN_INFOJSON),
                                        CursorUtil.getLong(cursor, FProfileCache.COLUMN_TIME)
                                );
                                infoLightweights.add(temp);
                            }
                            return infoLightweights;
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            CloseUtil.close(cursor);
                        }
                        return infoLightweights;
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
    public long storageDataCache(final String key, final String content) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean ret = isExist(key);
                    final ContentValues values = new ContentValues();
                    if (!ret) {
                        values.put(FHttpCache.COLUMN_KEY, key);

                        if (TextUtils.isEmpty(content))
                            values.put(FHttpCache.COLUMN_KEY, ByteUtil.toBytesUTF(content));

                        mDatabase.insert(FHttpCache.FHTTPCACHE_TABLE, values);
                    } else {

                        values.put(FHttpCache.COLUMN_KEY, ByteUtil.toBytesUTF(content));
                        mDatabase.update(FHttpCache.FHTTPCACHE_TABLE, values, FHttpCache.COLUMN_KEY + " = ? ", key);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return MessageConstant.OK;

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
        }).unsubscribeOn(Schedulers.io());
    }

    /**
     * 删除
     *
     * @param key
     * @return
     */
    public int delete(final String key) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                mDatabase.delete(FHttpCache.FHTTPCACHE_TABLE, FHttpCache.COLUMN_KEY + " = ? ", key);
            }
        });
        return MessageConstant.OK;
    }

    /**
     * 删除所有
     *
     * @return
     */
    public int deleteAll() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                mDatabase.delete(FHttpCache.FHTTPCACHE_TABLE, "", "");
            }
        });
        return MessageConstant.OK;
    }
}
