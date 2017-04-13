package com.juxin.predestinate.ui.xiaoyou;

import android.util.Log;

import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweightList;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.ui.xiaoyou.bean.SimpleFriendsList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zm on 2017/4/13
 */
public class FriendsUtils implements RequestComplete {

    public static List<UserInfoLightweight> friends;
    private static boolean isHaveData = false;
    public void reqFriendsList(){
        ModuleMgr.getCommonMgr().getFriendList(this);
    }

    @Override
    public void onRequestComplete(HttpResponse response) {
        Log.e("TTTTTTTTTTTfUtils",response.getResponseString()+"|||");
        if (response.getUrlParam() == UrlParam.reqFriendList){
            if (response.isOk()){//请求返回成功
                SimpleFriendsList lists = (SimpleFriendsList) response.getBaseData();
                List<SimpleFriendsList.SimpleFriendInfo> friendInfos = lists.getArr_frends();
                if (friendInfos != null && !friendInfos.isEmpty()){
                    int size = friendInfos.size();
                    ArrayList<String> uids = new ArrayList<>();
                    for (int i = 0;i < size;i++){
                        uids.add(friendInfos.get(i).getUid() + "");
                    }
                    Log.e("TTTTTTTTTTTfUtils","uids_size="+uids.size()+"|||");
                    ModuleMgr.getCommonMgr().getUserSimpleList(uids, this);
                }else {
                    isHaveData = false;
                }
            }else {
                isHaveData = false;
            }
        }else if (response.getUrlParam() == UrlParam.reqUserSimpleList){
            if (response.isOk()){//请求返回成功
                UserInfoLightweightList lists = (UserInfoLightweightList) response.getBaseData();
                List<UserInfoLightweight> friendInfos = lists.getUserInfos();
                if (friendInfos != null && !friendInfos.isEmpty()){
                    friends.addAll(friendInfos);
                    Log.e("TTTTTTTTTTTfUtils", "friens_size=" + friends.size() + "|||");
                    isHaveData = true;
                }else {
                    isHaveData = false;
                }
            }else {
                isHaveData = false;
            }
        }
    }
}