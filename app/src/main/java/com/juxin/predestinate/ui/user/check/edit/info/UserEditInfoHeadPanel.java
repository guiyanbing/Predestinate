package com.juxin.predestinate.ui.user.check.edit.info;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.library.observe.MsgMgr;
import com.juxin.library.observe.MsgType;
import com.juxin.library.view.BasePanel;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.module.local.album.ImgSelectUtil;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.UIUtil;
import com.juxin.predestinate.ui.utils.NoDoubleClickListener;

/**
 * 编辑页面头部
 * Created by Su on 2017/5/3.
 */
public class UserEditInfoHeadPanel extends BasePanel implements ImgSelectUtil.OnChooseCompleteListener {

    private UserDetail userDetail;

    private ImageView img_header;
    private RelativeLayout rl_header;
    private TextView user_id, user_gender, user_age, user_height, user_province;

    public UserEditInfoHeadPanel(Context context) {
        super(context);
        setContentView(R.layout.f1_user_edit_info_head_panel);

        initView();
        initData();
    }

    public void initView() {
        rl_header = (RelativeLayout) findViewById(R.id.edit_header);
        img_header = (ImageView) findViewById(R.id.img_header);
        user_id = (TextView) findViewById(R.id.user_id);
        user_gender = (TextView) findViewById(R.id.user_gender);
        user_age = (TextView) findViewById(R.id.user_age);
        user_height = (TextView) findViewById(R.id.user_height);
        user_province = (TextView) findViewById(R.id.user_province);

        img_header.setOnClickListener(listener);
    }

    public void initData() {
        userDetail = ModuleMgr.getCenterMgr().getMyInfo();

        user_id.setText("ID: " + userDetail.getUid());
        user_gender.setText(userDetail.isMan() ? "男" : "女");
        user_age.setText(getContext().getString(R.string.user_info_age, userDetail.getAge()));
        user_height.setText(userDetail.getHeight() + "cm");
        user_province.setText(userDetail.getProvince());

        if (userDetail.isMan()) {
            rl_header.setBackgroundColor(getContext().getResources().getColor(R.color.picker_blue_color));
        }

        ImageLoader.loadCircle(getContext(), userDetail.getAvatar(), img_header, UIUtil.dip2px(getContext(), 2), Color.WHITE);
    }

    public void refreshView() {
        userDetail = ModuleMgr.getCenterMgr().getMyInfo();
        ImageLoader.loadCircle(getContext(), userDetail.getAvatar(), img_header);
    }

    private NoDoubleClickListener listener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.img_header:
                    ImgSelectUtil.getInstance().pickPhoto(getContext(), UserEditInfoHeadPanel.this);
                    break;
            }
        }
    };

    @Override
    public void onComplete(String... path) {
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
}
