package com.pbids.sanqin.model.entity;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:38
 * @desscribe 类描述:流水列表的实体类
 * @remark 备注:
 * @see
 */
public class Accounts {
    private int aid;
    private long createTime;
    private long id;
    private String orderNo;
    private int orderStatus;
    private int orderType;
    private int payType;
    private String purpose;
    private float receiptAmount;
    private float totalAmount;
    private long userId;

    private boolean isView = true;

    public boolean isView() {
        return isView;
    }

    public void setView(boolean view) {
        isView = view;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public float getReceiptAmount() {
        return receiptAmount;
    }

    public void setReceiptAmount(float receiptAmount) {
        this.receiptAmount = receiptAmount;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Accounts{" +
                "aid=" + aid +
                ", createTime=" + createTime +
                ", id=" + id +
                ", orderNo='" + orderNo + '\'' +
                ", orderStatus=" + orderStatus +
                ", orderType=" + orderType +
                ", payType=" + payType +
                ", purpose='" + purpose + '\'' +
                ", receiptAmount=" + receiptAmount +
                ", totalAmount=" + totalAmount +
                ", userId=" + userId +
                '}';
    }
}
