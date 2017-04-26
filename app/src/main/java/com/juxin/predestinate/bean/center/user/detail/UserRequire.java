package com.juxin.predestinate.bean.center.user.detail;

import com.juxin.predestinate.bean.net.BaseData;
import com.juxin.predestinate.module.logic.config.AreaConfig;
import com.juxin.predestinate.module.logic.config.InfoConfig;

import org.json.JSONObject;

/**
 * 用户交友需求
 */
public class UserRequire extends BaseData {
    private int uid;
    private String rqMarry;
    private int rqAgeMin;
    private int rqAgeMax;
    private String rqProvince;
    private String rqCity;
    private String rqEdu;
    private String rqIncome;
    private int rqHeightMin;
    private int rqHeightMax;
    private String eMarry;
    private String eStar;
    private String eJob;
    private String eWeight;

    @Override
    public void parseJson(String jsonStr) {
        InfoConfig infoConfig = InfoConfig.getInstance();
        JSONObject requireObject = getJsonObject(jsonStr);

        this.setUid(requireObject.optInt("uid"));
        this.setRqMarry(infoConfig.getMarry().getShowWithSubmit(requireObject.optInt("rq_marry")));
        this.setRqAgeMin(requireObject.optInt("rq_age_min"));
        this.setRqAgeMax(requireObject.optInt("rq_age_max"));
        int rq_pid = requireObject.optInt("rq_province");
        this.setRqProvince(AreaConfig.getInstance().getProvinceByID(rq_pid));
        this.setRqCity(AreaConfig.getInstance().getCityByID(requireObject.optInt("rq_city")));
        this.setRqEdu(infoConfig.getEdu().getShowWithSubmit(requireObject.optInt("rq_edu")));
        this.setRqIncome(infoConfig.getIncome().getShowWithSubmit(requireObject.optInt("rq_income")));
        this.setRqHeightMin(requireObject.optInt("rq_height_min"));
        this.setRqHeightMax(requireObject.optInt("rq_height_max"));
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getRqAgeMin() {
        return rqAgeMin;
    }

    public void setRqAgeMin(int rqAgeMin) {
        this.rqAgeMin = rqAgeMin;
    }

    public int getRqAgeMax() {
        return rqAgeMax;
    }

    public void setRqAgeMax(int rqAgeMax) {
        this.rqAgeMax = rqAgeMax;
    }

    public int getRqHeightMin() {
        return rqHeightMin;
    }

    public void setRqHeightMin(int rqHeightMin) {
        this.rqHeightMin = rqHeightMin;
    }

    public int getRqHeightMax() {
        return rqHeightMax;
    }

    public void setRqHeightMax(int rqHeightMax) {
        this.rqHeightMax = rqHeightMax;
    }

    public String getRqMarry() {
        return rqMarry;
    }

    public void setRqMarry(String rqMarry) {
        this.rqMarry = rqMarry;
    }

    public String getRqProvince() {
        return rqProvince;
    }

    public void setRqProvince(String rqProvince) {
        this.rqProvince = rqProvince;
    }

    public String getRqCity() {
        return rqCity;
    }

    public void setRqCity(String rqCity) {
        this.rqCity = rqCity;
    }

    public String getRqEdu() {
        return rqEdu;
    }

    public void setRqEdu(String rqEdu) {
        this.rqEdu = rqEdu;
    }

    public String getRqIncome() {
        return rqIncome;
    }

    public void setRqIncome(String rqIncome) {
        this.rqIncome = rqIncome;
    }

    public String geteMarry() {
        return eMarry;
    }

    public void seteMarry(String eMarry) {
        this.eMarry = eMarry;
    }

    public String geteStar() {
        return eStar;
    }

    public void seteStar(String eStar) {
        this.eStar = eStar;
    }

    public String geteJob() {
        return eJob;
    }

    public void seteJob(String eJob) {
        this.eJob = eJob;
    }

    public String geteWeight() {
        return eWeight;
    }

    public void seteWeight(String eWeight) {
        this.eWeight = eWeight;
    }

    @Override
    public String toString() {
        return "UserRequire [uid=" + uid + ", rqMarry=" + rqMarry + ", rqAgeMin=" + rqAgeMin + ", rqAgeMax=" + rqAgeMax + ", rqProvince="
                + rqProvince + ", rqCity=" + rqCity + ", rqEdu=" + rqEdu + ", rqIncome=" + rqIncome + ", rqHeightMin=" + rqHeightMin
                + ", rqHeightMax=" + rqHeightMax + "]";
    }

    public UserRequire Clone() {
        UserRequire user = new UserRequire();

        user.uid = this.uid;
        user.rqMarry = this.rqMarry;
        user.rqAgeMin = this.rqAgeMin;
        user.rqAgeMax = this.rqAgeMax;
        user.rqProvince = this.rqProvince;
        user.rqCity = this.rqCity;
        user.rqEdu = this.rqEdu;
        user.rqIncome = this.rqIncome;
        user.rqHeightMin = this.rqHeightMin;
        user.rqHeightMax = this.rqHeightMax;
        user.eMarry = this.eMarry;
        user.eStar = this.eStar;
        user.eJob = this.eJob;
        user.eWeight = this.eWeight;
        return user;
    }
}
