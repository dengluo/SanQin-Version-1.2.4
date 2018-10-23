package com.pbids.sanqin.utils.eventbus;


import com.pbids.sanqin.model.entity.Catlog;

/**
 * 点击族谱目录
 */

public class OnClickGenealogCatalogEvent {

    private int position;

    private Catlog catlog;


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Catlog getCatlog() {
        return catlog;
    }

    public void setCatlog(Catlog catlog) {
        this.catlog = catlog;
    }
}
