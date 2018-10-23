package com.pbids.sanqin.utils.eventbus;


import com.netease.nimlib.sdk.msg.constant.SystemMessageType;

/**
 * 点击消息栏
 */

public class NimSystemMessageEvent {

	public static final int  ADD_FRIEND  = 1;

	private int type;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
