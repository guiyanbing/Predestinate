package com.juxin.predestinate.bean.config;

import android.text.TextUtils;

import com.juxin.predestinate.bean.net.BaseData;
import com.juxin.predestinate.module.logic.config.Hosts;

import org.json.JSONObject;

import java.util.List;

/**
 * 在线配置
 * Created by ZRP on 2017/4/14.
 */
public class CommonConfig extends BaseData {

    private String service_qq;      //客服QQ
    private String entrance_url;    //暂时为书城地址
    private String extra_url;       //1.0旧版广场地址
    private String square_url;      //广场地址
    private int minmoney;           //最小提现金额（分）

    private String push_url;        //活动页面地址
    private int pushshow;           //是否进行活动推送的展示
    private double pushrate;        //活动弹框展示宽高比

    private List<Diamond> diamondList;  //钻石配比列表

    private long plugin_version;        //插件版本号控制
    private int audiochat_minute_cost;  //语音通话每分钟费用
    private int videochat_minute_cost;  //视频通话每分钟费用
    private String video_chat_apk_url;  //视频插件地址
    private int checkYellow;            //鉴黄检测间隔时间,单位秒
    private int checkYellowFirst = 5;   //鉴黄首次截图时间，单位秒
    private boolean isVideoCallNeedVip; //发起视频聊天是否需要VIP
    private boolean isAudioCallNeedVip; //发起音频聊天是否需要VIP

    private PayTypeList payTypeList;    //支付方式控制
    private int secretary_dialog;       //小秘书对话框是否开放，1为开放，0为不开放

    /**
     * @return 是否展示活动弹窗
     */
    public boolean canPushShow() {
        return pushshow == 1;
    }

    /**
     * @return 小秘书对话框是否开放，暂时只判断是否为1
     */
    public boolean canSecretaryShow() {
        return secretary_dialog == 1;
    }

    @Override
    public void parseJson(String jsonStr) {
        JSONObject jsonObject = getJsonObject(jsonStr);
        service_qq = jsonObject.optString("service_qq");
        entrance_url = jsonObject.optString("entrance_url");
        extra_url = jsonObject.optString("extra_url");
        square_url = jsonObject.optString("square_url");
        minmoney = jsonObject.optInt("mmoney");

        push_url = jsonObject.optString("push_url");
        pushshow = jsonObject.optInt("pushshow");
        pushrate = jsonObject.optDouble("pushrate");

        diamondList = (List<Diamond>) getBaseDataList(getJsonArray(jsonObject.optString("diamand")), Diamond.class);

        plugin_version = jsonObject.optLong("plugin_version");
        audiochat_minute_cost = jsonObject.optInt("audiochat_minute_cost");
        videochat_minute_cost = jsonObject.optInt("videochat_minute_cost");
        video_chat_apk_url = jsonObject.optString("videochat_apk_url");
        checkYellow = jsonObject.optInt("check_yellow");
        isVideoCallNeedVip = jsonObject.optInt("videochat_call_vip") == 1;
        isAudioCallNeedVip = jsonObject.optInt("audiochat_call_vip") == 1;
        checkYellowFirst = jsonObject.optInt("frist_screenshot");

        secretary_dialog = jsonObject.optInt("secretary_dialog");

        payTypeList = new PayTypeList();
        payTypeList.parseJson(jsonObject.optString("paytype"));
    }

    public String getService_qq() {
        return service_qq;
    }

    public String getEntrance_url() {
        return entrance_url;
    }

    public String getExtra_url() {
        return extra_url;
    }

    public String getSquare_url() {
        return TextUtils.isEmpty(square_url) ? Hosts.LOCAL_SQUARE_URL : square_url;
    }

    public int getMinmoney() {
        return minmoney;
    }

    public String getPush_url() {
        return push_url;
    }

    public int getPushshow() {
        return pushshow;
    }

    public double getPushrate() {
        return pushrate;
    }

    public List<Diamond> getDiamondList() {
        return diamondList;
    }

    public long getPlugin_version() {
        return plugin_version;
    }

    public int getAudiochat_minute_cost() {
        return audiochat_minute_cost;
    }

    public int getVideochat_minute_cost() {
        return videochat_minute_cost;
    }

    public String getVideo_chat_apk_url() {
        return video_chat_apk_url;
    }

    public int getCheckYellow() {
        return checkYellow;
    }

    public boolean isVideoCallNeedVip() {
        return isVideoCallNeedVip;
    }

    public boolean isAudioCallNeedVip() {
        return isAudioCallNeedVip;
    }

    public PayTypeList getPayTypeList() {
        return payTypeList;
    }

    public int getCheckYellowFirst() {
        return checkYellowFirst;
    }

    public int getSecretary_dialog() {
        return secretary_dialog;
    }

    @Override
    public String toString() {
        return "CommonConfig{" +
                "service_qq='" + service_qq + '\'' +
                ", entrance_url='" + entrance_url + '\'' +
                ", extra_url='" + extra_url + '\'' +
                ", minmoney=" + minmoney +
                ", push_url='" + push_url + '\'' +
                ", pushshow=" + pushshow +
                ", pushrate=" + pushrate +
                ", diamondList=" + diamondList +
                ", plugin_version=" + plugin_version +
                ", audiochat_minute_cost=" + audiochat_minute_cost +
                ", videochat_minute_cost=" + videochat_minute_cost +
                ", video_chat_apk_url='" + video_chat_apk_url + '\'' +
                ", checkYellow=" + checkYellow +
                ", isVideoCallNeedVip=" + isVideoCallNeedVip +
                ", isAudioCallNeedVip=" + isAudioCallNeedVip +
                ", secretary_dialog=" + secretary_dialog +
                ", payTypeList=" + payTypeList +
                '}';
    }
}
