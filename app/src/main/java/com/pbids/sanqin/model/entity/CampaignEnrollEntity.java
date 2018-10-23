package com.pbids.sanqin.model.entity;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:39
 * @desscribe 类描述:活动报名的随从信息实体类
 * @remark 备注:
 * @see
 */

public class CampaignEnrollEntity {
    private String username ="";
    private String idNumber="";
    private String sex="";
    private int master= 0;

    private long aid =0;
    private long createTime =0;
    private long id =0;
    private long tid =0;
    private long uid =0;


    public int getMaster() {
        return master;
    }

    public void setMaster(int master) {
        this.master = master;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIdcard() {
        return idNumber;
    }

    public void setIdcard(String idcard) {
        this.idNumber = idcard;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public long getAid() {
        return aid;
    }

    public void setAid(long aid) {
        this.aid = aid;
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

    public long getTid() {
        return tid;
    }

    public void setTid(long tid) {
        this.tid = tid;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "CampaignEnrollEntity{" +
                "name='" + username + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", sex='" + sex + '\'' +
                ", master=" + master +
                ", aid=" + aid +
                ", createTime=" + createTime +
                ", id=" + id +
                ", tid=" + tid +
                ", uid=" + uid +
                '}';
    }
//    @Override
//    public String toString() {
//        return new Gson().toJson(this);
//    }
}
