package com.juxin.predestinate.ui.discover;

import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.module.local.statistics.SendPoint;
import com.juxin.predestinate.module.local.statistics.Statistics;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.ui.user.paygoods.GoodsConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    public static void onRecomendRefresh(List<UserInfoLightweight> list, boolean isNear) {
        JSONArray array = new JSONArray();
        JSONObject jsonObject = null;
        try {
            for (UserInfoLightweight info : list) {
                jsonObject = new JSONObject();
                jsonObject.put("uid", info.getUid());
                jsonObject.put("to_uid_tag", "");
                jsonObject.put("ui_source", isNear ? "fujin" : "all");
                array.put(jsonObject);
            }
            Statistics.userBehavior(SendPoint.menu_faxian_downrefresh.toString(), 0, array.toString());
        } catch (JSONException ex) {

        }
    }

    /**
     * 发现->查看用户资料
     *
     * @param uid
     * @param index
     * @param isNear
     */
    public static void onRecomendViewuser(long uid, int index, boolean isNear) {
        Map<String, Object> parms = new HashMap<>();
        parms.put("index", index);
        parms.put("to_uid_tag", "");
        parms.put("ui_source", isNear ? "fujin" : "all");
        Statistics.userBehavior(SendPoint.menu_faxian_viewuserinfo, uid, parms);

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
    public static void onClickCancle() {
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
        Map<String, Object> parms = new HashMap<>();
        parms.put("index", index);
        parms.put("to_uid_tag", "");
        parms.put("ui_source", isNear ? "fujin" : "all");
        Statistics.userBehavior(SendPoint.menu_faxian_tuijian_fujin_sayhello, uid, parms);
    }

    /**
     * 发现->热门->只看附近的人->群打招呼
     *
     * @param list
     */
    public static void onNearGroupSayHello(Map<Integer, Long> list) {
        JSONArray array = new JSONArray();
        JSONObject jsonObject = null;
        try {
            for (int index : list.keySet()) {
                jsonObject = new JSONObject();
                jsonObject.put("index", index);
                jsonObject.put("time", ModuleMgr.getAppMgr().getSecondTime());
                jsonObject.put("to_uid", list.get(index));
                jsonObject.put("to_uid_tag", "");
                jsonObject.put("uid", ModuleMgr.getCenterMgr().getMyInfo().getUid());
                jsonObject.put("ui_source", "fujin"); //群打招呼只有附近有
                array.put(jsonObject);
            }
            Statistics.userBehavior(SendPoint.menu_faxian_tuijian_fujin_batchsayhello.toString(), 0, array.toString());
        } catch (JSONException ex) {

        }
    }

    /**
     * 发现->热门->查看用户资料(外层传递touid)
     *
     * @param touid
     * @param index
     */
    public static void onHotViewUser(long touid, int index) {
        Map<String, Object> parms = new HashMap<>();
        parms.put("index", index);
        parms.put("to_uid_tag", "");
        Statistics.userBehavior(SendPoint.menu_faxian_hot_viewuserinfo, touid, parms);
    }

    /**
     * 发现->热门->图片数量按钮
     *
     * @param touid
     * @param picNum
     */
    public static void onClickHotImgNum(long touid, int picNum) {
        Map<String, Object> parms = new HashMap<>();
        parms.put("pic_num", picNum);
        parms.put("to_uid_tag", "");
        Statistics.userBehavior(SendPoint.menu_faxian_hot_picturenum, touid, parms);
    }

    /**
     * 发现->热门->发视频按钮
     *
     * @param touid
     */
    public static void onClickHotVideo(long touid) {
        Map<String, Object> parms = new HashMap<>();
        parms.put("to_uid_tag", "");
        Statistics.userBehavior(SendPoint.menu_faxian_hot_btnvideo, touid, parms);
    }

    /**
     * 发现->热门->发语音按钮
     *
     * @param touid
     */
    public static void onClickHotVoice(long touid) {
        Map<String, Object> parms = new HashMap<>();
        parms.put("to_uid_tag", "");
        Statistics.userBehavior(SendPoint.menu_faxian_hot_btnvoice, touid, parms);
    }

    /**
     * 发现->热门->发私信
     *
     * @param touid
     */
    public static void onClickHotMsg(long touid) {
        Map<String, Object> parms = new HashMap<>();
        parms.put("to_uid_tag", "");
        Statistics.userBehavior(SendPoint.menu_faxian_hot_btnsendmessage, touid, parms);
    }

    /**
     * 发现->热门->发礼物
     *
     * @param touid
     */
    public static void onClickHotGif(long touid) {
        Map<String, Object> parms = new HashMap<>();
        parms.put("to_uid_tag", "");
        Statistics.userBehavior(SendPoint.menu_faxian_hot_btngirl, touid, parms);
    }

    /**
     * 发现->热门->发礼物->赠送
     *
     * @param touid
     * @param gift_id
     * @param price
     */
    public static void onGiveGift(long touid, int gift_id, int price) {
        Map<String, Object> parms = new HashMap<>();
        parms.put("to_uid_tag", "");
        parms.put("price", price);
        parms.put("gift_id", gift_id);
        Statistics.userBehavior(SendPoint.menu_faxian_hot_btngirl_zongsong, touid, parms);
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
        Map<String, Object> parms = new HashMap<>();
        parms.put("to_uid_tag", "");
        parms.put("price", price);
        parms.put("pay_type", type);
        parms.put("gem_num", gem_num);
        Statistics.userBehavior(SendPoint.menu_faxian_hot_btngirl_zongsong_ljcz, touid, parms);
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
        Map<String, Object> parms = new HashMap<>();
        parms.put("to_uid_tag", "");
        Statistics.userBehavior(SendPoint.menu_faxian_hot_slideremove, touid);
    }

}
