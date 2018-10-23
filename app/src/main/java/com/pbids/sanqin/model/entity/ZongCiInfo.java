package com.pbids.sanqin.model.entity;

import java.util.List;

/**
 * @author 巫哲豪
 * @date on 2018/3/5 14:54
 * @desscribe 类描述:
 * @remark 备注:
 * @see
 */

public class ZongCiInfo {
    private String body;//家族介绍
    private int brickCount;//宗祠砖块
    private int totalDonateBrickCount;//用户贡献的砖块
    private String currentLevelName;//当前宗祠等级
    private int currentLevelCode;//当前宗祠等级码
    private String nextLevelName;//下一级宗祠等级名称
    private long createTime;
    private long id;
    private int level;
    private String organization;///组织机构
    private int peopleNum;
    private int rank;
    private int source;
    private int state;
    private String surname;//家族名称
    private String surnameIcon;
    private String surnameLevel;//宗祠等级
    private int upgradeBrickCount;//升级所需砖块
    private int wealth;
    private List<DonateRecord> donateRecords;//捐赠记录

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getBrickCount() {
        return brickCount;
    }

    public void setBrickCount(int brickCount) {
        this.brickCount = brickCount;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public int getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(int peopleNum) {
        this.peopleNum = peopleNum;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSurnameIcon() {
        return surnameIcon;
    }

    public void setSurnameIcon(String surnameIcon) {
        this.surnameIcon = surnameIcon;
    }

    public String getSurnameLevel() {
        return surnameLevel;
    }

    public void setSurnameLevel(String surnameLevel) {
        this.surnameLevel = surnameLevel;
    }

    public int getUpgradeBrickCount() {
        return upgradeBrickCount;
    }

    public void setUpgradeBrickCount(int upgradeBrickCount) {
        this.upgradeBrickCount = upgradeBrickCount;
    }

    public int getWealth() {
        return wealth;
    }

    public void setWealth(int wealth) {
        this.wealth = wealth;
    }

    public List<DonateRecord> getDonateRecords() {
        return donateRecords;
    }

    public void setDonateRecords(List<DonateRecord> donateRecords) {
        this.donateRecords = donateRecords;
    }

    public int getTotalDonateBrickCount() {
        return totalDonateBrickCount;
    }

    public void setTotalDonateBrickCount(int totalDonateBrickCount) {
        this.totalDonateBrickCount = totalDonateBrickCount;
    }

    public String getCurrentLevelName() {
        return currentLevelName;
    }

    public void setCurrentLevelName(String currentLevelName) {
        this.currentLevelName = currentLevelName;
    }

    public int getCurrentLevelCode() {
        return currentLevelCode;
    }

    public void setCurrentLevelCode(int currentLevelCode) {
        this.currentLevelCode = currentLevelCode;
    }

    public String getNextLevelName() {
        return nextLevelName;
    }

    public void setNextLevelName(String nextLevelName) {
        this.nextLevelName = nextLevelName;
    }

    @Override
    public String toString() {
        return "ZongCiInfo{" +
                ", brickCount=" + brickCount +
                ", totalDonateBrickCount=" + totalDonateBrickCount +
                ", currentLevelName='" + currentLevelName + '\'' +
                ", currentLevelCode=" + currentLevelCode +
                ", nextLevelName='" + nextLevelName + '\'' +
                ", createTime=" + createTime +
                ", id=" + id +
                ", level=" + level +
                ", organization='" + organization + '\'' +
                ", peopleNum=" + peopleNum +
                ", rank=" + rank +
                ", source=" + source +
                ", state=" + state +
                ", surname='" + surname + '\'' +
                ", surnameIcon='" + surnameIcon + '\'' +
                ", surnameLevel='" + surnameLevel + '\'' +
                ", upgradeBrickCount=" + upgradeBrickCount +
                ", wealth=" + wealth +
                ", donateRecords=" + donateRecords +
                '}';
    }
}
