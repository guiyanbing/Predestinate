package com.juxin.predestinate.bean.db;

import android.text.TextUtils;
import com.juxin.predestinate.bean.db.cache.DBCacheCenter;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.local.chat.utils.MessageConstant;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.squareup.sqlbrite.BriteDatabase;
import java.util.List;
import rx.Observable;
import rx.functions.Action1;

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

    public long insertUnRead(String key, String content) {
        if (TextUtils.isEmpty(key)) return MessageConstant.ERROR;
        return centerFUnRead.storageData(key, content);
    }

    /******************** FLetter **************************/
    public long insertMsg(BaseMessage baseMessage) {
        if (TextUtils.isEmpty(baseMessage.getWhisperID())) return MessageConstant.ERROR;

        long ret = centerFmessage.insertMsg(baseMessage);
        if (ret == MessageConstant.ERROR) return MessageConstant.ERROR;

        if(BaseMessage.BaseMessageType.hint.getMsgType() != baseMessage.getType()){
            ret = centerFLetter.storageData(baseMessage);
        }

        return ret;
    }

    public void insertListMsg(List<BaseMessage> list) {
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
     * 更新
     * @param message
     * @return
     */
    public long updateMsg(BaseMessage message) {
        String userID = message.getWhisperID();
        if (TextUtils.isEmpty(userID)) return MessageConstant.ERROR;

        if(BaseMessage.BaseMessageType.hint.getMsgType() != message.getType()){

            BaseMessage temp = centerFLetter.isExist(message.getWhisperID());
            if (temp == null) return MessageConstant.ERROR;  //没有数据

            if(BaseMessage.BaseMessageType.video.getMsgType() == message.getType()
                    && BaseMessage.BaseMessageType.video.getMsgType() == temp.getType()){
                long ret = centerFLetter.updateStatus(userID, message.getStatus());

                if (ret == MessageConstant.ERROR) return MessageConstant.ERROR;
            }else {
                if(!message.isSender() || (message.getcMsgID() >= temp.getcMsgID())){
                    long ret =  centerFLetter.updateStatus(userID, message.getStatus());
                    if (ret == MessageConstant.ERROR) return MessageConstant.ERROR;
                }
            }
        }

        return centerFmessage.updateMsg(message);
    }

    public DBCenterFLetter getCenterFLetter() {
        return centerFLetter;
    }

    /**
     * 删除消息列表及内容表的消息
     *
     * @param userID
     * @return
     */
    public int deleteMessage(long userID) {
        int ret = centerFLetter.delete(userID);
        if (ret != MessageConstant.ERROR) {
            return centerFmessage.delete(userID);
        }
        return ret;
    }

    /**
     * 删除多少个小时以前的消息
     *
     * @param hour
     * @return
     */
    public void deleteMessageHour(int hour) {
        final long delTime = ModuleMgr.getAppMgr().getTime() - (hour * 60 * 60 * 1000);
        Observable<List<BaseMessage>> observable = centerFLetter.deleteCommon(delTime);
        observable.subscribe(new Action1<List<BaseMessage>>() {
            @Override
            public void call(List<BaseMessage> baseMessages) {
                if (baseMessages == null || baseMessages.size() <= 0) {
                    return;
                }
                for (BaseMessage temp : baseMessages) {
                    if (temp.getTime() < delTime) {
                        centerFLetter.updateContent(temp.getWhisperID());
                    }
                    centerFmessage.delete(temp.getLWhisperID(), delTime);
                }
            }
        }).unsubscribe();
    }

    /**
     * 删除机器人
     *
     * @param hour
     * @return
     */
    public void deleteMessageKFIDHour(int hour) {
        final long delTime = ModuleMgr.getAppMgr().getTime() - (hour * 60 * 60 * 1000);
        Observable<List<BaseMessage>> observable = centerFLetter.deleteKFID();
        observable.subscribe(new Action1<List<BaseMessage>>() {
            @Override
            public void call(List<BaseMessage> baseMessages) {
                if (baseMessages == null || baseMessages.size() <= 0) {
                    return;
                }
                for (BaseMessage temp : baseMessages) {
                    if (temp.getTime() < delTime) {
                        centerFLetter.updateContent(temp.getWhisperID());
                    }
                    centerFmessage.delete(temp.getLWhisperID());
                }
            }
        }).unsubscribe();
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
}
