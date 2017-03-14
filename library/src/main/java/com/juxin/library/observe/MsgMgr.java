package com.juxin.library.observe;

import com.juxin.library.log.PLogger;

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
     * 在应用模块初始化的时候初始化ui-thread监听
     */
    public void initUiThread() {
        RxBus.getInstance().toFlowable(Runnable.class)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Runnable>() {
                    @Override
                    public void accept(Runnable runnable) throws Exception {
                        runnable.run();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        PLogger.printThrowable(throwable);
                    }
                });
    }

    /**
     * 抛出事件到ui线程，与initUiThread配套使用，只有其在初始化之后该方法才起效
     *
     * @param runnable 事件
     */
    public void runOnUiThread(Runnable runnable) {
        if (runnable == null) return;
        RxBus.getInstance().post(runnable);
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
            PLogger.e("------>PObserver为空");
            return;
        }
        PLogger.d("------>attach");
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
     * 子线程执行
     *
     * @param aClass            监听的消息类型
     * @param consumer          执行成功回调
     * @param throwableConsumer 执行失败回调
     */
    public static <T> void doOnChildThread(Class<T> aClass, Consumer<T> consumer, Consumer<Throwable> throwableConsumer) {
        RxBus.getInstance().toFlowable(aClass).
                onBackpressureBuffer().subscribeOn(Schedulers.io()).
                subscribeOn(Schedulers.newThread()).
                subscribe(consumer, throwableConsumer);
    }

    /**
     * 解绑（RxBus取消订阅），在调用对象进行销毁的时候进行解绑，防止内存溢出
     */
    public void detach() {
        if (!rxDisposable.isDisposed()) {
            PLogger.d("------>detach");
            rxDisposable.dispose();
        }
    }
}
