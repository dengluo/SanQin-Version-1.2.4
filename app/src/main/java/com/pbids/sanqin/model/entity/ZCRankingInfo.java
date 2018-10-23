package com.pbids.sanqin.model.entity;

/**
 * @author 巫哲豪
 * @date on 2018/3/6 10:30
 * @desscribe 类描述:
 * @remark 备注:
 * @see
 */

public class ZCRankingInfo {
    private int donateBrickCount;//捐赠砖块数
    private long donateTime;//捐赠时间
    private int empiric;//经验值
    private String faceUrl = "";//头像
    private long id;
    private String levelName = "";//等级头衔
    private int rank;//排名
    private int surnameId;//姓氏id
    private long userId;//用户id
    private String name = "";//用户名
    private int vip;//是否为VIP 0 不是 1 是
    private int rankStatus;//排行状态  1上升 0不变  -1 下降
    private int clanStatus; //宗

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

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
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

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    public int getRankStatus() {
        return rankStatus;
    }

    public void setRankStatus(int rankStatus) {
        this.rankStatus = rankStatus;
    }

    @Override
    public String toString() {
        return "ZCRankingInfo{" +
                "donateBrickCount=" + donateBrickCount +
                ", donateTime=" + donateTime +
                ", empiric=" + empiric +
                ", faceUrl='" + faceUrl + '\'' +
                ", id=" + id +
                ", levelName='" + levelName + '\'' +
                ", rank=" + rank +
                ", surnameId=" + surnameId +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", vip=" + vip +
                ", rankStatus=" + rankStatus +
                '}';
    }

    public int getClanStatus() {
        return clanStatus;
    }

    public void setClanStatus(int clanStatus) {
        this.clanStatus = clanStatus;
    }
}
