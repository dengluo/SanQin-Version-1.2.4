package com.pbids.sanqin.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:45
 * @desscribe 类描述:公众号订阅数据实体类
 * @remark 备注:
 * @see
 */
public class NewsTopicSubscribe {

	public static final int TOPIC_TYPE_TAGS = 1;
	public static final int TOPIC_TYPE_SURNAME = 6;

    private String title = "";
    private int type = 0;
    private int id = 0;
    private String iconUrl = "";
    private int tid; //  tag id
    private int sid; //  surname id
    private int uid; //  user id
    private String urlLink = "";
    private String surname = "";
    private String organization = ""; //宗亲会

    private int topicType ; //类型

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public int getTopicType() {
        return topicType;
    }

    public void setTopicType(int topicType) {
        this.topicType = topicType;
    }

    public String getUrlLink() {
        return urlLink;
    }

    public void setUrlLink(String urlLink) {
        this.urlLink = urlLink;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }
}
