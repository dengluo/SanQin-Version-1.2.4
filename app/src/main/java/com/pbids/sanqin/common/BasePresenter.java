package com.pbids.sanqin.common;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:34
 * @desscribe 类描述:所有P层的父类
 * @remark 备注
 * @see
 */
public abstract class BasePresenter{
    protected Reference viewRef;

    public void attachView(AbstractBaseView baseView){
        viewRef=new WeakReference(baseView);
    }

    public void detachView(){
        if(viewRef!=null){
            viewRef.clear();
            viewRef = null;
        }
    }

}
