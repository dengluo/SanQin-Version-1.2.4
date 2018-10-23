package com.pbids.sanqin.model.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:41
 * @desscribe 类描述:我的族谱json实体类
 * @remark 备注:
 * @see
 */
public class GenealogyInformation implements Serializable{

    //族谱是否已建设 0=未建设, 1=已建设,为1时再读取info信息
    private int genealogy;
    //族谱信息
    private ArrayList<Catlog> genealogy_list;
    //详情
    private GenealogyInfo info;
    //排行信息
    private ArrayList<GenealogyRank> rank;

    public int getGenealogy() {
        return genealogy;
    }

    public void setGenealogy(int genealogy) {
        this.genealogy = genealogy;
    }

    public ArrayList<Catlog> getGenealogy_list() {
        return genealogy_list;
    }

    public void setGenealogy_list(ArrayList<Catlog> genealogy_list) {
        this.genealogy_list = genealogy_list;
    }

    public GenealogyInfo getInfo() {
        return info;
    }

    public void setInfo(GenealogyInfo info) {
        this.info = info;
    }

    public ArrayList<GenealogyRank> getRank() {
        return rank;
    }

    public void setRank(ArrayList<GenealogyRank> rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "GenealogyInformation{" +
                "genealogy=" + genealogy +
                ", genealogy_list=" + genealogy_list +
                ", info=" + info +
                ", rank=" + rank +
                '}';
    }
}
