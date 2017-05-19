package com.juxin.predestinate.module.logic.config;

import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweightList;
import com.juxin.predestinate.bean.center.user.others.UserRemark;
import com.juxin.predestinate.bean.config.VideoVerifyBean;
import com.juxin.predestinate.bean.file.UpLoadResult;
import com.juxin.predestinate.bean.my.RedOneKeyList;
import com.juxin.predestinate.bean.my.RedbagList;
import com.juxin.predestinate.bean.net.BaseData;
import com.juxin.predestinate.bean.recommend.RecommendPeopleList;
import com.juxin.predestinate.bean.recommend.TagInfoList;
import com.juxin.predestinate.bean.settting.Setting;
import com.juxin.predestinate.bean.start.LoginResult;
import com.juxin.predestinate.bean.start.PhoneVerifyResult;
import com.juxin.predestinate.ui.user.check.bean.VideoConfig;
import com.juxin.predestinate.ui.user.check.bean.VideoSetting;
import com.juxin.predestinate.ui.user.check.secret.bean.UserVideoInfo;
import com.juxin.predestinate.ui.user.paygoods.bean.PayGoods;

import java.util.Map;

/**
 * 管理常用的Url参数信息
 */
public enum UrlParam {

    reqRegister("pubtest/quickReg", null, false),//注册接口
    reqLogin("public/login", LoginResult.class, false),//普通登录接口
    forgotPassword("Public/forgotPassword"),//找回密码
    reqForgotsms("Public/forgotsms", PhoneVerifyResult.class, false),//找回密码发送验证码

    sysRecommend("s/reco/SysRecommend", RecommendPeopleList.class, true),//推荐的人
    sysTags("s/reco/SysTags", TagInfoList.class),//推荐的人标签

    //================================ 配置项 ==================================
    CMDRequest(""),//cmd请求中默认拼接内容为空，通过resetHost方式进行使用
    checkUpdate("public/checkupNew", null, true),//检查软件升级
    staticConfig("public/getASet", null, false),//检查服务器静态配置
    serviceQQ("user/serviceQQ", null, false),//请求在线客服QQ
    statistics(Hosts.FATE_IT_GO, "xs/hdp/Action", null, false),//大数据统计
    reqSayHiList("pubtest/getSayHiUserNew", UserInfoLightweightList.class, true),//一键打招呼列表

    //============================== 设置页相关接口 =============================
    reqReqVerifyCode("public/sendSMS", PhoneVerifyResult.class, false),//获取手机验证码
    mobileAuth("user/bindCellPhone", PhoneVerifyResult.class, true),//手机认证
    modifyPassword("user/modifyPassword", null, true),//修改密码
    feedBack("user/feedback"),//意见反馈
    getSetting("s/uinfo/GetSetting", Setting.class, true),//获取设置信息
    updateSetting("s/uinfo/UpdateSetting", true),//设置信息修改
    //获取自己的音频、视频开关配置
    reqMyVideochatConfig(Hosts.FATE_IT_GO, "xs/message/MyVideochatConfig", VideoVerifyBean.class, true),
    //音视频开关修改
    setVideochatConfig(Hosts.FATE_IT_GO, "xs/message/SetVideochatConfig", null, true),
    //上传视频认证配置
    addVideoVerify(Hosts.FATE_IT_GO, "xs/message/AddVideoVerify", null, true),
    // 用户身份认证提交
    userVerify("User/Verify", true),
    // 获取用户验证信息(密)
    getVerifyStatus("User/getVerifyStatus", true),

    //============================== 用户资料相关接口 =============================
    reqSetInfo("i/uinfo/SecSetInfo", true),                   // 用户设置更新
    reqMyInfo("user/detail", UserDetail.class, true),         // 获取个人资料
    reqOtherInfo("user/otherdetail", UserDetail.class, true), // 获取他人资料
    updateMyInfo("user/modifyUserData"),                      // 修改用户个人信息
    reqYCoinInfo("ycoin/checkycoin"),                         // 用户Y币信息
    reqRedbagSum("fruit/redbagsum"),                          // 红包记录--红包总额
    reqAddBlack(Hosts.FATE_IT_GO, "xs/userrelation/AddBlack", null, true),          // 拉黑某用户
    reqRemoveBlack(Hosts.FATE_IT_GO, "xs/userrelation/RemoveBlack", null, true),    // 拉黑列表移除某用户
    reqSetRemarkName(Hosts.FATE_IT_GO, "xs/userrelation/SetRemakName", null, true),             // 设置用户备注名
    reqGetRemarkName(Hosts.FATE_IT_GO, "xs/userrelation/GetRemakName", UserRemark.class, true), // 获取用户备注名
    reqVideoChatConfig(Hosts.FATE_IT_GO, "xs/message/GetVideochatConfig", VideoConfig.class, true), // 获取他人音视频开关配置
    reqGetOpposingVideoSetting(Hosts.FATE_IT_GO, "xs/userrelation/GetOpposingVideoSetting", VideoSetting.class, true), // 获取接受他人音视频配置
    reqSetOpposingVideoSetting(Hosts.FATE_IT_GO, "xs/userrelation/SetOpposingVideoSetting", null, true), // 设置接受他人音视频配置


