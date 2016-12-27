package com.juxin.library.request;

import com.juxin.library.log.PLogger;
import com.juxin.library.observe.RxBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 文件下载FileCallback
 * Created by zrp on 2016/11/22.
 */
public class FileCallback implements Callback<ResponseBody> {

    private CompositeSubscription rxSubscriptions = new CompositeSubscription();//订阅下载进度

    private String url;                 // 文件下载地址
    private String filePath;            // 目标文件存储的文件夹路径
    private DownloadListener listener;  // 下载监听
    private int downloadProcess;        // 当前的下载进度

    public FileCallback(String url, String filePath, DownloadListener listener) {
        this.url = url;
        this.filePath = filePath;
        this.listener = listener;

        subscribeLoadProgress();// 订阅下载进度
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (listener != null) listener.onStart(url, filePath);
        try {
            saveFile(response);
        } catch (Exception e) {
            PLogger.printThrowable(e);
            if (listener != null) listener.onFail(url, e);
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        PLogger.printThrowable(t);
        if (listener != null) listener.onFail(url, t);
    }

    private void saveFile(Response<ResponseBody> response) throws Exception {
        InputStream in = null;
        FileOutputStream out = null;
        byte[] buf = new byte[2048];
        int len;
        try {
            File file = new File(filePath);
            if (file.exists()) file.delete();

            in = response.body().byteStream();
            out = new FileOutputStream(file);

            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            if (listener != null) listener.onSuccess(url, filePath);
            unSubscribe();// 取消订阅
        } finally {
            if (in != null) in.close();
            if (out != null) out.close();
        }
    }

    /**
     * 订阅文件下载进度
     */
    private void subscribeLoadProgress() {
        downloadProcess = 0;
        rxSubscriptions.add(RxBus.getInstance()
                .toObservable(FileLoadingBean.class)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<FileLoadingBean>() {
                    @Override
                    public void call(FileLoadingBean fileLoadEvent) {
                        int process = (int) ((100 * fileLoadEvent.getProgress()) / fileLoadEvent.getTotal());
                        if (process > downloadProcess) {
                            downloadProcess = process;
                            if (listener != null)
                                listener.onProcess(url, process, fileLoadEvent.getTotal());// 下载中
                        }
                    }
                }));
    }

    /**
     * 取消订阅，防止内存泄漏
     */
    private void unSubscribe() {
        if (!rxSubscriptions.isUnsubscribed()) {
            rxSubscriptions.unsubscribe();
        }
    }
}
