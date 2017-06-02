package com.juxin.predestinate.ui.user.util;

/**
 * 用户中心常量维护类
 * Created by Su on 2017/5/2.
 */

public class CenterConstant {

    public static final String USER_CHECK_INFO_KEY = "check_info";        // 查看资料区分TAG
    public static final String USER_CHECK_OTHER_KEY = "check_other_info"; // 查看TA人资料
    public static final String USER_CHECK_VIDEO_KEY = "check_other_video";// 查看TA人视频

    public static final int USER_CHECK_INFO_OWN = 0x11;      // 查看自己资料
    public static final int USER_CHECK_INFO_OTHER = 0x12;    // 查看TA人资料
    public static final int USER_CHECK_CONNECT_OTHER = 0x13; // 查看TA人资料 联系方式

    // 资料设置页跳转来源
    public static final String USER_SET_KEY = "user_set_key";   // 资料设置
    public static final int USER_SET_FROM_CHAT = 0x13;          // 聊天页跳转
    public static final int USER_SET_FROM_CHECK = 0x14;         // 个人主页跳转
    public static final int USER_SET_REQUEST_CODE = 0x15;       // 请求码
    public static final int USER_SET_RESULT_CODE = 0x16;        // 返回码

    // 头像审核状态码
    public static final int USER_AVATAR_UNCHECKING = 0;  // 未审核
    public static final int USER_AVATAR_PASS = 1;        // 通过
    public static final int USER_AVATAR_NO_PASS = 2;     // 拒绝
    public static final int USER_AVATAR_NO_UPLOAD = 3;   // 未上传
    public static final int USER_AVATAR_NICE = 4;        // 好
    public static final int USER_AVATAR_VERY_NICE = 5;   // 很好
}
