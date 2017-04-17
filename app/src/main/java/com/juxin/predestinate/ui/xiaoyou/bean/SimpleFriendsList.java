package com.juxin.predestinate.ui.xiaoyou.bean;


import android.os.Parcel;
import android.os.Parcelable;

import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONObject;

import java.util.List;

/**
 * 小友首页最近互动列表
 * Created by zm on 17/3/23.
 */
public class SimpleFriendsList extends BaseData{

    private List arr_frends;

    public List getArr_frends() {
        return arr_frends;
    }

    @Override
    public void parseJson(String s) {
        arr_frends = getBaseDataList(getJsonObject(s).optJSONArray("list"), SimpleFriendInfo.class);
    }

    public static class SimpleFriendInfo extends BaseFriendInfo implements Parcelable{
        private long uid;//用户ID
        private boolean isCheck;//选中状态 为true：选中 ；为false：未选中
        private int Intimate;

        public SimpleFriendInfo(){

        }
        protected SimpleFriendInfo(Parcel in) {
            nickname = in.readString();
            sortKey = in.readString();
            uid = in.readLong();
            isCheck = in.readByte() != 0;
            Intimate = in.readInt();
            mUserInfoLightweight = in.readParcelable(UserInfoLightweight.class.getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(nickname);
            dest.writeString(sortKey);
            dest.writeLong(uid);
            dest.writeByte((byte) (isCheck ? 1 : 0));
            dest.writeInt(Intimate);
            dest.writeParcelable(mUserInfoLightweight, flags);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<SimpleFriendInfo> CREATOR = new Creator<SimpleFriendInfo>() {
            @Override
            public SimpleFriendInfo createFromParcel(Parcel in) {
                return new SimpleFriendInfo(in);
            }

            @Override
            public SimpleFriendInfo[] newArray(int size) {
                return new SimpleFriendInfo[size];
            }
        };

        @Override
        public void parseJson(String s) {

            JSONObject jsonObject = getJsonObject(s);
            //json串解析
            this.setUid(jsonObject.optLong("Uid"));
            this.setIntimate(jsonObject.optInt("Intimate"));
//            this.setNickname(jsonObject.optString(""));
        }

        public long getUid() {
            return uid;
        }

        public void setUid(long uid) {
            this.uid = uid;
        }

        public boolean isCheck() {
            return isCheck;
        }

        public void setIsCheck(boolean isCheck) {
            this.isCheck = isCheck;
        }

        public int getIntimate() {
            return Intimate;
        }

        public void setIntimate(int intimate) {
            Intimate = intimate;
        }

        public void setUserInfoLightweight(UserInfoLightweight userInfoLightweight) {
            mUserInfoLightweight = userInfoLightweight;
            setNickname(mUserInfoLightweight.getNickname());
        }

        public String getSortKey() {
            return sortKey;
        }

        public void setSortKey(String sortKey) {
            this.sortKey = sortKey;
        }

        @Override
        public String toString() {
            return "RankList{" +
                    "uid=" + uid +
                    ", sortKey=" + sortKey +
                    ", nickname=" + nickname +
                    ", isCheck=" + isCheck +
                    ", Intimate=" + Intimate +
                    ", mUserInfoLightweight=" + mUserInfoLightweight +
                    '}';
        }
    }
}