    getSimpleDetail("user/getSimpleDetail", null, true),   //获取轻量级的用户信息





    // 私密视频相关
    reqSetPopnum("video2/setPopnum"),        // 增加私密视频人气值
    reqSetViewTime("video2/setviewtime"),    // 设置私密视频观看次数
    reqUnlockVideo("video2/unlockvideo"),    // 解锁视频
    reqGetVideoList("video2/getvideolist", UserVideoInfo.class),  // 获取用户私密视频列表(暂时，后续可能在用户资料里返回)
    reqGetGiftList("gift/getUserGiftList"),  // 获取用户礼物列表(暂时，后续可能在用户资料里返回)


    //批量获取用户简略信息
    reqUserSimpleList("s/uinfo/USimple", UserInfoLightweightList.class, true),
    //获取昵称和头像的最近变更 list
    reqBasicUserInfoMsg("s/uinfo/NickChangedList", UserInfoLightweightList.class, true),

    // 上传头像
    uploadAvatar(Hosts.FATE_IT_HTTP_PIC, "index/uploadAvatar", null, false),

    // 上传相册
    uploadPhoto(Hosts.FATE_IT_HTTP_PIC, "index/uploadPhoto", null, true),

    // 删除照片
    deletePhoto("user/deletePic", false),

    // 上传文件
    uploadFile(Hosts.FATE_IT_HTTP_PIC, "index/upload", UpLoadResult.class, false),

    //============================== 用户资料模块相关接口 =============================
    //客户端获得用户红包列表
    reqRedbagList("fruit/redbaglist", RedbagList.class, true),
    //客户端用户红包入袋fruit/addredonekey
    reqAddredTotal("fruit/addredtotalnew", true),
    // 红包记录--红包入袋 -- 一键入袋(24不能提现)
    reqAddredonekey("fruit/addredonekey", RedOneKeyList.class, true),
    // 客户端请求用户提现列表
    reqWithdrawlist("fruit/withdrawlistNew", true),
    // 红包记录--提现申请
    reqWithdraw("fruit/withdrawNew", true),
    // 红包记录--提现申请获取地址
    reqWithdrawAddress("fruit/withdrawaddressNew", true),
    // 红包记录--提现申请修改地址
    reqWithdrawModify("fruit/withdrawmodifyNew", true),
    // 获取礼物列表
    getGiftLists("gift/getGifts", true),
    // 获取钻石余额
    getMyDiamand("gift/getMyDiamand", false),
    // 索要礼物
    begGift("gift/begGift", true),
    //接收礼物
    receiveGift("gift/receiveGift", null, true),
    // 手机验证
    sendSMS("public/sendSMS", true),
    // 手机验证
    bindCellPhone("user/bindCellPhone", true),
    // 最近来访
    viewMeList("user/viewMeList", true),
    // 索要礼物群发
    qunFa(Hosts.FATE_IT_GO, "xs/discovery/Qunfa", null, true),
    // 索要礼物群发
    sendGift("gift/sendGift", true),
    //女用户群发累计发送的用户数
    qunCount(Hosts.FATE_IT_GO, "xs/discovery/QunCount", null, true),
    // 我关注的列表
    getFollowing("MoneyTree/getFollowing", true),
    // 关注我的列表
    getFollowers("MoneyTree/getFollowers", true),
    // 取消关注某某
    unfollow("follow/unfollow", true),
    // 关注某某
    follow("follow/follow", true),
    // 上传身份证照片
    uploadIdCard("User/uploadIdCard", true),

    //================= 发现 ===========
    //举报
    complainBlack(Hosts.FATE_IT_GO, "xs/userrelation/ComplainBlack", null, true),
    //获取发现列表
    getMainPage(Hosts.FATE_IT_GO, "xs/discovery/MainPage", UserInfoLightweightList.class, true),
    //获取附近的人列表
    getNearUsers2(Hosts.FATE_IT_GO, "xs/discovery/NearUsers2", UserInfoLightweightList.class, true),
    //获取我的好友列表
    getMyFriends(Hosts.FATE_IT_GO, "xs/userrelation/GiftFriends", UserInfoLightweightList.class, true),
    //获取黑名单列表
    getMyDefriends(Hosts.FATE_IT_GO, "xs/userrelation/BlackList", UserInfoLightweightList.class, true),

    //============ 支付 =============
    reqCommodityList("user/payListNode", PayGoods.class),  // 商品列表

    reqWX(Hosts.FATE_IT_PROTOCOL, "user/wxAllPay", null, true),  //微信支付

