package com.pbids.sanqin.model.entity;

/**
 *
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:41
 * @desscribe 类描述:家族排行实体类
 * @remark 备注:（姓氏认证列表）
 * @see
 */
public class FamilyRank {
    long id;
    String peopleNum;
    int rank;
    String surname;
    int wealth;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(String peopleNum) {
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

    @Override
    public String toString() {
        return "FamilyRank{" +
                "id=" + id +
                ", peopleNum='" + peopleNum + '\'' +
                ", rank=" + rank +
                ", surname='" + surname + '\'' +
                ", wealth=" + wealth +
                '}';
    }
}
