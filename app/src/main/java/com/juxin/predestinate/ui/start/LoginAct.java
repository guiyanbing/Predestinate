package com.juxin.predestinate.ui.start;

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
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.BaseUtil;
import com.juxin.predestinate.module.util.UIShow;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 用户登录页面
 * Created by Su on 2016/12/12.
 */
public class LoginAct extends BaseActivity implements OnItemClickListener, OnClickListener, UserPrivacyAdapter.OnDelItemListener {

    private LoginMgr loginMgr;

    // UI Object
    private EditText et_uid, et_pwd;
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
        setContentView(R.layout.p1_login_act);
        setBackView(getResources().getString(R.string.title_loginact));
        initView();
        initData();
    }

    private void initView() {
        loginMgr = ModuleMgr.getLoginMgr();
        lv_userData = (ListView) findViewById(R.id.list_user_login_account);
        et_uid = (EditText) findViewById(R.id.et_uid);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        iv_arrow = (ImageView) findViewById(R.id.img_user_login_arrow);
        findViewById(R.id.bt_submit).setOnClickListener(this);
        findViewById(R.id.bt_reg).setOnClickListener(this);
        findViewById(R.id.layout_parent).setOnClickListener(this);
        findViewById(R.id.txt_user_reset_pw).setOnClickListener(this);
        this.iv_arrow.setOnClickListener(this);
        this.lv_userData.setOnItemClickListener(this);
        this.et_uid.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_submit://提交
                if (validInput()) {
                    loginMgr.onLogin(this, currentUserID, currentUserPwd);
                }
                break;

            case R.id.img_user_login_arrow:
                if (lv_userData.getVisibility() == View.GONE) {
                    startAnimate();
                    return;
                }
                endAnimate();
                break;

            case R.id.layout_parent:
            case R.id.et_uid:
                endAnimate();
                break;

            case R.id.bt_reg:
                UIShow.showRegInfoAct(this);
                finish();
                break;

            case R.id.txt_user_reset_pw:  // 跳转重置密码
                UIShow.showFindPwdAct(this,FindPwdAct.OPEN_FINDPWD);
                break;
        }
    }

    // 检查输入信息是否为空
    private boolean validInput() {
        String uid = et_uid.getText().toString();
        if (TextUtils.isEmpty(uid)) {
            PToast.showShort(getResources().getString(R.string.toast_uid_isnull));
            return false;
        }
        currentUserPwd = et_pwd.getText().toString();
        if (TextUtils.isEmpty(currentUserPwd)) {
            PToast.showShort(getResources().getString(R.string.toast_pwd_isnull));
            return false;
        }
        currentUserID = BaseUtil.getLong(uid, 0);
        return true;
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
        et_uid.setText(loginMgr.getUserList().size() <= 0 ? "" : currentUserID + "");
        et_uid.setSelection(loginMgr.getUserList().size() > 0 ? String.valueOf(currentUserID).length() : 0);
        et_pwd.setText(loginMgr.getUserList().size() <= 0 ? "" : currentUserPwd);
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
