package com.pbids.sanqin.model.entity;

/**
 * @author 巫哲豪
 * @date on 2018/3/5 14:44
 * @desscribe 类描述:宗祠-捐赠记录实体类
 * @remark 备注:
 * @see
 */

public class DonateRecord {
    private int donateBrickCount;//捐赠砖块数
    private long donateTime;//捐赠时间
    private int empiric;
    private String faceUrl;
    private long id;
    private int surnameId;
    private long userId;
    private String name;

    public int getDonateBrickCount() {
        return donateBrickCount;
    }

    public void setDonateBrickCount(int donateBrickCount) {
        this.donateBrickCount = donateBrickCount;
    }

    public long getDonateTime() {
        return donateTime;
    }

    public void setDonateTime(long donateTime) {
        this.donateTime = donateTime;
    }

    public int getEmpiric() {
        return empiric;
    }

    public void setEmpiric(int empiric) {
        this.empiric = empiric;
    }

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSurnameId() {
        return surnameId;
    }

    public void setSurnameId(int surnameId) {
        this.surnameId = surnameId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DonateRecord{" +
                "donateBrickCount=" + donateBrickCount +
                ", donateTime=" + donateTime +
                ", empiric=" + empiric +
                ", faceUrl='" + faceUrl + '\'' +
                ", id=" + id +
                ", surnameId=" + surnameId +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                '}';
    }
}
