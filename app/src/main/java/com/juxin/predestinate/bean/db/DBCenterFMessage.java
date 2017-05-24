package com.juxin.predestinate.bean.db;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Build;
import android.text.TextUtils;

import com.juxin.library.log.PLogger;
import com.juxin.predestinate.bean.db.utils.CloseUtil;
import com.juxin.predestinate.bean.db.utils.CursorUtil;
import com.juxin.predestinate.bean.db.utils.DBConstant;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.util.ByteUtil;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.functions.Func1;

/**
 * fmessage 处理
 * Created by Kind on 2017/3/28.
 */

public class DBCenterFMessage {
    private BriteDatabase mDatabase;

    public DBCenterFMessage(BriteDatabase database) {
        this.mDatabase = database;
    }

    /**
     * 多条消息插入
     * @param list
     */
    public void insertMsg(List<BaseMessage> list){
        BriteDatabase.Transaction transaction = mDatabase.newTransaction();
        try {
            for (BaseMessage item : list) {
                insertMsg(item);
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    /**
     * 单条消息插入
     * @param baseMessage
     * @return
     */
    public long insertMsg(BaseMessage baseMessage) {
        if (baseMessage == null) {
            return DBConstant.ERROR;
        }

        try {
            final ContentValues values = new ContentValues();
            if (baseMessage.getChannelID() != null)
                values.put(FMessage.COLUMN_CHANNELID, baseMessage.getChannelID());

            if (baseMessage.getWhisperID() != null)
                values.put(FMessage.COLUMN_WHISPERID, baseMessage.getWhisperID());

            if (baseMessage.getMsgID() != -1)
                values.put(FMessage.COLUMN_MSGID, baseMessage.getMsgID());

            if (baseMessage.getcMsgID() != -1)
                values.put(FMessage.COLUMN_CMSGID, baseMessage.getcMsgID());

            values.put(FMessage.COLUMN_SENDID, baseMessage.getSendID());
            values.put(FMessage.COLUMN_TYPE, baseMessage.getType());
            values.put(FMessage.COLUMN_STATUS, baseMessage.getStatus());// 1.发送成功2.发送失败3.发送中 10.未读11.已读
            values.put(FMessage.COLUMN_FSTATUS, 1);// 默认为1插入的时候
            values.put(FMessage.COLUMN_TIME, baseMessage.getTime());
            values.put(FMessage.COLUMN_CONTENT, ByteUtil.toBytesUTF(baseMessage.getJsonStr()));

            return mDatabase.insert(FMessage.FMESSAGE_TABLE, values);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return DBConstant.ERROR;
    }

    /**
     * 更新fmessage
     * @param baseMessage
     * @return
     */
    public int updateMsg(BaseMessage baseMessage){
        if(baseMessage == null){
            return DBConstant.ERROR;
        }

        try {
            String channelID = baseMessage.getChannelID();
            String whisperID = baseMessage.getWhisperID();
            long cMsgID = baseMessage.getcMsgID();

            String sql;
            String[] str;
            if (!TextUtils.isEmpty(channelID) && !TextUtils.isEmpty(whisperID)) {
                sql = FMessage.COLUMN_CHANNELID + " = ? AND " + FMessage.COLUMN_WHISPERID + " = ? AND " + FMessage.COLUMN_CMSGID + " = ?";
                str = new String[]{channelID, whisperID, String.valueOf(cMsgID)};
            } else if (!TextUtils.isEmpty(channelID)) {
                sql = FMessage.COLUMN_CHANNELID + " = ? AND " + FMessage.COLUMN_CMSGID + " = ?";
                str = new String[]{channelID, String.valueOf(cMsgID)};
            } else {
                sql = FMessage.COLUMN_WHISPERID + " = ? AND " + FMessage.COLUMN_CMSGID + " = ?";
                str = new String[]{whisperID, String.valueOf(cMsgID)};
            }

            ContentValues values = new ContentValues();
            if (baseMessage.getMsgID() != -1)
                values.put(FMessage.COLUMN_MSGID, baseMessage.getMsgID());

            if (baseMessage.getStatus() != -1)
                values.put(FMessage.COLUMN_STATUS, baseMessage.getStatus());

            if (baseMessage.getTime() != -1)
                values.put(FMessage.COLUMN_TIME, baseMessage.getTime());

            if (baseMessage.getfStatus() != -1)
                values.put(FMessage.COLUMN_FSTATUS, baseMessage.getfStatus());

            if (baseMessage.getJsonStr() != null)
                values.put(FMessage.COLUMN_CONTENT, ByteUtil.toBytesUTF(baseMessage.getJsonStr()));
            return mDatabase.update(FMessage.FMESSAGE_TABLE, values, sql, str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DBConstant.ERROR;
    }

    public long updateToReadAll(){
        ContentValues values = new ContentValues();
        values.put(FMessage.COLUMN_STATUS, String.valueOf(DBConstant.READ_STATUS));
        return mDatabase.update(FMessage.FMESSAGE_TABLE, values, FMessage.COLUMN_STATUS + " = ?", String.valueOf(DBConstant.UNREAD_STATUS));
    }
    /**
     * 更新已读消息
     * @param channelID
     * @param userID
     * @return
     */
    public long updateToRead(String channelID, String userID){
        try {
            String sql;
            String[] str;
            if (!TextUtils.isEmpty(channelID) && !TextUtils.isEmpty(userID)) {
                sql = FMessage.COLUMN_CHANNELID + " = ? AND " + FMessage.COLUMN_WHISPERID + " = ? AND " + FMessage.COLUMN_STATUS + " = ?";
                str = new String[]{channelID, userID, String.valueOf(DBConstant.UNREAD_STATUS)};
            } else if (!TextUtils.isEmpty(channelID)) {
                sql = FMessage.COLUMN_CHANNELID + " = ? AND " + FMessage.COLUMN_STATUS + " = ?";
                str = new String[]{channelID, String.valueOf(DBConstant.UNREAD_STATUS)};
            } else {
                sql = FMessage.COLUMN_WHISPERID + " = ? AND " + FMessage.COLUMN_STATUS + " = ?";
                str = new String[]{userID, String.valueOf(DBConstant.UNREAD_STATUS)};
            }

            ContentValues values = new ContentValues();
            values.put(FMessage.COLUMN_STATUS, String.valueOf(DBConstant.READ_STATUS));
            return mDatabase.update(FMessage.FMESSAGE_TABLE, values, sql, str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DBConstant.ERROR;
    }


    public boolean updateToReadVoice(String channelID, String userID, String sendID) {
//        try {
//            String sql;
//            String[] str;
//            if (!TextUtils.isEmpty(channelID) && !TextUtils.isEmpty(userID)) {
//                sql = FMessage.COLUMN_CHANNELID + " = ? AND " + FMessage.COLUMN_WHISPERID + " = ? AND " + FMessage.COLUMN_STATUS + " = ? " + FMessage.COLUMN_TYPE + " = ?";
//                str = new String[]{channelID, userID, String.valueOf(DBConstant.UNREAD_STATUS)};
//            } else if (!TextUtils.isEmpty(channelID)) {
//                sql = FMessage.COLUMN_CHANNELID + " = ? AND " + FMessage.COLUMN_STATUS + " = ?";
//                str = new String[]{channelID, String.valueOf(DBConstant.UNREAD_STATUS)};
//            } else {
//                sql = FMessage.COLUMN_WHISPERID + " = ? AND " + FMessage.COLUMN_STATUS + " = ?";
//                str = new String[]{userID, String.valueOf(DBConstant.UNREAD_STATUS)};
//            }
//
//            ContentValues values = new ContentValues();
//            values.put(FMessage.COLUMN_STATUS, String.valueOf(DBConstant.READ_STATUS));
//            return mDatabase.update(FMessage.FMESSAGE_TABLE, values, sql, str);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return DBConstant.ERROR;
//
//
//        Cursor cursor = null;
//        try {
//            String sql;
//            String[] str;
//            if (!TextUtils.isEmpty(channelID) && !TextUtils.isEmpty(whisperID)) {
//                sql = "channelID = ? and whisperID = ? and sendId = ? and status != ? and type != ?";
//                str = new String[]{channelID, whisperID, sendID, String.valueOf(ChatMgr.LOCAL_READ_STATUS), String.valueOf(VOICE)};
//            } else if (!TextUtils.isEmpty(channelID)) {
//                sql = "channelID = ? and sendId = ? and status != ? and type != ?";
//                str = new String[]{channelID, sendID, String.valueOf(ChatMgr.LOCAL_READ_STATUS), String.valueOf(VOICE)};
//            } else {
//                sql = "whisperID = ? and sendId = ? and status != ? and type != ?";
//                str = new String[]{whisperID, sendID, String.valueOf(ChatMgr.LOCAL_READ_STATUS), String.valueOf(VOICE)};
//            }
//            cursor = mSQLiteDatabase.query(FMESSAGE_TABLE, null, sql, str, null, null, _ID + " desc", null);
//            return cursor.getCount() > 0;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        } finally {
//            if (cursor != null)
//                cursor.close();
//        }

        return true;
    }


    /**
     * 聊天记录
     *
     * @param channelID
     * @param whisperID
     * @param start
     * @param offset
     * @return
     */
    public Observable<List<BaseMessage>> queryMsgList(String channelID, String whisperID, int start, int offset) {
        if(TextUtils.isEmpty(channelID) && TextUtils.isEmpty(whisperID)){
            return null;
        }
        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(FMessage.FMESSAGE_TABLE)
                .append(" WHERE ");
                if(!TextUtils.isEmpty(channelID)){
                    sql.append(FMessage.COLUMN_CHANNELID + " = ")
                            .append(channelID)
                            .append(" AND ");
                }

        sql.append(FMessage.COLUMN_WHISPERID + " = ")
                .append(whisperID)
                .append(" ORDER BY ")
                .append(FMessage._ID)
                .append(" DESC")
                .append(" LIMIT ").append(start).append(",").append(offset);
        return queryBySqlFmessage(sql.toString());
    }

    /**
     * 查询
     * @param sql
     * @return
     */
    public Observable<List<BaseMessage>> queryBySqlFmessage(String sql) {
        PLogger.printObject("convert==" + "111111111");
        return mDatabase.createQuery(FMessage.FMESSAGE_TABLE, sql)
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
        PLogger.printObject("convert==" + "222222222");
        ArrayList<BaseMessage> result = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                PLogger.printObject("convert==" + "333333333");
                result.add(BaseMessage.parseToBaseMessage(
                        CursorUtil.getString(cursor, FMessage.COLUMN_CHANNELID),
                        CursorUtil.getString(cursor, FMessage.COLUMN_WHISPERID),
                        CursorUtil.getLong(cursor, FMessage.COLUMN_SENDID),
                        CursorUtil.getLong(cursor, FMessage.COLUMN_MSGID),
                        CursorUtil.getLong(cursor, FMessage.COLUMN_CMSGID),
                        CursorUtil.getLong(cursor, FMessage.COLUMN_SPECIALMSGID),
                        CursorUtil.getInt(cursor, FMessage.COLUMN_TYPE),
                        CursorUtil.getInt(cursor, FMessage.COLUMN_STATUS),
                        CursorUtil.getInt(cursor, FMessage.COLUMN_FSTATUS),
                        CursorUtil.getLong(cursor, FMessage.COLUMN_TIME),
                        CursorUtil.getBlobToString(cursor, FMessage.COLUMN_CONTENT)));
            }
            PLogger.printObject("convert==" + "4444444");
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
     * @param whisperID 私聊ID
     * @return
     */
    public int delete(long whisperID){
        return mDatabase.delete(FMessage.FMESSAGE_TABLE, FMessage.COLUMN_WHISPERID + " = ? ", String.valueOf(whisperID));
    }
}
