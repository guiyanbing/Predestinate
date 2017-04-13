package com.juxin.predestinate.ui.user.check.edit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.library.log.PToast;
import com.juxin.library.observe.MsgMgr;
import com.juxin.library.observe.MsgType;
import com.juxin.library.observe.PObserver;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.area.City;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.module.local.album.ImgSelectUtil;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.logic.baseui.picker.picker.AddressPicker;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.PickerDialogUtil;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.user.edit.EditKey;
import com.juxin.predestinate.ui.utils.NoDoubleClickListener;

import java.util.HashMap;

/**
 * 个人信息页
 * Created by Su on 2017/3/28.
 */

public class UserInfoAct extends BaseActivity implements PObserver, ImgSelectUtil.OnChooseCompleteListener {

    private ImageView img_header;
    private TextView tv_name, tv_age, tv_home, tv_sign;

    private boolean updateAvatar = false;
    private UserDetail userDetail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_user_selfinfo_act);
        setTitle(getString(R.string.user_self_info_act));
        setBackView();

        initView();
        initData();
    }

    private void initView() {
        img_header = (ImageView) findViewById(R.id.iv_avatar);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_age = (TextView) findViewById(R.id.tv_age);
        tv_home = (TextView) findViewById(R.id.tv_home);
        tv_sign = (TextView) findViewById(R.id.tv_sign);

        img_header.setOnClickListener(listener);
        findViewById(R.id.name_view).setOnClickListener(listener);
        findViewById(R.id.age_view).setOnClickListener(listener);
        findViewById(R.id.home_view).setOnClickListener(listener);
        findViewById(R.id.sign_view).setOnClickListener(listener);

        MsgMgr.getInstance().attach(this);
    }

    private void initData() {
        userDetail = ModuleMgr.getCenterMgr().getMyInfo();
        ImageLoader.loadRoundCorners(this, userDetail.getAvatar(), 10, img_header);
        tv_name.setText(userDetail.getNickname());
        tv_age.setText(userDetail.getAge() + "岁");
        tv_home.setText(userDetail.getAddress());
        tv_sign.setText(userDetail.getSignName());
    }

    private void refreshView() {
        PToast.showShort(getString(R.string.user_info_edit_suc));
        userDetail = ModuleMgr.getCenterMgr().getMyInfo();
        if (updateAvatar) {
            updateAvatar = false;
            ImageLoader.loadRoundCorners(this, userDetail.getAvatar(), 10, img_header);
            return;
        }
        tv_name.setText(userDetail.getNickname());
        tv_home.setText(userDetail.getAddress());
        tv_sign.setText(userDetail.getSignName());
    }

    private NoDoubleClickListener listener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.iv_avatar: // 更改头像
                    ImgSelectUtil.getInstance().pickPhoto(App.activity, UserInfoAct.this);
                    break;

                case R.id.name_view: // 打开修改昵称页
                    UIShow.showEditContentAct(UserInfoAct.this);
                    break;

                case R.id.age_view:  // 修改年龄
                    EditPopupWindow popupWindow = new EditPopupWindow(UserInfoAct.this, tv_age);
                    popupWindow.showPopupWindow(tv_age);
                    break;

                case R.id.home_view: // 修改地址
                    PickerDialogUtil.showAddressPickerDialog2(UserInfoAct.this, new AddressPicker.OnAddressPickListener() {
                        @Override
                        public void onAddressPicked(City city) {
                            HashMap<String, Object> post = new HashMap<>();
                            post.put(EditKey.s_key_pro, city.getProvinceID());
                            post.put(EditKey.s_key_city, city.getCityID());
                            ModuleMgr.getCenterMgr().updateMyInfo(post);
                        }
                    }, userDetail.getProvinceName(), userDetail.getCityName());
                    break;

                case R.id.sign_view:// 个性签名
                    UIShow.showUserEditSignAct(UserInfoAct.this, userDetail.getSignName());
                    break;
            }
        }
    };

    @Override
    public void onComplete(String... path) {
        if (path == null || path.length == 0 || TextUtils.isEmpty(path[0])) {
            return;
        }
        LoadingDialog.show(this, getString(R.string.user_info_avatar_upload));
        ModuleMgr.getCenterMgr().uploadAvatar(path[0], new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                if (response.isOk()) {
                    updateAvatar = true;
                    PToast.showShort(getString(R.string.user_info_avatar_suc));
                    MsgMgr.getInstance().sendMsg(MsgType.MT_Update_MyInfo, null);
                    return;
                }

                PToast.showShort(getString(R.string.user_info_avatar_fail));
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
