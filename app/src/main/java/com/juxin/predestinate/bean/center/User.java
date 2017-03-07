package com.juxin.predestinate.bean.center;

import android.os.Parcel;
import android.os.Parcelable;

import com.juxin.predestinate.module.logic.base.BaseData;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 用户资料
 * Created by ZRP on 2017/3/7.
 */
@Entity
public class User extends BaseData implements Parcelable {

    @Id(autoincrement = true)
    private Long id;

    @Unique
    private long uid;
    private String nickname;
    private String avatar;

    @Override
    public void parseJson(String jsonStr) {
        //TODO 解析
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", uid=" + uid +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeLong(this.uid);
        dest.writeString(this.nickname);
        dest.writeString(this.avatar);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.uid = in.readLong();
        this.nickname = in.readString();
        this.avatar = in.readString();
    }

    @Generated(hash = 157274613)
    public User(Long id, long uid, String nickname, String avatar) {
        this.id = id;
        this.uid = uid;
        this.nickname = nickname;
        this.avatar = avatar;
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
