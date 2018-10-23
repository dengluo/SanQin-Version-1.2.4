package com.pbids.sanqin.utils;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.convert.Converter;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okrx2.adapter.ObservableResponse;

import java.util.logging.Level;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 旧的
 */

public class OkGoUtils {
        public static void getString(){
            OkGo.<String>get("http://www.kuaidi100.com/query?type=快递公司代号&postid=快递单号")
                    .converter(new Converter<String>() {
                            @Override
                            public String convertResponse(Response response) throws Throwable {
                                    return null;
                            }
                    }).adapt();
//            new ObservableResponse<String>();
            Observable observable;
            OkGo.<String>get("").converter(new Converter<String>() {
                @Override
                public String convertResponse(Response response) throws Throwable {
                    return null;
                }
            }).adapt(new ObservableResponse<String>())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(new Observer<com.lzy.okgo.model.Response<String>>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {

                }

                @Override
                public void onNext(@NonNull com.lzy.okgo.model.Response<String> stringResponse) {

                }

                @Override
                public void onError(@NonNull Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });
        }
}
