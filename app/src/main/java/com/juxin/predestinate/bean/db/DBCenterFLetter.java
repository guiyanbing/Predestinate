package com.juxin.predestinate.bean.db;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Build;
import android.text.TextUtils;

import com.juxin.library.log.PLogger;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweightList;
import com.juxin.predestinate.bean.db.utils.CloseUtil;
import com.juxin.predestinate.bean.db.utils.CursorUtil;
import com.juxin.predestinate.bean.db.utils.DBConstant;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
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

    public long storageData(BaseMessage message){
       String whisperID = message.getWhisperID();
        if (!isExist(whisperID)) {//没有数据
            return insertLetter(message);
        } else {
            return updateLetter(message);
        }
    }

    /**
     * 多条消息插入
     * @param list
     */
    public void insertLetter(List<BaseMessage> list){
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
     * @param baseMessage
     * @return
     */
    public long insertLetter(BaseMessage baseMessage) {
        if (baseMessage == null) {
            return DBConstant.ERROR;
        }

        PLogger.d("Fl=== + insertLetter");
        try {
            final ContentValues values = new ContentValues();
            values.put(FLetter.COLUMN_USERID, baseMessage.getWhisperID());

            if(TextUtils.isEmpty(baseMessage.getInfoJson()))
                 values.put(FLetter.COLUMN_INFOJSON, ByteUtil.toBytesUTF(baseMessage.getInfoJson()));

            if (baseMessage.getKfID() != -1)
                values.put(FLetter.COLUMN_KFID, baseMessage.getKfID());

            values.put(FLetter.COLUMN_TYPE, baseMessage.getType());
            values.put(FLetter.COLUMN_STATUS, baseMessage.getStatus());// 1.发送成功2.发送失败3.发送中 10.未读11.已读
            values.put(FLetter.COLUMN_TIME, baseMessage.getTime());
            values.put(FLetter.COLUMN_CONTENT, ByteUtil.toBytesUTF(baseMessage.getJsonStr()));

            return mDatabase.insert(FLetter.FLETTER_TABLE, values);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return DBConstant.ERROR;
    }

    public int updateLetter(BaseMessage baseMessage){
        if(baseMessage == null){
            return DBConstant.ERROR;
        }
        PLogger.d("Fl=== + updateLetter");
        try {
            final ContentValues values = new ContentValues();
            if (baseMessage.getStatus() != -1)
                values.put(FLetter.COLUMN_STATUS, baseMessage.getStatus());

            if (baseMessage.getTime() != -1)
                values.put(FLetter.COLUMN_TIME, baseMessage.getTime());

            if (baseMessage.getJsonStr() != null)
                values.put(FLetter.COLUMN_INFOJSON, ByteUtil.toBytesUTF(baseMessage.getInfoJson()));
            values.put(FLetter.COLUMN_CONTENT, ByteUtil.toBytesUTF(baseMessage.getJsonStr()));

            return mDatabase.update(FLetter.FLETTER_TABLE, values, FLetter.COLUMN_USERID +  " = ? ", baseMessage.getWhisperID());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DBConstant.ERROR;
    }

    /**
     * 更新个人资料
     * @param lightweight
     * @return
     */
    public long updateUserInfoLight(UserInfoLightweight lightweight){
        if(lightweight == null){
            return DBConstant.ERROR;
        }
        try {
            if (!isExist(String.valueOf(lightweight.getUid()))) return DBConstant.ERROR;//没有数据

            final ContentValues values = new ContentValues();
            if (lightweight.getInfoJson() != null)
                values.put(FLetter.COLUMN_INFOJSON, ByteUtil.toBytesUTF(lightweight.getInfoJson()));

            return mDatabase.update(FLetter.FLETTER_TABLE, values, FLetter.COLUMN_USERID +  " = ? ", String.valueOf(lightweight.getUid()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DBConstant.ERROR;
    }


    private boolean isExist(String userid) {
        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(FLetter.FLETTER_TABLE)
                .append(" WHERE ")
                .append(FLetter.COLUMN_USERID + " = ?");
               // .append(userid);
        PLogger.printObject("isExist-onStart");
        Cursor cursor = mDatabase.query(sql.toString(), userid);
        PLogger.printObject("isExist-onEnd");
        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else {
            if (cursor != null) cursor.close();
            return false;
        }
    }

    /**
     * 聊天记录
     *
     * @return
     */
    public Observable<List<BaseMessage>> queryLetterList() {
        String sql = "select f._id, f.userID, f.infoJson, f.type, f.kfID, f.status, f.time, f.content, " +
                "m.whisperID, m.num from " + FLetter.FLETTER_TABLE + " f left join (select whisperID,count(*) " +
                "num from " + FMessage.FMESSAGE_TABLE + " where status = 10 group by whisperID) m on f.userID = m.whisperID";
        return queryBySqlFletter(sql);
    }


    /**
     * 查询 列表
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
                result.add(new BaseMessage(
                        CursorUtil.getLong(cursor, FLetter._ID),
                        CursorUtil.getString(cursor, FLetter.COLUMN_USERID),
                        CursorUtil.getBlobToString(cursor, FLetter.COLUMN_INFOJSON),
                        CursorUtil.getInt(cursor, FLetter.COLUMN_TYPE),
                        CursorUtil.getInt(cursor, FLetter.COLUMN_KFID),
                        CursorUtil.getInt(cursor, FLetter.COLUMN_STATUS),
                        CursorUtil.getLong(cursor, FLetter.COLUMN_TIME),
                        CursorUtil.getBlobToString(cursor, FLetter.COLUMN_CONTENT),
                        CursorUtil.getInt(cursor, FLetter.Num)
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
        return mDatabase.delete(FLetter.FLETTER_TABLE, FLetter.COLUMN_USERID + " = ? ", String.valueOf(whisperID));
    }
}
