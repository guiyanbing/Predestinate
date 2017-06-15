package com.juxin.predestinate.bean.db;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;

import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.bean.db.utils.CloseUtil;
import com.juxin.predestinate.bean.db.utils.CursorUtil;
import com.juxin.predestinate.module.local.chat.inter.ChatMsgInterface;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.local.chat.utils.MessageConstant;
import com.juxin.predestinate.module.local.mail.MailSpecialID;
import com.juxin.predestinate.module.util.ByteUtil;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import rx.Observable;
import rx.Observer;
import rx.functions.Func1;

/**
 * fletter 处理
 * Created by Kind on 2017/3/28.
 */

public class DBCenterFLetter {
    private BriteDatabase mDatabase;
    private Handler handler;

    public DBCenterFLetter(BriteDatabase database, Handler handler) {
        this.mDatabase = database;
        this.handler = handler;
    }

    public long storageData(final BaseMessage message) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                BaseMessage temp = isExist(message.getWhisperID());
                if (temp == null) {//没有数据
                    insertOneLetter(message);
                    return;
                }
                if (BaseMessage.BaseMessageType.video.getMsgType() == message.getType()
                        && BaseMessage.BaseMessageType.video.getMsgType() == temp.getType()) {
                    updateOneLetter(message);
                    return;
                }

