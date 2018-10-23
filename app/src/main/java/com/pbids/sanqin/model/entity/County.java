package com.pbids.sanqin.model.entity;

import java.io.Serializable;


/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:40
 * @desscribe 类描述:地区选择列表的城市实体类
 * @remark 备注:
 * @see
 */
public class County implements Serializable {
    String countyId;
    String countyName;

    public String getCountyId() {
        return countyId;
    }

    public void setCountyId(String countyId) {
        this.countyId = countyId;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }
}
