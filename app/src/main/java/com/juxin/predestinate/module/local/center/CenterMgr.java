package com.juxin.predestinate.module.local.center;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.juxin.library.log.PLogger;
import com.juxin.library.log.PSP;
import com.juxin.library.log.PToast;
import com.juxin.library.observe.ModuleBase;
import com.juxin.library.observe.MsgMgr;
import com.juxin.library.observe.MsgType;
import com.juxin.library.observe.PObserver;
import com.juxin.library.utils.StringUtils;
import com.juxin.mumu.bean.utils.FileUtil;
import com.juxin.mumu.bean.utils.MMToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.bean.file.UpLoadResult;
import com.juxin.predestinate.bean.settting.Setting;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.logic.request.RequestParam;
import com.juxin.predestinate.module.logic.socket.IMProxy;
import com.juxin.predestinate.module.util.CommonUtil;
import com.juxin.predestinate.ui.user.edit.EditKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 个人中心管理类
 */
public class CenterMgr implements ModuleBase, PObserver {

    private static final String INFO_SAVE_KEY = "INFO_SAVE_KEY"; // 本地化个人资料key
    private static final String SETTING_SAVE_KEY = "SETTING_SAVE_KEY"; // 本地化设置key
    private UserDetail userDetail = null;
    private Setting setting = null;

    @Override
    public void init() {
        MsgMgr.getInstance().attach(this);
    }

    @Override
    public void release() {
    }

    @Override
    public void onMessage(String key, Object value) {
        switch (key) {
            case MsgType.MT_App_Login:
                PLogger.d("---MT_App_Login--->" + value);
                if ((Boolean) value) {
                    IMProxy.getInstance().connect();//登录成功之后连接socket
                    reqMyInfo();// 请求个人资料
//                    reqSetting();//请求设置信息
                } else {
                    IMProxy.getInstance().logout();//退出登录的时候退出socket
                    userDetail = null;
                    setMyInfo(null);
                    putSettingPsp(null);
                }
                break;
            case MsgType.MT_App_CoreService://socket已连接，登录
                IMProxy.getInstance().login();
                break;

            case MsgType.MT_Update_MyInfo:
                reqMyInfo();
                break;
        }
    }

    /**
     * 请求手机验证码
     *
     * @param mobile
     * @param complete
     */
    public void reqVerifyCodeEx(String mobile, RequestComplete complete) {
        HashMap<String, Object> getparam = new HashMap<>();
        getparam.put("cellPhone", mobile);
        getparam.put("type", "1");
        ModuleMgr.getHttpMgr().reqGet(UrlParam.reqReqVerifyCode, null, getparam, RequestParam.CacheType.CT_Cache_Url, true, complete);
    }


    /**
     * 手机认证
     *
     * @param mobile   手机号
     * @param code     验证码
     * @param complete
     */
    public void mobileAuthEx(String mobile, String code, RequestComplete complete) {
        HashMap<String, Object> getParams = new HashMap<>();
        getParams.put("cellPhone", mobile);
        getParams.put("verifyCode", code);
        ModuleMgr.getHttpMgr().reqGet(UrlParam.mobileAuth, null, getParams, RequestParam.CacheType.CT_Cache_No, true, complete);
    }

