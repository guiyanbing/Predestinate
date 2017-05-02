package com.juxin.predestinate.bean.center.user.others;

import android.os.Parcel;
import android.text.TextUtils;

import com.juxin.predestinate.bean.center.user.detail.UserBasic;
import com.juxin.predestinate.bean.center.user.detail.UserPhoto;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * TA人详细资料
 * <p>
 * 与个人资料解析暂时分离：相同标志  解析字段不同，返回类型不同
 * <p>
 * Created by Su on 2017/4/27.
 */
public class UserProfile extends UserBasic {
    private int cuid;//是否是机器人
    private int is_vip;//是否是VIP
    private String sign; //内心独白
    private int isOnline; // 是否在线
    private String Integrity; // 资料完整度
    private int isSayHello;// 是否打过招呼 true是打过
    private String phone;//手机号
    private String qq;//QQ号码
    private int phoneAuth; // 1为公开，2为保密
    private int qqNumAuth;// 1为公开，2为保密
    private String wechat;//微信号
    private int wechatAuth;// 1为公开，2为保密
    private int isverifycellphone;//是否绑定了手机
    private int photoNum;//相片数量
    private int ismonthmail;//是否开通了包月发信
    private int isBindRose;//是否绑定了红娘'微信认证，0为默认，1为已认证，2为拒绝',
    private int distance;// 距离
    private int isFollowed;//是否关注
    private int kf_id;
    private int isHot;
    private String online_address;//上线地区   2015-05-06
    private String online_date; //上线时间 2015-05-06
    private String weChatNo;//微信号
    private int FollowCont;//关注数量
    private String lastOnlineTime;//最后登录时间
    private int mtRedbag;//摇钱树红包数量
    private int gameFlag;//是否玩过游戏：1为玩过游戏，0为没有玩过
    private int hong;//判断软件版本是否为红包来了：1红包来了用户  2 非红包来了用户

    private String zytj_address;//征友条件--居住地
    private String zytj_age;//征友条件-年龄
    private String zytj_edu;//征友条件--学历
    private String zytj_height;//征友条件--身高
    private String zytj_income;//征友条件--收入

    private String purpose;//交友目的
    private String loveIdea;//恋爱观念
    private String meetHope;//首次见面希望
    private String datePlace;//喜欢约会的地点

    private UserVideoInfo videoInfo;//用户视频信息
    private List<UserPhoto> userPhotoList = new ArrayList<>();

    // 请求失败结果码
    private String result;
    private int status;
    private String content;

