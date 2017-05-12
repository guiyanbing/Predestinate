package com.juxin.predestinate.ui.start;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.juxin.predestinate.module.util.BaseUtil;
import com.juxin.predestinate.module.util.UIShow;

import java.util.List;

/**
 * 登录界面
 *
 * @author xy
 */
public class UserLoginExtAct extends BaseActivity implements OnItemClickListener, OnClickListener, UserPrivacyAdapter.OnDelItemListener {
    private EditText et_uid, et_pwd;
    private ImageView iv_arrow;
    private ListView lv_account;
    private UserPrivacyAdapter privacyAdapter;

    private long chosenUID;// 登录页默认展示的ID
    private String chosenPwd;
    private int position;           // 当前用户所在list位置
    private LoginMgr loginMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login_act);
        setBackView(getResources().getString(R.string.title_loginact));
        initView();
        initData();
    }

    private void initData() {
        List<UP> userList = loginMgr.getUserList();
        showCurrentUser(userList);
        // 箭头显隐
        if (userList.size() <= 0) this.iv_arrow.setVisibility(View.GONE);
    }

    private void initView() {
        loginMgr = ModuleMgr.getLoginMgr();
        lv_account = (ListView) findViewById(R.id.list_user_login_account);
        et_uid = (EditText) findViewById(R.id.edtTxt_user_login_username);
        et_pwd = (EditText) findViewById(R.id.edtTxt_user_login_password);
        iv_arrow = (ImageView) findViewById(R.id.img_user_login_arrow);
        findViewById(R.id.layout_parent).setOnClickListener(this);
        findViewById(R.id.btn_user_login_submit).setOnClickListener(this);
        findViewById(R.id.txt_user_login_toReg).setOnClickListener(this);
        iv_arrow.setOnClickListener(this);
        lv_account.setOnItemClickListener(this);
        et_uid.setOnClickListener(this);
        // 列表
        privacyAdapter = new UserPrivacyAdapter(this, loginMgr.getUserList());
        privacyAdapter.setDelListener(this);
        lv_account.setAdapter(privacyAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_user_login_submit:
                if (validInput()) loginMgr.onLogin(this, chosenUID, chosenPwd);
                break;
            case R.id.img_user_login_arrow:
                if (lv_account.getVisibility() == View.GONE) {
                    startAnimate();
                }else{
                    endAnimate();
                }
                break;
            case R.id.layout_parent:
            case R.id.et_uid:
                endAnimate();
                break;
            case R.id.txt_user_login_toReg:
                UIShow.showUserRegInfoAct(this);
                finish();
                break;
        }
    }

    private void showCurrentUserData() {
        et_uid.setText(loginMgr.getUserList().size() <= 0 ? "" : chosenUID + "");
        et_uid.setSelection(loginMgr.getUserList().size() > 0 ? String.valueOf(chosenUID).length() : 0);
        et_pwd.setText(loginMgr.getUserList().size() <= 0 ? "" : chosenPwd);
    }


    private boolean validInput() {
        String uid = et_uid.getText().toString();
        if (TextUtils.isEmpty(uid)) {
            PToast.showShort(getResources().getString(R.string.toast_uid_isnull));
            return false;
        }
        chosenPwd = et_pwd.getText().toString();
        if (TextUtils.isEmpty(chosenPwd)) {
            PToast.showShort(getResources().getString(R.string.toast_pwd_isnull));
            return false;
        }
        chosenUID = BaseUtil.getLong(uid, 0);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.position = position;
        showCurrentUser(loginMgr.getUserList());
        endAnimate();
    }

    public void onDelClick(int position) {
        UP user = loginMgr.removeLoginUser(position);
        if (loginMgr.getUserList().size() <= 0) {
            this.iv_arrow.setVisibility(View.GONE);
        }

        // 删除的用户是当前默认的用户
        if (user.getUid() == chosenUID) {
            this.position = 0;
            showCurrentUser(loginMgr.getUserList());
            return;
        }
        privacyAdapter.setList(loginMgr.getUserList());
    }


    // =========================== uid/pw展示 =========================================
    private void showCurrentUser(List<UP> userList) {
        if (userList.size() > 0) {
            chosenUID = userList.get(position).getUid();
            chosenPwd = userList.get(position).getPw();
        } else {
            endAnimate();
        }
        showCurrentUserData();
        privacyAdapter.setList(userList);
    }

    // =========================== 账户列表显隐动画 =========================================

    private void changeArrowDir(boolean open){
        iv_arrow.setImageResource(open?R.drawable.f1_filter_up_arrow:R.drawable.filter_down_arrow);
    }
    private void startAnimate() {
        changeArrowDir(true);
        lv_account.setVisibility(View.VISIBLE);
        ViewAnimator.animate(lv_account)
                .slideTop()
                .duration(200)
                .start();
    }

    private void endAnimate() {
        changeArrowDir(false);
        ViewAnimator.animate(lv_account)
                .translationY(0, -300)
                .alpha(1, 0)
                .duration(200)
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        lv_account.setVisibility(View.GONE);
                    }
                })
                .start();
    }
}
