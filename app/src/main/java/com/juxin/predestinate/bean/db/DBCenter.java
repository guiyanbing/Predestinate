package com.juxin.predestinate.bean.db;

import android.text.TextUtils;

import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweightList;
import com.juxin.predestinate.bean.db.cache.DBCacheCenter;
import com.juxin.predestinate.bean.db.utils.DBConstant;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.squareup.sqlbrite.BriteDatabase;
import java.util.List;
import java.util.Map;
import rx.Observable;

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

    public long insertUnRead(String key, String content){
        if (TextUtils.isEmpty(key)) return DBConstant.ERROR;
        return centerFUnRead.storageData(key, content);
    }

    /**
     * 单个查询
     * @param key
     * @return
     */
    public Observable<String> queryUnRead(String key) {
        return centerFUnRead.queryUnRead(key);
    }

    public Observable<Map<String, String>> queryUnReadList() {
        return centerFUnRead.queryUnReadList();
    }

    public long deleteUnRead(String key){
        return centerFUnRead.delete(key);
    }

    /******************** FLetter **************************/
    public Observable<List<BaseMessage>> queryLetterList() {
        return centerFLetter.queryLetterList();
    }

    public long insertMsg(BaseMessage baseMessage){
        if (TextUtils.isEmpty(baseMessage.getWhisperID())) return DBConstant.ERROR;

        long ret = centerFLetter.storageData(baseMessage);
        if(ret == DBConstant.ERROR) return DBConstant.ERROR;

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
        if(ret != DBConstant.ERROR){
            return deleteFmessage(userID);
        }
       return ret;
    }

    /******************** FMessage **************************/

    /**
     * 多条消息插入
     * @param list
     * @return
     */
    public void insertFmessage(List<BaseMessage> list){
        centerFmessage.insertMsg(list);
    }

    /**
     * 单条消息插入
     * @param baseMessage
     * @return
     */
    public long insertFmessage(BaseMessage baseMessage){
       return centerFmessage.insertMsg(baseMessage);
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
     * 聊天记录
     *
     * @param channelID
     * @param whisperID
     * @param start
     * @param offset
     * @return
     */
    public Observable<List<BaseMessage>> queryFmessageList(String channelID, String whisperID, int start, int offset) {
        return centerFmessage.queryMsgList(channelID, whisperID, start, offset);
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
