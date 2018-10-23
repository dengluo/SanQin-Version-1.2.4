package com.pbids.sanqin.model.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:45
 * @desscribe 类描述:地区选择列表中的省份对象
 * @remark 备注:
 * @see
 */
public class Province implements Serializable {
    int peopleNum;
    String provinceId;
    String provinceName;
    ArrayList<com.pbids.sanqin.model.entity.District> district;

    public int getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(int peopleNum) {
        this.peopleNum = peopleNum;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public void setDistrict(ArrayList<com.pbids.sanqin.model.entity.District> district) {
        this.district = district;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public ArrayList<com.pbids.sanqin.model.entity.District> getDistrict() {
        return district;
    }
}
