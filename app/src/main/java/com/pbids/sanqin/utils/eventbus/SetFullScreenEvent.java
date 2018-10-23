package com.pbids.sanqin.utils.eventbus;


/**
 * 全屏状态
 */

public class SetFullScreenEvent {

	public static final  int TYPE_IN_WBE = 1;

	private boolean sta;
	private int type ;

	public SetFullScreenEvent(boolean sta,int type){
		this.sta = sta ;
		this.type = type;
	}

	public boolean isSta() {
		return sta;
	}

	public void setSta(boolean sta) {
		this.sta = sta;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
