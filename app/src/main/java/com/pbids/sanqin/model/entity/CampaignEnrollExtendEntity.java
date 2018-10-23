package com.pbids.sanqin.model.entity;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:40
 * @desscribe 类描述:活动报名的扩展信息
 * @remark 备注:
 * @see
 */

public class CampaignEnrollExtendEntity {
    private long aid=0;
    private long attendTime=0;
    private float discountNum=0;
    private long id=0;
    private int isPay=-1;
    private String orderNo="";
    private String phone="";
    private String remark="";
    private float price=0;
    private int reviewed=-1;
    private long uid=0;

    public long getAid() {
        return aid;
    }

    public void setAid(long aid) {
        this.aid = aid;
    }

    public long getAttendTime() {
        return attendTime;
    }

    public void setAttendTime(long attendTime) {
        this.attendTime = attendTime;
    }

    public float getDiscountNum() {
        return discountNum;
    }

    public void setDiscountNum(float discountNum) {
        this.discountNum = discountNum;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getIsPay() {
        return isPay;
    }

    public void setIsPay(int isPay) {
        this.isPay = isPay;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getReviewed() {
        return reviewed;
    }

    public void setReviewed(int reviewed) {
        this.reviewed = reviewed;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "CampaignEnrollExtendEntity{" +
                "aid=" + aid +
                ", attendTime=" + attendTime +
                ", discountNum=" + discountNum +
                ", id=" + id +
                ", isPay=" + isPay +
                ", orderNo='" + orderNo + '\'' +
                ", phone='" + phone + '\'' +
                ", remark='" + remark + '\'' +
                ", price=" + price +
                ", reviewed=" + reviewed +
                ", uid=" + uid +
                '}';
    }
}
