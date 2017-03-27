package com.juxin.predestinate.ui.user.check;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.BaseViewPanel;
import com.juxin.predestinate.module.logic.baseui.custom.FlowLayout;
import com.juxin.predestinate.ui.utils.NoDoubleClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 查看他人资料底部panel
 */
public class UserCheckInfoFootPanel extends BaseViewPanel {
    private final int channel;
    private UserDetail userDetail;
    private final List<String> labels = new ArrayList<>(); // 资料介绍标签

    private LinearLayout albumLayout, videoLayout;
    private AlbumHorizontalPanel albumPanel, videoPanel;

    private FlowLayout mFlowLayout;

    public UserCheckInfoFootPanel(Context context, int channel, UserDetail userDetail) {
        super(context);
        setContentView(R.layout.p1_user_checkinfo_footer);
        this.channel = channel;
        this.userDetail = userDetail;
        initView();
        initLabels();
    }

    private void initView() {
        albumLayout = (LinearLayout) findViewById(R.id.album_item);
        videoLayout = (LinearLayout) findViewById(R.id.video_item);

        mFlowLayout = (FlowLayout) findViewById(R.id.flowlayout);
    }

    /**
     * 设置右滑退出忽略view
     */
    public void setSlideIgnoreView(BaseActivity activity) {
//        activity.addIgnoredView(albumLayout);
    }

    /**
     * 印象标签
     */
    private void initLabels() {
        mFlowLayout.removeAllViews();
        labels.clear();
    }

    private final NoDoubleClickListener clickListener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            switch (v.getId()) {
//                case R.id.lmv_home_info:
//                    break;
            }
        }
    };
}
