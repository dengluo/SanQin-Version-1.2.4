package com.pbids.sanqin.model.entity;

import java.io.Serializable;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:41
 * @desscribe 类描述:我的族谱json实体类
 * @remark 备注:
 * @see
 */
public class GenealogyInfo  implements Serializable {
    private String body;
    private long createTime;
    private long id;
    private String organize;
    private int peopleNum;
    private String surname;
    private int wealth;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrganize() {
        return organize;
    }

    public void setOrganize(String organize) {
        this.organize = organize;
    }

    public int getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(int peopleNum) {
        this.peopleNum = peopleNum;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getWealth() {
        return wealth;
    }

    public void setWealth(int wealth) {
        this.wealth = wealth;
    }

    @Override
    public String toString() {
        return "GenealogyInfo{" +
                "body='" + body + '\'' +
                ", createTime=" + createTime +
                ", id=" + id +
                ", organize='" + organize + '\'' +
                ", peopleNum=" + peopleNum +
                ", surname='" + surname + '\'' +
                ", wealth=" + wealth +
                '}';
    }
}
