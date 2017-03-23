package com.juxin.predestinate.ui.start;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.start.UP;
import com.juxin.predestinate.module.local.login.LoginMgr;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.main.MainActivity;

import java.util.List;

/**
 * 用户登录页面
 * <p>
 * Created by Su on 2016/12/12.
 */
public class LoginAct extends BaseActivity implements OnItemClickListener, OnClickListener, UserPrivacyAdapter.OnDelItemListener {

    private LoginMgr loginMgr;

    // UI Object
    private EditText et_userID;
    private EditText et_userPwd;
    private ImageView iv_arrow;
    private ListView lv_userData;
    private UserPrivacyAdapter privacyAdapter;

    private long currentUserID;     // 登录页默认展示的ID
    private String currentUserPwd;  // 登录页默认pwd
    private int position;           // 当前用户所在list位置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isCanBack(false);
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        setContentView(R.layout.r1_login_act);
        setBackView(R.id.back_view, "登录");

        initView();
        initData();
        initEvent();
    }

    private void initView() {
        loginMgr = ModuleMgr.getLoginMgr();
        this.lv_userData = (ListView) findViewById(R.id.list_user_login_account);
        this.et_userID = (EditText) findViewById(R.id.edtTxt_user_login_username);
        this.et_userPwd = (EditText) findViewById(R.id.edtTxt_user_login_password);
        this.iv_arrow = (ImageView) findViewById(R.id.img_user_login_arrow);

        // 列表
        privacyAdapter = new UserPrivacyAdapter(this, loginMgr.getUserList());
        privacyAdapter.setDelListener(this);
        lv_userData.setAdapter(privacyAdapter);
    }

    // 初始化用户列表
    private void initData() {
        List<UP> userList = loginMgr.getUserList();
        showCurrentUser(userList);

        // 箭头显隐
        if (userList.size() <= 0) {
            this.iv_arrow.setVisibility(View.GONE);
        }
    }

    private void initEvent() {
        findViewById(R.id.btn_user_login_submit).setOnClickListener(this);
        findViewById(R.id.txt_user_login_toReg).setOnClickListener(this);
        findViewById(R.id.layout_parent).setOnClickListener(this);
        findViewById(R.id.txt_user_reset_pw).setOnClickListener(this);
        this.iv_arrow.setOnClickListener(this);
        this.lv_userData.setOnItemClickListener(this);
        this.et_userID.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_user_login_submit:
                Intent intent = new Intent(LoginAct.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
//                if (!checkUserDataIsEmpty()) {
//                    loginMgr.onLogin(this, currentUserID, currentUserPwd, null, true);
//                }
                break;

            case R.id.img_user_login_arrow:
                if (lv_userData.getVisibility() == View.GONE) {
                    startAnimate();
                    return;
                }
                endAnimate();
                break;

            case R.id.layout_parent:
            case R.id.edtTxt_user_login_username:
                endAnimate();
                break;

            case R.id.txt_user_login_toReg:
                UIShow.showRegInfoAct(this);
                finish();
                break;

            case R.id.txt_user_reset_pw:  // 跳转重置密码
//                UIShow.showResetPwAct(this);
                break;
        }
    }

    // 检查输入信息是否为空
    private boolean checkUserDataIsEmpty() {
        String uid = et_userID.getText().toString();
        if (TextUtils.isEmpty(uid)) {
            PToast.showShort("请输入您的用户名");
            return true;
        }
        currentUserPwd = et_userPwd.getText().toString();
        if (TextUtils.isEmpty(currentUserPwd)) {
            PToast.showShort("请输入您的密码");
            return true;
        }
//        currentUserID = BaseUtil.getLong(uid, 0);
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.position = position;
        showCurrentUser(loginMgr.getUserList());
        endAnimate();
    }

    @Override
    public void onDelClick(int position) {
        UP user = loginMgr.removeLoginUser(position);
        if (loginMgr.getUserList().size() <= 0) {
            this.iv_arrow.setVisibility(View.GONE);
        }

        // 删除的用户是当前默认的用户
        if (user.getUid() == currentUserID) {
            this.position = 0;
            showCurrentUser(loginMgr.getUserList());
            return;
        }
        privacyAdapter.setList(loginMgr.getUserList());
    }

    @Override
    public void back() {
        UIShow.showNavUserAct(this);
        super.back();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (loginMgr.IF_PW_RESET) {   // 忘记密码重置成功后
            position = 0;
            showCurrentUser(loginMgr.getUserList());
            loginMgr.setResetStatus(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        privacyAdapter.setDelListener(null);
    }

    // =========================== uid/pw展示 =========================================
    private void showCurrentUser(List<UP> userList) {
        if (userList.size() > 0) {
            currentUserID = userList.get(position).getUid();
            currentUserPwd = userList.get(position).getPw();
        } else {
            endAnimate();
        }
        showCurrentUserData();
        privacyAdapter.setList(userList);
    }

    private void showCurrentUserData() {
        et_userID.setText(loginMgr.getUserList().size() <= 0 ? "" : currentUserID + "");
        et_userID.setSelection(loginMgr.getUserList().size() > 0 ? String.valueOf(currentUserID).length() : 0);
        et_userPwd.setText(loginMgr.getUserList().size() <= 0 ? "" : currentUserPwd);
    }

    // =========================== 账户列表显隐动画 =========================================
    private void startAnimate() {
        lv_userData.setVisibility(View.VISIBLE);
        ViewAnimator.animate(lv_userData)
                .slideTop()
                .duration(200)
                .start();
    }

    private void endAnimate() {
        ViewAnimator.animate(lv_userData)
                .translationY(0, -300)
                .alpha(1, 0)
                .duration(200)
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        lv_userData.setVisibility(View.GONE);
                    }
                })
                .start();
    }


}
