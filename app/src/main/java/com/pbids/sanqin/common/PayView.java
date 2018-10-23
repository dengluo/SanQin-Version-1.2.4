package com.pbids.sanqin.common;

import com.pbids.sanqin.model.entity.WechatPayInfo;

import java.util.Map;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:37
 * @desscribe 类描述:公用的支付view层接口
 * @remark 备注:
 * @see
 */

public interface PayView extends BaseView{
    void getPayInfo(String info);
    void getPayInfoForBalance(String info);
    void getPayInfoForBalanceError(String info);
    void getPayInfoForWechat(WechatPayInfo wechatPayInfo);
}
