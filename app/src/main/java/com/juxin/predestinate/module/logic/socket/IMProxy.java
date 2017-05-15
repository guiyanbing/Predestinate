package com.juxin.predestinate.module.logic.socket;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.juxin.library.log.PLogger;
import com.juxin.library.observe.MsgMgr;
import com.juxin.library.observe.MsgType;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.custom.SimpleTipDialog;
import com.juxin.predestinate.module.util.PickerDialogUtil;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.start.UserLoginExtAct;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 即时通许代理，负责将接收到的服务器消息通知给上层
 */
public class IMProxy {

    private ConcurrentHashMap<Long, SendCallBack> mSendCallBackMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Long ,NetData> mSendMap = new ConcurrentHashMap<>();
    private CallBackHandler mHandler = new CallBackHandler(Looper.getMainLooper());

    private static class SingletonHolder {
        static IMProxy instance = new IMProxy();
    }

    public static IMProxy getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 登录socket
     *
     * @param uid   登录用户的uid
     * @param token EncryptUtil.md5(pw)
     */
    public void login(long uid, String token) {
        try {
            if (iCoreService != null) iCoreService.login(uid, token);
        } catch (Exception e) {
            PLogger.printThrowable(e);
        }
    }

    /**
     * 使用存储的账户密码进行登录
     */
    public void login() {
        long uid = ModuleMgr.getLoginMgr().getUid();
        String auth = ModuleMgr.getLoginMgr().getAuth();
        if (uid == 0 || TextUtils.isEmpty(auth)) {
            PLogger.d("---IMProxy--->login：auth is empty.");
        } else {
            login(uid, auth);
        }
    }

    /**
     * 退出登录，即断开即时通讯的连接，同时清除登录用的token
     *
     * @see #login(long, String) login(long, String)
     */
    public void logout() {
        try {
            if (iCoreService != null) iCoreService.logout();
        } catch (Exception e) {
            PLogger.printThrowable(e);
        }
    }

    /**
     * 设置GPS坐标，经纬度坐标
     *
     * @param longitude 经度坐标
     * @param latitude  维度坐标
     */
    public void setLocationGPS(double longitude, double latitude) {
        try {
            if (iCoreService != null) iCoreService.setLocationGPS(longitude, latitude);
        } catch (Exception e) {
            PLogger.printThrowable(e);
        }
    }

    /**
     * 设置系统类型
     *
     * @param systemInfo 操作系统:0 其它；1 苹果；2 小米
     */
    public void setSystemInfo(int systemInfo) {
        try {
            if (iCoreService != null) iCoreService.setSystemInfo(systemInfo);
        } catch (Exception e) {
            PLogger.printThrowable(e);
        }
    }

    private Map<String, Set<IMListener>> typeMapListener = new HashMap<>();
    private Set<IMListener> noTypeMapListener = new LinkedHashSet<>();

    private Intent sIntent = null;

