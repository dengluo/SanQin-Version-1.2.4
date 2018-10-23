package com.pbids.sanqin.model.entity;

import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:45
 * @desscribe 类描述:系统通知数据实体类
 * @remark 备注:
 * @see
 */
@Entity
public class SystemMessage {
    @Id(autoincrement = true)
    private Long _id;
    private String sessionId ="";
    private int sessionType;
    private String fromAccount="";
    private long time;
    private String title=""; //消息
    private int type; //类型
    private int msgtype; // 消息类型
    private String content=""; //内容
    private boolean isread;   //已读标记
    private String url="";   //url
    private String topic=""; //话题
	private String icon=""; //icon
    private String subTitle="";//简介
    private Long uid ; //用户id
    private String surname="";
    private String tags="";
    private String surnameIcon = "";

    @Generated(hash = 1678475089)
    public SystemMessage(Long _id, String sessionId, int sessionType,
            String fromAccount, long time, String title, int type, int msgtype,
            String content, boolean isread, String url, String topic, String icon,
            String subTitle, Long uid, String surname, String tags,
            String surnameIcon) {
        this._id = _id;
        this.sessionId = sessionId;
        this.sessionType = sessionType;
        this.fromAccount = fromAccount;
        this.time = time;
        this.title = title;
        this.type = type;
        this.msgtype = msgtype;
        this.content = content;
        this.isread = isread;
        this.url = url;
        this.topic = topic;
        this.icon = icon;
        this.subTitle = subTitle;
        this.uid = uid;
        this.surname = surname;
        this.tags = tags;
        this.surnameIcon = surnameIcon;
    }
    @Generated(hash = 859060589)
    public SystemMessage() {
    }
    public Long get_id() {
        return this._id;
    }
    public void set_id(Long _id) {
        this._id = _id;
    }
    public String getSessionId() {
        return this.sessionId;
    }
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    public int getSessionType() {
        return this.sessionType;
    }
    public void setSessionType(int sessionType) {
        this.sessionType = sessionType;
    }
    public String getFromAccount() {
        return this.fromAccount;
    }
    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }
    public long getTime() {
        return this.time;
    }
    public void setTime(long time) {
        this.time = time;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public int getMsgtype() {
        return this.msgtype;
    }
    public void setMsgtype(int msgtype) {
        this.msgtype = msgtype;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public boolean getIsread() {
        return this.isread;
    }
    public void setIsread(boolean isread) {
        this.isread = isread;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getTopic() {
        return this.topic;
    }
    public void setTopic(String topic) {
        this.topic = topic;
    }
    public String getIcon() {
        return this.icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
    public String getSurname() {
        return this.surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public String getTags() {
        return this.tags;
    }
    public void setTags(String tags) {
        this.tags = tags;
    }
    public String getSurnameIcon() {
        return this.surnameIcon;
    }
    public void setSurnameIcon(String surnameIcon) {
        this.surnameIcon = surnameIcon;
    }
}
