package com.pbids.sanqin.utils;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.lzy.okrx2.adapter.ObservableResponse;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.model.entity.UserInfo;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by pbids903 on 2017/12/27.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:34
 * @desscribe 类描述:OKGO工具
 * @remark 备注:
 * @see
 */
public class OkGoUtil {
    public static void getStringObservableForPost(String url, HttpParams httpParams, DisposableObserver disposableObserver){
        Observable<Response<String>> observable;
        if(MyApplication.getUserInfo()!=null
                && MyApplication.getUserInfo().getToken()!=null
                && !"".equals(MyApplication.getUserInfo().getToken())){
            observable = OkGo.<String>post(url)
                    .headers("token",MyApplication.getUserInfo().getToken())
                    .params(httpParams)
                    .converter(new StringConvert())
                    .adapt(new ObservableResponse<String>());
        }else{
            observable = OkGo.<String>post(url)
                    .params(httpParams)
                    .converter(new StringConvert())
                    .adapt(new ObservableResponse<String>());
        }
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(disposableObserver);
    }

    public static void getStringObservableForGet(String url, HttpParams httpParams, DisposableObserver disposableObserver){
        Observable<Response<String>> observable;
        UserInfo userInfo = MyApplication.getUserInfo();
        if(MyApplication.getUserInfo()!=null
                && MyApplication.getUserInfo().getToken()!=null
                && !"".equals(MyApplication.getUserInfo().getToken())){
            observable = OkGo.<String>get(url)
                    .headers("token",MyApplication.getUserInfo().getToken())
                    .params(httpParams)
                    .converter(new StringConvert())
                    .adapt(new ObservableResponse<String>());
        }else{
            observable = OkGo.<String>get(url)
                    .params(httpParams)
                    .converter(new StringConvert())
                    .adapt(new ObservableResponse<String>());
        }
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(disposableObserver);
    }

//    public static void ss(){
//        DisposableObserver<>
//    }

}