                if (!message.isSender() || (message.getcMsgID() >= temp.getcMsgID())) {
                    updateOneLetter(message);
                }
            }
        });

        return MessageConstant.OK;
    }

    /**
     * 多条消息插入
     *
     * @param list
     */
    public void insertLetter(final List<BaseMessage> list) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                BriteDatabase.Transaction transaction = mDatabase.newTransaction();
                try {
                    for (BaseMessage item : list) {
                        insertOneLetter(item);
                    }
                    transaction.markSuccessful();
                } finally {
                    transaction.end();
                }
            }
        });

    }

    /**
     * 单条消息插入
     *
     * @param baseMessage
     * @return
     */
    private long insertOneLetter(final BaseMessage baseMessage) {
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

            mDatabase.insert(FLetter.FLETTER_TABLE, values);
        } catch (Exception e) {
            e.printStackTrace();
            return MessageConstant.ERROR;
        }

        return MessageConstant.OK;
    }

    private int updateOneLetter(BaseMessage baseMessage) {
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

            mDatabase.update(FLetter.FLETTER_TABLE, values, FLetter.COLUMN_USERID + " = ? ", baseMessage.getWhisperID());
        } catch (Exception e) {
            e.printStackTrace();
            return MessageConstant.ERROR;
        }

        return MessageConstant.OK;
    }

    public int updateLetter(final BaseMessage baseMessage) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                updateOneLetter(baseMessage);
            }
        });

        //// TODO: 2017/6/15 yuchenl: ret
        return MessageConstant.OK;
    }

    public boolean updateUserInfoLightList(final List<UserInfoLightweight> lightweights) {
        if (lightweights == null || lightweights.size() <= 0) {
            return false;
        }

        handler.post(new Runnable() {
            @Override
            public void run() {
                BriteDatabase.Transaction transaction = mDatabase.newTransaction();
                try {
                    for (UserInfoLightweight temp : lightweights) {
                        updateOneUserInfoLight(temp);
                    }
                    transaction.markSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    transaction.end();
                }
            }
        });

        return true;
    }

    private long updateOneUserInfoLight(UserInfoLightweight lightweight) {
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
    /**
     * 更新个人资料
     *
     * @param lightweight
     * @return
     */
    public long updateUserInfoLight(final UserInfoLightweight lightweight) {
        if (lightweight == null) {
            return MessageConstant.ERROR;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                updateOneUserInfoLight(lightweight);
            }
        });

        //// TODO: 2017/6/15 yuchenl: ret
        return MessageConstant.OK;
    }

    public BaseMessage isExist(String userid) {
        Cursor cursor = null;
        try {
            StringBuilder sql = new StringBuilder("SELECT * FROM ").append(FLetter.FLETTER_TABLE)
                    .append(" WHERE ").append(FLetter.COLUMN_USERID + " = ?");
            cursor = mDatabase.query(sql.toString(), userid);
            if (cursor != null && cursor.moveToFirst()) {
                BaseMessage message = new BaseMessage();
                message.setWhisperID(CursorUtil.getString(cursor, FLetter.COLUMN_USERID));
                message.setInfoJson(CursorUtil.getBlobToString(cursor, FLetter.COLUMN_INFOJSON));
                message.setType(CursorUtil.getInt(cursor, FLetter.COLUMN_TYPE));
                message.setKfID(CursorUtil.getInt(cursor, FLetter.COLUMN_KFID));
                message.setStatus(CursorUtil.getInt(cursor, FLetter.COLUMN_STATUS));
                message.setcMsgID(CursorUtil.getLong(cursor, FLetter.COLUMN_CMSGID));
                message.setRu(CursorUtil.getInt(cursor, FLetter.COLUMN_RU));
                message.setTime(CursorUtil.getLong(cursor, FLetter.COLUMN_TIME));
                message.setJsonStr(CursorUtil.getBlobToString(cursor, FLetter.COLUMN_CONTENT));
                return message;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(cursor);
        }
        return null;
    }


    //// TODO: 2017/6/15 yuchenl: need refact
    public Observable<BaseMessage> isExistEx(String userid) {
        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(FLetter.FLETTER_TABLE)
                .append(" WHERE ").append(FLetter.COLUMN_USERID + " = ").append(userid);

        return mDatabase.createQuery(FLetter.COLUMN_USERID, sql.toString())
                .map(new Func1<SqlBrite.Query, BaseMessage>() {
                    @Override
                    public BaseMessage call(SqlBrite.Query query) {
                        Cursor cursor = null;
                        try {
                            cursor = query.run();
                            if (cursor != null && cursor.moveToFirst()) {
                                BaseMessage message = new BaseMessage();
                                message.setWhisperID(CursorUtil.getString(cursor, FLetter.COLUMN_USERID));
                                message.setInfoJson(CursorUtil.getBlobToString(cursor, FLetter.COLUMN_INFOJSON));
                                message.setType(CursorUtil.getInt(cursor, FLetter.COLUMN_TYPE));
                                message.setKfID(CursorUtil.getInt(cursor, FLetter.COLUMN_KFID));
                                message.setStatus(CursorUtil.getInt(cursor, FLetter.COLUMN_STATUS));
                                message.setcMsgID(CursorUtil.getLong(cursor, FLetter.COLUMN_CMSGID));
                                message.setRu(CursorUtil.getInt(cursor, FLetter.COLUMN_RU));
                                message.setTime(CursorUtil.getLong(cursor, FLetter.COLUMN_TIME));
                                message.setJsonStr(CursorUtil.getBlobToString(cursor, FLetter.COLUMN_CONTENT));
                                return message;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            CloseUtil.close(cursor);
                        }
                        return null;
                    }
                });
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
                        return convert(query);
                    }
                });
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private List<BaseMessage> convert(SqlBrite.Query query) {
        Cursor cursor = null;
        ArrayList<BaseMessage> result = new ArrayList<>();
        try {
            cursor = query.run();
            while (cursor != null && cursor.moveToNext()) {
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
    public int delete(final long whisperID) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                mDatabase.delete(FLetter.FLETTER_TABLE, FLetter.COLUMN_USERID + " = ? ", String.valueOf(whisperID));
            }
        });
        return MessageConstant.OK;
    }

    /**
     * 更新成内容
     *
     * @param userid
     * @return
     */
    public long updateContent(final String userid) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                ContentValues values = new ContentValues();
                values.put(FLetter.COLUMN_CONTENT, new byte[0]);
                values.put(FLetter.COLUMN_TYPE, 0);
                values.put(FLetter.COLUMN_TIME, 0);
                values.put(FLetter.COLUMN_STATUS, 0);
                values.put(FLetter.COLUMN_CMSGID, 0);
                mDatabase.update(FLetter.FLETTER_TABLE, values, FLetter.COLUMN_USERID + " = ? ", userid);
            }
        });

        return MessageConstant.OK;
    }

    public long updateStatus(final long userID) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                ContentValues values = new ContentValues();
                values.put(FLetter.COLUMN_STATUS, String.valueOf(MessageConstant.READ_STATUS));
                mDatabase.update(FLetter.FLETTER_TABLE, values, FLetter.COLUMN_USERID + " = ? AND "
                        + FLetter.COLUMN_STATUS + " = ?", String.valueOf(userID), String.valueOf(MessageConstant.OK_STATUS));
            }
        });

        return MessageConstant.OK;
    }

    public long updateStatusFail() {

        handler.post(new Runnable() {
            @Override
            public void run() {
                ContentValues values = new ContentValues();
                values.put(FLetter.COLUMN_STATUS, String.valueOf(MessageConstant.FAIL_STATUS));
                mDatabase.update(FLetter.FLETTER_TABLE, values, FLetter.COLUMN_STATUS + " = ?", String.valueOf(MessageConstant.SENDING_STATUS));
            }
        });

        return MessageConstant.OK;
    }


    public long updateMsgStatus(final BaseMessage message) {
        final String userID = message.getWhisperID();
        handler.post(new Runnable() {
            @Override
            public void run() {
                BaseMessage temp = isExist(message.getWhisperID());
                if (temp == null) {
                    return; //没有数据
                }

                if (BaseMessage.BaseMessageType.video.getMsgType() == message.getType()
                        && BaseMessage.BaseMessageType.video.getMsgType() == temp.getType() ) {
                    updateStatus(userID, message.getStatus());
                } else if(!message.isSender() || (message.getcMsgID() >= temp.getcMsgID() ) ){
                    updateStatus(userID, message.getStatus());
                }
            }
        });

        return MessageConstant.OK;
    }
    /**
     * 发送成功或失败更新状态
     *
     * @param userID
     * @param status
     * @return
     */
    private long updateStatus(String userID, int status) {
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
                        return convertFletter(query);
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
                        return convertFletter(query);
                    }
                });
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private List<BaseMessage> convertFletter(SqlBrite.Query query) {
        Cursor cursor = null;
        ArrayList<BaseMessage> result = new ArrayList<>();
        try {
            cursor = query.run();
            while (cursor != null && cursor.moveToNext()) {
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
