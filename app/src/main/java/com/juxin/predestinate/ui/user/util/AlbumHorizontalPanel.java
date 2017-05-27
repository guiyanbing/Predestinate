package com.juxin.predestinate.ui.user.util;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;

import com.juxin.library.view.BasePanel;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.custom.HorizontalListView;
import com.juxin.predestinate.module.logic.baseui.custom.SimpleTipDialog;
import com.juxin.predestinate.module.util.PickerDialogUtil;
import com.juxin.predestinate.module.util.UIShow;

import java.io.Serializable;

/**
 * 水平查看相册/视频panel
 * Created by Su on 2017/3/23.
 */
public class AlbumHorizontalPanel extends BasePanel implements AdapterView.OnItemClickListener {

    public static final int EX_HORIZONTAL_ALBUM = 1;  // 展示照片
    public static final int EX_HORIZONTAL_VIDEO = 2;  // 展示视频
    private int channel = CenterConstant.USER_CHECK_INFO_OWN; // 默认查看自己

    private int showType;               // 展示类型：相册，视频，礼物
    private UserDetail userDetail;      // 用户数据

    private HorizontalListView horiListView;  // 水平列表
    private HorizontalAdapter albumAdapter;   // 相册适配器
    private VideoHoriAdapter videoAdapter;    // 视频适配器

    /**
     * @param channel    查看方：自己，他人
     * @param showType   展示类型：相册，视频，礼物
     * @param userDetail 数据列表
     */
    public AlbumHorizontalPanel(Context context, int channel, int showType, UserDetail userDetail) {
        super(context);
        setContentView(R.layout.p1_album_horizontal_panel);
        this.channel = channel;
        this.showType = showType;
        this.userDetail = userDetail;

        initview();
    }

    private void initview() {
        int horizontalSpacing = ModuleMgr.getAppMgr().getScreenWidth() / 50;
        int columnWidth = (ModuleMgr.getAppMgr().getScreenWidth() - 8 * horizontalSpacing) / 4;
        horiListView = (HorizontalListView) findViewById(R.id.list_horizontal);
        horiListView.setItemMargin(horizontalSpacing);

        // 相册列表
        if (showType == EX_HORIZONTAL_ALBUM) {
            albumAdapter = new HorizontalAdapter(getContext(), columnWidth, userDetail.getUserPhotos());
            horiListView.setAdapter(albumAdapter);
        }

        // 视频列表
        if (showType == EX_HORIZONTAL_VIDEO) {
            videoAdapter = new VideoHoriAdapter(getContext(), columnWidth, userDetail.getUserVideos());
            horiListView.setAdapter(videoAdapter);
        }
        horiListView.setOnItemClickListener(this);
    }

    public void refresh(UserDetail userDetail) {
        if (showType == EX_HORIZONTAL_ALBUM) {
            albumAdapter.setList(userDetail.getUserPhotos());
            return;
        }
        videoAdapter.setList(userDetail.getUserVideos());
    }

    /**
     * 开通Vip提示
     */
    private void showVipTips() {
        PickerDialogUtil.showSimpleTipDialogExt((FragmentActivity) getContext(), new SimpleTipDialog.ConfirmListener() {
            @Override
            public void onCancel() {
            }

            @Override
            public void onSubmit() {
                UIShow.showBuyCoinActivity(getContext());
            }
        }, getContext().getString(R.string.goods_vip_check_other_album), "", "取消", "去开通", true, R.color.text_zhuyao_black);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (showType) {
            case EX_HORIZONTAL_ALBUM:
                UserDetail info = ModuleMgr.getCenterMgr().getMyInfo();
                if (channel == CenterConstant.USER_CHECK_INFO_OWN) {
                    UIShow.showPhotoOfSelf((FragmentActivity) getContext(), (Serializable) userDetail.getUserPhotos(), position);
                    return;
                }

                if (!info.isMan()) {
                    UIShow.showPhotoOfOther((FragmentActivity) getContext(), (Serializable) userDetail.getUserPhotos(), position);
                    return;
                }

                if (!info.isVip()) {
                    showVipTips();
                    return;
                }

                UIShow.showPhotoOfOther((FragmentActivity) getContext(), (Serializable) userDetail.getUserPhotos(), position);
                break;

            case EX_HORIZONTAL_VIDEO:
                UIShow.showUserSecretAct(getContext(), channel, userDetail);
                break;
        }
    }
}
