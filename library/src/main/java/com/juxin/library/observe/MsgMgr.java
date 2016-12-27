package com.juxin.library.observe;

import com.juxin.library.log.PLogger;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 消息处理中枢
 * Created by ZRP on 2016/12/27.
 */
public class MsgMgr {

    private static MsgMgr instance = new MsgMgr();

    private CompositeSubscription rxSubscriptions = new CompositeSubscription();//订阅中心

    public static MsgMgr getInstance() {
        return instance;
    }

    /**
     * 抛出事件
     *
     * @param key   事件的key
     * @param value 事件的value
     */
    public void sendMsg(String key, Object value) {
        Msg msg = new Msg(key, value);
        PLogger.d("------>" + msg.toString());
        RxBus.getInstance().post(msg);
    }

    /**
     * 绑定所有类型的消息通知，具体类型根据PObserver#onMessage的key进行区分
     *
     * @param observer 事件回调
     */
    public void attach(final PObserver observer) {
        if (observer == null) {
            PLogger.e("------>PObserver为空");
            return;
        }
        PLogger.d("------>attach");
        rxSubscriptions.add(RxBus.getInstance()
                .toObservable(Msg.class)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Msg>() {
                    @Override
                    public void call(Msg msg) {
                        observer.onMessage(msg.getKey(), msg.getData());
                    }
                }));
    }

    /**
     * 解绑（RxBus取消订阅），在调用对象进行销毁的时候进行解绑，防止内存溢出
     */
    public void detach() {
        if (!rxSubscriptions.isUnsubscribed()) {
            PLogger.d("------>detach");
            rxSubscriptions.unsubscribe();
        }
    }

    public interface PObserver {
        void onMessage(String key, Object value);
    }
}
