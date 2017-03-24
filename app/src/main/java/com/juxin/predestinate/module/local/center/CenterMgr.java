package com.juxin.predestinate.module.local.center;

import android.text.TextUtils;

import com.juxin.library.log.PSP;
import com.juxin.library.observe.ModuleBase;
import com.juxin.predestinate.bean.center.user.UserDetail;

/**
 * 个人中心管理类
 */
public class CenterMgr implements ModuleBase {
    private static final String INFO_SAVE_KEY = "myInfo"; // 本地化个人资料key
    private UserDetail userDetail = null;

    @Override
    public void init() {
    }

    @Override
    public void release() {
    }

    /**
     * 获取我的个人资料
     */
    public UserDetail getMyInfo() {
        if (userDetail == null) {
            userDetail = new UserDetail();
            try {
                String result = PSP.getInstance().getString(INFO_SAVE_KEY, "");
                if (!TextUtils.isEmpty(result)) {
                    userDetail.parseJson(result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return userDetail;
    }

    /**
     * 从服务器获取自己的个人资料
     */
    public void loadMyInfo() {
//        ModuleMgr.getHttpMgr().request()
    }
}