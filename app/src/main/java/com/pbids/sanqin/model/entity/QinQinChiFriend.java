package com.pbids.sanqin.model.entity;

import com.netease.nimlib.sdk.msg.model.RecentContact;

/**
 * Created by pbids903 on 2018/1/25.
 */

public class QinQinChiFriend {

    private long id=0;
    private String sortLetters; //
    private String name; //名称
    private boolean selected ; //选择
    private String faceUrl; //头像
    private String sex="";//性别
    private int surnameId;//姓氏表的id
    private int isVIP = 0; //是否为vip 0 不是 1 是
    private int empiric; //经验值
    private long userId; //用户id
    private String levelName="";

    private RecentContact recentContact;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getSurnameId() {
        return surnameId;
    }

    public void setSurnameId(int surnameId) {
        this.surnameId = surnameId;
    }

    public int getIsVIP() {
        return isVIP;
    }

    public void setIsVIP(int isVIP) {
        this.isVIP = isVIP;
    }

    public int getEmpiric() {
        return empiric;
    }

    public void setEmpiric(int empiric) {
        this.empiric = empiric;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

	@Override
	public String toString() {
		return "SortUserModel{" +
				"sortLetters='" + sortLetters + '\'' +
				", name='" + name + '\'' +
				'}';
	}

    public RecentContact getRecentContact() {
        return recentContact;
    }

    public void setRecentContact(RecentContact recentContact) {
        this.recentContact = recentContact;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
