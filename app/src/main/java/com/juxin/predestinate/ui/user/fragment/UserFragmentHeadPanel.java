package com.juxin.predestinate.ui.user.fragment;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.library.log.PLogger;
import com.juxin.library.observe.MsgMgr;
import com.juxin.library.observe.MsgType;
import com.juxin.library.observe.PObserver;
import com.juxin.mumu.bean.utils.MMToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.module.local.album.ImgSelectUtil;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseViewPanel;
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.utils.NoDoubleClickListener;

/**
 * 个人中心条目头部
 */
public class UserFragmentHeadPanel extends BaseViewPanel implements PObserver, ImgSelectUtil.OnChooseCompleteListener {
    private ImageView user_head, vip_status;
    private TextView user_nick, user_id, invite_code;
    private UserFragmentFunctionPanel functionPanel;

    private UserDetail myInfo;

    public UserFragmentHeadPanel(Context context) {
        super(context);
        setContentView(R.layout.p1_user_fragment_header);

        initView();
        initEvent();
        refreshView();
    }

    private void initView() {
        user_head = (ImageView) findViewById(R.id.user_head);
        user_nick = (TextView) findViewById(R.id.user_nick);
        vip_status = (ImageView) findViewById(R.id.user_vip_status);
        user_id = (TextView) findViewById(R.id.user_id);
        invite_code = (TextView) findViewById(R.id.invite_code);

        //根据屏幕分辨率设置最大显示长度
        user_nick.setMaxEms(App.context.getResources().getDisplayMetrics().density <= 1.5 ? 5 : 7);
        LinearLayout function_container = (LinearLayout) findViewById(R.id.function_container);
        functionPanel = new UserFragmentFunctionPanel(getContext());
        function_container.addView(functionPanel.getContentView());
    }

    private void initEvent() {
        user_head.setOnClickListener(listener);
        user_nick.setOnClickListener(listener);

        findViewById(R.id.ll_edit).setOnClickListener(listener);
        findViewById(R.id.code_copy).setOnClickListener(listener);

        MsgMgr.getInstance().attach(this);
    }

    /**
     * 界面刷新
     */
    public void refreshView() {
        myInfo = ModuleMgr.getCenterMgr().getMyInfo();
        ImageLoader.loadRoundCorners(getContext(), myInfo.getAvatar(), 10, user_head);
        vip_status.setVisibility(myInfo.isVip() ? View.VISIBLE : View.GONE);
        user_id.setText("ID:" + myInfo.getUid());
        user_nick.setText(myInfo.getNickname());
        functionPanel.refreshView(myInfo);

        invite_code.setText(getContext().getString(R.string.user_fragment_header_invCode) + myInfo.getShareCode());
    }

    /**
     * 角标刷新
     */
    public void refreshBadge() {
        functionPanel.refreshBadge();
    }

    private NoDoubleClickListener listener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.user_head://上传头像
                    ImgSelectUtil.getInstance().pickPhoto(getContext(), UserFragmentHeadPanel.this);
                    break;

                case R.id.ll_edit://跳转到个人信息页面
                    UIShow.showUserInfoAct(getContext());
                    break;

                case R.id.code_copy: // 邀请码复制
                    copyString(getContext(), myInfo.getShareCode());
                    break;
            }
        }
    };

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
        PLogger.d("path=== " + path[0]);
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

    /**
     * 复制字符串到剪贴板
     */
    private void copyString(Context context, String copy_string) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setText(copy_string);
        MMToast.showShort("已复制到剪贴板");
    }
}