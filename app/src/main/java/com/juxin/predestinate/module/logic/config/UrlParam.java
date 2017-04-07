package com.juxin.predestinate.module.logic.config;

import com.juxin.predestinate.bean.UserLogin;
import com.juxin.predestinate.bean.center.update.AppUpdate;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweightList;
import com.juxin.predestinate.bean.net.BaseData;
import com.juxin.predestinate.bean.start.UserReg;
import com.juxin.predestinate.ui.xiaoyou.bean.SimpleFriendsList;

import java.util.Map;

/**
 * 管理常用的Url参数信息
 */
public enum UrlParam {

    reqRegister("i/reg/Reg", UserReg.class),//注册接口
    reqLogin("i/reg/CLogin", UserLogin.class),//普通登录接口
    reqReqVerifyCode("i/ver/ReqRestVerifyCode"),//获取手机验证码
    resetPassword("i/reg/ResetPassword"),//找回密码
    //检查软件升级
    checkUpdate("i/version/CheckVersion", AppUpdate.class, false),

    CMDRequest(""),//cmd请求中默认拼接内容为空，通过resetHost方式进行使用

    //============================== 用户资料相关接口 =============================

    reqSetInfo("i/uinfo/SecSetInfo", true),  // 用户设置更新
    reqMyInfo("s/uinfo/UDetail", UserDetail.class, true),  //获取详细个人资料
    reqOtherInfo("s/uinfo/ODetail", UserDetail.class, true),

    //批量获取用户简略信息
    reqUserSimpleList("s/uinfo/USimple", UserInfoLightweightList.class, true),
    //获取昵称和头像的最近变更 list
    reqBasicUserInfoMsg("s/uinfo/NickChangedList", UserInfoLightweightList.class, true),

    //============================== 小友模块相关接口 =============================
    //好友标签分组成员
    reqTagGroupMember("/s/friend/TagGroupMember", SimpleFriendsList.class),

    // 最后一个，占位
    LastUrlParam("");

    // -------------------------------内部处理逻辑----------------------------------------

    private String host = Constant.HOST_URL;    //请求host
    private String spliceUrl = null;            //接口url，与host拼接得到完整url
    private boolean needLogin = false;          //请求是否需要登录才会发送
    private Class<? extends BaseData> parseClass = null;//请求返回体解析类

    // --------------构造方法 start--------------

    /**
     * 接口url+解析bean+是否需要登录
     */
    UrlParam(final String spliceUrl, final Class<? extends BaseData> parseClass, final boolean needLogin) {
        this.spliceUrl = spliceUrl;
        this.parseClass = parseClass;
        this.needLogin = needLogin;
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