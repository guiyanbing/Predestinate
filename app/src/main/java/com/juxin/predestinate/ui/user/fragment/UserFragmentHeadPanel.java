package com.juxin.predestinate.ui.user.fragment;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.library.observe.MsgMgr;
import com.juxin.library.observe.MsgType;
import com.juxin.library.observe.PObserver;
import com.juxin.library.view.CircleImageView;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.module.local.album.ImgSelectUtil;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseViewPanel;
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.ChineseFilter;
import com.juxin.predestinate.ui.user.check.edit.custom.EditPopupWindow;
import com.juxin.predestinate.ui.user.edit.EditKey;
import com.juxin.predestinate.ui.user.util.CenterConstant;

/**
 * 个人中心条目头部
 */
public class UserFragmentHeadPanel extends BaseViewPanel implements View.OnClickListener, PObserver, ImgSelectUtil.OnChooseCompleteListener {

    private CircleImageView user_head, user_head_vip, user_head_status;
    private TextView user_nick, user_id, iv_invite_code;
    private UserFragmentFunctionPanel functionPanel;

    private UserDetail myInfo;

    public UserFragmentHeadPanel(Context context) {
        super(context);
        setContentView(R.layout.p1_user_fragment_header);

        initView();
        refreshView();
    }

    private void initView() {
        user_head = (CircleImageView) findViewById(R.id.user_head);
        user_head_vip = (CircleImageView) findViewById(R.id.user_head_vip);
        user_head_status = (CircleImageView) findViewById(R.id.user_head_status);
        user_nick = (TextView) findViewById(R.id.user_nick);
        user_id = (TextView) findViewById(R.id.user_id);
        iv_invite_code = (TextView) findViewById(R.id.iv_invite_code);

        //根据屏幕分辨率设置最大显示长度
        user_nick.setMaxEms(App.context.getResources().getDisplayMetrics().density <= 1.5 ? 5 : 7);

        user_head.setOnClickListener(this);
        user_nick.setOnClickListener(this);
        findViewById(R.id.iv_code_copy).setOnClickListener(this);

        LinearLayout function_container = (LinearLayout) findViewById(R.id.function_container);
        functionPanel = new UserFragmentFunctionPanel(getContext());
        function_container.addView(functionPanel.getContentView());

        MsgMgr.getInstance().attach(this);
    }

    /**
     * 界面刷新
     */
    public void refreshView() {
        myInfo = ModuleMgr.getCenterMgr().getMyInfo();
        ImageLoader.loadAvatar(getContext(), myInfo.getAvatar(), user_head);
        user_id.setText("ID:" + myInfo.getUid());
        user_nick.setText(myInfo.getNickname());
        iv_invite_code.setText(String.format(getContext().getResources().
                getString(R.string.center_my_invite_code), myInfo.getShareCode()));
        functionPanel.refreshView(myInfo);

        refreshHeader();
    }

    private void refreshHeader() {
        if (myInfo.isVip()) {
            user_head_vip.setVisibility(View.VISIBLE);
        }

        switch (myInfo.getAvatar_status()) {
            case CenterConstant.USER_AVATAR_CHECKING:  // 审核中
                user_head_status.setVisibility(View.VISIBLE);
                user_head_status.setImageResource(R.drawable.f1_user_avatar_checking);
                break;

            case CenterConstant.USER_AVATAR_NO_PASS:   // 未通过
                user_head_status.setVisibility(View.VISIBLE);
                user_head_status.setImageResource(R.drawable.f1_user_avatar_notpass);
                break;

            default:
                user_head_status.setVisibility(View.GONE); // 其他状态默认通过
                break;
        }
    }

    /**
     * 角标刷新
     */
    public void refreshBadge() {
        functionPanel.refreshBadge();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_head://上传头像
                ImgSelectUtil.getInstance().pickPhoto(getContext(), this);
                break;
            case R.id.user_nick://修改昵称
                EditPopupWindow popupWindow = new EditPopupWindow(getContext(), EditKey.s_key_nickName, user_nick);
                popupWindow.showPopupWindow();
                break;
            case R.id.iv_code_copy://复制邀请码
                ChineseFilter.copyString(getContext(), ModuleMgr.getCenterMgr().getMyInfo().getShareCode());
                break;
        }
    }

    //处理其他界面返回结果
    public void onActivityResult(int requestCode, int resultCode) {
        if (requestCode == 101) {
            refreshView();
        }
    }

    @Override
    public void onComplete(final String... path) {
        if (path == null || path.length == 0 || TextUtils.isEmpty(path[0])) {
            return;
        }
        LoadingDialog.show((FragmentActivity) getContext(), "正在上传头像");
        ModuleMgr.getCenterMgr().uploadAvatar(path[0], new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                if (response.isOk()) {
                    LoadingDialog.closeLoadingDialog();
                    MsgMgr.getInstance().sendMsg(MsgType.MT_Update_MyInfo, null);
                }
            }
        });
    }

    @Override
    public void onMessage(String key, Object value) {
        switch (key) {
            case MsgType.MT_MyInfo_Change:
                refreshView();
                break;
        }
    }
}