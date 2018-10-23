package com.pbids.sanqin.presenter;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.model.entity.Accounts;
import com.pbids.sanqin.model.entity.AccountsGroup;
import com.pbids.sanqin.model.entity.Bank;
import com.pbids.sanqin.ui.activity.me.MeBindingBankView;
import com.pbids.sanqin.utils.OkGoUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:53
 * @desscribe 类描述:绑定银行卡
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.me.MeBindingBankFragment
 */
public class MeBindingBankPresenter extends BasePresenter{
    MeBindingBankView meBindingBankView;
    public MeBindingBankPresenter(MeBindingBankView meBindingBankView){
        this.meBindingBankView = meBindingBankView;
    }

    public DisposableObserver<Response<String>> submitInformation(String url, HttpParams httpParams, final String type){
        DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(@NonNull Response<String> stringResponse) {
                Log.i("wzh","stringResponse: "+stringResponse.body());
                JsonObject jsonObject = new JsonParser().parse(stringResponse.body()).getAsJsonObject();
                int status = jsonObject.get("status").getAsInt();
                if(status == MyApplication.OK){
                    if("1".equals(type)){
                        List<AccountsGroup> accountsGroups = new ArrayList<>();
                        JsonArray data = jsonObject.get("data").getAsJsonArray();
                        List<Bank> banks = new ArrayList<>();
                        for(int i=0;i<data.size();i++){
                            Bank bank = new GsonBuilder().create().fromJson(data.get(i).toString(),Bank.class);
                            banks.add(bank);
                        }
                        meBindingBankView.getBanks(banks);
                    }else if("2".equals(type)){

                    }
                    meBindingBankView.onHttpSuccess(type);
                }else{
                    meBindingBankView.onHttpError(jsonObject.get("message").getAsString());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                meBindingBankView.onHttpError(Const.REQUEST_ERROR_NETWOR_DISCONNECT_MESSAGE);
            }

            @Override
            public void onComplete() {

            }
        };
        OkGoUtil.getStringObservableForPost(url,httpParams,observer);
        return observer;
    }

    public DisposableObserver<Response<String>> submitInformationForUnbundling(String url, HttpParams httpParams, final String type){
        DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(@NonNull Response<String> stringResponse) {
                Log.i("wzh","stringResponse: "+stringResponse.body());
                JsonObject jsonObject = new JsonParser().parse(stringResponse.body()).getAsJsonObject();
                int status = jsonObject.get("status").getAsInt();
                if(status == MyApplication.OK){
                    if("1".equals(type)){
                        List<AccountsGroup> accountsGroups = new ArrayList<>();
                        JsonArray data = jsonObject.get("data").getAsJsonArray();
                        List<Bank> banks = new ArrayList<>();
                        for(int i=0;i<data.size();i++){
                            Bank bank = new GsonBuilder().create().fromJson(data.get(i).toString(),Bank.class);
                            banks.add(bank);
                        }
                        meBindingBankView.getBanks(banks);
                    }else if("2".equals(type)){
                    }
                    meBindingBankView.onHttpSuccess(type);
                }else{
                    meBindingBankView.onHttpError(jsonObject.get("message").getAsString());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                meBindingBankView.onHttpError(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        };
//        OkGoUtil.getStringObservableForGet(url,httpParams,observer);
        OkGoUtil.getStringObservableForPost(url,httpParams,observer);
        return observer;
    }
}