    /**
     * 意见反馈
     *
     * @param contract 联系方式
     * @param views    意见
     * @param complete
     */
    public void feedBack(String contract, String views, RequestComplete complete) {
        HashMap<String, Object> postparam = new HashMap<>();
        postparam.put("contract", contract);
        postparam.put("views", views);
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.feedBack, postparam, complete);
    }

    /**
     * 昵称：个人资料限制昵称最大字数
     * 过滤空格 禁止注册的时候 昵称带有空格
     *
     * @param edit
     */
    public void inputFilterSpace(final EditText edit) {
        edit.addTextChangedListener(new TextWatcher() {
            int cou = 0;
            int selectionEnd = 0;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cou = before + count;
                String editable = edit.getText().toString();
                String str = StringUtils.noSpace(editable);
                if (!editable.equals(str)) {
                    edit.setText(str);
                }
                edit.setSelection(edit.length());
                cou = edit.length();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (cou > 7) {
                    selectionEnd = edit.getSelectionEnd();
                    s.delete(7, selectionEnd);
                    if (!TextUtils.isEmpty(s)) {
                        edit.setText(s.toString());
                    }
                }
            }
        });
    }

    /**
     * 获取我的个人资料
     */
    public UserDetail getMyInfo() {
        if (userDetail == null) {
            userDetail = new UserDetail();
            String result = PSP.getInstance().getString(INFO_SAVE_KEY, "");
            if (!TextUtils.isEmpty(result)) {
                userDetail.parseJson(result);
            }
        }
        return userDetail;
    }

    /**
     * 获取自己的个人资料
     */
    public void reqMyInfo() {
        Map<String, Object> getParams = new HashMap<>();
        getParams.put("ver", Constant.SUB_VERSION);

        ModuleMgr.getHttpMgr().reqGetAndCacheHttp(UrlParam.reqMyInfo, getParams, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                String responseStr = response.getResponseString();
                if (userDetail == null) userDetail = new UserDetail();
                userDetail.parseJson(responseStr);
                setMyInfo(responseStr);         // 保存到SP
                if (!response.isCache()) {
                    MsgMgr.getInstance().sendMsg(MsgType.MT_MyInfo_Change, null);
                }
            }
        });
    }

    /**
     * 获取他人用户详细信息
     */
    public void reqOtherInfo(final long uid, RequestComplete complete) {
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.reqOtherInfo, new HashMap<String, Object>() {
            {
                put("uid", uid);
            }
        }, complete);
    }

    /**
     * 批量获取用户简略信息
     */
    public void reqUserSimpleList(final long[] uidList, RequestComplete complete) {
        HashMap<String, Object> post_param = new HashMap<>();
        post_param.put("uidlist", uidList);
        ModuleMgr.getHttpMgr().reqPostAndCacheHttp(UrlParam.reqUserSimpleList, post_param, complete);
    }

    /**
     * 修改个人信息
     */
    public void updateMyInfo(final HashMap<String, Object> params) {
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.updateMyInfo, params, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                if (response.isOk()) {
                    reqMyInfo();
                    return;
                }
                PToast.showShort("修改失败");
            }
        });
    }

    /**
     * 上传头像
     *
     * @param url      图片本地地址
     * @param complete 上传完成回调
     */
    public void uploadAvatar(final String url, final RequestComplete complete) {
        if (FileUtil.isExist(url)) {
            ModuleMgr.getMediaMgr().sendHttpFile(Constant.INT_AVATAR, url, new RequestComplete() {
                @Override
                public void onRequestComplete(HttpResponse response) {
                    if (response.isOk()) {
                        FileUtil.deleteFile(url);  // 删除裁切文件
                        UpLoadResult upLoadResult = (UpLoadResult) response.getBaseData();
                        String pic = upLoadResult.getHttpPathPic();
                        if (TextUtils.isEmpty(pic)) {
                            return;
                        }
                        final String avatarUrl = getInterceptUrl(pic);
                        HashMap<String, Object> postParams = new HashMap<>();
                        postParams.put(EditKey.s_key_avatar, avatarUrl);
                        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.updateMyInfo, postParams, complete);
                    }
                }
            });
        } else {
            LoadingDialog.closeLoadingDialog();
            MMToast.showShort("图片地址无效");
        }
    }

    /**
     * 保存个人信息Json串到SP
     */
    private void setMyInfo(String resultStr) {
        PSP.getInstance().put(INFO_SAVE_KEY, resultStr);
    }

    /**
     * 图片上传时截取的字符串
     * <p>
     * 短存储： oss
     * 长存储： jxfile
     */
    public String getInterceptUrl(String picUrl) {
        if (TextUtils.isEmpty(picUrl)) return "";
        String tag = Constant.STR_SHORT_TAG;
        if (picUrl.contains(Constant.STR_LONG_TAG)) {
            tag = Constant.STR_LONG_TAG;
        }
        return StringUtils.getAfterWithFlag(picUrl, tag);
    }

    /*设置信息*/

    /**
     * 获取系统设置信息
     */
    public void reqSetting() {
        ModuleMgr.getHttpMgr().reqGetAndCacheHttp(UrlParam.getSetting, null, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                if (response.isOk()) {
                    setting = (Setting) response.getBaseData();
                    try {
                        JSONObject json = new JSONObject(response.getResponseString());
                        putSettingPsp(json.optString("res"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 更新系统设置信息
     */
    public void updateSetting(HashMap<String, Object> post_param) {
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.updateSetting, post_param, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                if (response.isOk()) {
                    PToast.showShort(App.context.getResources().getString(R.string.toast_update_ok));
                } else {
                    PToast.showShort(CommonUtil.getErrorMsg(response.getMsg()));
                }
            }
        });
    }

    /**
     * 获取我的设置信息
     */
    public Setting getSetting() {
        if (setting == null) {
            setting = new Setting();
            String result = PSP.getInstance().getString(SETTING_SAVE_KEY, "");
            if (!TextUtils.isEmpty(result)) {
                setting.parseJson(result);
            }
        }
        return setting;
    }

    /**
     * 保存设置信息Json串到SP
     */
    private void putSettingPsp(String resultStr) {
        PSP.getInstance().put(SETTING_SAVE_KEY, resultStr);
    }

}