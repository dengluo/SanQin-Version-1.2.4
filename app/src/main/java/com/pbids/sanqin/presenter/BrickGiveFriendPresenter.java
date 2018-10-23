package com.pbids.sanqin.presenter;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.model.entity.UserInfo;
import com.pbids.sanqin.ui.activity.zongquan.BrickGiveFriendView;
import com.pbids.sanqin.utils.OkGoUtil;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by pbids903 on 2018/3/24.
 */

public class BrickGiveFriendPresenter extends BasePresenter{
    BrickGiveFriendView brickGiveFriendView;
    public BrickGiveFriendPresenter(BrickGiveFriendView brickGiveFriendView){
        this.brickGiveFriendView = brickGiveFriendView;
    }

    public DisposableObserver<Response<String>> submitInformation(String url, HttpParams httpParams, final String type){
        DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>(){
            @Override
            public void onNext(@NonNull Response<String> stringResponse){
                Log.i("wzh","stringResponse: "+stringResponse.body());
                JsonObject jsonObject = new JsonParser().parse(stringResponse.body()).getAsJsonObject();
                int status = jsonObject.get("status").getAsInt();
                if(status == MyApplication.OK){
                    JsonObject data = jsonObject.get("data").getAsJsonObject();
                    UserInfo userInfo = new GsonBuilder().create().fromJson(data.toString(),UserInfo.class);
                    brickGiveFriendView.getUserInfo(userInfo);
//                    data.toString()
                    brickGiveFriendView.onHttpSuccess(type);
                }else{
                    brickGiveFriendView.onHttpError(jsonObject.get("message").getAsString());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                brickGiveFriendView.onHttpError(Const.REQUEST_ERROR_NETWOR_DISCONNECT_MESSAGE);
            }

            @Override
            public void onComplete() {

            }
        };
        OkGoUtil.getStringObservableForPost(url,httpParams,observer);
        return observer;
    }
}
