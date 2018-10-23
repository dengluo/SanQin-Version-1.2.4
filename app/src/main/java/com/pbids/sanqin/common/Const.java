package com.pbids.sanqin.common;

public class Const {

    //http 请求结果
    public static final  int  OK = 1;
    public static final  int  LOST = 0;

    public static final int REQUEST_ERROR_JSON_DATA = -4 ;

    //发送 http 请求成功
    public static final int REQUEST_SUCCESS = 200;
    //发送 http 请求失败 -- 无网络
    public static final int  REQUEST_ERROR_NETWOR_DISCONNECT = 400;
    public static final String  REQUEST_ERROR_NETWOR_DISCONNECT_MESSAGE = "网络未连接，请检查您的网络！";
    public static final String  JSONERROR = "数据异常";

    //支付用途
    public static final String PURPOSE_REWARD  = "打赏";
    public static final String PURPOSE_SUPER_EXPRESSION  = "超级表情";
    public static final String PURPOSE_ACTIVE_PAYMENT  = "活动支付";

    //订单类型 1打赏 2活动支付 3 超级表情
    public static final int ORDER_TYPE_REWARD  = 1;
    public static final int ORDER_TYPE_ACTIVE_PAYMENT  = 2;
    public static final int ORDER_TYPE_SUPER_EXPRESSION  = 3;

    public static final String PAY_CODE_UNION  = "unspay";
    public static final String PAY_CODE_UNION_TEST  = "unspay_test";



}
