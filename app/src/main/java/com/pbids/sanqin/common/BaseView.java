package com.pbids.sanqin.common;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:34
 * @desscribe 类描述:view层回掉接口
 * @remark 备注
 * @see
 */

public interface BaseView<T> extends AbstractBaseView {

    public void onHttpSuccess(String type);

    public void onHttpError(String type);

}