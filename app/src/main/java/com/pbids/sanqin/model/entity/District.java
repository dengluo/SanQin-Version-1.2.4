package com.pbids.sanqin.model.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:41
 * @desscribe 类描述:地区选择列表的县实体类
 * @remark 备注:
 * @see
 */
public class District implements Serializable {
    String districtId;
    String districtName;
    ArrayList<County> county;

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public ArrayList<County> getCounty() {
        return county;
    }

    public void setCounty(ArrayList<County> county) {
        this.county = county;
    }
}
