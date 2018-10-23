package com.pbids.sanqin.model.entity;

import java.util.List;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:39
 * @desscribe 类描述:地区实体类，其下级为省
 * @remark 备注:
 * @see
 */
public class Area {
    List<Province> data;

    public List<Province> getData() {
        return data;
    }
}
