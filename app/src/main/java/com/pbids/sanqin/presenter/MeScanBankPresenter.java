package com.pbids.sanqin.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.pbids.sanqin.common.BaseFragment;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.model.entity.Accounts;
import com.pbids.sanqin.model.entity.Bank;
import com.pbids.sanqin.model.entity.UserInfo;
import com.pbids.sanqin.ui.activity.me.MeScanBankView;
import com.pbids.sanqin.utils.OkGoUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;


/**
 * @author 巫哲豪
 * @date on 2018/3/2 14:04
 * @desscribe 类描述:我的界面-绑定银行卡
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.me.MeScanBankFragment
 */
public class MeScanBankPresenter extends BasePresenter{
    MeScanBankView meNewAuthenticationView;
    public MeScanBankPresenter(MeScanBankView meNewAuthenticationView){
        this.meNewAuthenticationView = meNewAuthenticationView;
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
                JsonObject data = jsonObject.getAsJsonObject("data");
                if(status == MyApplication.OK){
                    if("1".equals(type)){
                        JsonObject bindCard = data.getAsJsonObject("bindCard");
                        Bank bank = new GsonBuilder().create().fromJson(bindCard.toString(),Bank.class);
//                        int isBindCard = data.get("isBindCard").getAsInt();
                        meNewAuthenticationView.getBank(bank);
                    }else if("2".equals(type)){
                        int isBindCard = data.get("isBindCard").getAsInt();
                        meNewAuthenticationView.getIsBindCard(isBindCard);
                    }
                    meNewAuthenticationView.onHttpSuccess(type);
                }else{
                    meNewAuthenticationView.onHttpError(jsonObject.get("message").getAsString());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                meNewAuthenticationView.onHttpError(Const.REQUEST_ERROR_NETWOR_DISCONNECT_MESSAGE);
            }

            @Override
            public void onComplete() {

            }
        };
        OkGoUtil.getStringObservableForPost(url,httpParams,observer);
        return observer;
    }
}
