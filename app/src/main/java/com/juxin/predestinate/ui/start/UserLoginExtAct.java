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
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.BaseUtil;
import com.juxin.predestinate.module.util.UIShow;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class UserLoginExtAct extends BaseActivity implements OnItemClickListener, OnClickListener, UserPrivacyAdapter.OnDelItemListener {
    private EditText et_uid, et_pwd;
    private ImageView iv_arrow;
    private ListView lv_userData;


    private UserPrivacyAdapter privacyAdapter;

    private long currentUserID;// 登录页默认展示的ID
    private String currentUserPwd;
    private int position;           // 当前用户所在list位置
    private LoginMgr loginMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login_act);
        setBackView("用户登录");
        initView();
        initData();
    }

    private void initData() {
        List<UP> userList = loginMgr.getUserList();
        showCurrentUser(userList);

        // 箭头显隐
        if (userList.size() <= 0) {
            this.iv_arrow.setVisibility(View.GONE);
        }
    }

    private void initView() {
        loginMgr = ModuleMgr.getLoginMgr();
        this.lv_userData = (ListView) findViewById(R.id.list_user_login_account);
        findViewById(R.id.layout_parent).setOnClickListener(this);
        this.et_uid = (EditText) findViewById(R.id.edtTxt_user_login_username);
        this.et_pwd = (EditText) findViewById(R.id.edtTxt_user_login_password);
        this.iv_arrow = (ImageView) findViewById(R.id.img_user_login_arrow);
        findViewById(R.id.btn_user_login_submit).setOnClickListener(this);
        findViewById(R.id.txt_user_login_toReg).setOnClickListener(this);
        this.iv_arrow.setOnClickListener(this);
        this.lv_userData.setOnItemClickListener(this);
        this.et_uid.setOnClickListener(this);
        // 列表
        privacyAdapter = new UserPrivacyAdapter(this, loginMgr.getUserList());
        privacyAdapter.setDelListener(this);
        lv_userData.setAdapter(privacyAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_user_login_submit:
                if (validInput()) {
                    loginMgr.onLogin(this, currentUserID, currentUserPwd, new RequestComplete() {
                        @Override
                        public void onRequestComplete(HttpResponse response) {
                            LoadingDialog.closeLoadingDialog(500);
                            String jsonResult = response.getResponseString();
                            try {
                                JSONObject jsonObject = new JSONObject(jsonResult);
                                String respCode = jsonObject.optString("respCode");
                                if ("success".equals(respCode)) {
                                    // Cookie 在http响应头中返回
                                    loginMgr.putAllLoginInfo(currentUserID, currentUserPwd, true);
                                    // 临时资料设置
                                    JSONObject json = jsonObject.optJSONObject("user_info");
                                    ModuleMgr.getCenterMgr().getMyInfo().setNickname(json.optString("nickname"));
                                    ModuleMgr.getCenterMgr().getMyInfo().setUid(json.optLong("uid"));

                                    // 判断是否缺失数据,缺失则继续跳转到用户注册
                                    if (json.optInt("miss_info") == 1) {
                                        PToast.showLong("请完善您的资料");
                                        UIShow.showUserInfoCompleteAct(UserLoginExtAct.this);
                                        return;
                                    }
                                    UIShow.showMainClearTask(UserLoginExtAct.this);
                                } else {
                                    PToast.showShort(getResources().getString(R.string.toast_login_iserror));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                break;
            case R.id.img_user_login_arrow:
                if (lv_userData.getVisibility() == View.GONE) {
                    startAnimate();
                    return;
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
        et_uid.setText(loginMgr.getUserList().size() <= 0 ? "" : currentUserID + "");
        et_uid.setSelection(loginMgr.getUserList().size() > 0 ? String.valueOf(currentUserID).length() : 0);
        et_pwd.setText(loginMgr.getUserList().size() <= 0 ? "" : currentUserPwd);
    }


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
