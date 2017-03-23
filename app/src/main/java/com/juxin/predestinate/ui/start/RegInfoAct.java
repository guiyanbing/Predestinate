package com.juxin.predestinate.ui.start;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.juxin.library.utils.FileUtil;
import com.juxin.library.utils.StringUtils;
import com.juxin.library.view.CustomFrameLayout;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.album.ImgSelectUtil;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.base.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.request.HTCallBack;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.utils.NoDoubleClickListener;

import java.util.HashMap;

/**
 * 用户注册
 * <p/>
 * Created by Su on 2016/8/22.
 */
public class RegInfoAct extends BaseActivity implements ImgSelectUtil.OnChooseCompleteListener {

    private EditText txt_reg_info_nickname;     // 昵称
    private RelativeLayout btn_reg_info_submit; // 提交
    private TextView txt_header_describe;       // 头像底部描述
    private ImageView img_header;
    private CustomFrameLayout fl_choose_man, fl_choose_woman;

    // 保存临时数据
    private String _nickname, district, _photoUrl;
    private int _gender = 1;
    private HashMap<String, Object> commitMap;
    private boolean canSubmit = false;

    private boolean isCompleteHead;             // 是否已设置头像
    private Bitmap headPicBitmap;               // 头像btm
    private String pickFile;                    // 头像地址

    private UrlParam urlParam = UrlParam.reqRegister;  // 默认为常规注册
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isCanBack(false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.r1_user_reg_info_act);

