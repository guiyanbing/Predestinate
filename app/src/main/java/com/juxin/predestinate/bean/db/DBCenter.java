package com.juxin.predestinate.bean.db;

import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;

import com.juxin.predestinate.bean.db.cache.DBCacheCenter;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.local.chat.utils.MessageConstant;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.squareup.sqlbrite.BriteDatabase;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import rx.Observable;
import rx.Observer;

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

//    private final Executor dbExecutor = Executors.newSingleThreadExecutor();
    private HandlerThread workerThread = new HandlerThread("LightTaskThread");
    private Handler handler = null;

    public DBCenter(BriteDatabase database) {
        this.mDatabase = database;

        workerThread.start();
        handler = new Handler(workerThread.getLooper() );


        centerFLetter = new DBCenterFLetter(database, handler);
        centerFmessage = new DBCenterFMessage(database, handler);
        centerFUnRead = new DBCenterFUnRead(database, handler);
        cacheCenter = new DBCacheCenter(database, handler);

    }

    /******************** DBCacheCenter **************************/

    public DBCacheCenter getCacheCenter() {
        return cacheCenter;
    }


    /******************** FUnRead **************************/


    public DBCenterFUnRead getCenterFUnRead() {
        return centerFUnRead;
    }

    public long insertUnRead(final String key, final String content) {
        if (TextUtils.isEmpty(key)) {
            return MessageConstant.ERROR;
        }

        return centerFUnRead.storageData(key, content);
    }

    /******************** FLetter **************************/
    public long insertMsg(final BaseMessage baseMessage) {

        if (BaseMessage.BaseMessageType.hint.getMsgType() == baseMessage.getType()) {
            baseMessage.setStatus(MessageConstant.READ_STATUS);
        }

        long ret = centerFmessage.insertMsg(baseMessage);
        if (ret != MessageConstant.OK) {
            return ret;
        }
        if (BaseMessage.BaseMessageType.hint.getMsgType() != baseMessage.getType()) {
            ret = centerFLetter.storageData(baseMessage);
        }

        return ret;
    }

    /**
     * 更新
     *
     * @param message
     * @return
     */
    public long updateMsg(final BaseMessage message) {
        final String userID = message.getWhisperID();
        if (TextUtils.isEmpty(userID)) return MessageConstant.ERROR;

        long ret = MessageConstant.OK;
        if (BaseMessage.BaseMessageType.hint.getMsgType() != message.getType()) {
            ret = centerFLetter.updateMsgStatus(message);
        }

        if (ret!= MessageConstant.OK) {
            return ret;
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
    public int deleteMessage(final long userID) {
        int ret = centerFLetter.delete(userID);
        if (ret != MessageConstant.OK) {
            return ret;
        }

        return centerFmessage.delete(userID);
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
        observable.subscribe(new Observer<List<BaseMessage>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(List<BaseMessage> baseMessages) {
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
        observable.subscribe(new Observer<List<BaseMessage>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(List<BaseMessage> baseMessages) {
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
    public int updateFmessage(final BaseMessage baseMessage) {
        return centerFmessage.updateMsg(baseMessage);
    }

    public long updateToReadAll() {
        return centerFmessage.updateToReadAll();
    }
}
