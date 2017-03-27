package com.juxin.predestinate.module.logic.socket;

import android.os.DeadObjectException;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.juxin.library.enc.MD5;
import com.juxin.library.log.PLogger;
import com.juxin.library.log.PSP;
import com.juxin.library.observe.Msg;
import com.juxin.library.observe.MsgType;
import com.juxin.library.observe.RxBus;
import com.juxin.library.utils.NetworkUtils;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.logic.config.ServerTime;
import com.juxin.predestinate.module.util.JniUtil;
import com.juxin.predestinate.module.util.TimerUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 负责即时通讯的重连机制。同时提供对外的操作接口。
 *
 * @author JohnsonLi
 * @version 1.0
 * @qq 505214658
 * @date 2015-04-16
 */
public class AutoConnectMgr implements SocketCallback {

    private static final String TAG = "AutoConnectMgr";

    private static AutoConnectMgr ourInstance = new AutoConnectMgr();

    public static AutoConnectMgr getInstance() {
        return ourInstance;
    }

    private AutoConnectMgr() {
        RxBus.getInstance().toFlowable(Msg.class)
                .onBackpressureBuffer().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Msg>() {
                    @Override
                    public void accept(Msg msg) throws Exception {
                        switch (msg.getKey()) {
                            case MsgType.MT_Socket_DNS_Parse:
                                DNSParse();
                                break;
                            case MsgType.MT_Socket_Fail_Statistics://发送失败统计
                                if (failCount == 0) {
                                    firstFailTime = System.currentTimeMillis();//第一次记时
                                }
                                failCount++;
                                if (System.currentTimeMillis() - firstFailTime > 10 * 60 * 1000) {
                                    socketConnectFailReport();//如果距第一次记时超过了10min，就发送统计消息
                                }
                                break;
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        PLogger.printThrowable(throwable);
                    }
                });
    }

    /**
     * SimpleSocket封装了连接服务器、接收、发送数据等功能。
     */
    private SimpleSocket simpleSocket = null;
    private Gson gson = new Gson();

    private ICSCallback iCSCallback = null;
    private long uid = 0;//用户ID
    private String token;//用户token

    /**
     * 设置回调，和App进行交互。
     *
     * @param iCSCallback 回调实例。
     */
    public void setCallback(ICSCallback iCSCallback) {
        this.iCSCallback = iCSCallback;
    }

    /**
     * 登录用的信息。
     * 自动重连时需要用到，需要保存到内存中。Service重启后，需要再次调用login。<br>
     * 如果token是null或者""，则将现有Socket断开。否则则重新连接。
     *
     * @param uid   用户uid。
     * @param token 登录用的cookie。
     */
    public void login(long uid, String token) {
        PLogger.d("---socket login--->uid：" + uid + "，token：" + token);
        if (TextUtils.isEmpty(token)) {
            logout();
            return;
        }
        this.uid = uid;
        this.token = token;

        if (heartBeating) return;//如果是保持心跳连接状态，就不再次登录
        RxBus.getInstance().post(new Msg(MsgType.MT_Socket_DNS_Parse, null));//开始DNS解析
        PLogger.d("login: ------>发送连接服务器的消息");
    }

    /**
     * 退出登录，即断开即时通讯的连接。同时清除登录用的token。
     *
     * @see #login(long, String) login(long uid, String token)
     */
    public void logout() {
        PLogger.d("logout: ------>与服务器断开连接");
        this.uid = 0;
        this.token = "";

        heartBeating = false;
        loopHeartbeatStatus();
        heartbeatSend = 0;
        heartbeatResend = 0;

        if (simpleSocket != null) {
            SimpleSocket temp = simpleSocket;
            simpleSocket = null;
            temp.disconnect();
        }
        this.token = null;
    }

    // =================以下是关于内部自动连接维护的功能=================
    /**
     * 重连递增时间，每次2秒
     */
    private int incrementTime = 0;

    /**
     * @return 请求发送的延时时长
     */
    private int getIncrementTime() {
        incrementTime = TimerUtil.increaseTime(TCPConstant.SOCKET_AUTO_CONNECT_Increment_Time, 60 * 1000, false);
        PLogger.d("socket reconnect delayed time：" + incrementTime);
        return incrementTime;
    }

    private String hostIP = "sc.app.yuanfenba.net";   //socket域名解析之后的IP地址
    private String port = "8823";     //socket端口

