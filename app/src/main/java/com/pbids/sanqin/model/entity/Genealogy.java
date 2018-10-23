package com.pbids.sanqin.model.entity;

import java.io.Serializable;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:41
 * @desscribe 类描述:我的族谱目录信息
 * @remark 备注:
 * @see
 */
public class Genealogy implements Serializable{
    private int aid;
    private int catid;
    private int surid;
    private String body;

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public int getCatid() {
        return catid;
    }

    public void setCatid(int catid) {
        this.catid = catid;
    }

    public int getSurid() {
        return surid;
    }

    public void setSurid(int surid) {
        this.surid = surid;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Genealogy{" +
                "aid=" + aid +
                ", catid=" + catid +
                ", surid=" + surid +
                ", body='" + body + '\'' +
                '}';
    }
}
