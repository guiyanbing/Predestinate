package com.juxin.predestinate.ui.user.util;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserPhoto;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseViewPanel;
import com.juxin.predestinate.module.logic.baseui.custom.HorizontalListView;
import com.juxin.predestinate.module.util.UIShow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 水平查看相册/视频panel
 * Created by Su on 2017/3/23.
 */
public class AlbumHorizontalPanel extends BaseViewPanel implements AdapterView.OnItemClickListener {
    public static final int EX_HORIZONTAL_ALBUM = 1;  // 展示照片
    public static final int EX_HORIZONTAL_VIDEO = 2;  // 展示视频
    private int channel = CenterConstant.USER_CHECK_INFO_OWN; // 默认查看自己

    private int showType;               // 展示类型：相册，视频，礼物
    private Serializable list;          // 数据列表
    private List<UserPhoto> albumList;  // 相册列表

    private HorizontalListView albumListView;
    private HorizontalAdapter albumAdapter;

    /**
     * @param channel  查看方：自己，他人
     * @param showType 展示类型：相册，视频，礼物
     * @param list     数据列表
     */
    public AlbumHorizontalPanel(Context context, int channel, int showType, Serializable list) {
        super(context);
        setContentView(R.layout.p1_album_horizontal_panel);
        this.channel = channel;
        this.showType = showType;
        this.list = list;

        initData();
        initview();
    }

    private void initData() {
        if (showType == EX_HORIZONTAL_ALBUM) {
            if (albumList == null) albumList = new ArrayList<>();
            albumList = (List<UserPhoto>) list;
        }
    }

    private void initview() {
        int horizontalSpacing = ModuleMgr.getAppMgr().getScreenWidth() / 50;
        int columnWidth = (ModuleMgr.getAppMgr().getScreenWidth() - 8 * horizontalSpacing) / 4;
        albumListView = (HorizontalListView) findViewById(R.id.list_horizontal);
        albumListView.setItemMargin(horizontalSpacing);

        albumAdapter = new HorizontalAdapter(getContext(), showType, columnWidth, albumList);
        albumListView.setAdapter(albumAdapter);
        albumListView.setOnItemClickListener(this);
        refresh();
    }

    public void refresh() {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (channel == CenterConstant.USER_CHECK_INFO_OWN) {
            UIShow.showPhotoOfSelf((FragmentActivity) getContext(), (Serializable) albumList, position);
            return;
        }

        UIShow.showPhotoOfOther((FragmentActivity) getContext(), (Serializable) albumList, position);
    }
}
