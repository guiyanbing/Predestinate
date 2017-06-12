package com.juxin.predestinate.module.local.statistics;

import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.ui.user.paygoods.GoodsConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息模块统计项添加
 * Created by IQQ on 2017/6/9.
 */
public class StatisticsMessage {

    //我的好友(普通点击)
    public static void friendClick() {
        Statistics.userBehavior(SendPoint.menu_xiaoxi_myfriend);
    }

    //黑名单(普通点击)
    public static void blackUser() {
        Statistics.userBehavior(SendPoint.menu_xiaoxi_myfriend_lahei);
    }

    //消息->我的好友->黑名单->查看用户资料
    public static void blackUserInfo(long touid) {
        Statistics.userBehavior(SendPoint.menu_xiaoxi_myfriend_lahei_viewuserinfo, touid);
    }

    //消息->我的好友->黑名单->查看用户资料
    public static void blackRemove(long touid) {
        Statistics.userBehavior(SendPoint.menu_xiaoxi_myfriend_lahei_remove, touid);
    }

    //消息->我的好友->打开聊天框(需要传递to_uid)
    public static void openFriendChat(long touid, int unReadNum) {
//        Map<String, Object> parms = new HashMap<>();
//        parms.put("unread_num", unReadNum);
        Statistics.userBehavior(SendPoint.menu_xiaoxi_myfriend_chatframe, touid);
    }

    //消息->我的好友->打开聊天框(需要传递to_uid)
    public static void openChat(long touid, int unReadNum) {
//        Map<String, Object> parms = new HashMap<>();
//        parms.put("unread_num", unReadNum);
        Statistics.userBehavior(SendPoint.menu_xiaoxi_chatframe, touid);
    }

    //消息->删除用户
    public static void deleteUser(List<BaseMessage> lst) {
        if (lst == null || lst.isEmpty()) return;

        List<Long> uids = new ArrayList<>();
        for (BaseMessage message : lst) {
            uids.add(message.getId());
        }
        Map<String, Object> params = new HashMap<>();
        params.put("to_uid", uids);
        Statistics.userBehavior(SendPoint.menu_xiaoxi_deluser, params);
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
        Statistics.userBehavior(SendPoint.menu_xiaoxi_sgzw_wgz_viewuserinfo, touid);
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
    public static void followMeToSeeUserInfo(long touid) {
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

    // -----------------------------聊天框------------------------------

    //消息->谁关注我->关注我的->关注TA
    public static void chatSendBtn(String msg, long to_uid) {
        Map<String, Object> params = new HashMap<>();
        params.put("msg", msg);
        Statistics.userBehavior(SendPoint.chatframe_tool_btnsend, to_uid, params);
    }

    /**
     * 聊天框->工具栏->礼物按钮->赠送按钮->立即充值
     *
     * @param to_uid  产生交互的uid
     * @param gift_id 礼物ID
     * @param gemNum  礼物钻石数
     */
    public static void chatGiveGift(long to_uid, int gift_id, int gemNum) {
        Map<String, Object> map = new HashMap<>();
        map.put("gift_id", gift_id);
        map.put("price", gemNum / 10);
        Statistics.userBehavior(SendPoint.chatframe_tool_gift_give, to_uid, map);
    }

    /**
     * 聊天框->导航栏->充值弹窗确认支付按钮
     *
     * @param to_uid  产生交互的uid
     * @param payType 支付方式微信/支付宝/其他支付
     * @param price   人民币金额
     */
    public static void chatNavConfirmPay(SendPoint sendPoint, long to_uid, int payType, double price) {
        Map<String, Object> map = new HashMap<>();
        map.put("payType", getPayType(payType));
        map.put("price", (int) (price / 100));//元
        Statistics.userBehavior(sendPoint, to_uid, map);
    }

    /**
     * 根据整数支付类型获取对应等等字符串展示
     *
     * @param payType 支付类型
     * @return 支付类型
     */
    public static String getPayType(int payType) {
        String type;
        switch (payType) {
            case GoodsConstant.PAY_TYPE_WECHAT:
                type = GoodsConstant.PAY_TYPE_WECHAT_NAME;
                break;
            case GoodsConstant.PAY_TYPE_ALIPAY:
                type = GoodsConstant.PAY_TYPE_ALIPAY_NAME;
                break;
            case GoodsConstant.PAY_TYPE_OTHER:
                type = GoodsConstant.PAY_TYPE_OTHER_NAME;
                break;
            default:
                type = GoodsConstant.PAY_TYPE_WECHAT_NAME;
                break;
        }
        return type;
    }
}
