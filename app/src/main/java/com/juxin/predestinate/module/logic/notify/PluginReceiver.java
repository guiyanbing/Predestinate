package com.juxin.predestinate.module.logic.notify;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.juxin.library.log.PLogger;
import com.juxin.predestinate.bean.my.InviteInfo;
import com.juxin.predestinate.module.local.statistics.Statistics;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.util.VideoAudioChatHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 接收插件广播
 *
 * @author gwz
 */
public class PluginReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("com.juxin.action.plugin".equals(intent.getAction())) {
            String json = intent.getStringExtra("extra_json");
            PLogger.d("PluginReceiver=tid=" + json);
            if (json != null) {
                try {
                    JSONObject jo = new JSONObject(json);
                    switch (jo.optInt("mt")) {
                        case 1://发送文本消息
                            ModuleMgr.getChatMgr().sendTextMsg(null, jo.getString("tid"), jo.getString("text"));
                            break;
                        case 3://发送礼物新消息
                            ModuleMgr.getChatMgr().sendGiftMsg(null, jo.optString("tid"), jo.optInt("giftid"),
                                    jo.optInt("giftnum"), jo.optInt("gtype"));
                            break;
                        case 7://更改消息状态
                            ModuleMgr.getChatMgr().sendMailReadedMsg(null, jo.optLong("tid"));
                            ModuleMgr.getChatMgr().updateLocalReadStatus(null, jo.optString("tid"), jo.optLong("msgId"));
                            break;

                        case 8: // 重置主包群发状态
                            VideoAudioChatHelper.getInstance().setGroupInviteStatus(false);
                            break;
                        case 1000://通知主包插件是否在前台
                            boolean plugInForeground = jo.optBoolean("foreground",false);
                            App.setPluginForeground(plugInForeground);
                            break;
                        default:
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
            String packageName = intent.getData().getSchemeSpecificPart();
            if (VideoAudioChatHelper.PACKAGE_PLUGIN_VIDEO.equals(packageName)) {
                Statistics.installVideoPlugin(true);
                if (VideoAudioChatHelper.getInstance().isBeInvite() && VideoAudioChatHelper.getInstance().getmInviteInfo() != null){
                    InviteInfo info = VideoAudioChatHelper.getInstance().getmInviteInfo();
                    //跳转视频
                    VideoAudioChatHelper.getInstance().openInvitedActivity((Activity) App.getActivity(),
                            info.getVcId(), info.getDstUid(), info.getChatType(), info.getPrice());
                }

            }
        }
    }
}
