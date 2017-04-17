package com.juxin.predestinate.ui.xiaoyou;

import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweightList;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.ui.xiaoyou.bean.FriendsList;
import com.juxin.predestinate.ui.xiaoyou.bean.IntimacyList;
import com.juxin.predestinate.ui.xiaoyou.bean.SimpleFriendsList;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zm on 2017/4/13
 */
public class FriendsUtils implements RequestComplete {

    public static List<UserInfoLightweight> friends = new ArrayList<>();
    private static boolean isHaveData = false;
    public static ArrayList<IntimacyList.IntimacyInfo> intimacyInfos;
    public static List<SimpleFriendsList.SimpleFriendInfo> arr_uids = new ArrayList<>();
    public void reqFriendsList(){
        intimacyInfos = getLevelInfo();
        ModuleMgr.getCommonMgr().getFriendList(this);
    }

    public static UserInfoLightweight getHandleUserInfo(SimpleFriendsList.SimpleFriendInfo info){
        int size = friends.size();
        for (int i = 0 ;i < size ;i++){
            if (info.getUid() == friends.get(i).getUid()){
                return  friends.get(i);
            }
        }
        return null;
    }
    public static UserInfoLightweight getHandleUserInfo(FriendsList.FriendInfo info){
        int size = friends.size();
        for (int i = 0 ;i < size ;i++){
            if (info.getUid() == friends.get(i).getUid()){
                return  friends.get(i);
            }
        }
        return null;
    }
    @Override
    public void onRequestComplete(HttpResponse response) {
//        Log.e("TTTTTTTTTTTfUtils", response.getResponseString() + "|||");
        if (UrlParam.reqFriendList.equals(response.getUrlParam())){
            if (response.isOk()){//请求返回成功
                SimpleFriendsList lists = (SimpleFriendsList) response.getBaseData();
                List<SimpleFriendsList.SimpleFriendInfo> friendInfos = lists.getArr_frends();
                if (friendInfos != null && !friendInfos.isEmpty()){
                    int size = friendInfos.size();
                    arr_uids = friendInfos;
                    ArrayList<String> uids = new ArrayList<>();
                    for (int i = 0;i < size;i++){
                        uids.add(friendInfos.get(i).getUid() + "");
                    }
//                    Log.e("TTTTTTTTTTTfUtils","uids_size="+uids.size()+"|||");
                    ModuleMgr.getCommonMgr().getUserSimpleList(uids, this);
                }else {
                    isHaveData = false;
                }
            }else {
                isHaveData = false;
            }
        }else if (UrlParam.reqUserSimpleList.equals(response.getUrlParam())){
            if (response.isOk()){//请求返回成功
                UserInfoLightweightList lists = (UserInfoLightweightList) response.getBaseData();
                List<UserInfoLightweight> friendInfos = lists.getUserInfos();
//                Log.e("TTTTTTTTTTTfUtils11",friendInfos+"||"+friendInfos.size());
                if (friendInfos != null && !friendInfos.isEmpty()){
                    friends.addAll(friendInfos);
//                    Log.e("TTTTTTTTTTTfUtils", "friens_size=" + friends.size() + "|||");
                    int size = arr_uids.size();
                    for (int i = 0;i < size && i < friends.size();i++){
                        arr_uids.get(i).setUserInfoLightweight(friends.get(i));
                        for (int j = intimacyInfos.size()-2;j >= 0 ;j--){
                            if (arr_uids.get(i).getIntimate()>intimacyInfos.get(j).getExperience()){
                                intimacyInfos.get(j).addFriend(arr_uids.get(i));
                                break;
                            }
                        }
//                        if (arr_uids.get(i).getIntimate())
                    }
                    isHaveData = true;
                }else {
                    isHaveData = false;
                }
            }else {
                isHaveData = false;
            }
        }
    }

    //亲密度配置
    public static ArrayList<IntimacyList.IntimacyInfo> getLevelInfo() {
        ArrayList<IntimacyList.IntimacyInfo> list = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray("[{\"level\": 0,\"tip\": \"陌生人\",\"experience\": 0},{\"level\": 1,\"tip\": \"相识\",\"experience\": 10},{\"level\": 2,\"tip\": \"熟悉\",\"experience\": 100},{\"level\": 3,\"tip\": \"朋友\",\"experience\": 500},{\"level\": 4,\"tip\": \"密友\",\"experience\": 1000},{\"level\": 5,\"tip\": \"知音\",\"experience\": 10000},{\"level\": 6,\"tip\": \"情侣\",\"experience\": 50000},{\"level\":-1,\"tip\": \"黑名单\",\"experience\": -1}]");
            for (int i = 0; i < jsonArray.length(); i++) {
                IntimacyList.IntimacyInfo info = new IntimacyList.IntimacyInfo();
//                Log.e("TTTTTTTTTYy",jsonArray.optString(i)+"||");
                info.parseJson(jsonArray.optString(i));
                list.add(info);
            }
        } catch (JSONException e) {
            e.printStackTrace();
//            Log.e("TTTTTTTTTYy11", e.getMessage() + "||");
        }
        return list;
    }
}