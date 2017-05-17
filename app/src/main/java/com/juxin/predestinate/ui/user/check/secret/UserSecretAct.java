package com.juxin.predestinate.ui.user.check.secret;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.bean.center.user.others.UserProfile;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.module.util.UIUtil;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;
import com.juxin.predestinate.ui.user.util.CenterConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * 私密相册/视频页
 * Created by Su on 2017/3/29.
 */
public class UserSecretAct extends BaseActivity implements BaseRecyclerViewHolder.OnItemClickListener {
    private float toDpMutliple = 1; //根据屏幕密度获取屏幕转换倍数
    private static final int SECRET_SHOW_COLUMN = 3; // 列数
    private int channel = CenterConstant.USER_CHECK_INFO_OWN; // 默认查看自己

    private String title;
    private UserDetail userDetail;   // 自己
    private UserProfile userProfile; // 他人

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
        toDpMutliple = UIUtil.toDpMultiple(this);
        channel = getIntent().getIntExtra(CenterConstant.USER_CHECK_INFO_KEY, CenterConstant.USER_CHECK_INFO_OWN);

        userProfile = getIntent().getParcelableExtra(CenterConstant.USER_CHECK_OTHER_KEY);
        if (channel == CenterConstant.USER_CHECK_INFO_OWN) {
            userDetail = ModuleMgr.getCenterMgr().getMyInfo();
        }

        if (userDetail != null) {
            title = getString(R.string.user_info_own_video);
            return;
        }

        if (userProfile == null) return;
        title = getString(R.string.user_info_other_video, userProfile.getNickname());
    }

    private void initView() {
        setBackView(title);
        tv_hot = (TextView) findViewById(R.id.tv_video_browse_hot);
        recyclerView = (RecyclerView) findViewById(R.id.secret_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, SECRET_SHOW_COLUMN));
        recyclerView.addItemDecoration(new ItemSpaces((int) (10 * toDpMutliple)));

        secretAdapter = new UserSecretAdapter();
        secretAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(secretAdapter);

        // 视频列表目前在个人资料里给返回，不另外请求接口
        List<String> data = new ArrayList<>();
        data.add("1");
        data.add("2");
        data.add("3");
        data.add("4");
        data.add("5");
        data.add("6");
        data.add("7");
        data.add("8");
        secretAdapter.setList(data);
    }

    @Override
    public void onItemClick(View convertView, int position) {
        PToast.showShort("position: " + position);

        UIShow.showSecretGiftDlg(this);
    }

    /**
     * margin
     */
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
