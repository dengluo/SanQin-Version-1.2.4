package com.pbids.sanqin.model.entity;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:39
 * @desscribe 类描述:银行卡实体类
 * @remark 备注:
 * @see
 */
public class Bank {
    private String bankName;
    private String cardNumber;
    private String cardType;
    private String bankIcon;
    private long id;
    private int userId;
    private String bankCode;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getBankIcon() {
        return bankIcon;
    }

    public void setBankIcon(String bankIcon) {
        this.bankIcon = bankIcon;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    @Override
    public String toString() {
        return "Bank{" +
                "bankName='" + bankName + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", cardType=" + cardType +
                ", id=" + id +
                ", userId=" + userId +
                ", bankCode=" + bankCode +
                '}';
    }
}
