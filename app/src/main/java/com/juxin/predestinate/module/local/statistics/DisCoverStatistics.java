package com.juxin.predestinate.module.local.statistics;

import com.alibaba.fastjson.JSON;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.ui.user.paygoods.GoodsConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发现统计
 * Created by zhang on 2017/6/9.
 */
public class DisCoverStatistics {

    /**
     * 发现->推荐(普通点击)
     */
    public static void onClickRecommend() {
        Statistics.userBehavior(SendPoint.menu_faxian_tuijian);
    }

    /**
     * 发现->热门(普通点击)
     */
    public static void onClickHot() {
        Statistics.userBehavior(SendPoint.menu_faxian_hot);
    }

    /**
     * 发现->下拉刷新(传递用户列表)
     *
     * @param list
     * @param isNear
     */
    public static void onRecommendRefresh(List<UserInfoLightweight> list, boolean isNear) {
        List<Map<String, Object>> users = new ArrayList<>();
        Map<String, Object> user;
        for (UserInfoLightweight info : list) {
            user = new HashMap<>();
            user.put("uid", info.getUid());//用户UID
            user.put("tag", "");//推荐系统预留推荐用户AB反馈标签当前返回空字符串
            user.put("ui_source", isNear ? "fujin" : "all");//用户列表数据来源取值范围(all,fujin)，默认all
            users.add(user);
        }
        Statistics.userBehavior(SendPoint.menu_faxian_tuijian_downrefresh.toString(), 0, JSON.toJSONString(users));
    }

    /**
     * 发现->查看用户资料
     *
     * @param uid
     * @param index
     * @param isNear
     */
    public static void onRecommendViewUser(long uid, int index, boolean isNear) {
        Map<String, Object> params = new HashMap<>();
        params.put("to_uid_tag", "");//推荐系统预留推荐用户AB反馈标签当前返回空字符串
        params.put("index", index + 1);//滚动框区域内的用户列表索引,由上往下计数,从1开始到N结束
        params.put("ui_source", isNear ? "fujin" : "all");//用户列表数据来源取值范围(all,fujin)，默认all
        Statistics.userBehavior(SendPoint.menu_faxian_tuijian_viewuserinfo, uid, params);

    }

    /**
     * 发现->更多->发现->更多->查看全部(普通点击)
     */
    public static void onClickLookAll() {
        Statistics.userBehavior(SendPoint.menu_faxian_tuijian_more_viewall);
    }

    /**
     * 发现->更多->发现->更多->只看附近的人(普通点击)
     */
    public static void onClickLookNear() {
        Statistics.userBehavior(SendPoint.menu_faxian_tuijian_more_viewfujin);
    }

    /**
     * 发现->更多->发现->更多->取消(普通点击)
     */
    public static void onClickCancel() {
        Statistics.userBehavior(SendPoint.menu_faxian_tuijian_more_cancel);
    }

    /**
     * 发现->热门->打招呼
     *
     * @param uid
     * @param index
     * @param isNear
     */
    public static void onSayHello(long uid, int index, boolean isNear) {
        Map<String, Object> params = new HashMap<>();
        params.put("to_uid_tag", "");//推荐系统预留推荐用户AB反馈标签,当前返回空字符串
        params.put("index", index + 1);//滚动框区域内的用户列表索引,由上往下计数,从1开始到N结束
        params.put("ui_source", isNear ? "fujin" : "all");//用户列表数据来源取值范围(all,fujin)，默认all
        Statistics.userBehavior(SendPoint.menu_faxian_tuijian_fujin_sayhello, uid, params);
    }

    /**
     * 发现->热门->只看附近的人->群打招呼
     *
     * @param list
     */
    public static void onNearGroupSayHello(Map<Integer, Long> list) {
        List<Map<String, Object>> users = new ArrayList<>();
        Map<String, Object> user;
        for (int index : list.keySet()) {
            user = new HashMap<>();
            user.put("time", ModuleMgr.getAppMgr().getSecondTime());//打招呼时间
            user.put("uid", ModuleMgr.getCenterMgr().getMyInfo().getUid());//打招呼的用户UID
            user.put("to_uid", list.get(index));//被打招呼的用户UID
            user.put("to_uid_tag", "");//推荐系统预留推荐用户AB反馈标签,当前返回空字符串
            user.put("index", index + 1);//滚动框区域内的用户列表索引,由上往下计数,从1开始到N结束
            user.put("ui_source", "fujin"); //群打招呼只有附近有
            users.add(user);
        }
        Statistics.userBehavior(SendPoint.menu_faxian_tuijian_fujin_batchsayhello.toString(), 0, users.toString());
    }

