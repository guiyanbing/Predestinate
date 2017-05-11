package com.juxin.predestinate.bean.db;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Build;
import android.text.TextUtils;
import com.juxin.mumu.bean.utils.ByteUtil;
import com.juxin.predestinate.bean.db.utils.CloseUtil;
import com.juxin.predestinate.bean.db.utils.CursorUtil;
import com.juxin.predestinate.bean.db.utils.DBConstant;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
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


            final ContentValues values = new ContentValues();
            if (baseMessage.getMsgID() != -1)
                values.put(FMessage.COLUMN_MSGID, baseMessage.getMsgID());

            if (baseMessage.getStatus() != -1)
                values.put(FMessage.COLUMN_STATUS, baseMessage.getStatus());

            if (baseMessage.getTime() != -1)
                values.put(FMessage.COLUMN_TIME, baseMessage.getTime());

            if (baseMessage.getJsonStr() != null)
                values.put(FMessage.COLUMN_CONTENT, ByteUtil.toBytesUTF(baseMessage.getJsonStr()));
            return mDatabase.update(FMessage.FMESSAGE_TABLE, values, sql, str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DBConstant.ERROR;
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
                .append(" WHERE ")
                .append(FMessage.COLUMN_CHANNELID + " = ")
                .append(channelID)
                .append(" AND ")
                .append(FMessage.COLUMN_WHISPERID + " = ")
                .append(whisperID)
                .append(" ORDER BY ")
                .append(FMessage._ID)
                .append(" DESC")
                .append(" LIMIT ")
                .append(start + "," + offset);
        return queryBySqlFmessage(sql.toString());
    }


    /**
     * 查询
     * @param sql
     * @return
     */
    public Observable<List<BaseMessage>> queryBySqlFmessage(String sql) {
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
        ArrayList<BaseMessage> result = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                result.add(new BaseMessage(
                        CursorUtil.getString(cursor, FMessage.COLUMN_CHANNELID),
                        CursorUtil.getString(cursor, FMessage.COLUMN_WHISPERID),
                        CursorUtil.getLong(cursor, FMessage.COLUMN_SENDID),
                        CursorUtil.getLong(cursor, FMessage.COLUMN_MSGID),
                        CursorUtil.getLong(cursor, FMessage.COLUMN_CMSGID),
                        CursorUtil.getInt(cursor, FMessage.COLUMN_TYPE),
                        CursorUtil.getInt(cursor, FMessage.COLUMN_STATUS),
                        CursorUtil.getInt(cursor, FMessage.COLUMN_FSTATUS),
                        CursorUtil.getLong(cursor, FMessage.COLUMN_TIME),
                        CursorUtil.getString(cursor, FMessage.COLUMN_CONTENT)
                ));
            }
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
