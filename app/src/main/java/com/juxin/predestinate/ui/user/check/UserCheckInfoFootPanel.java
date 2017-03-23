package com.juxin.predestinate.ui.user.check;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.UserDetail;
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

    private LinearLayout albumLayout;
    private LinearLayout jiayuanLayout;
    private FlowLayout mFlowLayout;
    private TextView phothNum, tv_relationship;
    private AlbumHorizontalPanel albumPanel;
    private TextView tv_fans_count, fans_state;
    private List<String> data = new ArrayList<>();
    private int currentSwitcher;
    private final String DYNAMIC_NO = "暂时没有动态，和TA聊聊天吧";


    public UserCheckInfoFootPanel(Context context, int channel, UserDetail userDetail) {
        super(context);
        setContentView(R.layout.p1_user_checkinfo_footer);
        this.channel = channel;
        this.userDetail = userDetail;
        initView();
        initLabelDatas();
    }


    private void initView() {
    }

    /**
     * 设置右滑退出忽略view
     *
     * @param activity BaseExActivity实例
     */
    public void setSlideIgnoreView(BaseActivity activity) {
//        activity.addIgnoredView(albumLayout);
    }

    /**
     * 初始化资料介绍标签
     */
    private void initLabelDatas() {
        mFlowLayout.removeAllViews();
        labels.clear();

        if (userDetail.getAge() != 0) labels.add(userDetail.getAge() + "岁");
        if (userDetail.getHeight() != 0 && userDetail.getHeight() >= 150)
            labels.add(userDetail.getHeight() + "cm");
        if (!TextUtils.isEmpty(userDetail.getIncome())) labels.add(userDetail.getIncome());
        if (!TextUtils.isEmpty(userDetail.getAddressShow().trim()))
            labels.add(userDetail.getAddressShow());

    }

    private final NoDoubleClickListener clickListener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.lmv_home_info:

                    break;
            }
        }
    };
}