    /**
     * 发现->热门->查看用户资料(外层传递touid)
     *
     * @param touid
     * @param index
     */
    public static void onHotViewUser(long touid, int index) {
        Map<String, Object> params = new HashMap<>();
        params.put("to_uid_tag", "");//推荐系统预留推荐用户AB反馈标签当前返回空字符串
        params.put("index", index + 1);//本次打开功能，第几个查看的用户,从1开始计数
        Statistics.userBehavior(SendPoint.menu_faxian_hot_viewuserinfo, touid, params);
    }

    /**
     * 发现->热门->图片数量按钮
     *
     * @param touid
     * @param picNum
     */
    public static void onClickHotImgNum(long touid, int picNum) {
        Map<String, Object> params = new HashMap<>();
        params.put("pic_num", picNum);//照片数量
        params.put("to_uid_tag", "");//推荐系统预留推荐用户AB反馈标签当前返回空字符串
        Statistics.userBehavior(SendPoint.menu_faxian_hot_picturenum, touid, params);
    }

    /**
     * 发现->热门->发视频按钮
     *
     * @param touid
     */
    public static void onClickHotVideo(long touid) {
        Map<String, Object> params = new HashMap<>();
        params.put("to_uid_tag", "");//推荐系统预留推荐用户AB反馈标签当前返回空字符串
        Statistics.userBehavior(SendPoint.menu_faxian_hot_btnvideo, touid, params);
    }

    /**
     * 发现->热门->发语音按钮
     *
     * @param touid
     */
    public static void onClickHotVoice(long touid) {
        Map<String, Object> params = new HashMap<>();
        params.put("to_uid_tag", "");//推荐系统预留推荐用户AB反馈标签当前返回空字符串
        Statistics.userBehavior(SendPoint.menu_faxian_hot_btnvoice, touid, params);
    }

    /**
     * 发现->热门->发私信
     *
     * @param touid
     */
    public static void onClickHotMsg(long touid) {
        Map<String, Object> params = new HashMap<>();
        params.put("to_uid_tag", "");//推荐系统预留推荐用户AB反馈标签当前返回空字符串
        Statistics.userBehavior(SendPoint.menu_faxian_hot_btnsendmessage, touid, params);
    }

    /**
     * 发现->热门->发礼物
     *
     * @param touid
     */
    public static void onClickHotGif(long touid) {
        Map<String, Object> params = new HashMap<>();
        params.put("to_uid_tag", "");//推荐系统预留推荐用户AB反馈标签当前返回空字符串
        Statistics.userBehavior(SendPoint.menu_faxian_hot_btngirl, touid, params);
    }

    /**
     * 发现->热门->发礼物->赠送
     *
     * @param touid
     * @param gift_id
     * @param price
     */
    public static void onGiveGift(long touid, int gift_id, int price) {
        Map<String, Object> params = new HashMap<>();
        params.put("to_uid_tag", "");//推荐系统预留推荐用户AB反馈标签当前返回空字符串
        params.put("gift_id", gift_id);//礼物ID
        params.put("price", price / 10);//礼物价值(人民币)-人民币与钻石兑换为1:10
        Statistics.userBehavior(SendPoint.menu_faxian_hot_btngirl_zongsong, touid, params);
    }

    /**
     * 发现->热门->发礼物->赠送->立即充值
     *
     * @param touid
     * @param gem_num
     * @param price
     * @param payType
     */
    public static void onPayGift(long touid, int gem_num, String price, int payType) {
        String type = "";
        switch (payType) {
            case GoodsConstant.PAY_TYPE_WECHAT:
                type = "weixin";
                break;
            case GoodsConstant.PAY_TYPE_ALIPAY:
                type = "zhifubao";
                break;
            case GoodsConstant.PAY_TYPE_OTHER:
                type = "other";
                break;
            default:
                break;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("to_uid_tag", "");//推荐系统预留推荐用户AB反馈标签当前返回空字符串
        params.put("pay_type", type);//支付方式微信/支付宝/其他支付(weixin,zhifubao,other)
        params.put("price", price);//人民币金额(钻石对应的金额)
        params.put("gem_num", gem_num);//钻石数量
        Statistics.userBehavior(SendPoint.menu_faxian_hot_btngirl_zongsong_ljcz, touid, params);
    }

    /**
     * 发现->热门->发礼物->充值(普通点击,外层传递touid)
     *
     * @param touid
     */
    public static void onClickGiftPay(long touid) {
        Statistics.userBehavior(SendPoint.menu_faxian_hot_btngirl_pay, touid);
    }

    /**
     * 发现->热门->发礼物->滑动移除一个用户(普通点击,外层传递touid)
     *
     * @param touid
     */
    public static void onHotRemove(long touid) {
        Statistics.userBehavior(SendPoint.menu_faxian_hot_slideremove, touid);
    }

}
