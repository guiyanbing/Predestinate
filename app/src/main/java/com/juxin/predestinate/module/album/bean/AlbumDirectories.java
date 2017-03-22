package com.juxin.predestinate.module.album.bean;

import com.juxin.predestinate.module.album.bean.SingleDirectoryModel;

/**
 * 相册目录
 * <p>
 * Created by Su on 2016/12/22.
 */

public class AlbumDirectories {

    public String directoryPath;        // 相册某个目录的路径。例如：/storage/emulated/0/QQBrowser/图片收藏

    public SingleDirectoryModel images; // 某目录下的所有图片实体
}
