package com.juxin.predestinate.module.logic.config;

import android.os.Environment;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.App;

import java.io.File;

/**
 * 软件名目录下文件夹集，通过get方法获取目录的同时自动进行创建[目录末尾已拼接separator]
 * Created by ZRP on 2017/4/11.
 */
public class DirType {

    /**
     * 文件夹名称
     */
    private enum Dir {
        root, cache, download, apk, video, voice, image, upload
    }

    private static final String SD_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static final String APP_NAME = App.context.getResources().getString(R.string.app_storage_name);
    private static String DIR_ROOT = SD_ROOT + File.separator + APP_NAME + File.separator;

    static {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            DIR_ROOT = SD_ROOT + File.separator + APP_NAME + File.separator;
        } else {
            DIR_ROOT = App.context.getFilesDir().getAbsolutePath() + File.separator + APP_NAME + File.separator;
        }
    }

    private static final String CACHE = DIR_ROOT + Dir.cache + File.separator;
    private static final String DOWNLOAD = DIR_ROOT + Dir.download + File.separator;
    private static final String APK = DIR_ROOT + Dir.apk + File.separator;
    private static final String VIDEO = DIR_ROOT + Dir.video + File.separator;
    private static final String VOICE = DIR_ROOT + Dir.voice + File.separator;
    private static final String IMAGE = DIR_ROOT + Dir.image + File.separator;
    private static final String UPLOAD = DIR_ROOT + Dir.upload + File.separator;

    /**
     * 文件夹是否存在/是否创建成功（不存在则创建）
     *
     * @param folder 文件夹路径
     * @return 文件夹是否存在/是否创建成功
     */
    public static boolean isFolderExists(String folder) {
        File file = new File(folder);
        return file.exists() || file.mkdirs();
    }

    /**
     * @return 获取软件名根目录
     */
    public static String getRootDir() {
        return isFolderExists(DIR_ROOT) ? DIR_ROOT : "";
    }

    /**
     * @return 获取缓存文件夹目录
     */
    public static String getCacheDir() {
        return isFolderExists(CACHE) ? CACHE : "";
    }

    /**
     * @return 获取下载文件夹目录
     */
    public static String getDownloadDir() {
        return isFolderExists(DOWNLOAD) ? DOWNLOAD : "";
    }

    /**
     * @return 获取apk文件夹目录
     */
    public static String getApkDir() {
        return isFolderExists(APK) ? APK : "";
    }

    /**
     * @return 获取视频文件夹目录
     */
    public static String getVideoDir() {
        return isFolderExists(VIDEO) ? VIDEO : "";
    }

    /**
     * @return 获取音频文件夹目录
     */
    public static String getVoiceDir() {
        return isFolderExists(VOICE) ? VOICE : "";
    }

    /**
     * @return 获取图片文件夹目录
     */
    public static String getImageDir() {
        return isFolderExists(IMAGE) ? IMAGE : "";
    }

    /**
     * @return 获取上传临时存储文件夹目录
     */
    public static String getUploadDir() {
        return isFolderExists(UPLOAD) ? UPLOAD : "";
    }
}
