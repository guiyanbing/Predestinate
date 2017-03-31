package com.juxin.predestinate.bean.center.user.detail;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.juxin.predestinate.bean.net.BaseData;
import com.juxin.predestinate.module.logic.config.AreaConfig;
import com.juxin.predestinate.module.logic.config.InfoConfig;

import org.json.JSONObject;

/**
 * 用户信息基类
 */
public class UserBasic extends BaseData implements Parcelable {
    private long uid;            // 用户Id
    private String avatar;       // 头像
    private int avatar_status;   // 头像状态 -1:没有数据  0:正在审核 1:审核通过 2:未通过 3:未上传（老版本） 4：好 5：很好 6待复审 7 新版未审核
    private int gender;          // 性别 1男2女
    private String nickname;     // 昵称
    private int nickName_status; // 昵称状态  0未审核 1 通过 2 未通过'

    // 地址
    private String province;      // 完整省份
    private String city;          // 完整城市
    private int scity;            // 城市代码
    private int sprovince;        // 省份代码

    // 自己
    private int age;             // 年龄

    // 他人
    private String birthday;     // 生日
    private String edu;          // 学历  1:初中及以下 2：高中及中专 3，大专 4 ，本科 5：硕士及以上
    private int height;          // 身高
    private String income;       // 收入情况
    private String job;          // 工作情况
    private String marry;        // 情感状态  1:单身， 2:恋爱中， 3:已婚， 4:保密
    private String weight;       //体重
    private String star;         // 星座

    @Override
    public void parseJson(String s) {
        InfoConfig infoConfig = InfoConfig.getInstance();
        JSONObject jsonObject = getJsonObject(s);
        this.setAge(jsonObject.optInt("age"));
        this.setAvatar(jsonObject.optString("avatar"));
        this.setAvatar_status(jsonObject.optInt("avatar_status"));
        this.setBirthday(jsonObject.optString("birthday"));
        this.setUid(jsonObject.optLong("uid"));
        this.setNickname(jsonObject.optString("nickname"));
        this.setNickName_status(jsonObject.optInt("nickname_status"));
        this.setGender(jsonObject.optInt("gender"));
        this.setHeight(jsonObject.optInt("height"));

        this.setJob(infoConfig.getJob().getShowWithSubmit(jsonObject.optInt("job")));
        this.setEdu(infoConfig.getEdu().getShowWithSubmit(jsonObject.optInt("edu")));
        this.setIncome(infoConfig.getIncome().getShowWithSubmit(jsonObject.optInt("income")));
        this.setMarry(infoConfig.getMarry().getShowWithSubmit(jsonObject.optInt("marry")));
        this.setWeight(infoConfig.getWeight().getShowWithSubmit(jsonObject.optInt("weight")));
        this.setStar(infoConfig.getStar().getShowWithSubmit(jsonObject.optInt("star")));

        int pid = jsonObject.optInt("province");
        int cit = jsonObject.optInt("city");
        this.setScity(cit);
        this.setSprovince(pid);
        this.setProvince(AreaConfig.getInstance().getProvinceByID(pid));
        this.setCity(AreaConfig.getInstance().getCityByID(cit));
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getAvatar_status() {
        return avatar_status;
    }

    public void setAvatar_status(int avatar_status) {
        this.avatar_status = avatar_status;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getNickName_status() {
        return nickName_status;
    }

    public void setNickName_status(int nickName_status) {
        this.nickName_status = nickName_status;
    }

    public String getProvince() {
        return province;
    }

    public String getAddress() {
        String _province = (TextUtils.isEmpty(province) || "不限".equals(province)) ? "" : province;
        String _city = (TextUtils.isEmpty(city) || "不限".equals(city)) ? "" : " " + city;
        return _province + _city;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getScity() {
        return scity;
    }

    public void setScity(int scity) {
        this.scity = scity;
    }

    public int getSprovince() {
        return sprovince;
    }

    public void setSprovince(int sprovince) {
        this.sprovince = sprovince;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEdu() {
        return edu;
    }

    public void setEdu(String edu) {
        this.edu = edu;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getMarry() {
        return marry;
    }

    public void setMarry(String marry) {
        this.marry = marry;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.uid);
        dest.writeString(this.avatar);
        dest.writeInt(this.avatar_status);
        dest.writeInt(this.gender);
        dest.writeString(this.nickname);
        dest.writeInt(this.nickName_status);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeInt(this.scity);
        dest.writeInt(this.sprovince);
        dest.writeInt(this.age);
        dest.writeString(this.birthday);
        dest.writeString(this.edu);
        dest.writeInt(this.height);
        dest.writeString(this.income);
        dest.writeString(this.job);
        dest.writeString(this.marry);
        dest.writeString(this.weight);
        dest.writeString(this.star);
    }

    public UserBasic() {
    }

    protected UserBasic(Parcel in) {
        this.uid = in.readLong();
        this.avatar = in.readString();
        this.avatar_status = in.readInt();
        this.gender = in.readInt();
        this.nickname = in.readString();
        this.nickName_status = in.readInt();
        this.province = in.readString();
        this.city = in.readString();
        this.scity = in.readInt();
        this.sprovince = in.readInt();
        this.age = in.readInt();
        this.birthday = in.readString();
        this.edu = in.readString();
        this.height = in.readInt();
        this.income = in.readString();
        this.job = in.readString();
        this.marry = in.readString();
        this.weight = in.readString();
        this.star = in.readString();
    }

    public static final Creator<UserBasic> CREATOR = new Creator<UserBasic>() {
        @Override
        public UserBasic createFromParcel(Parcel source) {
            return new UserBasic(source);
        }

        @Override
        public UserBasic[] newArray(int size) {
            return new UserBasic[size];
        }
    };

}
