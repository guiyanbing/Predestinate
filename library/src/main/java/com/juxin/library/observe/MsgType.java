package com.juxin.library.observe;

/**
 * 主线订阅消息类型
 * Created by ZRP on 2016/12/27.
 */
public class MsgType {

    /**
     * 系统相关的消息类型。
     */
    public static final String MT_System = "MT_System";

    /**
     * 服务器配置更新通知
     */
    public static final String MT_Server_Config = "MT_Server_Config";

    /**
     * 应用相关的消息类型。<br>
     * 登录、退出登录消息。true登录；false退出。
     */
    public static final String MT_App_Login = "MT_App_Login";

    /**
     * 应用相关的消息类型。<br>
     * 退出应用程序。
     */
    public static final String MT_App_Exit = "MT_App_Exit";

    /**
     * 和CoreService建立通讯。<br>
     * 连接、断开消息。true连接；false断开。
     */
    public static final String MT_App_CoreService = "MT_App_CoreService";

    /**
     * 推送消息。发送小米或者信鸽发过来的消息。
     */
    public static final String MT_App_Push = "MT_App_Push";

    /**
     * 聊天的状态。
     */
    public static final String MT_App_IMStatus = "MT_App_IMStatus";

    /**
     * 应用相关的消息类型。<br>
     * 进入前台、后台的消息。true前台；false后台。
     * Boolean isForeground = msg.getData();
     */
    public static final String MT_App_Foreground = "MT_App_Foreground";

    /**
     * 地图定位功能，经纬度。<br>
     * bundle.getDouble("lng");<br>
     * bundle.getDouble("lat");
     */
    public static final String MT_Located = "MT_Located";

    /**
     * 个人信息变更消息类型
     * 消息变更注册这个
     */
    public static final String MT_MyInfo_Change = "MT_MyInfo_Change";

    /**
     * 请求更新myInfo消息
     * 通知更新消息用这个
     */
    public static final String MT_Update_MyInfo = "MT_App_IMStatus";

    /**
     * 未读数变更消息
     */
    public static final String MT_Unread_change = "MT_Unread_change";

    /**
     * 有cmd指令
     */
    public static final String MT_CMD_Notify = "MT_CMD_Notify";

    /**
     * 跳转Tab
     */
    public static final String MT_Jump_Tab = "MT_Jump_Tab";

    /**
     * 充值成功
     */
    public static final String MT_Pay_Success = "MT_Pay_Success";

    /**
     * 网络状态变化
     */
    public static final String MT_Network_Status_Change = "MT_Network_Status_Change";

    /**
     * App内悬浮窗通知
     */
    public static final String MT_APP_Suspension_Notice = "MT_APP_Suspension_Notice";

    /**
     * 悬浮窗内部互调通知
     * 非TipsBarMgr相关不监听
     */
    public static final String MT_Inner_Suspension_Notice = "MT_Inner_Suspension_Notice";

    /**
     * 消息列表更新
     */
    public static final String MT_User_List_Msg_Change = "MT_User_List_Msg_Change";

    // -----------------------------socket相关 start---------------------------------

    /**
     * socket开始连接
     */
    public static final String MT_Socket_Start_Connect = "MT_Socket_Start_Connect";

    /**
     * socket消息写入
     */
    public static final String MT_Socket_Write_Content = "MT_Socket_Write_Content";

    /**
     * socket消息读取
     */
    public static final String MT_Socket_Read_Content = "MT_Socket_Read_Content";

    // -----------------------------socket相关 end---------------------------------

    /**
     * 一键打招呼完成后回调用
     */
    public static final String MT_Say_Hello_Notice = "MT_Say_Hello_Notice";

}