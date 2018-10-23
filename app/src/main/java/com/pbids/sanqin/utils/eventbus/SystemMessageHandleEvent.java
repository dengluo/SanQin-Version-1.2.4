package com.pbids.sanqin.utils.eventbus;

/**
 * 系统消息事件
 */

public class SystemMessageHandleEvent {



    private int requestCode;
    private int Type;
    private int msgTpme;

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public int getMsgTpme() {
        return msgTpme;
    }

    public void setMsgTpme(int msgTpme) {
        this.msgTpme = msgTpme;
    }
}