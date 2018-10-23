package com.pbids.sanqin.utils.eventbus;


import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.ArrayList;

/**
 * 点击消息栏
 */

public class OnClickNotificationEvent {

    private String accid;

    private IMMessage message;

    private String notifyType ;

    public String getAccid() {
        return accid;
    }

    public void setAccid(String accid) {
        this.accid = accid;
    }


    public IMMessage getMessage() {
        return message;
    }

    public void setMessage(IMMessage message) {
        this.message = message;
    }


    public String getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType;
    }
}
