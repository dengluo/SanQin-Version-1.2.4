package com.pbids.sanqin.utils.eventbus;

/**
 * @Author:上官名鹏
 * @Description:  加载地图数据
 * @Date:Create in 2018/4/24 11:29
 * Modified By:
 */

public class OnLoadMapDistributionDataEvent {

    private boolean loadComplete ;

    public OnLoadMapDistributionDataEvent(boolean load){
        this.loadComplete = load;
    }

    public boolean isLoadComplete() {
        return loadComplete;
    }

    public void setLoadComplete(boolean loadComplete) {
        this.loadComplete = loadComplete;
    }
}
