package com.pbids.sanqin.model.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * group
 * 模块：我的 -> 我的礼券  ->  数据分组
 * Created by caiguoliang 2018-3-2
 */

public class GiftGroup {

    private String header; //分组头部文字

    private List<Gift> gifts = new ArrayList<Gift>(); //分组列表数据

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public List<Gift> getGifts() {
        return gifts;
    }

/*    public void setGifts(List<Gift> gifts) {
        this.gifts = gifts;
    }*/

    public void addGift(Gift gift){
        gifts.add(gift);
    }
}