    @Override
    public void parseJson(String jsonStr) {
        super.parseJson(jsonStr);
        JSONObject jsonObject = getJsonObject(jsonStr);

        if (!jsonObject.isNull("result")) {
            this.setResult(jsonObject.optString("result"));
            this.setStatus(jsonObject.optInt("status"));
            this.setContent(jsonObject.optString("content"));
            return;
        }

        // 用户相册
        if (!jsonObject.isNull("photo")) {
            this.userPhotoList = (List<UserPhoto>) getBaseDataList(jsonObject.optJSONArray("photo"), UserPhoto.class);
        }

        // 机器人打招呼list
        //if (AppModel.getInstance().ifHaveHelloRobot(this.getUid())) {
        //    this.setIsSayHello(1);
        //}

        // 详细信息
        this.setCuid(jsonObject.optInt("cuid"));
        this.setIs_vip(jsonObject.optInt("is_vip"));
        this.setSign(jsonObject.optString("sign"));
        this.setIsOnline(jsonObject.optInt("is_online"));
        this.setIntegrity(jsonObject.optString("Integrity"));
        this.setIsSayHello(jsonObject.optInt("isSayHello"));
        this.setPhone(jsonObject.optString("mobile"));
        this.setQq(jsonObject.optString("qq"));
        this.setPhoneAuth(jsonObject.optInt("mobile_auth"));
        this.setQqNumAuth(jsonObject.optInt("qq_auth"));
        this.setWechat(jsonObject.optString("wechat"));
        this.setWechatAuth(jsonObject.optInt("wechat_auth"));
        this.setIsverifycellphone(jsonObject.optInt("isverifycellphone"));
        this.setPhotoNum(jsonObject.optInt("photoNum"));
        this.setIsmonthmail(jsonObject.optInt("ismonthmail"));
        this.setIsBindRose(jsonObject.optInt("isBindRose"));
        this.setDistance(jsonObject.optInt("distance"));
        this.setIsFollowed(jsonObject.optInt("is_follow"));
        this.setKf_id(jsonObject.isNull("kf_id") ? 0 : jsonObject.optInt("kf_id"));
        this.setIsHot(jsonObject.optBoolean("hot") ? 1 : 0);
        this.setOnline_address(jsonObject.optString("online_address"));
        this.setOnline_date(jsonObject.optString("online_time"));
        this.setWeChatNo(jsonObject.optString("weChat"));
        this.setFollowCont(jsonObject.optInt("followMeCount"));
        this.setLastOnlineTime(jsonObject.optString("l_online_time"));
        this.setMtRedbag(jsonObject.optInt("mt_redbag"));
        this.setGameFlag(jsonObject.optInt("gameFlag"));

        this.setZytj_address(jsonObject.optString("zytj_address"));
        this.setZytj_age(jsonObject.optString("zytj_age"));
        this.setZytj_edu(jsonObject.optString("zytj_edu"));
        this.setZytj_height(jsonObject.optString("zytj_height"));
        this.setZytj_income(jsonObject.optString("zytj_income"));

        // this.setReserve_Channel_uid(jsonObject.optString("channel_uid"));
        this.setPurpose(jsonObject.optString("mudi"));
        this.setLoveIdea(jsonObject.optString("guannian"));
        this.setMeetHope(jsonObject.optString("xiwang"));
        this.setDatePlace(jsonObject.optString("didian"));
        this.setHong(jsonObject.optInt("hong"));

        if ((this.getCuid() == 1) &&
                (jsonObject.optJSONObject("user_address") != null)) {
            JSONObject jso = jsonObject.optJSONObject("user_address");

            this.setProvince("");
            this.setDistance(transDistance(jso.optString("distance")));
            this.setCity(jso.optString("address"));
        }
    }

    public UserVideoInfo getVideoInfo() {
        return videoInfo;
    }

    public void setVideoInfo(UserVideoInfo videoInfo) {
        this.videoInfo = videoInfo;
    }

    public int getGameFlag() {
        return gameFlag;
    }

    public void setGameFlag(int gameFlag) {
        this.gameFlag = gameFlag;
    }

    public int getMtRedbag() {
        return mtRedbag;
    }

    public void setMtRedbag(int mtRedbag) {
        this.mtRedbag = mtRedbag;
    }

    public String getLastOnlineTime() {
        return lastOnlineTime;
    }

    public void setLastOnlineTime(String lastOnlineTime) {
        this.lastOnlineTime = lastOnlineTime;
    }

    public int getHong() {
        return hong;
    }

    public void setHong(int hong) {
        this.hong = hong;
    }

    public String getDatePlace() {
        return datePlace;
    }

    public void setDatePlace(String datePlace) {
        this.datePlace = datePlace;
    }

    public String getMeetHope() {
        return meetHope;
    }

    public void setMeetHope(String meetHope) {
        this.meetHope = meetHope;
    }

    public String getLoveIdea() {
        return loveIdea;
    }

