package com.juxin.predestinate.module.logic.notify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.juxin.predestinate.module.local.chat.ChatMgr;
import com.juxin.predestinate.module.logic.application.ModuleMgr;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author gwz
 * 接收插件广播
 */
public class PluginReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String json = intent.getStringExtra("extra_json");
        if(json != null){
            try {
                JSONObject jo = new JSONObject(json);
                switch (jo.optInt("mt")){
                    case 1:
                        ModuleMgr.getChatMgr().sendTextMsg(null,jo.getString("tid"),jo.getString("text"));
                        break;
                    case 3:

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
