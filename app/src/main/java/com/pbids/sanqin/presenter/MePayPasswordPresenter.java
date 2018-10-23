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
import com.pbids.sanqin.model.entity.Bank;
import com.pbids.sanqin.ui.activity.me.MePayPasswordView;
import com.pbids.sanqin.utils.OkGoUtil;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 14:03
 * @desscribe 类描述:我的界面-密码设置
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.me.MePayPasswordSetFragment,com.pbids.sanqin.ui.activity.me.MePayPasswordReviseFirstFragment
 */
public class MePayPasswordPresenter extends BasePresenter{
    MePayPasswordView mePayPasswordView;
    public MePayPasswordPresenter(MePayPasswordView mePayPasswordView){
        this.mePayPasswordView = mePayPasswordView;
    }

    public DisposableObserver<Response<String>> submitInfotmation(String url, HttpParams httpParams, final String type){
//        final Context context = ((BaseFragment)meNewAuthenticationView).getActivity();
//        if(context==null){
//            return null;
//        }
        DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(@NonNull Response<String> stringResponse) {
                Log.i("wzh","stringResponse.body(): "+stringResponse.body());
                JsonObject jsonObject = new JsonParser().parse(stringResponse.body()).getAsJsonObject();
                int status = jsonObject.get("status").getAsInt();
                if(status == MyApplication.OK){
                    if("1".equals(type)){
                        JsonObject data = jsonObject.getAsJsonObject("data");
                        int isSetPayword = data.get("isSetPayword").getAsInt();
                        mePayPasswordView.onIsSetPayword(isSetPayword);
                    }else if("2".equals(type)){

                    }
                    mePayPasswordView.onHttpSuccess(type);
                }else{
                    mePayPasswordView.onHttpError(jsonObject.get("message").getAsString());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                mePayPasswordView.onHttpError(Const.REQUEST_ERROR_NETWOR_DISCONNECT_MESSAGE);
            }

            @Override
            public void onComplete() {

            }
        };
        OkGoUtil.getStringObservableForPost(url,httpParams,observer);
        return observer;
    }
}
