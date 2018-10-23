package com.pbids.sanqin.utils.eventbus;

/**
 * Created by pbids903 on 2018/1/26.
 */

public class LocationEvent {
    private int resultCode;
    private String province;
    private String ctiy;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCtiy() {
        return ctiy;
    }

    public void setCtiy(String ctiy) {
        this.ctiy = ctiy;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
}
