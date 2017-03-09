package com.juxin.predestinate.module.logic.model.mgr;

import com.juxin.library.observe.ModuleBase;
import com.juxin.library.request.DownloadListener;
import com.juxin.predestinate.module.logic.request.HTCallBack;
import com.juxin.predestinate.module.logic.request.RequestParam;

/**
 * 网络请求接口类
 * Created by ZRP on 2016/12/29.
 */
public interface HttpMgr extends ModuleBase {

    /**
     * 发起普通请求
     *
     * @param requestParam 请求参数
     * @return HTCallBack
     */
    HTCallBack request(RequestParam requestParam);

    /**
     * 文件下载
     *
     * @param url              下载地址
     * @param filePath         本地存放路径
     * @param downloadListener 下载监听
     * @return HTCallBack
     */
    HTCallBack download(String url, String filePath, DownloadListener downloadListener);
}
