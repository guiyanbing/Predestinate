package com.juxin.predestinate.module.local.statistics;

/**
 * 用户行为统计项发送点，[统计项参数文档](http://test.game.xiaoyouapp.cn:20080/juxin-data/juxin-data-doc/src/master/flume/userbehavior-event-all.md)
 * Created by ZRP on 2017/5/24.
 */
public enum SendPoint {

    // -------首页tab--------
    menu_faxian,        // 发现
    menu_xiaoxi,        // 消息
    menu_fengyunbang,   // 风云榜
    menu_guangchang,    // 广场
    menu_me,            // 我的

    // --------------发现---------------
    menu_faxian_tuijian,                        //发现->推荐(普通点击)
    menu_faxian_hot,                            //发现->热门(普通点击)
    menu_faxian_tuijian_downrefresh,            //发现->下拉刷新(传递用户列表)
    menu_faxian_tuijian_viewuserinfo,           //发现->查看用户资料
    menu_faxian_tuijian_more_viewall,           //发现->更多->发现->更多->查看全部(普通点击)
    menu_faxian_tuijian_more_viewfujin,         //发现->更多->发现->更多->只看附近的人(普通点击)
    menu_faxian_tuijian_more_cancel,            //发现->更多->发现->更多->取消(普通点击)
    menu_faxian_tuijian_fujin_sayhello,         //发现->热门->只看附近的人->打招呼
    menu_faxian_tuijian_fujin_batchsayhello,    //发现->热门->只看附近的人->群打招呼
    menu_faxian_hot_viewuserinfo,               //发现->热门->查看用户资料(外层传递touid)
    menu_faxian_hot_picturenum,                 //发现->热门->图片数量按钮
    menu_faxian_hot_btnvideo,                   //发现->热门->发视频按钮
    menu_faxian_hot_btnvoice,                   //发现->热门->发语音按钮
    menu_faxian_hot_btnsendmessage,             //发现->热门->发私信
    menu_faxian_hot_btngirl,                    //发现->热门->发礼物
    menu_faxian_hot_btngirl_zongsong,           //发现->热门->发礼物->赠送
    menu_faxian_hot_btngirl_zongsong_ljcz,      //发现->热门->发礼物->赠送->立即充值
    menu_faxian_hot_btngirl_pay,                //发现->热门->发礼物->充值(普通点击,外层传递touid)
    menu_faxian_hot_slideremove,                //发现->热门->发礼物->滑动移除一个用户(普通点击,外层传递touid)

    // --------------消息---------------
    menu_xiaoxi_myfriend,                   // 消息->我的好友
    menu_xiaoxi_myfriend_lahei,             // 消息->我的好友->黑名单
    menu_xiaoxi_myfriend_lahei_viewuserinfo,// 消息->我的好友->黑名单->查看用户资料
    menu_xiaoxi_myfriend_lahei_remove,      // 消息->我的好友->黑名单->移出黑名单
    menu_xiaoxi_myfriend_chatframe,         // 消息->我的好友->打开聊天框(需要传递to_uid)
    menu_xiaoxi_sgzw,                       // 消息->谁关注我
    menu_xiaoxi_chatframe,                  // 消息->打开聊天框(需要传递to_uid)
    menu_xiaoxi_deluser,                    // 消息->删除用户
    menu_xiaoxi_sgzw_wgz,                   // 消息->谁关注我->我关注的
    menu_xiaoxi_sgzw_wgz_cancelfollow,      // 消息->谁关注我->我关注的->取消关注
    menu_xiaoxi_sgzw_wgz_viewuserinfo,      // 消息->谁关注我->我关注的->查看用户资料
    menu_xiaoxi_sgzw_gzw,                   // 消息->谁关注我->关注我的
    menu_xiaoxi_sgzw_gzw_vippay,            // 消息->谁关注我->关注我的->升级VIP会员,查看关注用户资料(男用户)
    menu_xiaoxi_sgzw_gzw_viewuserinfo,      // 消息->谁关注我->关注我的->查看用户资料
    menu_xiaoxi_sgzw_gzw_cancelfollow,      // 消息->谁关注我->关注我的->取消关注
    menu_xiaoxi_sgzw_gzw_followit,          // 消息->谁关注我->关注我的->关注TA

