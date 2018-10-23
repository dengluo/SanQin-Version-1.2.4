package com.pbids.sanqin.model.entity;

import java.io.Serializable;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:42
 * @desscribe 类描述:我的族谱排行实体类
 * @remark 备注:
 * @see
 */
public class GenealogyRank  implements Serializable {
    private String body;
    private long createTime;
    private long id;
    private String organize;
    private int peopleNum;
    private int rank;
    private String surname;
    private int wealth;
    private int trend;
    private String surnameIcon;

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

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
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

    public int getTrend() {
        return trend;
    }

    public void setTrend(int trend) {
        this.trend = trend;
    }

    public String getSurnameIcon() {
        return surnameIcon;
    }

    public void setSurnameIcon(String surnameIcon) {
        this.surnameIcon = surnameIcon;
    }

    @Override
    public String toString() {
        return "GenealogyRank{" +
                "body='" + body + '\'' +
                ", createTime=" + createTime +
                ", id=" + id +
                ", organize='" + organize + '\'' +
                ", peopleNum=" + peopleNum +
                ", rank=" + rank +
                ", surname='" + surname + '\'' +
                ", wealth=" + wealth +
                ", trend=" + trend +
                ", surnameIcon='" + surnameIcon + '\'' +
                '}';
    }
}
