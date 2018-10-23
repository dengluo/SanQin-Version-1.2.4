package com.netease.nim.uikit.business.eventbus;

/**
 * Created by pbids920 on 2018/6/22.
 */

public class InviteMessage {

    public String message;

    public int type;

    public InviteMessage(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
