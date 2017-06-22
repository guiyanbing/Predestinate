package com.juxin.predestinate.module.util;

import android.util.SparseArray;

import com.juxin.library.log.PSP;
import com.juxin.library.request.DownloadListener;
import com.juxin.library.utils.FileUtil;
import com.juxin.predestinate.module.logic.config.DirType;
import com.juxin.predestinate.module.logic.model.impl.HttpMgrImpl;
import com.juxin.predestinate.module.logic.model.mgr.HttpMgr;

import java.util.HashMap;


/**
 * @author guowz
 * 下载H5到本地
 */
public class WebAppDownloader {
    private static WebAppDownloader instance;
    private static final String KEY_WEB_APP_ROOT = "sp_key_webapp_root";
    private static final String KEY_WEB_APP_VER = "sp_key_webapp_ver";
    private HashMap<String,String> downMap = new HashMap<>();
    private WebAppDownloader(){

    }

    public static WebAppDownloader getInstance(){
        if(instance == null){
            synchronized (WebAppDownloader.class){
                if(instance == null){
                    instance = new WebAppDownloader();
                }
            }
        }
        return instance;
    }

    public void download(String url, final long ver){
        if(downMap.containsKey(url) || PSP.getInstance().getLong(KEY_WEB_APP_VER,0) >= ver)
            return;
        downMap.put(url,url);
        HttpMgr httpMgr = new HttpMgrImpl();
        String zipFile = DirType.getCacheDir() + ver +  FileUtil.getFileNameFromUrl(url);
        httpMgr.download(url, zipFile, new DownloadListener() {
            @Override
            public void onStart(String url, String filePath) {

            }

            @Override
            public void onProcess(String url, int process, long size) {

            }

            @Override
            public void onSuccess(String url, String filePath) {
                downMap.remove(url);
                String outputPath = DirType.getCacheDir() + "yfb";
                boolean success = FileUtil.unZipFile(filePath , outputPath);
                if(success){
                    PSP.getInstance().put(KEY_WEB_APP_ROOT,"file://" + outputPath);
                    PSP.getInstance().put(KEY_WEB_APP_VER,ver);
                }
                System.out.println("unzip: " + outputPath + " success:" + success);
            }

            @Override
            public void onFail(String url, Throwable throwable) {
                downMap.remove(url);
            }
        });
    }
}
