package com.juxin.predestinate.ui.user.check;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    private FlowLayout mFlowLayout;
    private LinearLayout albumLayout, videoLayout;
    private AlbumHorizontalPanel albumPanel, videoPanel;

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

        findViewById(R.id.item_sign).setOnClickListener(listener);
        findViewById(R.id.item_dynamic).setOnClickListener(listener);
        findViewById(R.id.item_game).setOnClickListener(listener);
        findViewById(R.id.item_secret_photo).setOnClickListener(listener);
        findViewById(R.id.item_secret_video).setOnClickListener(listener);
        findViewById(R.id.item_gift).setOnClickListener(listener);
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

        labels.add("长头发");
        labels.add("大长腿");
        labels.add("丰满");

        for (int i = 0; i < labels.size(); i++) {
            View view = createView(R.layout.common_label_layout);
            TextView label_text = (TextView) view.findViewById(R.id.label_text);
            label_text.setText(labels.get(i));
            mFlowLayout.addView(view);
        }
    }

    private final NoDoubleClickListener listener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.item_sign:   // 标签
                    break;

                case R.id.item_dynamic: // 动态
                    break;

                case R.id.item_game: // 游戏
                    break;

                case R.id.item_secret_photo://私密相册
                    break;

                case R.id.item_secret_video://私密视频
                    break;

                case R.id.item_gift:
                    break;
            }
        }
    };
}