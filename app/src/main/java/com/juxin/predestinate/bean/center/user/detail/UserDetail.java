package com.juxin.predestinate.bean.center.user.detail;

import com.juxin.library.log.PSP;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户详细信息
 */
public class UserDetail extends UserInfo {

    private List<UserPhoto> userPhotos = new ArrayList<>();
    private int voice = 1;  //1为开启语音，0为关闭

    @Override
    public void parseJson(String s) {
        String jsonData = getJsonObject(s).optString("res");
        JSONObject jsonObject = getJsonObject(jsonData);

        // 详细信息
        if (!jsonObject.isNull("userDetail")) {
            String json = jsonObject.optString("userDetail");
            super.parseJson(json);
        }

        // 用户相册
        if (!jsonObject.isNull("myPhoto")) {
            this.userPhotos = (List<UserPhoto>) getBaseDataList(jsonObject.optJSONArray("myPhoto"), UserPhoto.class);
        }

        // 开启语音状态
        if (!jsonObject.isNull("voice")) {
            this.voice = jsonObject.optInt("voice");
        }
    }

    public List<UserPhoto> getUserPhotos() {
        return userPhotos;
    }

    public int getVoice() {
        return voice;
    }

    public int getDiamondsSum() {
        return PSP.getInstance().getInt("diamondsSum" + uid, 0);
    }

    public void setDiamondsSum(int diamondsSum) {
        PSP.getInstance().put("diamondsSum" + uid, diamondsSum);
    }
}