    /**
     * 进行域名解析
     */
    private void DNSParse() {
        if (TextUtils.isEmpty(hostIP) || TextUtils.isEmpty(port)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String online_server = "";//TODO
                        PLogger.d("---DNSParse--->logic_server：" + online_server);
                        if (TextUtils.isEmpty(online_server)) return;

                        String[] split = online_server.split(":");
                        InetAddress inetAddress = InetAddress.getByName(split[0]);
                        hostIP = inetAddress.getHostAddress();
                        port = split[1];
                        PLogger.d("---DNSParse--->DNS解析成功：host->" + hostIP + "，port->" + port);
                        connect();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                        RxBus.getInstance().post(new Msg(MsgType.MT_Socket_DNS_Parse, null));
                    }
                }
            }).start();
        } else {
            connect();
        }
    }

    /**
     * 以获取到的地址和秘钥登录及时通讯服务器
     */
    private void connect() {
        if (TextUtils.isEmpty(hostIP) || TextUtils.isEmpty(port) || heartBeating) return;

        simpleSocket = new SimpleSocket();
        simpleSocket.setRemoteAddress(hostIP, Integer.parseInt(port));
        simpleSocket.setCallback(this);
        simpleSocket.connect();
        PLogger.d("connect: ------>socket开始连接，hostIP：" + hostIP);
    }

    private boolean heartBeating = false;   //是否需要发送心跳：长连接断线状态为false
    private int heartbeatSend = 0;          //本地发送心跳次数，当数值为2，再次发送心跳的时候重置为0
    private int heartbeatResend = 0;        //心跳回送次数

    /**
     * 发送心跳消息
     */
    public void heartbeat() {
        PLogger.d("---heartbeat--->heartBeating：" +
                heartBeating + "，heartbeatSend：" + heartbeatSend + "，heartbeatResend：" +
                heartbeatResend + "，packet loss：" + (heartbeatSend - heartbeatResend));
        if (!heartBeating) return;

        if (simpleSocket == null) {
            heartBeating = false;
            loopHeartbeatStatus();
        } else {
            //回送次数与发送次数误差校准，允许1次丢包。如果两次心跳均未收到回送，就重连socket
            if (heartbeatSend - heartbeatResend > 1) {
                heartBeating = false;
                loopHeartbeatStatus();
                heartbeatSend = 0;
                heartbeatResend = 0;

                onStatusChange(TCPConstant.SOCKET_STATUS_Disconnect, "IM 心跳回送失败，socket重新进行连接");
                RxBus.getInstance().post(new Msg(MsgType.MT_Socket_DNS_Parse, null));
            } else {
                simpleSocket.send(getRevertHeartbeat().getBytes());
            }
        }
    }

    private int lastRandomNum = 0;//心跳旧随机数
    private int newRandomNum = 0;//心跳新随机数

    /**
     * 获取心跳消息数据
     *
     * @return NetData socket发送消息
     */
    private NetData getHeartbeat(int heartbeatType) {
        if (lastRandomNum == 0) {
            newRandomNum = PSP.getInstance().getInt("Net_Random_Last", new Random(System.currentTimeMillis()).nextInt());
            lastRandomNum = newRandomNum;
        } else {
            lastRandomNum = newRandomNum;
            newRandomNum = new Random(System.currentTimeMillis()).nextInt();
            PSP.getInstance().put("Net_Random_Last", newRandomNum);
        }
        NetData heartbeatData = new NetData(uid, heartbeatType, lastRandomNum, newRandomNum);
        PLogger.d("getHeartbeat: ---心跳--->类型：" + heartbeatType + "/" + heartbeatData.toString());
        return heartbeatData;
    }

    /**
     * 获取心回送跳消息数据
     *
     * @return NetData socket发送消息
     */
    private NetData getRevertHeartbeat() {
        if (heartbeatSend == 2) {//重置心跳发送与回送次数统计
            heartbeatSend = 0;
            heartbeatResend = 0;
        }
        heartbeatSend++;//叠加心跳发送次数
        return getHeartbeat(Constant.MSG_ID_Heartbeat_Reply);
    }

    @Override
    public void onConnected(SimpleSocket id) {
        if (id != simpleSocket) return;

        //与聊天服务器连接成功之后，同时发送一条心跳消息与登录验证消息
        simpleSocket.send(getHeartbeat(Constant.MSG_ID_Heartbeat).getBytes());
        simpleSocket.send(getLoginData().getBytes());
        onStatusChange(TCPConstant.SOCKET_STATUS_Connected, "IM 连接服务器成功");
    }

    /**
     * 获取登录信息
     *
     * @return NetData socket发送消息
     */
    private NetData getLoginData() {
        long curTime = ServerTime.getServerTime().getTimeInMillis() / 1000;
        // 注意：md字段md5加密字符串为string的拼接
        String md = MD5.encode(String.valueOf(uid) + String.valueOf(curTime) + MD5.encode(token)).toUpperCase();
        Map<String, Object> loginMap = new HashMap<>();
        loginMap.put("fid", uid);
        loginMap.put("mt", curTime);
        loginMap.put("md", md);
        loginMap.put("ms", Constant.MS);
        loginMap.put("xt", 0);//android传0，或者不传。暂时不区分系统
        NetData data = new NetData(uid, Constant.MSG_ID_Login, gson.toJson(loginMap));
        PLogger.d("getLoginData: ---socket登录消息--->" + data.toString());
        return data;
    }

    @Override
    public void onReceive(SimpleSocket id, PSocketHeader header, byte[] buffer, int length) {
        if (id != simpleSocket) return;
        if (header == null || buffer == null || length < 0) return;

        String content = "";

        if (length > 0) content = JniUtil.GetDecryptString(new String(buffer, 0, length));
        PLogger.d("---socket消息头：" + header.toString() + "\n---socket消息体：" + content);

        if (header.type == Constant.MSG_ID_KICK_Offline) {//帐号异地登陆消息或切换服务器消息
            //TODO 根据mct字段的错误描述进行dialog提示，如果无此字段，直接跳转到登录页面。服务器暂无此实现
            accountInvalid(1);
            return;
        }

        if (header.type == Constant.MSG_ID_Heartbeat_Reply) {//心跳回送消息
            heartbeatResend++;
        }

        if (!TextUtils.isEmpty(content)) {
            try {
                JSONObject contentObject = new JSONObject(content);
                if (contentObject.has("mt")) {
                    ServerTime.setServerTime(contentObject.optLong("mt"));//保存服务器时间戳
                }
                //socket登录处理
                if (contentObject.has("s")) {
                    int s = contentObject.optInt("s");//登录状态，0表示成功，其他见下面详情
                    if (s == 0) {//登录成功
                        heartBeating = true;
                        loopHeartbeatStatus();
                        incrementTime = 0;
                        onStatusChange(TCPConstant.SOCKET_STATUS_Login_Success, "IM 登录成功：" + content);
                    } else {//登录失败，默认为与服务器断开连接，重新连接
                        onStatusChange(TCPConstant.SOCKET_STATUS_Login_Fail, "IM 登录失败：" + content);
                        switch (s) {
                            case -1:// 登录错误(密码错误之类)，跳转到登录页面
                                PLogger.d("socket登录错误---->" + contentObject.optString("c"));
                                accountInvalid(2);
                                break;
                            case -2://时间戳不对，需要读取返回的mt字段来重新登录
                                PLogger.d("socket登录错误---->时间戳不对");
                                RxBus.getInstance().post(new Msg(MsgType.MT_Socket_DNS_Parse, null));
//                                socketHandler.sendEmptyMessageDelayed(SH_MSG_DNSParse, getIncrementTime());
                                break;
                        }
                    }
                    return;//如果含s字段，为登录返回消息，处理完成之后直接return，不抛出消息
                }

                //消息重要字段解析
                long msgId = -1, sender = -1;//消息id，发送者id
                if (contentObject.has("d")) msgId = contentObject.optLong("d");
                if (contentObject.has("fid")) sender = contentObject.optLong("fid");

                //消息接收反馈
                simpleSocket.send(getLoopbackData(header.type, msgId).getBytes());

                //抛出消息
                onMessage(msgId, false, "", sender, content);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param msgType 消息类型：从消息头中获取
     * @param msgId   消息id：从消息头中获取
     * @return 回送消息体结构
     */
    private NetData getLoopbackData(int msgType, long msgId) {
        Map<String, Object> loopbackMap = new HashMap<>();
        loopbackMap.put("s", 0);
        loopbackMap.put("d", msgId);
        return new NetData(uid, msgType, gson.toJson(loopbackMap));
    }

    /**
     * 应用退出登录
     *
     * @param reason 退出原因：1[异地登陆踢下线]，2[密码验证失败，用户不存在等]
     */
    private void accountInvalid(int reason) {
        try {
            if (iCSCallback != null) iCSCallback.accountInvalid(reason);
        } catch (RemoteException e) {
            PLogger.printThrowable(e);
        }
    }

    /**
     * 回送心跳状态变更
     */
    private void loopHeartbeatStatus() {
        try {
            if (iCSCallback != null) iCSCallback.heartbeatStatus(heartBeating);
        } catch (RemoteException e) {
            PLogger.printThrowable(e);
        }
    }

    /* -------------socket重连失败统计start----------- */
    private int failCount = 0;//重连失败次数统计
    private long firstFailTime = 0;//首次重连失败的时间

    /**
     * socket重连失败统计
     */
    private void socketConnectFailReport() {
        if (failCount >= 3) {//重连失败三次以上，发送统计消息
            PLogger.d("------>socket三次重连失败，发送失败统计");
            onFeedback(-1, -1);
            failCount = 0;//消息回馈之后，不管成功失败，直接重置
        }
    }

    /* -------------socket重连失败统计end------------- */
    @Override
    public void onDisconnect(SimpleSocket id, int type) {
        if (id != simpleSocket) return;

        //打印日志
        String disconnectType = "";
        switch (type) {
            case 1:
                disconnectType = "连接服务器失败";
                break;
            case 2:
                disconnectType = "发送数据超时";
                break;
            case 3:
                disconnectType = "服务器主动关闭";
                break;
        }
        PLogger.d("socket断开连接：" + disconnectType);

        //暂停心跳，开始断线重连
        heartBeating = false;
        loopHeartbeatStatus();
        heartbeatSend = 0;
        heartbeatResend = 0;

        onStatusChange(TCPConstant.SOCKET_STATUS_Disconnect, "IM 断开服务器连接");
        if (NetworkUtils.isConnected(App.context)) {//网络可用情况下才立即发送重连
            RxBus.getInstance().post(new Msg(MsgType.MT_Socket_DNS_Parse, null));
//            socketHandler.sendEmptyMessageDelayed(SH_MSG_DNSParse, getIncrementTime());
            RxBus.getInstance().post(new Msg(MsgType.MT_Socket_Fail_Statistics, null));
        }
    }

    /**
     * 将即时通讯中收到消息通过ICSCallback抛出。
     *
     * @param msgId    消息Id。
     * @param group    是否群聊消息。
     * @param groupId  群聊Id。
     * @param sender   消息发送者的uid。
     * @param contents 消息内容，一个json格式的String。
     */
    private void onMessage(final long msgId, final boolean group, final String groupId, final long sender, final String contents) {
        Log.d(TAG, "onMessage:---->msgId:" + msgId + ",sender:" + sender + ",content:" + contents);
        try {
            if (iCSCallback != null) {
                iCSCallback.onMessage(msgId, group, groupId, sender, contents);
            }
        } catch (DeadObjectException de) {//如果服务挂了，就缓存当前消息，并重启ChatService
//            CoreService.startChatService(App.context);
            PLogger.d("---AutoConnectMgr--->DeadObjectException");
        } catch (RemoteException e) {
            PLogger.d("---AutoConnectMgr--->RemoteException");
        }
    }

    /**
     * 将即时通讯中的消息反馈动作抛出
     *
     * @param msgId   消息Id。
     * @param msgType 消息类型
     */
    private void onFeedback(long msgId, int msgType) {
        try {
            if (iCSCallback != null) {
                iCSCallback.onFeedback(msgId, msgType, hostIP);
            }
        } catch (DeadObjectException de) {//如果服务挂了，就重启ChatService
//            CoreService.startChatService(App.context);
            PLogger.d("---AutoConnectMgr--->DeadObjectException");
        } catch (RemoteException e) {
            PLogger.d("---AutoConnectMgr--->RemoteException");
        }
    }

    /**
     * 将即时通讯中的状态变化发送到App中。
     *
     * @param type 类型。
     * @param msg  消息内容。
     */
    private void onStatusChange(final int type, final String msg) {
        PLogger.d(msg);

        try {
            if (iCSCallback != null) {
                iCSCallback.onStatusChange(type, msg);
            }
        } catch (DeadObjectException de) {//如果服务挂了，就重启聊天service
//            CoreService.startChatService(App.context);
            PLogger.d("---AutoConnectMgr--->DeadObjectException");
        } catch (RemoteException e) {
            PLogger.d("---AutoConnectMgr--->RemoteException");
        }
    }
}
