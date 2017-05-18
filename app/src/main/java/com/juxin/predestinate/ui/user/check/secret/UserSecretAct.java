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
import com.juxin.predestinate.bean.center.user.others.UserProfile;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.module.util.UIUtil;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;
import com.juxin.predestinate.ui.user.check.secret.bean.UserVideoInfo;
import com.juxin.predestinate.ui.user.util.CenterConstant;

/**
 * 私密相册/视频页
 * Created by Su on 2017/3/29.
 */
public class UserSecretAct extends BaseActivity implements BaseRecyclerViewHolder.OnItemClickListener, RequestComplete {
    private float toDpMutliple = 1; //根据屏幕密度获取屏幕转换倍数
    private static final int SECRET_SHOW_COLUMN = 3; // 列数
    private int channel = CenterConstant.USER_CHECK_INFO_OWN; // 默认查看自己

    private UserDetail userDetail;   // 自己
    private UserProfile userProfile; // 他人
    private UserVideoInfo userVideoInfo; // 用户视频信息

    private RecyclerView recyclerView;
    private UserSecretAdapter secretAdapter;
    private TextView tv_hot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_user_secret_act);

        initView();
        initData();
    }

    private void initData() {
        channel = getIntent().getIntExtra(CenterConstant.USER_CHECK_INFO_KEY, CenterConstant.USER_CHECK_INFO_OWN);
        userProfile = getIntent().getParcelableExtra(CenterConstant.USER_CHECK_OTHER_KEY);
        if (channel == CenterConstant.USER_CHECK_INFO_OWN) {
            userDetail = ModuleMgr.getCenterMgr().getMyInfo();
        }

        if (userDetail != null) {
            setBackView(getString(R.string.user_info_own_video));
            ModuleMgr.getCenterMgr().reqGetVideoList(userDetail.getUid(), this);
            return;
        }

        if (userProfile == null) return;
        setBackView(getString(R.string.user_info_other_video, userProfile.getNickname()));
        ModuleMgr.getCenterMgr().reqGetVideoList(userProfile.getUid(), this);
        ModuleMgr.getCenterMgr().reqSetPopnum(userProfile.getUid(), null);
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
    }

    @Override
    public void onItemClick(View convertView, int position) {
        if (channel == CenterConstant.USER_CHECK_INFO_OWN) {
            UIShow.showSecretVideoPlayerDlg(this);
            return;
        }

        // TODO 判断当前选择视频解锁状态，未解锁礼物弹框，解锁直接进入播放弹框
//        if () {
//            UIShow.showSecretGiftDlg(this, userProfile);
//            return;
//        }
        UIShow.showSecretVideoPlayerDlg(this);
        reqSetViewTime();
    }

    /**
     * 设置视频播放次数
     */
    private void reqSetViewTime() {
        if (userProfile == null) return;

        ModuleMgr.getCenterMgr().reqSetViewTime(userProfile.getUid(), 0, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
            }
        });
    }

    @Override
    public void onRequestComplete(HttpResponse response) {
        if (response.getUrlParam() == UrlParam.reqGetVideoList) {
            if (response.isOk()) {
                userVideoInfo = (UserVideoInfo) response.getBaseData();

                if (userVideoInfo == null) return;
                secretAdapter.setList(userVideoInfo.getVideoList());
                tv_hot.setText(String.valueOf(userVideoInfo.getPopNum()));
            }
        }
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