    // --------------风云榜---------------
    menu_fengyunbang_bz,                        //菜单->风云榜->本周(普通点击)
    menu_fengyunbang_sz,                        //菜单->风云榜->上周(普通点击)

    // --------------我的---------------
    menu_me_vippay,                         // 立即开通VIP
    menu_me_meauth,                         // 我的认证
    menu_me_meauth_videoauth,               // 视频认证
    menu_me_meauth_videoauth_capturepicture,// 拍摄照片按钮
    menu_me_meauth_videoauth_capturevideo,  // 拍摄视频按钮
    menu_me_meauth_telauth,                 // 手机认证
    menu_me_meauth_telauth_btnverifycode,   // 获取验证码
    menu_me_meauth_telauth_btnverify,       // 立即验证按钮
    menu_me_avatar,                         // 上传头像
    menu_me_sgzw,                           // 谁关注我
    menu_me_money,                          // 我的钱包
    menu_me_money_withdraw,                 // 立即提现
    menu_me_money_explain,                  // 提现说明
    menu_me_money_onekey,                   // 一键放入钱袋
    menu_me_redpackage,                     // 我要赚红包
    menu_me_redpackage_sylw,                // 我要赚红包->索要礼物(普通点击)
    menu_me_redpackage_sylw_send,           // 我要赚红包->索要礼物->发送按钮
    menu_me_redpackage_sylw_voice,          // 我要赚红包->索要礼物->按住说话按钮(普通点击)
    menu_me_redpackage_dzp,                 // 我的->我要赚红包->开心大转盘(普通点击)
    menu_me_y,                              // 我的Y币
    menu_me_gem,                            // 我的钻石
    menu_me_gem_explain,                    // 钻石说明
    menu_me_gem_btnpay,                     // 点击钻石支付按钮
    menu_me_gift,                           // 我的礼物
    menu_me_profile,                        // 个人资料
    menu_me_album,                          // 我的相册
    menu_me_setting,                        // 设置中心
    menu_me_setting_enablevideo,            // 开启视频通话
    menu_me_setting_enablevoice,            // 开启语音通话
    menu_me_setting_newmsgalert,            // 新消息提醒
    menu_me_setting_shockalert,             // 震动提醒
    menu_me_setting_soundalert,             // 声音提醒
    menu_me_setting_exitmsgalert,           // 退出消息提醒
    menu_me_setting_modifypassword,         // 修改密码
    menu_me_setting_feedback,               // 意见反馈
    menu_me_setting_checkupdates,           // 检查更新
    menu_me_setting_huodong,                // 活动相关
    menu_me_setting_about,                  // 关于
    menu_me_setting_clearcache,             // 清除缓存
    menu_me_setting_signout,                // 退出按钮

    // --------------聊天框---------------
    chatframe_tool_btnsend,         // 发送按钮
    chatframe_tool_btnvoice,        // 语音说话按钮
    chatframe_tool_btngift,         // 礼物按钮
    chatframe_tool_face,            // 表情按钮
    chatframe_tool_prcture,         // 聊天框->工具栏->图片按钮(普通点击)
    chatframe_tool_video,           // 聊天框->工具栏->视频聊天按钮(普通点击)
    chatframe_tool_voice,           // 聊天框->工具栏->语音聊天按钮(普通点击)
    chatframe_nav_follow,           // 关注TA
    chatframe_nav_y,                // 查看Y币
    chatframe_nav_tel,              // 查看手机
    chatframe_nav_weixin,           // 查看微信

