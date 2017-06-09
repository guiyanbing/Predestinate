package com.juxin.predestinate.bean.db;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.bean.db.utils.CloseUtil;
import com.juxin.predestinate.bean.db.utils.CursorUtil;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.local.chat.utils.MessageConstant;
import com.juxin.predestinate.module.local.mail.MailSpecialID;
import com.juxin.predestinate.module.util.ByteUtil;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.functions.Func1;

/**
 * fletter 处理
 * Created by Kind on 2017/3/28.
 */

public class DBCenterFLetter {
    private BriteDatabase mDatabase;

    public DBCenterFLetter(BriteDatabase database) {
        this.mDatabase = database;
    }

    public long storageData(final BaseMessage message) {
        BaseMessage temp = isExist(message.getWhisperID());
        if (temp == null) {//没有数据
            return insertLetter(message);
        } else {
            if(BaseMessage.BaseMessageType.video.getMsgType() == message.getType()
                    && BaseMessage.BaseMessageType.video.getMsgType() == temp.getType()){
                return updateLetter(message);
            }else {
                if(!message.isSender() || (message.getcMsgID() >= temp.getcMsgID())){
                    return updateLetter(message);
                }
            }

            return MessageConstant.OK;
        }
    }

    /**
     * 多条消息插入
     *
     * @param list
     */
    public void insertLetter(List<BaseMessage> list) {
        BriteDatabase.Transaction transaction = mDatabase.newTransaction();
        try {
            for (BaseMessage item : list) {
                insertLetter(item);
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    /**
     * 单条消息插入
     *
     * @param baseMessage
     * @return
     */
    public long insertLetter(BaseMessage baseMessage) {
        if (baseMessage == null) {
            return MessageConstant.ERROR;
        }

        try {
            final ContentValues values = new ContentValues();
            values.put(FLetter.COLUMN_USERID, baseMessage.getWhisperID());

            if (TextUtils.isEmpty(baseMessage.getInfoJson()))
                values.put(FLetter.COLUMN_INFOJSON, ByteUtil.toBytesUTF(baseMessage.getInfoJson()));

            if (baseMessage.getKfID() != -1)
                values.put(FLetter.COLUMN_KFID, baseMessage.getKfID());

            if (baseMessage.getRu() != -1)
                values.put(FLetter.COLUMN_RU, baseMessage.getRu());

            values.put(FLetter.COLUMN_TYPE, baseMessage.getType());
            values.put(FLetter.COLUMN_STATUS, baseMessage.getStatus());

            if (baseMessage.getcMsgID() != -1)
                values.put(FLetter.COLUMN_CMSGID, baseMessage.getcMsgID());

            values.put(FLetter.COLUMN_TIME, baseMessage.getTime());
            values.put(FLetter.COLUMN_CONTENT, ByteUtil.toBytesUTF(baseMessage.getJsonStr()));

            return mDatabase.insert(FLetter.FLETTER_TABLE, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MessageConstant.ERROR;
    }

    public int updateLetter(BaseMessage baseMessage) {
        if (baseMessage == null) {
            return MessageConstant.ERROR;
        }
        try {
            final ContentValues values = new ContentValues();
            if (baseMessage.getInfoJson() != null)
                values.put(FLetter.COLUMN_INFOJSON, ByteUtil.toBytesUTF(baseMessage.getInfoJson()));

            if (baseMessage.getType() != -1)
                values.put(FLetter.COLUMN_TYPE, baseMessage.getType());

            if (baseMessage.getStatus() != -1)
                values.put(FLetter.COLUMN_STATUS, baseMessage.getStatus());

            long cMsgID = baseMessage.getcMsgID();
            if (cMsgID != -1)
                values.put(FLetter.COLUMN_CMSGID, cMsgID);

            if (baseMessage.getRu() == MessageConstant.Ru_Friend)
                values.put(FLetter.COLUMN_RU, baseMessage.getRu());

            if (baseMessage.getTime() != -1)
                values.put(FLetter.COLUMN_TIME, baseMessage.getTime());

            values.put(FLetter.COLUMN_CONTENT, ByteUtil.toBytesUTF(baseMessage.getJsonStr()));

            return mDatabase.update(FLetter.FLETTER_TABLE, values, FLetter.COLUMN_USERID + " = ? ", baseMessage.getWhisperID());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MessageConstant.ERROR;
    }

    public boolean updateUserInfoLightList(List<UserInfoLightweight> lightweights) {
        if (lightweights == null || lightweights.size() <= 0) {
            return false;
        }
        BriteDatabase.Transaction transaction = mDatabase.newTransaction();
        try {
            for (UserInfoLightweight temp : lightweights) {
                updateUserInfoLight(temp);
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
     * 更新个人资料
     *
     * @param lightweight
     * @return
     */
    public long updateUserInfoLight(UserInfoLightweight lightweight) {
        if (lightweight == null) {
            return MessageConstant.ERROR;
        }
        try {
            if (isExist(String.valueOf(lightweight.getUid())) == null)
                return MessageConstant.ERROR;//没有数据

            final ContentValues values = new ContentValues();
            if (lightweight.getInfoJson() != null)
                values.put(FLetter.COLUMN_INFOJSON, ByteUtil.toBytesUTF(lightweight.getInfoJson()));

            return mDatabase.update(FLetter.FLETTER_TABLE, values, FLetter.COLUMN_USERID + " = ? ", String.valueOf(lightweight.getUid()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MessageConstant.ERROR;
    }

    private BaseMessage isExist(String userid) {
        Cursor cursor = null;
        try {
            StringBuilder sql = new StringBuilder("SELECT * FROM ").append(FLetter.FLETTER_TABLE)
                    .append(" WHERE ")
                    .append(FLetter.COLUMN_USERID + " = ?");
            cursor = mDatabase.query(sql.toString(), userid);
            if (cursor != null && cursor.moveToFirst()) {
                Bundle bundle = new Bundle();
                bundle.putLong(FLetter._ID, CursorUtil.getLong(cursor, FMessage._ID));
                bundle.putString(FLetter.COLUMN_USERID, CursorUtil.getString(cursor, FLetter.COLUMN_USERID));
                bundle.putString(FLetter.COLUMN_INFOJSON, CursorUtil.getBlobToString(cursor, FLetter.COLUMN_INFOJSON));
                bundle.putInt(FLetter.COLUMN_TYPE, CursorUtil.getInt(cursor, FLetter.COLUMN_TYPE));
                bundle.putInt(FLetter.COLUMN_KFID, CursorUtil.getInt(cursor, FLetter.COLUMN_KFID));
                bundle.putInt(FLetter.COLUMN_STATUS, CursorUtil.getInt(cursor, FLetter.COLUMN_STATUS));
                bundle.putLong(FLetter.COLUMN_CMSGID, CursorUtil.getLong(cursor, FLetter.COLUMN_CMSGID));
                bundle.putInt(FLetter.COLUMN_RU, CursorUtil.getInt(cursor, FLetter.COLUMN_RU));
                bundle.putLong(FLetter.COLUMN_TIME, CursorUtil.getLong(cursor, FLetter.COLUMN_TIME));
                bundle.putString(FLetter.COLUMN_CONTENT, CursorUtil.getBlobToString(cursor, FLetter.COLUMN_CONTENT));

                return BaseMessage.parseToLetterMessage(bundle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(cursor);
        }
        return null;
    }

    public Observable<Boolean> isHaveMsg(String userid) {
        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(FLetter.FLETTER_TABLE)
                .append(" WHERE ")
                .append(FLetter.COLUMN_USERID + " = ")
                .append(userid);

        return mDatabase.createQuery(FLetter.COLUMN_USERID, sql.toString())
                .map(new Func1<SqlBrite.Query, Boolean>() {
                    @Override
                    public Boolean call(SqlBrite.Query query) {
                        Cursor cursor = null;
                        try {
                            cursor = query.run();
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
                });
    }

    /**
     * 聊天记录
     *
     * @return
     */
    public Observable<List<BaseMessage>> queryLetterList() {
        String sql = "select f._id, f.userID, f.infoJson, f.type, f.kfID, f.status, f.cMsgID, f.ru, f.time, f.content, " +
                "m.whisperID, m.num from " + FLetter.FLETTER_TABLE + " f left join (select whisperID,count(*) " +
                "num from " + FMessage.FMESSAGE_TABLE + " where status = 10 group by whisperID) m on f.userID = m.whisperID";
        return queryBySqlFletter(sql);
    }

    /**
     * 查询 列表
     *
     * @param sql
     * @return
     */
    public Observable<List<BaseMessage>> queryBySqlFletter(String sql) {
        return mDatabase.createQuery(FLetter.FLETTER_TABLE, sql)
                .map(new Func1<SqlBrite.Query, List<BaseMessage>>() {
                    @Override
                    public List<BaseMessage> call(SqlBrite.Query query) {
                        return convert(query.run());
                    }
                });
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private List<BaseMessage> convert(Cursor cursor) {
        if (null == cursor) {
            return null;
        }
        ArrayList<BaseMessage> result = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                Bundle bundle = new Bundle();
                bundle.putLong(FLetter._ID, CursorUtil.getLong(cursor, FMessage._ID));
                bundle.putString(FLetter.COLUMN_USERID, CursorUtil.getString(cursor, FLetter.COLUMN_USERID));
                bundle.putString(FLetter.COLUMN_INFOJSON, CursorUtil.getBlobToString(cursor, FLetter.COLUMN_INFOJSON));
                bundle.putInt(FLetter.COLUMN_TYPE, CursorUtil.getInt(cursor, FLetter.COLUMN_TYPE));
                bundle.putInt(FLetter.COLUMN_KFID, CursorUtil.getInt(cursor, FLetter.COLUMN_KFID));
                bundle.putInt(FLetter.COLUMN_STATUS, CursorUtil.getInt(cursor, FLetter.COLUMN_STATUS));
                bundle.putLong(FLetter.COLUMN_CMSGID, CursorUtil.getLong(cursor, FLetter.COLUMN_CMSGID));
                bundle.putInt(FLetter.COLUMN_RU, CursorUtil.getInt(cursor, FLetter.COLUMN_RU));
                bundle.putLong(FLetter.COLUMN_TIME, CursorUtil.getLong(cursor, FLetter.COLUMN_TIME));
                bundle.putString(FLetter.COLUMN_CONTENT, CursorUtil.getBlobToString(cursor, FLetter.COLUMN_CONTENT));
                bundle.putInt(FLetter.Num, CursorUtil.getInt(cursor, FLetter.Num));

                result.add(BaseMessage.parseToLetterMessage(bundle));
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(cursor);
        }
        return result;
    }

    /**
     * 删除
     *
     * @param whisperID 私聊ID
     * @return
     */
    public int delete(long whisperID) {
        return mDatabase.delete(FLetter.FLETTER_TABLE, FLetter.COLUMN_USERID + " = ? ", String.valueOf(whisperID));
    }

    /**
     * 更新成内容
     *
     * @param userid
     * @return
     */
    public long updateContent(String userid) {
        ContentValues values = new ContentValues();
        values.put(FLetter.COLUMN_CONTENT, new byte[0]);
        values.put(FLetter.COLUMN_TYPE, 0);
        values.put(FLetter.COLUMN_TIME, 0);
        values.put(FLetter.COLUMN_STATUS, 0);
        values.put(FLetter.COLUMN_CMSGID, 0);
        return mDatabase.update(FLetter.FLETTER_TABLE, values, FLetter.COLUMN_USERID + " = ? ", userid);
    }

    public long updateStatus(long userID) {
        ContentValues values = new ContentValues();
        values.put(FLetter.COLUMN_STATUS, String.valueOf(MessageConstant.READ_STATUS));
        return mDatabase.update(FLetter.FLETTER_TABLE, values, FLetter.COLUMN_USERID + " = ? AND "
                + FLetter.COLUMN_STATUS + " = ?", String.valueOf(userID), String.valueOf(MessageConstant.OK_STATUS));
    }

    public long updateStatusFail() {
        ContentValues values = new ContentValues();
        values.put(FLetter.COLUMN_STATUS, String.valueOf(MessageConstant.FAIL_STATUS));
        return mDatabase.update(FLetter.FLETTER_TABLE, values, FLetter.COLUMN_STATUS + " = ?", String.valueOf(MessageConstant.SENDING_STATUS));
    }

    /**
     * 发送成功或失败更新状态
     *
     * @param userID
     * @param status
     * @return
     */
    public long updateStatus(String userID, int status) {
        ContentValues values = new ContentValues();
        values.put(FLetter.COLUMN_STATUS, String.valueOf(status));
        return mDatabase.update(FLetter.FLETTER_TABLE, values, FLetter.COLUMN_USERID + " = ?", userID);
    }

    public Observable<List<BaseMessage>> deleteCommon(long delTime) {
        final StringBuilder sql = new StringBuilder("SELECT * FROM ").append(FLetter.FLETTER_TABLE)
                .append(" WHERE ")
                .append(FLetter.COLUMN_TIME + " < ")
                .append(delTime);

        return mDatabase.createQuery(FLetter.FLETTER_TABLE, sql.toString())
                .map(new Func1<SqlBrite.Query, List<BaseMessage>>() {
                    @Override
                    public List<BaseMessage> call(SqlBrite.Query query) {
                        return convertFletter(query.run());
                    }
                });
    }

    public Observable<List<BaseMessage>> deleteKFID() {
        final StringBuilder sql = new StringBuilder("SELECT * FROM ").append(FLetter.FLETTER_TABLE)
                .append(" WHERE ")
                .append(FLetter.COLUMN_KFID + " = ")
                .append(MessageConstant.KF_ID)
                .append(" AND ")
                .append(FLetter.COLUMN_USERID + " <> ")
                .append(MailSpecialID.customerService.getSpecialID());

        return mDatabase.createQuery(FLetter.FLETTER_TABLE, sql.toString())
                .map(new Func1<SqlBrite.Query, List<BaseMessage>>() {
                    @Override
                    public List<BaseMessage> call(SqlBrite.Query query) {
                        return convertFletter(query.run());
                    }
                });
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private List<BaseMessage> convertFletter(Cursor cursor) {
        if (null == cursor) {
            return null;
        }
        ArrayList<BaseMessage> result = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                BaseMessage message = new BaseMessage();
                message.setWhisperID(CursorUtil.getString(cursor, FLetter.COLUMN_USERID));
                message.setKfID(CursorUtil.getInt(cursor, FLetter.COLUMN_KFID));
                message.setTime(CursorUtil.getLong(cursor, FLetter.COLUMN_TIME));
                result.add(message);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(cursor);
        }
        return result;
    }
}
