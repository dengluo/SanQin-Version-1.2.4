package com.pbids.sanqin.base;

/**
 * @author caiguoliang
 * @date on 2018/4/2
 * @desscribe 类描述:view层回掉接口
 * @remark 备注
 * @see
 */

public interface BaaseView {

/*    //http 请求成功返回
    void onHttpSuccess(int resultCode, int requestCode, HttpJsonResponse rescb);

    //http 请求失败返回
    void onHttpError(int resultCode, int requestCode, int errorCode, String errorMessage);*/

    //显示提示信息
    void showToast(String message);

    // open web page
    void openWebFragment(String url);
    
}