    // --------------聊天框(礼物和充值相关)统计项---------------
    chatframe_tool_gift_pay,                //礼物框充值钻石链接
    chatframe_tool_gift_give,               //赠送按钮
    chatframe_tool_gift_give_btnljcz,       //立即充值//// TODO: 2017/6/11  
    chatframe_nav_y_ypay_btnqrzf,           //聊天框->导航栏->Y币->Y币充值确认支付按钮
    chatframe_nav_tel_vippay_btnqrzf,       //聊天框->导航栏->查看手机->vip开通确认支付按钮
    chatframe_nav_weixin_vippay_btnqrzf,    //聊天框->导航栏->查看微信->vip开通确认支付按钮
    chatframe_bottom_replyandcontact,       //回复并索要联系方式(男用户)

    // --------------充值页面统计项（H5）---------------
    pay_y_btnljzf,  // 立即支付按钮:y币
    pay_vip_btnljzf,// 立即支付按钮:vip
    pay_zixun,      // 充值咨询按钮

    // --------------用户资料---------------
    userinfo_btnsendmessage,        // 发信按钮
    userinfo_btnsayhello,           // 打招呼按钮
    userinfo_btnfollow,             // 关注按钮
    userinfo_btngirl,               // 礼物按钮
    userinfo_btnvideo,              // 发视频
    userinfo_btnvoice,              // 发语音
    userinfo_album,                 // 相册按钮(传递to_uid,第几张照片,照片index)
    userinfo_navalbum_leftflip,     // 左翻相册
    userinfo_navalbum_rightflip,    // 右翻相册

    userinfo_more_setting_remark,           //用户资料->更多->备注名(传递最新备注信息)
    userinfo_more_setting_clear,            //用户资料->更多->清空聊天记录(普通点击,外层传递touid)
    userinfo_more_setting_shield,           //用户资料->更多->屏蔽(普通点击,外层传递touid)
    userinfo_more_setting_jubao,            //用户资料->更多->举报(普通点击,外层传递touid)
    userinfo_more_setting_jubao_submit,     //用户资料->更多->举报->提交按钮(上传举报资料)
    userinfo_face,                          //用户资料->点击用户头像(普通点击,外层传递touid)


    // --------------视频语音框（插件实现）--------------
//    video_close,// 视频挂断(传递to_uid)
//    voice_close,// 语音挂断(传递to_uid)
//    voice_tool_chat//视频语音框->工具栏->聊天
//    voice_tool_voice_enable//视频语音框->工具栏->开启语音
//    voice_tool_voice_disable//视频语音框->工具栏->关闭语音
//    voice_tool_dzp//视频语音框->工具栏->大转盘
//    voice_tool_dzp_loop//视频语音框->工具栏->大转盘->转
//    voice_tool_gift//视频语音框->工具栏->礼物
//    voice_tool_gift_give//视频语音框->工具栏->礼物->赠送
//    voice_tool_ps//视频语音框->工具栏->美颜
//    voice_tool_avatar//视频语音框->工具栏->头像
//    voice_tool_closeall//视频语音框->工具栏->关闭结束聊天
//    voice_tool_closevideo//视频语音框->工具栏->关闭自己摄像头
//    voice_tool_enablevideo//视频语音框->工具栏->开启自己摄像头
//    voice_tool_switch//视频语音框->工具栏->切换
//    voice_tool_send//视频语音框->工具栏->发送消息

    // --------------欢迎页---------------
    welcome_login,  //登录按钮
    welcome_regist, //注册按钮

    // --------------登录页---------------
    login_btnlogin, //登录按钮
    login_freereg,  //免费注册按钮

    // --------------注册页---------------
    regist_btnreg,  // 注册按钮

    // --------------登录后引导---------------
    login_guide_onekeysayhello, // 一键打招呼(男用户)
    login_guide_moneyhelp,      // 如何赚钱(女用户)
}
