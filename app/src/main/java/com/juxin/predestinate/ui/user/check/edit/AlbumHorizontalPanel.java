package com.juxin.predestinate.ui.user.check.edit;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.bean.center.user.others.SecretMedia;
import com.juxin.predestinate.module.logic.baseui.BaseViewPanel;
import com.juxin.predestinate.module.util.UIUtil;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;
import com.juxin.predestinate.ui.user.check.adapter.SecretMediaAdapter;

import java.util.List;

/**
 * 水平查看相册/视频panel
 * Created by Su on 2017/3/23.
 */

public class AlbumHorizontalPanel extends BaseViewPanel implements BaseRecyclerViewHolder.OnItemClickListener {
    private float toDpMutliple = 1; //根据屏幕密度获取屏幕转换倍数
    private UserDetail userDetail;

    private int secretType;
    private SecretMediaAdapter mediaAdapter;

    public AlbumHorizontalPanel(Context context, int secretType, UserDetail userDetail) {
        super(context);
        setContentView(R.layout.p1_album_horizontal_panel);
        this.secretType = secretType;
        this.userDetail = userDetail;

        initview();
    }

    private void initview() {
        toDpMutliple = UIUtil.toDpMultiple((Activity) getContext());
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_album_horizontal);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // 设置RecyclerView横向滚动
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new RightItemSpaces((int) (10 * toDpMutliple)));

        mediaAdapter = new SecretMediaAdapter();
        recyclerView.setAdapter(mediaAdapter);
        mediaAdapter.setOnItemClickListener(this);

        setParameter();
        refresh();
    }

    /**
     * 设置照片显示参数
     */
    private void setParameter() {
        mediaAdapter.setSecretType(secretType);
        mediaAdapter.setParams((int) (53.3 * toDpMutliple));
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
