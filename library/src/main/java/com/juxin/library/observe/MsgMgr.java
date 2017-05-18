package com.juxin.library.observe;

import com.juxin.library.log.PLogger;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 消息处理中枢
 * Created by ZRP on 2016/12/27.
 */
public class MsgMgr {

    private static MsgMgr instance = new MsgMgr();

    private CompositeDisposable rxDisposable = new CompositeDisposable();//订阅中心

    public static MsgMgr getInstance() {
        return instance;
    }

    /**
     * 抛出事件到ui线程并执行
     *
     * @param runnable 事件
     */
    public void runOnUiThread(final Runnable runnable) {
        Flowable.empty().observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
            @Override
            public void onSubscribe(Subscription s) {
            }

            @Override
            public void onNext(Object o) {
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onComplete() {
                if (runnable != null) runnable.run();
            }
        });
    }

    /**
     * 抛出事件到子线程并执行
     *
     * @param runnable 事件
     */
    public void runOnChildThread(final Runnable runnable) {
        Flowable.empty().observeOn(Schedulers.io()).subscribe(new Subscriber<Object>() {
            @Override
            public void onSubscribe(Subscription s) {
            }

            @Override
            public void onNext(Object o) {
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onComplete() {
                if (runnable != null) runnable.run();
            }
        });
    }

    /**
     * 抛出事件到主线程
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
            PLogger.e("------>PObserver is null.");
            return;
        }
        PLogger.d("------>attach[" + observer.toString() + "], attached-size[" + rxDisposable.size() + "]");
        rxDisposable.add(RxBus.getInstance().toFlowable(Msg.class)
                .onBackpressureBuffer().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Msg>() {
                    @Override
                    public void accept(Msg msg) throws Exception {
                        observer.onMessage(msg.getKey(), msg.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        PLogger.printThrowable(throwable);
                    }
                }));
    }

    /**
     * 清除所有已经attach的监听，防止内存溢出[谨慎调用]
     */
    public void clear() {
        if (!rxDisposable.isDisposed()) {
            PLogger.d("------>clear");
            rxDisposable.clear();
        }
    }
}