    public void setLoveIdea(String loveIdea) {
        this.loveIdea = loveIdea;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public List<UserPhoto> getUserPhotoList() {
        return userPhotoList;
    }

    public int getFollowCont() {
        return FollowCont;
    }

    public void setFollowCont(int followCont) {
        FollowCont = followCont;
    }

    public String getWeChatNo() {
        return weChatNo;
    }

    public void setWeChatNo(String weChatNo) {
        this.weChatNo = weChatNo;
    }

    public String getOnline_date() {
        return online_date;
    }

    public void setOnline_date(String online_date) {
        this.online_date = online_date;
    }

    public void setOnline_address(String online_address) {
        this.online_address = online_address;
    }

    public String getOnline_address() {
        return online_address;
    }

    public int getIsHot() {
        return isHot;
    }

    public void setIsHot(int isHot) {
        this.isHot = isHot;
    }

    public String getZytj_age() {
        return zytj_age;
    }

    public void setZytj_age(String zytj_age) {
        this.zytj_age = zytj_age;
    }

    public String getZytj_address() {
        return zytj_address;
    }

    public void setZytj_address(String zytj_address) {
        this.zytj_address = zytj_address;
    }

    public String getZytj_height() {
        return zytj_height;
    }

    public void setZytj_height(String zytj_height) {
        this.zytj_height = zytj_height;
    }

    public String getZytj_income() {
        return zytj_income;
    }

    public void setZytj_income(String zytj_income) {
        this.zytj_income = zytj_income;
    }

    public String getZytj_edu() {
        return zytj_edu;
    }

    public void setZytj_edu(String zytj_edu) {
        this.zytj_edu = zytj_edu;
    }

    public int getKf_id() {
        return kf_id;
    }

    public void setKf_id(int kf_id) {
        this.kf_id = kf_id;
    }

    public boolean isFollowed() {
        if (isFollowed == 1) {
            return true;
        }
        return false;
    }

    public void setIsFollowed(int isFollowed) {
        this.isFollowed = isFollowed;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getIsBindRose() {
        return isBindRose;
    }

    public void setIsBindRose(int isBindRose) {
        this.isBindRose = isBindRose;
    }

    public int getIsmonthmail() {
        return ismonthmail;
    }

    public void setIsmonthmail(int ismonthmail) {
        this.ismonthmail = ismonthmail;
    }

    public int getPhotoNum() {
        return photoNum;
    }

    public void setPhotoNum(int photoNum) {
        this.photoNum = photoNum;
    }

    public int getIsverifycellphone() {
        return isverifycellphone;
    }

    public void setIsverifycellphone(int isverifycellphone) {
        this.isverifycellphone = isverifycellphone;
    }

    public int getWechatAuth() {
        return wechatAuth;
    }

    public void setWechatAuth(int wechatAuth) {
        this.wechatAuth = wechatAuth;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public int getQqNumAuth() {
        return qqNumAuth;
    }

    public void setQqNumAuth(int qqNumAuth) {
        this.qqNumAuth = qqNumAuth;
    }

    public int getPhoneAuth() {
        return phoneAuth;
    }

    public void setPhoneAuth(int phoneAuth) {
        this.phoneAuth = phoneAuth;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isSayHello() {
        if (isSayHello == 1) {
            return true;
        }
        return false;
    }

    public void setIsSayHello(int isSayHello) {
        this.isSayHello = isSayHello;
    }

    public String getIntegrity() {
        return Integrity;
    }

    public void setIntegrity(String integrity) {
        Integrity = integrity;
    }

    public boolean isOnline() {
        if (isOnline == 1) {
            return true;
        }
        return false;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public boolean isVip() {
        if (is_vip == 1) {
            return true;
        }
        return false;
    }

    public void setIs_vip(int is_vip) {
        this.is_vip = is_vip;
    }

    public int getCuid() {
        return cuid;
    }

    public void setCuid(int cuid) {
        this.cuid = cuid;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private static int transDistance(String s) {
        int result = 3000;
        if (TextUtils.isEmpty(s))
            return result;
        try {
            int chr;
            int k = 1;
            StringBuilder strbd = new StringBuilder();

            for (int i = 0; i < s.length(); i++) {
                chr = s.charAt(i);
                // 0-9
                if ((chr >= 48 && chr <= 58) || (chr == ".".charAt(0))) {
                    strbd.append((char) chr);
                } else if (chr == "k".charAt(0)) {
                    k = 1000;
                }
            }
            result = (int) Float.parseFloat(strbd.toString()) * k;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.cuid);
        dest.writeInt(this.is_vip);
        dest.writeString(this.sign);
        dest.writeInt(this.isOnline);
        dest.writeString(this.Integrity);
        dest.writeInt(this.isSayHello);
        dest.writeString(this.phone);
        dest.writeString(this.qq);
        dest.writeInt(this.phoneAuth);
        dest.writeInt(this.qqNumAuth);
        dest.writeString(this.wechat);
        dest.writeInt(this.wechatAuth);
        dest.writeInt(this.isverifycellphone);
        dest.writeInt(this.photoNum);
        dest.writeInt(this.ismonthmail);
        dest.writeInt(this.isBindRose);
        dest.writeInt(this.distance);
        dest.writeInt(this.isFollowed);
        dest.writeInt(this.kf_id);
        dest.writeInt(this.isHot);
        dest.writeString(this.online_address);
        dest.writeString(this.online_date);
        dest.writeString(this.weChatNo);
        dest.writeInt(this.FollowCont);
        dest.writeString(this.lastOnlineTime);
        dest.writeInt(this.mtRedbag);
        dest.writeInt(this.gameFlag);
        dest.writeInt(this.hong);
        dest.writeString(this.zytj_address);
        dest.writeString(this.zytj_age);
        dest.writeString(this.zytj_edu);
        dest.writeString(this.zytj_height);
        dest.writeString(this.zytj_income);
        dest.writeString(this.purpose);
        dest.writeString(this.loveIdea);
        dest.writeString(this.meetHope);
        dest.writeString(this.datePlace);
        dest.writeParcelable(this.videoInfo, flags);
        dest.writeTypedList(this.userPhotoList);
        dest.writeString(this.result);
        dest.writeInt(this.status);
        dest.writeString(this.content);
    }

    public UserProfile() {
    }

    protected UserProfile(Parcel in) {
        super(in);
        this.cuid = in.readInt();
        this.is_vip = in.readInt();
        this.sign = in.readString();
        this.isOnline = in.readInt();
        this.Integrity = in.readString();
        this.isSayHello = in.readInt();
        this.phone = in.readString();
        this.qq = in.readString();
        this.phoneAuth = in.readInt();
        this.qqNumAuth = in.readInt();
        this.wechat = in.readString();
        this.wechatAuth = in.readInt();
        this.isverifycellphone = in.readInt();
        this.photoNum = in.readInt();
        this.ismonthmail = in.readInt();
        this.isBindRose = in.readInt();
        this.distance = in.readInt();
        this.isFollowed = in.readInt();
        this.kf_id = in.readInt();
        this.isHot = in.readInt();
        this.online_address = in.readString();
        this.online_date = in.readString();
        this.weChatNo = in.readString();
        this.FollowCont = in.readInt();
        this.lastOnlineTime = in.readString();
        this.mtRedbag = in.readInt();
        this.gameFlag = in.readInt();
        this.hong = in.readInt();
        this.zytj_address = in.readString();
        this.zytj_age = in.readString();
        this.zytj_edu = in.readString();
        this.zytj_height = in.readString();
        this.zytj_income = in.readString();
        this.purpose = in.readString();
        this.loveIdea = in.readString();
        this.meetHope = in.readString();
        this.datePlace = in.readString();
        this.videoInfo = in.readParcelable(UserVideoInfo.class.getClassLoader());
        this.userPhotoList = in.createTypedArrayList(UserPhoto.CREATOR);
        this.result = in.readString();
        this.status = in.readInt();
        this.content = in.readString();
    }

    public static final Creator<UserProfile> CREATOR = new Creator<UserProfile>() {
        @Override
        public UserProfile createFromParcel(Parcel source) {
            return new UserProfile(source);
        }

        @Override
        public UserProfile[] newArray(int size) {
            return new UserProfile[size];
        }
    };
}
