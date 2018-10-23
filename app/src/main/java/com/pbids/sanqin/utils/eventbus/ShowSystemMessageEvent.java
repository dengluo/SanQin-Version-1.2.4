package com.pbids.sanqin.utils.eventbus;


import com.netease.nimlib.sdk.msg.model.IMMessage;

/**
 * 点击消息栏
 */

public class ShowSystemMessageEvent {

	private int type;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
