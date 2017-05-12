package com.juxin.predestinate.bean.db;

import android.text.TextUtils;
import com.juxin.predestinate.bean.db.utils.DBConstant;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.squareup.sqlbrite.BriteDatabase;
import java.util.List;
import rx.Observable;

/**
 * Created by Kind on 2017/3/28.
 * TODO 线程问题
 */

public class DBCenter {

    private BriteDatabase mDatabase;
    private DBCenterFLetter centerFLetter;
    private DBCenterFMessage centerFmessage;

    public DBCenter(BriteDatabase database) {
        this.mDatabase = database;
        centerFLetter = new DBCenterFLetter(database);
        centerFmessage = new DBCenterFMessage(database);
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
    public int delete(long whisperID) {
        return centerFmessage.delete(whisperID);
    }

    /********************FMessage end **************************/
}