        initTitle();
        initView();
        initData();
        initEvent();
    }

    private void initTitle() {
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void initView() {
        txt_reg_info_nickname = (EditText) findViewById(R.id.edtTxt_reg_info_nickname);
        btn_reg_info_submit = (RelativeLayout) findViewById(R.id.btn_reg_submit);
        img_header = (ImageView) findViewById(R.id.img_header);
        txt_header_describe = (TextView) findViewById(R.id.txt_header_describe);
        fl_choose_man = (CustomFrameLayout) findViewById(R.id.fl_choose_man);
        fl_choose_woman = (CustomFrameLayout) findViewById(R.id.fl_choose_woman);
        fl_choose_man.show(R.id.has_choose_man);
        fl_choose_woman.show(R.id.no_choose_woman);

//        ModuleMgr.getCenterMgr().inputFilterSpace(txt_reg_info_nickname);
        txt_reg_info_nickname.addTextChangedListener(textWatcher);
    }

    private void initData() {
        commitMap = new HashMap<>();
    }

    private void initEvent() {
        img_header.setOnClickListener(listener);
        btn_reg_info_submit.setOnClickListener(listener);
        findViewById(R.id.rl_boy).setOnClickListener(listener);
        findViewById(R.id.rl_girl).setOnClickListener(listener);
    }


    // 填充性别布局
    private void fillGender(boolean isMan) {
        if (isMan) {
            fl_choose_man.show(R.id.has_choose_man);
            fl_choose_woman.show(R.id.no_choose_woman);
            txt_header_describe.setText(getResources().getString(R.string.upload_head_man));
            return;
        }
        fl_choose_man.show(R.id.no_choose_man);
        fl_choose_woman.show(R.id.has_choose_woman);
        txt_header_describe.setText(getResources().getString(R.string.upload_head_female));
    }

    // 填充提交数据
    private void fillCommitMap() {
        // 头像
        commitMap.put("avatar", _photoUrl);
        // 昵称
        commitMap.put("nickname", _nickname);
        // 性别
        commitMap.put("gender", _gender);

    }

    // 设置提交btn
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void resetSubmit() {
        if (checkInputInfo()) { // 信息完整
            canSubmit = true;
            btn_reg_info_submit.setBackgroundResource(R.drawable.r1_btn_login_bg);
        } else {
            canSubmit = false;
            btn_reg_info_submit.setBackgroundColor(getResources().getColor(R.color.color_e8e8e8));
        }
    }


    private HTCallBack htCallBack;

    private void cancel() {
        if (htCallBack != null) {
            htCallBack.cancel();
        }
    }

    private NoDoubleClickListener listener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.img_header:  // 上传头像
                    ImgSelectUtil.getInstance().pickPhoto(RegInfoAct.this, RegInfoAct.this);
                    break;

                case R.id.rl_boy:
                    _gender = 1; // 性别男
                    fillGender(true);
                    resetSubmit();
                    break;

                case R.id.rl_girl:
                    _gender = 2; // 性别女
                    fillGender(false);
                    resetSubmit();
                    break;

                case R.id.btn_reg_submit:
//                    if (null != thirdInfo && !isUpHeadSuc) {   // 三方头像上传本服务器失败
//                        MMToast.showShort("头像审核失败，请重新上传");
//                    }

                    if (canSubmit) {
                        LoadingDialog.show(RegInfoAct.this, "保存资料中...", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancel(); // 取消请求
                            }
                        });
                        fillCommitMap();
                        htCallBack = ModuleMgr.getLoginMgr().onRegister(RegInfoAct.this, urlParam, commitMap, new RequestComplete() {
                            @Override
                            public void onRequestComplete(HttpResponse response) {
                                LoadingDialog.closeLoadingDialog(300);
                            }
                        });
                    }
                    break;

            }
        }
    };

    /**
     * 昵称改变监听
     */
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (TextUtils.isEmpty(s.toString()) || s.length() < 2) {
                canSubmit = false;
                btn_reg_info_submit.setBackgroundColor(getResources().getColor(R.color.color_e8e8e8));
                return;
            }
            _nickname = s.toString();
            resetSubmit();
        }
    };

    /**
     * 检查用户输入的信息是否有误
     */
    private boolean checkInputInfo() {
        _nickname = txt_reg_info_nickname.getText().toString();

        if (_gender == 2 && !isCompleteHead) {   // 上传头像
            return false;
        }
        if (_nickname.length() < 2) {            // 昵称限制
            return false;
        }
        if (TextUtils.isEmpty(_nickname)) {      // 未输入昵称
            return false;
        }
        return true;
    }

    @Override
    public void back() {
        UIShow.showNavUserAct(this);
        super.back();
        overridePendingTransition(R.anim.slide_fragment_right_in, R.anim.slide_fragment_right_out);
    }

    /**
     * 选择头像完成
     */
    @Override
    public void onComplete(final String... path) {
//        if (path == null || path.length == 0 || TextUtils.isEmpty(path[0])) {
//            return;
//        }
//        if (FileUtil.isExist(path[0])) {
//            LoadingDialog.show(RegInfoAct.this, "头像上传，请稍侯");
//            ModuleMgr.getCommonMgr().sendHttpFile(Constant.INT_AVATAR, path[0], null, new HttpMgr.IReqComplete() {
//                @Override
//                public void onReqComplete(HttpResult result) {
//                    LoadingDialog.closeLoadingDialog();
//                    if (result.isOk()) {
//                        UpLoadResult upLoadResult = (UpLoadResult) result.getBaseData();
//                        String pic = upLoadResult.getHttpPathPic();
//                        if (!TextUtils.isEmpty(pic)) {
//                            isCompleteHead = true;
//                            headPicBitmap = PhotoUtils.getSmallBitmap(path[0]);
//                            img_header.setImageBitmap(headPicBitmap);
//                            _photoUrl = ModuleMgr.getCenterMgr().getInterceptUrl(pic);
//                            resetSubmit();
//                        }
//                    } else {
//                        MMToast.showShort("头像上传失败，请重试");
//                    }
//                    FileUtil.deleteFile(path[0]);   // 上传完成后清除临时裁切文件
//                }
//            });
//        } else {
//            MMToast.showShort("图片地址无效");
//        }
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 释放回收Bitmap对象
        if (headPicBitmap != null && !headPicBitmap.isRecycled()) {
            headPicBitmap.isRecycled();
            headPicBitmap = null;
        }
    }

}
