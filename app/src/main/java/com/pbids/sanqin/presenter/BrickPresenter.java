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
import com.pbids.sanqin.ui.activity.zongquan.BrickView;
import com.pbids.sanqin.utils.OkGoUtil;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:46
 * @desscribe 类描述:
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.zongquan.BrickFragment
 */
public class BrickPresenter extends BasePresenter{
    BrickView brickView;
    public BrickPresenter(BrickView brickView){
        this.brickView = brickView;
    }

    public DisposableObserver<Response<String>> submitInformation(String url, HttpParams httpParams, final String type){
        DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>(){
            @Override
            public void onNext(@NonNull Response<String> stringResponse){
                //Log.i("wzh","stringResponse: "+stringResponse.body());
                JsonObject jsonObject = new JsonParser().parse(stringResponse.body()).getAsJsonObject();
                int status = jsonObject.get("status").getAsInt();
                if(status == MyApplication.OK){
                    JsonObject data = jsonObject.get("data").getAsJsonObject();
                    if("1".equals(type)){
                        UserInfo userInfo = new GsonBuilder().create().fromJson(data.toString(), UserInfo.class);
                        brickView.getUserInfo(userInfo);
                    }else if("2".equals(type)){
                        JsonObject userInfoJsonObject = data.get("userInfo").getAsJsonObject();
                        UserInfo userInfo = new GsonBuilder().create().fromJson(userInfoJsonObject.toString(), UserInfo.class);
                        int laveBurningBrickCount = data.get("laveBurningBrickCount").getAsInt();
                        brickView.getUserInfoForBrick(userInfo,laveBurningBrickCount);
                    }
                    brickView.onHttpSuccess(type);
                }else{
                    brickView.onHttpError(jsonObject.get("message").getAsString());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                brickView.onHttpError(Const.REQUEST_ERROR_NETWOR_DISCONNECT_MESSAGE);
            }

            @Override
            public void onComplete() {

            }
        };
        OkGoUtil.getStringObservableForPost(url,httpParams,observer);
        return observer;
    }
}