    reqUnionPay(Hosts.FATE_IT_CUP_HTTP, "user/unionPay", null, true),  //银联支付

    reqAlipay(Hosts.FATE_IT_PROTOCOL, "user/alipay", null, true),  //银联支付

    reqPhoneCard(Hosts.FATE_IT_CUP_HTTP, "user/card", null, true),  //手机卡

    reqSearchPhoneCard(Hosts.FATE_IT_CUP_HTTP, "user/checkSZPay", null, true),  //手机卡查询

    reqangelPayF(Hosts.FATE_IT_PROTOCOL, "user/angelPayF", null, true),   // 充值记录页面 银联语音 查询之前是否支付过

    reqangelPay(Hosts.FATE_IT_PROTOCOL, "user/angelPay", null, true),   //充值记录页面 银联语音 没有绑定用户接口

    reqangelPayB(Hosts.FATE_IT_PROTOCOL, "user/angelPayB", null, true),   //充值记录页面 银联语音 绑定用接口

    reqAnglePayQuery(Hosts.FATE_IT_PROTOCOL, "user/anglePayQuery", null, true),   //充值记录页面 查询

    reqAliWapPay(Hosts.FATE_IT_CUP_HTTP, "user/aliWapPay", null, true),   //支付宝wap充值

    reqCustomFace(Hosts.FATE_IT_GO, "xs/message/GetCustomFace", null, true),   //获取自定义表情列表

    delCustomFace(Hosts.FATE_IT_GO, "xs/message/DelCustomFace", null, true),   //删除自定义表情

    AddCustomFace(Hosts.FATE_IT_GO, "xs/message/AddCustomFace", null, true),   //添加自定义表情

    // 最后一个，占位
    LastUrlParam("");

    // -------------------------------内部处理逻辑----------------------------------------

    private String host = Hosts.HOST_URL;    //请求host
    private String spliceUrl = null;            //接口url，与host拼接得到完整url
    private boolean needLogin = false;          //请求是否需要登录才会发送
    private Class<? extends BaseData> parseClass = null;//请求返回体解析类

    // --------------构造方法 start--------------

    /**
     * host+接口url+解析bean
     */
    UrlParam(final String host, final String spliceUrl, final Class<? extends BaseData> parseClass, final boolean needLogin) {
        this.host = host;
        this.spliceUrl = spliceUrl;
        this.parseClass = parseClass;
        this.needLogin = needLogin;
    }

    /**
     * 接口url+解析bean+是否需要登录
     */
    UrlParam(final String spliceUrl, final Class<? extends BaseData> parseClass, final boolean needLogin) {
        this(Hosts.HOST_URL, spliceUrl, parseClass, needLogin);
    }

    /**
     * 接口url+解析bean
     */
    UrlParam(final String spliceUrl, final Class<? extends BaseData> parseClass) {
        this(spliceUrl, parseClass, false);
    }

    /**
     * 接口url+是否需要登录
     */
    UrlParam(final String spliceUrl, final boolean needLogin) {
        this(spliceUrl, null, needLogin);
    }

    /**
     * 接口url
     */
    UrlParam(final String spliceUrl) {
        this(spliceUrl, null);
    }

    // --------------构造方法 end--------------

    /**
     * 重设请求host
     *
     * @param host host地址
     * @return UrlParam
     */
    public UrlParam resetHost(String host) {
        this.host = host;
        return this;
    }

    /**
     * 重设接口url，特殊情况使用
     *
     * @param spliceUrl 接口url
     */
    public void resetSpliceUrl(String spliceUrl) {
        this.spliceUrl = spliceUrl;
    }

    /**
     * 是否需要登录才能发送的请求
     *
     * @return boolean值
     */
    public boolean isNeedLogin() {
        return needLogin;
    }

    /**
     * 获取完整Url
     *
     * @return url
     */
    public String getFinalUrl() {
        if (host.equals(Hosts.NO_HOST)) {
            return spliceUrl;
        }
        return host + spliceUrl;
    }

    /**
     * 获取一个实现了BaseData接口的实例
     *
     * @return 有Class的newInstance生成的实例
     */
    public BaseData getBaseData() {
        BaseData baseData = null;
        try {
            if (parseClass != null) baseData = parseClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseData;
    }

    /**
     * 获取完整拼接参数的Url，用于缓存url等
     *
     * @param param 需要传送的参数
     * @return spliceUrl
     */
    public String getEntireUrl(Map<String, Object> param) {
        String url = this.spliceUrl;
        if (param != null) {
            for (Object o : param.entrySet()) {
                Map.Entry entry = (Map.Entry) o;
                Object key = entry.getKey();
                Object val = entry.getValue();
                url = url.replaceAll("\\{" + key.toString() + "\\}", val.toString());
            }
        }
        if (!host.equals(Hosts.NO_HOST)) {
            url = host + url;
        }
        return url;
    }
}