    /**
     * 代理和服务器建立连接
     */
    public void connect() {
        if (status != ConnectStatus.NO_CONNECT && status != ConnectStatus.DISCONNECTED) {
            login();
        } else {
            Context context = App.context;
            sIntent = new Intent();
            sIntent.setClass(context, CoreService.class);
            try {
                context.startService(sIntent);//启动CoreService服务
                if (context.bindService(sIntent, connection, Context.BIND_AUTO_CREATE)) {//服务是否绑定成功
                    if (status == ConnectStatus.NO_CONNECT) {//如果本地记录的连接状态是未连接，就更新为已绑定，否则更新为重连
                        status = ConnectStatus.BINDING;
                    } else {
                        status = ConnectStatus.REBINDING;
                    }
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * socket发消息
     *
     * @param netData tcp数据包封装
     */
    public void send(NetData netData) {
        try {
            if (iCoreService != null) iCoreService.sendMsg(netData);
        } catch (Exception e) {
            PLogger.printThrowable(e);
        }
    }

    /**
     * socket发消息
     *
     * @param netData  数据包封装
     * @param callBack 发送消息回调
     */
    public void send(NetData netData, SendCallBack callBack) {
        if(callBack == null){
            send(netData);
            return;
        }

        long msgId = netData.getMessageId();
        if (msgId != -1) {
            mSendCallBackMap.put(msgId, callBack);
        }
        send(netData);

        /**
         * 发送消息后发送响应超时消息
         */
        mSendMap.put(msgId, netData);
        Message message = Message.obtain();
        message.what = CallBackHandler.MESSAGE_TYPE_RESPONDS_TIME_OUT;
        message.obj = netData;
        mHandler.sendMessageDelayed(message, TCPConstant.SEND_RESPOND_TIME_OUT);
    }

    /**
     * 代理和服务器断开连接（默认不主动断开连接，特殊情况调用）
     */
    public void disconnect() {
        if (status != ConnectStatus.CONNECTED) {
            return;
        }

        if (sIntent != null) {//解绑并停止服务
            App.context.unbindService(connection);
            App.context.stopService(sIntent);
        }
    }

    private ChatServiceConnection connection = new ChatServiceConnection();

    private class ChatServiceConnection implements ServiceConnection {

        @Override
        public void onServiceDisconnected(final ComponentName name) {
            status = ConnectStatus.DISCONNECTED;
            iCoreService = null;
        }

        @Override
        public void onServiceConnected(final ComponentName name, final IBinder service) {
            if (status == ConnectStatus.BINDING || status == ConnectStatus.REBINDING) {
                iCoreService = ICoreService.Stub.asInterface(service);
                setCSCallback();

                //发送CoreService启动消息
                MsgMgr.getInstance().sendMsg(MsgType.MT_App_CoreService, true);
            }
            status = ConnectStatus.CONNECTED;
        }
    }

    /**
     * 注册一个监听者，将监听者和具体消息类型绑定
     *
     * @param imType     消息类型
     * @param imListener 监听者实例
     */
    public void attach(final String imType, final IMListener imListener) {
        Set<IMListener> observers = typeMapListener.get(imType);

        if (observers == null) {
            observers = new LinkedHashSet<>();
            typeMapListener.put(imType, observers);
        }

        observers.add(imListener);
    }

    /**
     * 取消注册的监听者，解除将监听者和具体消息类型的绑定
     *
     * @param imType     消息类型
     * @param imListener 监听者实例
     */
    public void detach(final String imType, final IMListener imListener) {
        Set<IMListener> observers = typeMapListener.get(imType);

        if (observers != null) {
            observers.remove(imListener);
        }
    }

    /**
     * 注册一个监听者，将监听者和所有消息类型绑定<br>
     * 使用此接口时，通过{@link #attach(String, IMProxy.IMListener)}
     * 绑定的监听者将会被解除绑定关系
     *
     * @param imListener 监听者实例
     */
    public void attach(final IMListener imListener) {
        noTypeMapListener.add(imListener);
    }

    /**
     * 取消注册的监听者，解除监听者的所有绑定
     *
     * @param imListener 监听者实例
     */
    public void detach(final IMListener imListener) {
        for (Map.Entry<String, Set<IMListener>> entry : typeMapListener.entrySet()) {
            entry.getValue().remove(imListener);
        }

        noTypeMapListener.remove(imListener);
    }

    /**
     * 监听事件的回调接口
     */
    public interface IMListener {
        /**
         * 处理{@link CoreService}
         * 回调过来的消息
         *
         * @param msgId    消息Id
         * @param group    是否群聊消息
         * @param groupId  群聊Id，私聊为null或空
         * @param sender   消息发送者的uid
         * @param contents 消息内容，一个json格式的String
         */
        void onMessage(final long msgId, final boolean group, final String groupId, final long sender, final String contents);
    }

    /**
     * 监听发送消息回调
     */
    public interface SendCallBack {
        /**
         * 发送消息后返回的结果回调
         *
         * @param msgId    消息Id
         * @param group    是否群聊消息
         * @param groupId  群聊Id，私聊为null或空
         * @param sender   消息发送者的uid
         * @param contents 消息内容，一个json格式的String
         */
        void onResult(final long msgId, final boolean group, final String groupId, final long sender, final String contents);

        /**
         * 当发送消息失败时回调
         *
         * @param data
         */
        void onSendFailed(NetData data);
    }

    /**
     * 网络连接状态
     */
    private enum ConnectStatus {
        NO_CONNECT,         //未连接
        BINDING,            //连接中
        CONNECTED,          //已连接
        DISCONNECTED,       //断开连接
        REBINDING           //重连
    }

    private static ConnectStatus status = ConnectStatus.NO_CONNECT;//默认连接状态为未连接

    /**
     * 调用{@link CoreService}的接口实例
     */
    private ICoreService iCoreService = null;

    /**
     * 将{@link CoreService}
     * 对{@link IMProxy}的回调在这里设置
     */
    private void setCSCallback() {
        try {
            if (iCoreService != null) iCoreService.setCallback(iCSCallback);
        } catch (Exception e) {
            PLogger.printThrowable(e);
        }
    }

    /**
     * 将{@link CoreService}
     * 对{@link IMProxy}的回调清除
     */
    private void removeCSCallback() {
        try {
            if (iCoreService != null) iCoreService.setCallback(null);
        } catch (Exception e) {
            PLogger.printThrowable(e);
        }
    }

    // =======================ICSCallback.aidl start=========================
    /**
     * {@link CoreService}回调IMProxy的接口实例
     */
    private ICSCallback iCSCallback = new CSCallback();

    private class CSCallback extends ICSCallback.Stub {
        @Override
        public void onMessage(final NetData data) throws RemoteException {
            final long msgId = data.getMessageId();
            //检查是否有发送回调
            if (msgId != -1) {
                final SendCallBack callBack = mSendCallBackMap.remove(msgId);
                if (callBack != null) {
                    removeTimoutCallBack(msgId);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onResult(msgId, false, "", data.getFromId(), data.getContent());
                        }
                    });
                    return;
                }
            }

            IMProxy.this.onMessage(data.getMessageId(), false, "", data.getFromId(), data.getContent());
        }

        @Override
        public void onSendMsgError(final NetData data) throws RemoteException {
            final long msgId = data.getMessageId();
            //检查是否有发送回调
            if (msgId != -1) {
                final SendCallBack callBack = mSendCallBackMap.remove(msgId);
                if (callBack != null) {
                    removeTimoutCallBack(msgId);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onSendFailed(data);
                        }
                    });
                }
            }
        }

        @Override
        public void onStatusChange(int type, String msg) throws RemoteException {
            IMProxy.this.onStatusChange(type, msg);
        }

        @Override
        public void accountInvalid(int reason) throws RemoteException {
            IMProxy.this.accountInvalid(reason);
        }

        @Override
        public void heartbeatStatus(boolean isBeating) throws RemoteException {
            IMProxy.this.setSocketValidStatus(isBeating);
        }
    }

    /**
     * 处理{@link CoreService}
     * 回调过来的消息<br>
     * 添加特殊处理记录下最新的消息Id，如果小于此Id的消息，认为是重复消息，直接丢弃
     *
     * @param msgId    消息Id
     * @param group    是否群聊消息
     * @param groupId  群聊Id
     * @param sender   消息发送者的uid
     * @param contents 消息内容，一个json格式的String
     */
    public void onMessage(final long msgId, final boolean group, final String groupId, final long sender, final String contents) {
        PLogger.d("msgId: " + msgId + "; group: " + group + "; groupId: " + groupId + "; sender: " + sender + "; contents: " + contents);

        //不区分消息类型
        for (Map.Entry<String, Set<IMListener>> stringSetEntry : typeMapListener.entrySet()) {
            for (IMListener imListener : stringSetEntry.getValue()) {
                imListener.onMessage(msgId, group, groupId, sender, contents);
            }
        }

        for (IMListener imListener : noTypeMapListener) {
            imListener.onMessage(msgId, group, groupId, sender, contents);
        }
    }

    /**
     * 处理{@link CoreService}
     * 回调过来的消息
     *
     * @param type 消息类型：0-登录成功；1-登录失败；2-连接成功；3-断开连接
     * @param msg  提示消息
     */
    private void onStatusChange(final int type, final String msg) {
        PLogger.d("type：" + type + "，msg：" + msg);
        MsgMgr.getInstance().sendMsg(MsgType.MT_App_IMStatus, new HashMap<String, Object>() {
            {
                put("type", type);
                put("msg", msg);
            }
        });
    }

    /**
     * 帐号无效
     *
     * @param reason 重登陆原因：1[异地登陆踢下线]，2[密码验证失败，用户不存在等]
     */
    private void accountInvalid(int reason) {
        if (reason == 1) {// 踢下线弹窗
            showInvalidDialog((FragmentActivity) App.getActivity(), "您的账号在另一台设备登录！");
        } else if (reason == 2) {// 帐号无效
            showInvalidDialog((FragmentActivity) App.getActivity(), "账号无效，请重新登录。");
        }
    }

    /**
     * 显示帐号无效弹框
     *
     * @param context FragmentActivity上下文
     * @param tip     弹框提示文字
     */
    private void showInvalidDialog(final FragmentActivity context, final String tip) {
        ModuleMgr.getLoginMgr().logout();
        MsgMgr.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    PickerDialogUtil.showTipDialogCancelBack(context, new SimpleTipDialog.ConfirmListener() {
                        @Override
                        public void onCancel() {
                        }

                        @Override
                        public void onSubmit() {
                            UIShow.showActivityClearTask(context, UserLoginExtAct.class);
                        }
                    }, tip, "提示", "", "确定", false, false);
                } catch (Exception e) {
                    UIShow.showActivityClearTask(context, UserLoginExtAct.class);
                }
            }
        });
    }

    private boolean isSocketValid = false;//socket是否在线

    /**
     * @return 判断socket是否在线true表示socket存活并且保持在线状态；false表示socket掉线或者socket对象被杀死
     */
    public boolean isSocketValid() {
        return isSocketValid;
    }

    private void setSocketValidStatus(boolean isSocketValid) {
        this.isSocketValid = isSocketValid;
    }

    /**
     * 移除发送消息响应超时消息
     * @param msgId
     */
    private void removeTimoutCallBack(long msgId){
        NetData sendData = mSendMap.remove(msgId);
        if(sendData != null) {
            mHandler.removeMessages(CallBackHandler.MESSAGE_TYPE_RESPONDS_TIME_OUT, sendData);
        }
    }

    class CallBackHandler extends Handler{
        static final int MESSAGE_TYPE_RESPONDS_TIME_OUT = 1;
        public CallBackHandler(Looper looper){
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if(msg.obj == null || !(msg.obj instanceof NetData)) return;

            NetData data = (NetData) msg.obj;
            long msgId = data.getMessageId();
            SendCallBack callBack = mSendCallBackMap.remove(msgId);
            if(callBack == null) return;

            switch (msg.what){
                case MESSAGE_TYPE_RESPONDS_TIME_OUT:
                    callBack.onSendFailed(data);
                    break;
            }
        }
    }
}
