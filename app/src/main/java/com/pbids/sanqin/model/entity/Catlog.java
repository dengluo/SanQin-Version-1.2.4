package com.pbids.sanqin.model.entity;

import java.io.Serializable;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:40
 * @desscribe 类描述:我的族谱目录实体类
 * @remark 备注:
 * @see
 */
public class Catlog implements Serializable{
    private String catlog;
    private Genealogy genealogy;

    public String getCatlog() {
        return catlog;
    }

    public void setCatlog(String catlog) {
        this.catlog = catlog;
    }

    public Genealogy getGenealogy() {
        return genealogy;
    }

    public void setGenealogy(Genealogy genealogy) {
        this.genealogy = genealogy;
    }

    @Override
    public String toString() {
        return "Catlog{" +
                "catlog='" + catlog + '\'' +
                ", genealogy=" + genealogy +
                '}';
    }
}
