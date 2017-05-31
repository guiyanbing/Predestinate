package com.juxin.predestinate.module.util.my;

import com.google.gson.Gson;
import com.juxin.library.log.PSP;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.bean.my.AttentionList;
import com.juxin.predestinate.bean.my.AttentionUserDetail;
import com.juxin.predestinate.bean.my.AttentionUserDetailList;
import com.juxin.predestinate.module.logic.application.ModuleMgr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zm on 2017/5/4
 */
public class AttentionUtil {

    private static final String USERSKEY = "USERSKEY";
    public static final int MYATTENTION = 1;
    public static final int ATTENTIONME = 2;
    private static Map<Long,AttentionUserDetail> userInfos = new HashMap<>();
    private static List<AttentionUserDetail> userDetails = new ArrayList<>();
    private static List<AttentionUserDetail> userMyDetails = new ArrayList<>();

    //初始化
    public static void initUserDetails(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (userInfos.size()<=0){
                    String jsonStr = PSP.getInstance().getString(USERSKEY+ModuleMgr.getCenterMgr().getMyInfo().getUid(),"");
//                                Log.e("TTTTTTTJJJ111", jsonStr);
//                                PSP.getInstance().remove(USERSKEY+ModuleMgr.getCenterMgr().getMyInfo().getUid());
                    AttentionUserDetailList list = new AttentionUserDetailList();
                    list.parseJson(jsonStr);
                    List<AttentionUserDetail> userDetails = list.getAttentionUserDetailList();
                    for (AttentionUserDetail detail:userDetails){
                        userInfos.put(detail.getUid(),detail);
                    }
//                    Log.e("TTTTTTTTTTTTPPP",userDetails.size()+"||"+userInfos.size());
                }
            }
        }).start();
    }

    public static void addUser(AttentionUserDetail userDetail){
        userInfos.put(userDetail.getUid(), userDetail);
    }

    public static AttentionUserDetail getUserDetail(Long uid){
        return userInfos.get(uid);
    }

    public static void saveUserDetails(){
        Gson gson = new Gson();
        List<AttentionUserDetail> userDetails = new ArrayList<>();
        for (Long uid:userInfos.keySet()){
            userDetails.add(userInfos.get(uid));
        }
        String jsonStr = gson.toJson(userDetails);
//        Log.e("TTTTTTTJJJ",jsonStr);
        PSP.getInstance().put(USERSKEY + ModuleMgr.getCenterMgr().getMyInfo().getUid(), jsonStr);
    }

    /**
     * 返回内存中存储的用户信息
     *
     * @return
     */
    public static void updateUserDetails(String jsonStr){
        UserDetail detail = new UserDetail();
        AttentionUserDetail userDetail = new AttentionUserDetail();
        detail.parseJson(jsonStr);
        userDetail.parse(detail);
        if (userInfos.containsKey(userDetail.getUid())){
            userInfos.put(userDetail.getUid(),userDetail);
            saveUserDetails();
        }
    }

    public static void updateUserDetails(UserDetail detail){
        AttentionUserDetail userDetail = new AttentionUserDetail();
        userDetail.parse(detail);
        if (userInfos.containsKey(userDetail.getUid())){
            userInfos.put(userDetail.getUid(),userDetail);
            saveUserDetails();
        }
    }

    /**
     * 返回内存中存储的用户信息
     *
     * @return
     */
    public static List<AttentionUserDetail> HandleAttentionList(List<AttentionList.AttentionInfo> infos ,int attention){
        if (attention == ATTENTIONME)
            userDetails.clear();
        else
            userMyDetails.clear();
        for (int i = 0 ;i < infos.size();i++){
            AttentionUserDetail detail = getUserDetail(infos.get(i).getUid());
//            Log.e("TTTTTTTTTTTTYYY",detail+"|||i="+i+"||uid="+infos.get(i).getUid());
            if ( detail != null){
                if (attention == ATTENTIONME){
                    userDetails.add(detail);
                }else {
                    userMyDetails.add(detail);
                }
                infos.remove(i);
                i--;
                continue;
            }
        }
        if (attention == ATTENTIONME){
            return userDetails;
        }else {
            return userMyDetails;
        }
    }
}
