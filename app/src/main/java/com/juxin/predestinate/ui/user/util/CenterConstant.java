package com.juxin.predestinate.ui.user.util;

/**
 * 用户中心常量维护类
 * Created by Su on 2017/5/2.
 */

public class CenterConstant {

    public static final String USER_CHECK_INFO_KEY = "check_info";       // 查看资料区分TAG
    public static final String USER_CHECK_OTHER_KEY = "check_other_info";// 查看TA人资料

    public static final int USER_CHECK_INFO_OWN = 0x11;    // 查看自己资料
    public static final int USER_CHECK_INFO_OTHER = 0x12;  // 查看TA人资料

    // 资料设置页跳转来源
    public static final String USER_SET_KEY = "user_set_key";   // 资料设置
    public static final int USER_SET_FROM_CHAT = 0x13;          // 聊天页跳转
    public static final int USER_SET_FROM_CHECK = 0x14;         // 个人主页跳转

    // 头像审核状态码
    public static final int USER_AVATAR_CHECKING = 0;  // 审核中
    public static final int USER_AVATAR_NO_PASS = 2;   // 未通过
}
