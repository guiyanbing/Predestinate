package com.juxin.predestinate.bean.center.user;

import android.os.Parcel;
import android.os.Parcelable;

import com.juxin.predestinate.module.config.AreaConfig;
import com.juxin.predestinate.module.config.InfoConfig;
import com.juxin.predestinate.module.logic.base.BaseData;

import org.json.JSONObject;

/**
 * 用户信息基类
 */
public class UserBasic extends BaseData implements Parcelable {
    private String avatar; // 头像
    private int avatar_status; // 头像状态 -1:没有数据  0:正在审核 1:审核通过 2:未通过 3:未上传（老版本） 4：好 5：很好 6待复审 7 新版未审核
    private String birthday; // 生日
    private String edu; // 学历  1:初中及以下 2：高中及中专 3，大专 4 ，本科 5：硕士及以上
    private int gender; // 性别 1男2女
    private int height; // 身高
    private String income; // 收入情况
    private String job; // 工作情况
    private String marry; // 情感状态  1:单身， 2:恋爱中， 3:已婚， 4:保密
    private String nickname; // 昵称
    private int nickName_status; // 昵称状态  0未审核 1 通过 2 未通过'
    private String weight; //体重
    private long uid; // 用户Id

    // 地址
    private String province;      // 完整省份
    private String city;          // 完整城市
    private String provinceName;  // 展示省份
    private String cityName;      // 展示城市
    private int sprovince;        // 省份代码
    private int scity;            // 城市代码
    private int lprovince;        //当前所在省份
    private int lcity;            //当前所在城市

    public static final Creator<UserBasic> CREATOR = new Creator<UserBasic>() {
        @Override
        public UserBasic createFromParcel(Parcel in) {
            return new UserBasic(in);
        }

        @Override
        public UserBasic[] newArray(int size) {
            return new UserBasic[size];
        }
    };

    @Override
    public void parseJson(String s) {
        InfoConfig infoConfig = InfoConfig.getInstance();
        JSONObject jsonObject = getJsonObject(s);
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

        int pid = jsonObject.optInt("province");
        int cit = jsonObject.optInt("city");
        this.setScity(cit);
        this.setSprovince(pid);
        this.setProvince(AreaConfig.getInstance().getProvinceByID(pid));
        this.setCity(AreaConfig.getInstance().getCityByID(cit));
        this.setProvinceName(AreaConfig.getInstance().getProvinceNameByID(pid));
        this.setCityName(AreaConfig.getInstance().getCityNameByID(cit));
        this.setLprovince(jsonObject.optInt("lprovince"));
        this.setLcity(jsonObject.optInt("lcity"));
    }

    public int getAge() {
//        return ModuleMgr.getCenterMgr().getUserAge(birthday);
        return 18;
    }

    public String getMarry() {
        if ("不限".equals(marry)) {
            return "";
        }
        return marry;
    }

    public void setMarry(String marry) {
        this.marry = marry;
    }

    public String getIncome() {
        if ("不限".equals(income)) {
            return "";
        }
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getEdu() {
        if ("不限".equals(edu)) {
            return "";
        }
        return edu;
    }

    public void setEdu(String edu) {
        this.edu = edu;
    }

    public String getJob() {
        if ("不限".equals(job)) {
            return "";
        }
        return job;
    }

    public void setJob(String job) {
        this.job = job;
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

    public int getNickName_status() {
        return nickName_status;
    }

    public void setNickName_status(int nickName_status) {
        this.nickName_status = nickName_status;
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

    public String getProvince() {
        return province;
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

    public int getLprovince() {
        return lprovince;
    }

    public void setLprovince(int lprovince) {
        this.lprovince = lprovince;
    }

    public int getLcity() {
        return lcity;
    }

    public void setLcity(int lcity) {
        this.lcity = lcity;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    /**
     * 获取简略省市拼接地址
     */
    public String getAddressShow() {
        String _province = (provinceName == null || "不限".equals(provinceName)) ? "" : provinceName;
        String _city = (cityName == null || "不限".equals(cityName)) ? "" : " " + cityName;
        return _province + _city;
    }

    /**
     * 获取省市拼接地址
     */
    public String getAddress() {
        String _province = (province == null || "不限".equals(province)) ? "" : province;
        String _city = (city == null || "不限".equals(city)) ? "" : " " + city;
        return _province + _city;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getWeight() {
        if ("不限".equals(weight)) {
            return "";
        }
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }


    public UserBasic() {
    }

    @Override
    public String toString() {
        return "UserBasic{" +
                "avatar='" + avatar + '\'' +
                ", avatar_status=" + avatar_status +
                ", birthday='" + birthday + '\'' +
                ", edu='" + edu + '\'' +
                ", gender=" + gender +
                ", height=" + height +
                ", income='" + income + '\'' +
                ", job='" + job + '\'' +
                ", marry='" + marry + '\'' +
                ", nickname='" + nickname + '\'' +
                ", nickName_status=" + nickName_status +
                ", weight='" + weight + '\'' +
                ", uid=" + uid +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", provinceName='" + provinceName + '\'' +
                ", cityName='" + cityName + '\'' +
                ", sprovince=" + sprovince +
                ", scity=" + scity +
                ", lprovince=" + lprovince +
                ", lcity=" + lcity +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.avatar);
        dest.writeInt(this.avatar_status);
        dest.writeString(this.birthday);
        dest.writeString(this.edu);
        dest.writeInt(this.gender);
        dest.writeInt(this.height);
        dest.writeString(this.income);
        dest.writeString(this.job);
        dest.writeString(this.marry);
        dest.writeString(this.nickname);
        dest.writeInt(this.nickName_status);
        dest.writeString(this.weight);
        dest.writeLong(this.uid);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.provinceName);
        dest.writeString(this.cityName);
        dest.writeInt(this.sprovince);
        dest.writeInt(this.scity);
        dest.writeInt(this.lprovince);
        dest.writeInt(this.lcity);
    }

    protected UserBasic(Parcel in) {
        this.avatar = in.readString();
        this.avatar_status = in.readInt();
        this.birthday = in.readString();
        this.edu = in.readString();
        this.gender = in.readInt();
        this.height = in.readInt();
        this.income = in.readString();
        this.job = in.readString();
        this.marry = in.readString();
        this.nickname = in.readString();
        this.nickName_status = in.readInt();
        this.weight = in.readString();
        this.uid = in.readLong();
        this.province = in.readString();
        this.city = in.readString();
        this.provinceName = in.readString();
        this.cityName = in.readString();
        this.sprovince = in.readInt();
        this.scity = in.readInt();
        this.lprovince = in.readInt();
        this.lcity = in.readInt();
    }

}
