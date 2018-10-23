package com.pbids.sanqin.model.entity;

/**
 * @author : 上官名鹏
 *         Description : pickBrick
 *         Date :Create in 2018/6/13 11:25
 *         Modified By :
 */

public class PickBrick {
    /**
     * code : REWARD-ZC-15
     * endTime : 1528905600000
     * id : 10
     * name : 登陆奖励砖头
     * remarks : 在某个时间段内登陆即可获得砖块
     * sort : 1
     * startTime : 1528819200000
     * state : 1
     * unit : 块
     * value : 15
     */

    private int state;
    private int value;
    private String isRecived;


    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getIsRecived() {
        return isRecived;
    }

    public void setIsRecived(String isRecived) {
        this.isRecived = isRecived;
    }
}
