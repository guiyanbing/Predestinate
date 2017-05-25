package com.juxin.predestinate.bean.db;

import android.text.TextUtils;
import com.juxin.predestinate.bean.db.cache.DBCacheCenter;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.local.chat.utils.MessageConstant;
import com.squareup.sqlbrite.BriteDatabase;
/**
 * DB处理中心
 * Created by Kind on 2017/3/28.
 */
public class DBCenter {

    private BriteDatabase mDatabase;
    private DBCenterFLetter centerFLetter;
    private DBCenterFMessage centerFmessage;
    private DBCenterFUnRead centerFUnRead;
    private DBCacheCenter cacheCenter;

    public DBCenter(BriteDatabase database) {
        this.mDatabase = database;
        centerFLetter = new DBCenterFLetter(database);
        centerFmessage = new DBCenterFMessage(database);
        centerFUnRead = new DBCenterFUnRead(database);
        cacheCenter = new DBCacheCenter(database);
    }

    /******************** DBCacheCenter **************************/

    public DBCacheCenter getCacheCenter() {
        return cacheCenter;
    }


    /******************** FUnRead **************************/


    public DBCenterFUnRead getCenterFUnRead() {
        return centerFUnRead;
    }

    public long insertUnRead(String key, String content){
        if (TextUtils.isEmpty(key)) return MessageConstant.ERROR;
        return centerFUnRead.storageData(key, content);
    }

    /******************** FLetter **************************/
    public long insertMsg(BaseMessage baseMessage){
        if (TextUtils.isEmpty(baseMessage.getWhisperID())) return MessageConstant.ERROR;

        long ret = centerFLetter.storageData(baseMessage);
        if(ret == MessageConstant.ERROR) return MessageConstant.ERROR;

        return centerFmessage.insertMsg(baseMessage);
    }

    public DBCenterFLetter getCenterFLetter() {
        return centerFLetter;
    }

    /**
     * 删除消息列表及内容表的消息
     * @param userID
     * @return
     */
    public int deleteMessage(long userID) {
        int ret = centerFLetter.delete(userID);
        if(ret != MessageConstant.ERROR){
            return deleteFmessage(userID);
        }
       return ret;
    }

    /******************** FMessage **************************/

    public DBCenterFMessage getCenterFMessage() {
        return centerFmessage;
    }

    /**
     * 更新fmessage
     *
     * @param baseMessage
     * @return
     */
    public int updateFmessage(BaseMessage baseMessage) {
        return centerFmessage.updateMsg(baseMessage);
    }

    public long updateToReadAll() {
        return centerFmessage.updateToReadAll();
    }

    /**
     * 更新未读
     * @param channelID
     * @param userID
     * @return
     */
    public long updateToRead(String channelID, String userID) {
        return centerFmessage.updateToRead(channelID, userID);
    }


    /**
     * 删除
     *
     * @param whisperID 私聊ID
     * @return
     */
    public int deleteFmessage(long whisperID) {
        return centerFmessage.delete(whisperID);
    }

    /********************FMessage end **************************/
}
