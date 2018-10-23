package com.netease.nim.uikit.business.contact.core.model;

/**
 * Created by liang on 2018/3/21.
 */

public class UserInfoExtends {
	//等级名称
	private String levelName ="";
	//vip
	private int vip ;
	//商务合作
	private int clanStatus;

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public int getVip() {
		return vip;
	}

	public void setVip(int vip) {
		this.vip = vip;
	}

	public int getClanStatus() {
		return clanStatus;
	}

	public void setClanStatus(int clanStatus) {
		this.clanStatus = clanStatus;
	}
}
