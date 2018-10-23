package com.pbids.sanqin.model.entity;

/**
 * @author 巫哲豪
 * @date on 2018/3/10 16:20
 * @desscribe 类描述:
 * @remark 备注:
 * @see
 */

public class WechatPayInfo {
    private String appid;
    private String sign;
    private String trade_type;
    private String return_msg;
    private String result_code;
    private String partnerid;
    private String prepayid;
    private String return_code;
    private String noncestr;
    private String timeStamp;
    private String packageValue;

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    @Override
    public String toString() {
        return "WechatPayInfo{" +
                "appid='" + appid + '\'' +
                ", sign='" + sign + '\'' +
                ", trade_type='" + trade_type + '\'' +
                ", return_msg='" + return_msg + '\'' +
                ", result_code='" + result_code + '\'' +
                ", partnerid='" + partnerid + '\'' +
                ", prepayid='" + prepayid + '\'' +
                ", return_code='" + return_code + '\'' +
                ", noncestr='" + noncestr + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", packageValue='" + packageValue + '\'' +
                '}';
    }
}
