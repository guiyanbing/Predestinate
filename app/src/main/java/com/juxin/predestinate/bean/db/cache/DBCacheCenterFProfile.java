package com.juxin.predestinate.bean.db.cache;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Build;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweightList;
import com.juxin.predestinate.bean.db.FMessage;
import com.juxin.predestinate.bean.db.cache.FProfile;
import com.juxin.predestinate.bean.db.utils.CloseUtil;
import com.juxin.predestinate.bean.db.utils.CursorUtil;
import com.juxin.predestinate.bean.db.utils.DBConstant;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import java.util.List;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * FProfile 处理
 * Created by Kind on 2017/3/28.
 */

public class DBCacheCenterFProfile {
    private BriteDatabase mDatabase;

    public DBCacheCenterFProfile(BriteDatabase database) {
        this.mDatabase = database;
    }


    public Observable<UserInfoLightweightList> queryBySqlFProfile(String sql) {
        return mDatabase.createQuery(FProfile.FPROFILE_TABLE, sql)
                .map(new Func1<SqlBrite.Query, UserInfoLightweightList>() {
                    @Override
                    public UserInfoLightweightList call(SqlBrite.Query query) {
                        return convert(query.run());
                    }
                });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private UserInfoLightweightList convert(Cursor cursor) {
        UserInfoLightweightList lightweightList = new UserInfoLightweightList();
        if (null == cursor) {
            return lightweightList;
        }

        try {
            while (cursor.moveToNext()) {
                lightweightList.getUserInfos().add(new UserInfoLightweight(
                        CursorUtil.getLong(cursor, FProfile.COLUMN_USERID),
                        CursorUtil.getString(cursor, FProfile.COLUMN_INFOJSON),
                        CursorUtil.getLong(cursor, FProfile.COLUMN_TIME)
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(cursor);
        }
        return lightweightList;
    }


    /**
     * 多条数据更新或插入
     *
     * @param list
     */
    public void updateMsg(List<UserInfoLightweight> list) {
        BriteDatabase.Transaction transaction = mDatabase.newTransaction();
        try {
            for (final UserInfoLightweight item : list) {
                Observable<Boolean> observable = isExistProfile(item.getUid());
                observable.subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean b) {
                        updateMsg(b, item);
                    }
                });
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    private Observable<Boolean> queryBySqlFmessage(String sql) {
        return mDatabase.createQuery(FMessage.FMESSAGE_TABLE, sql)
                .map(new Func1<SqlBrite.Query, Boolean>() {
                    @Override
                    public Boolean call(SqlBrite.Query query) {
                        Cursor cursor = query.run();
                        if (cursor != null && cursor.moveToFirst()) {
                            CloseUtil.close(cursor);
                            return true;
                        } else {
                            CloseUtil.close(cursor);
                            return false;
                        }
                    }
                });
    }


    private Observable<Boolean> isExistProfile(long userid) {
        StringBuilder sql = new StringBuilder("SELECT * FROM ")
                .append(FProfile.FPROFILE_TABLE)
                .append(" WHERE ")
                .append(FProfile.COLUMN_USERID + " = ")
                .append(userid);

        return mDatabase.createQuery(FProfile.FPROFILE_TABLE, sql.toString())
                .map(new Func1<SqlBrite.Query, Boolean>() {
                    @Override
                    public Boolean call(SqlBrite.Query query) {
                        Cursor cursor = query.run();
                        if (cursor != null && cursor.moveToFirst()) {
                            CloseUtil.close(cursor);
                            return true;
                        } else {
                            CloseUtil.close(cursor);
                            return false;
                        }
                    }
                });
    }

    private long updateMsg(Boolean b, UserInfoLightweight lightweight) {
        try {
            final ContentValues values = new ContentValues();
            values.put(FProfile.COLUMN_USERID, lightweight.getUid());

            if (lightweight.getInfoJson() != null)
                values.put(FProfile.COLUMN_INFOJSON, lightweight.getInfoJson());
            if (lightweight.getTime() != -1)
                values.put(FProfile.COLUMN_TIME, lightweight.getTime());

            if (!b) {
                return mDatabase.insert(FProfile.FPROFILE_TABLE, values);
            } else {
//                return mDatabase.update(FProfile.FPROFILE_TABLE, values, FProfile.COLUMN_USERID + " = ? ", lightweight.getSUid());
                return mDatabase.update(FProfile.FPROFILE_TABLE, values, FProfile.COLUMN_USERID + " = ? ", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DBConstant.ERROR;
    }

    /**
     * 删除
     *
     * @param userid 用户ID
     * @return
     */
    public int delete(long userid) {
        return mDatabase.delete(FProfile.FPROFILE_TABLE, FProfile.COLUMN_USERID + " = ? ", String.valueOf(userid));
    }
}
