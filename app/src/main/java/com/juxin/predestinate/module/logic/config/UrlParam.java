package com.juxin.predestinate.module.logic.config;

import com.juxin.predestinate.bean.center.update.AppUpdate;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweightList;
import com.juxin.predestinate.bean.file.UpLoadResult;
import com.juxin.predestinate.bean.net.BaseData;
import com.juxin.predestinate.bean.recommend.RecommendPeopleList;
import com.juxin.predestinate.bean.recommend.TagInfoList;
import com.juxin.predestinate.bean.settting.Setting;
import com.juxin.predestinate.bean.start.LoginResult;
import com.juxin.predestinate.bean.start.PhoneVerifyResult;
import com.juxin.predestinate.bean.start.RegResult;
import com.juxin.predestinate.ui.user.paygoods.bean.PayGoods;
import com.juxin.predestinate.ui.xiaoyou.bean.FriendsList;
import com.juxin.predestinate.ui.xiaoyou.bean.LabelsList;
import com.juxin.predestinate.ui.xiaoyou.bean.SimpleFriendsList;

import java.util.Map;

/**
 * 管理常用的Url参数信息
 */
public enum UrlParam {
    reqRegister("pubtest/quickReg", null, false),//注册接口
    modifyUserData("user/modifyUserData", null, true),//修改用户资料
    reqLogin("public/login", LoginResult.class, false),//普通登录接口
    reqReqVerifyCode("public/sendSMS", PhoneVerifyResult.class, false),//获取手机验证码
    resetPassword("i/reg/ResetPassword"),//找回密码
    mobileAuth("user/bindCellPhone", PhoneVerifyResult.class, true),//手机认证
    feedBack("s/uinfo/FeedBack"),//意见反馈
    sysRecommend("s/reco/SysRecommend", RecommendPeopleList.class, true),//推荐的人
    sysTags("s/reco/SysTags", TagInfoList.class),//推荐的人标签
    getSetting("s/uinfo/GetSetting", Setting.class, true),//获取设置信息
    updateSetting("s/uinfo/UpdateSetting", true),//设置信息修改
    //检查软件升级
    checkUpdate("i/version/CheckVersion", AppUpdate.class, false),
    //检查服务器静态配置
    staticConfig(Constant.FATE_IT_HTTP, "public/getASet", null, false),

    CMDRequest(""),//cmd请求中默认拼接内容为空，通过resetHost方式进行使用

    reqSayHiList("s/reco/DayRecommend", UserInfoLightweightList.class, true),


    //============================== 用户资料相关接口 =============================

    reqSetInfo("i/uinfo/SecSetInfo", true),                  // 用户设置更新
    reqMyInfo("user/detail", UserDetail.class, true),    // 获取个人资料
    reqOtherInfo("s/uinfo/ODetail", UserDetail.class, true), // 获取他人资料
    updateMyInfo("s/uinfo/UpdateUserData"),                  // 修改用户个人信息

    //批量获取用户简略信息
    reqUserSimpleList("s/uinfo/USimple", UserInfoLightweightList.class, true),
    //获取昵称和头像的最近变更 list
    reqBasicUserInfoMsg("s/uinfo/NickChangedList", UserInfoLightweightList.class, true),

    // 上传文件
    uploadFile(Constant.HOST_FILE_SERVER_URL, "jxfile/Jxupload", UpLoadResult.class, false),

    //============================== 小友模块相关接口 =============================
    //好友标签分组成员
    reqTagGroupMember("s/friend/TagGroupMember", SimpleFriendsList.class, true),
    //增加自己的好友的 tag
    reqAddFriendTag("s/friend/AddFriendTag", null, true),
    //添加标签分组
    reqAddTagGroup("s/friend/AddTagGroup", LabelsList.class, true),
    //添加好友标签分组成员
    reqAddTagGroupMember("s/friend/AddTagGroupMember", SimpleFriendsList.class, true),
    //删除自己好友的 tag
    reqDelFriendTag("s/friend/DelFriendTag", SimpleFriendsList.class, true),
    //删除标签分组
    reqDelTagGroup("s/friend/DelTagGroup", SimpleFriendsList.class, true),
    //删除好友标签分组成员
    reqDelTagGroupMember("s/friend/DelTagGroupMember", SimpleFriendsList.class, true),
    //好友列表
    reqFriendList("s/friend/FriendList", SimpleFriendsList.class, true),
    //最近互动好友列表
    reqLatestInteractive("s/friend/LatestInteractive", FriendsList.class, true),
    //修改标签分组
    reqModifyTagGroup("s/friend/ModifyTagGroup", SimpleFriendsList.class, true),
    //好友标签分组
    reqTagGroup("s/friend/TagGroup", LabelsList.class, true),
    //送礼物
    givePresent("s/present/GivePresent", null, true),

    //============ 支付 =============
    reqCommodityList("user/payListNode", PayGoods.class),  // 商品列表

    reqWX(Constant.FATE_IT_PROTOCOL, "user/wxAllPay", null, true),  //微信支付

    reqUnionPay(Constant.FATE_IT_CUP_HTTP, "user/unionPay", null, true),  //银联支付

    reqAlipay(Constant.FATE_IT_PROTOCOL, "user/alipay", null, true),  //银联支付

    // 最后一个，占位
    LastUrlParam("");

    // -------------------------------内部处理逻辑----------------------------------------

    private String host = Constant.HOST_URL;    //请求host
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
        this(Constant.HOST_URL, spliceUrl, parseClass, needLogin);
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
    public void setSpliceUrl(String spliceUrl) {
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
        if (host.equals(Constant.NO_HOST)) {
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
        if (!host.equals(Constant.NO_HOST)) {
            url = host + url;
        }
        return url;
    }
}