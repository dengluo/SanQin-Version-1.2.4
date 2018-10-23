package com.pbids.sanqin.ui.activity;

import com.pbids.sanqin.common.BaseView;

/**
 * Created by pbids903 on 2017/11/9.
 */

public interface LoginPageView extends BaseView{
    public static final String LONGIN_ERROR_PHONE_NUMBER = "0";
    public static final String LONGIN_ERROR_TEST_CODE = "1";
    public static final String LONGIN_EMPTY_PHONE_NUMBER = "2";
    public static final String LONGIN_EMPTY_TEST_CODE = "3";
    public static final String LONGIN_EMPTY_PICTURE_CODE = "4";
    public static final String LONGIN_ERROR_PICTURE_CODE = "5";
    public static final String LONGIN_ERROR_HTTP_CODE = "6";
    public static final String NO_NEED_PICTURE_CODE ="no_need_picture_code";
    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";


//    public void phoneNumberTest(String phoneNumber);
    public void returnVerifyCode(String testCode);
//    public void localLogin(String phoneNumber,String testCode,String pictureCode);
//    public void anothreLogin(int terraceCode);
//    public void loginErrow(String errorCode);
//    public void loginSuccess();

    public void showToastInfo( String mess );

    void bindPhone();

    void toastStauts(String message);
}
