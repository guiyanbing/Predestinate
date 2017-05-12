package com.juxin.predestinate.ui.user.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserPhoto;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseViewPanel;
import com.juxin.predestinate.module.util.UIUtil;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 水平查看相册/视频panel
 * Created by Su on 2017/3/23.
 */

public class AlbumHorizontalPanel extends BaseViewPanel implements BaseRecyclerViewHolder.OnItemClickListener {
    public static final int EX_HORIZONTAL_ALBUM = 1;  // 展示照片
    public static final int EX_HORIZONTAL_VIDEO = 2;  // 展示视频

    private int showType;
    private Serializable list;          // 数据列表
    private MediaAdapter mediaAdapter;

    private List<UserPhoto> albumList;

    public AlbumHorizontalPanel(Context context, int showType, Serializable list) {
        super(context);
        setContentView(R.layout.p1_album_horizontal_panel);
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
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_album_horizontal);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // 设置RecyclerView横向滚动
        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.addItemDecoration(new RightItemSpaces(horizontalSpacing));

        mediaAdapter = new MediaAdapter(showType, columnWidth);
        recyclerView.setAdapter(mediaAdapter);
        mediaAdapter.setList(albumList);
        mediaAdapter.setOnItemClickListener(this);

        refresh();
    }

    public void refresh() {
//        List<SecretMedia> data = secretType == mediaAdapter.SECRET_VIDEO ?
//                userDetail.getSecretVideos() : userDetail.getSecretPhotos();

//        mediaAdapter.setList(data);
    }

    @Override
    public void onItemClick(View convertView, int position) {

    }

    /**
     * right margin
     */
    private class RightItemSpaces extends RecyclerView.ItemDecoration {
        private int space;

        public RightItemSpaces(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            outRect.right = space;
        }
    }
}
