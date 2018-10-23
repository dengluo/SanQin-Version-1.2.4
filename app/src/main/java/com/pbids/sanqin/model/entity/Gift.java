package com.pbids.sanqin.model.entity;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:42
 * @desscribe 类描述:礼券实体类
 * @remark 备注:
 * @see
 */
public class Gift {
    private int aid;
    private long endDate;
    private long id;
    private String couponName;
    private long receiveTime;
    private String remarks;
    private long startDate;
    private int state;
    private int userId;
    private int couponType; //1红，2黄，3蓝
    private String code="";
    private float totalAmount = 0;
    private String redemptionCode = "";

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getRedemptionCode() {
        return redemptionCode;
    }

    public void setRedemptionCode(String redemptionCode) {
        this.redemptionCode = redemptionCode;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public long getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(long receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCouponType() {
        return couponType;
    }

    public void setCouponType(int couponType) {
        this.couponType = couponType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Gift{" +
                "aid=" + aid +
                ", endDate=" + endDate +
                ", id=" + id +
                ", couponName='" + couponName + '\'' +
                ", receiveTime=" + receiveTime +
                ", remarks='" + remarks + '\'' +
                ", startDate=" + startDate +
                ", state=" + state +
                ", userId=" + userId +
                ", couponType=" + couponType +
                ", code='" + code + '\'' +
                ", totalAmount=" + totalAmount +
                ", redemptionCode='" + redemptionCode + '\'' +
                '}';
    }
}
