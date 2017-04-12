package com.juxin.predestinate.module.logic.media;

import android.text.TextUtils;

import com.juxin.library.observe.ModuleBase;
import com.juxin.mumu.bean.log.MMLog;
import com.juxin.mumu.bean.utils.BitmapUtil;
import com.juxin.mumu.bean.utils.FileUtil;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 媒体文件管理类
 * Created by Su on 2017/4/12.
 */
public class MediaMgr implements ModuleBase {

    @Override
    public void init() {
    }

    @Override
    public void release() {
    }

    /**
     * 所有 文件,图片,语音上传等
     *
     * @param sourceType 上传类型
     * @param url        文件地址
     */
    public void sendHttpFile(int sourceType, String url, RequestComplete complete) {
        sendHttpFile(sourceType, url, null, complete);
    }

    /**
     * 身份认证： 图片，视频(暂时保留)
     */
    public void sendHttpFile(String url, RequestComplete complete) {
        sendHttpFile(Constant.INT_CHAT_PIC, url, null, complete);
    }

    /**
     * 给视频专用
     */
    public void sendHttpFileVideo(int sourceType, String url, String url2, RequestComplete complete) {
        sendHttpFile(sourceType, url, url2, complete);
    }

    /**
     * 上传文件
     *
     * @param sourceType 文件来源： 头像、聊天图片、集赞图片的区分，用于服务器判断长/短存储
     * @param url        文件地址
     * @param url2       视频缩略图地址
     */
    private void sendHttpFile(int sourceType, String url, String url2, RequestComplete complete) {
        Map<String, File> uploadFile = new HashMap<>();
        Map<String, Object> postParams = new HashMap<>();
        if (TextUtils.isEmpty(url)) return;
        postParams.put("type", sourceType);
        switch (sourceType) {
            case Constant.INT_AVATAR:
            case Constant.INT_ALBUM:
            case Constant.INT_CHAT_PIC:
            case Constant.INT_PRAISE_PIC: {//图片
                uploadImage(UrlParam.uploadFile, url, postParams, complete);
                break;
            }
            case Constant.INT_CHAT_VOICE:
            case Constant.INT_PRAISE_VOICE:
            case Constant.INT_GAME_VOICE: {//语音
                uploadFile.put("voice", new File(url));
                ModuleMgr.getHttpMgr().uploadFile(UrlParam.uploadFile, postParams, uploadFile, complete);
                break;
            }
            case Constant.INT_CHAT_VIDEO: {//视频
                if (TextUtils.isEmpty(url2)) return;
                uploadFile.put("video", new File(url));
                uploadFile.put("thumb", new File(url2));
                ModuleMgr.getHttpMgr().uploadFile(UrlParam.uploadFile, postParams, uploadFile, complete);
                break;
            }
        }
    }

    /**
     * 上传图片，内部处理图片压缩并删除临时压缩文件操作
     */
    private void uploadImage(UrlParam urlParam, final String uri, Map<String, Object> post_param, final RequestComplete logicCallBack) {
        Map<String, File> uploadFile = new HashMap<>();
        final String path = BitmapUtil.getSmallBitmapAndSave(uri);
        if (TextUtils.isEmpty(path)) {
            MMLog.autoDebug("图片地址无效");
            return;
        }
        uploadFile.put("pic", new File(path));
        ModuleMgr.getHttpMgr().uploadFile(urlParam, post_param, uploadFile, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                if (logicCallBack != null) {
                    logicCallBack.onRequestComplete(response);
                }
                FileUtil.deleteFile(path);// 删除缩略图
            }
        });
    }
}
