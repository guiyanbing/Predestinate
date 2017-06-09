package com.juxin.predestinate.module.local.statistics;

import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IQQ on 2017/6/9.
 */

public class StatisticsMessage {
    //    消息
    //    我的好友(普通点击)
    public static void firendClick() {
        Statistics.userBehavior(SendPoint.menu_xiaoxi_myfriend);
    }

    //黑名单(普通点击)
    public static void blackUser() {
        Statistics.userBehavior(SendPoint.menu_xiaoxi_myfriend_lahei);
    }

    //消息->我的好友->黑名单->查看用户资料
    public static void blackUserinfo(long touid) {
        Statistics.userBehavior(SendPoint.menu_xiaoxi_myfriend_lahei_viewuserinfo, touid);
    }

    //消息->我的好友->黑名单->查看用户资料
    public static void blackRemove(long touid) {
        Statistics.userBehavior(SendPoint.menu_xiaoxi_myfriend_lahei_remove, touid);
    }

    //消息->我的好友->打开聊天框(需要传递to_uid)
    public static void openFirendChat(long touid, int unReadNum) {
        Map<String, Object> parms = new HashMap<>();
        parms.put("unread_num", unReadNum);
        Statistics.userBehavior(SendPoint.menu_xiaoxi_myfriend_chatframe, touid);
    }

    //消息->我的好友->打开聊天框(需要传递to_uid)
    public static void openChat(long touid, int unReadNum) {
        Map<String, Object> parms = new HashMap<>();
        parms.put("unread_num", unReadNum);
        Statistics.userBehavior(SendPoint.menu_xiaoxi_chatframe, touid);
    }


    //消息->删除用户
    public static void deleteUser(List<BaseMessage> lst) {
        long[] users = new long[lst != null ?lst.size():0];
        for (int i = 0; i < users.length; i++) {
            users[i] = lst.get(i).getId();
        }
        Map<String, Object> parms = new HashMap<>();
        parms.put("to_uid", users);
        Statistics.userBehavior(SendPoint.menu_xiaoxi_deluser, parms);
    }


    //消息->谁关注我(普通点击)
    public static void whoFollowMe() {
        Statistics.userBehavior(SendPoint.menu_xiaoxi_sgzw);
    }


    //消息->谁关注我->我关注的(普通点击)
    public static void meFollow() {
        Statistics.userBehavior(SendPoint.menu_xiaoxi_sgzw_wgz);
    }

    ///消息->谁关注我->我关注的->取消关注
    public static void cancelFollow(long touid) {
        Statistics.userBehavior(SendPoint.menu_xiaoxi_sgzw_wgz_cancelfollow, touid);
    }

    //谁关注我->我关注的->查看用户资料
    public static void seeFollowUserInfo(long touid) {
        Statistics.userBehavior(SendPoint.menu_xiaoxi_sgzw_wgz_cancelfollow, touid);
    }

    //消息->谁关注我->关注我的(普通点击)
    public static void followMe() {
        Statistics.userBehavior(SendPoint.menu_xiaoxi_sgzw_gzw);
    }

    ///消息->谁关注我->关注我的->升级VIP会员,查看关注用户资料(男用户)
    public static void followMeToVip(long touid) {
        Statistics.userBehavior(SendPoint.menu_xiaoxi_sgzw_gzw_vippay, touid);
    }

    //消息->谁关注我->关注我的->查看用户资料
    public static void followMeToSeeUserinfo(long touid) {
        Statistics.userBehavior(SendPoint.menu_xiaoxi_sgzw_gzw_viewuserinfo, touid);
    }

    //消息->谁关注我->关注我的->取消关注
    public static void followMeToCancel(long touid) {
        Statistics.userBehavior(SendPoint.menu_xiaoxi_sgzw_gzw_cancelfollow, touid);
    }

    //消息->谁关注我->关注我的->关注TA
    public static void followMeToFollow(long touid) {
        Statistics.userBehavior(SendPoint.menu_xiaoxi_sgzw_gzw_followit, touid);
    }
}
