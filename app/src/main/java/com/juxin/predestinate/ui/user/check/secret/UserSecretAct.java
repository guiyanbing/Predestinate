package com.juxin.predestinate.ui.user.check.secret;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.bean.center.user.detail.UserVideo;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.module.util.UIUtil;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;
import com.juxin.predestinate.ui.user.util.CenterConstant;

/**
 * 私密相册/视频页
 * Created by Su on 2017/3/29.
 */
public class UserSecretAct extends BaseActivity implements BaseRecyclerViewHolder.OnItemClickListener {
    private float toDpMutliple = 1; //根据屏幕密度获取屏幕转换倍数
    private static final int SECRET_SHOW_COLUMN = 3; // 列数
    private int channel = CenterConstant.USER_CHECK_INFO_OWN; // 默认查看自己

    private UserDetail userDetail;       // 用户资料
    private RecyclerView recyclerView;
    private UserSecretAdapter secretAdapter;
    private TextView tv_hot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_user_secret_act);

        initData();
        initView();
    }

    private void initData() {
        channel = getIntent().getIntExtra(CenterConstant.USER_CHECK_INFO_KEY, CenterConstant.USER_CHECK_INFO_OWN);

        if (channel == CenterConstant.USER_CHECK_INFO_OWN) {
            userDetail = ModuleMgr.getCenterMgr().getMyInfo();
            setBackView(getString(R.string.user_info_own_video));
            return;
        }

        userDetail = getIntent().getParcelableExtra(CenterConstant.USER_CHECK_OTHER_KEY);
        if (userDetail == null) return;
        setBackView(getString(R.string.user_info_other_video, userDetail.getNickname()));
        ModuleMgr.getCenterMgr().reqSetPopnum(userDetail.getUid(), null);
    }

    private void initView() {
        toDpMutliple = UIUtil.toDpMultiple(this);
        tv_hot = (TextView) findViewById(R.id.tv_video_browse_hot);
        recyclerView = (RecyclerView) findViewById(R.id.secret_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, SECRET_SHOW_COLUMN));
        recyclerView.addItemDecoration(new ItemSpaces((int) (10 * toDpMutliple)));

        secretAdapter = new UserSecretAdapter();
        secretAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(secretAdapter);
        secretAdapter.setList(userDetail.getUserVideos());

        tv_hot.setText(String.valueOf(userDetail.getVideopopularity()));
    }

    @Override
    public void onItemClick(View convertView, int position) {
        UserVideo userVideo = userDetail.getUserVideos().get(position);
        if (channel == CenterConstant.USER_CHECK_INFO_OWN) {
            UIShow.showSecretVideoPlayerDlg(this, userVideo);
            return;
        }

        // 判断当前选择视频解锁状态，未解锁礼物弹框，解锁直接进入播放弹框
        if (!userVideo.isCanView() && userVideo.getGiftid() != 0) {
            UIShow.showSecretGiftDlg(this, userVideo);
            return;
        }
        UIShow.showSecretVideoPlayerDlg(this, userVideo);
        reqSetViewTime(userVideo);
    }

    /**
     * 设置视频播放次数
     */
    private void reqSetViewTime(UserVideo userVideo) {
        if (channel == CenterConstant.USER_CHECK_INFO_OWN) return;

        ModuleMgr.getCenterMgr().reqSetViewTime(userDetail.getUid(), userVideo.getId(), null);
    }

    // RecyclerView margin
    private class ItemSpaces extends RecyclerView.ItemDecoration {
        private int space;

        public ItemSpaces(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.right = space;
            outRect.left = space;
            outRect.top = space;
        }
    }
}
