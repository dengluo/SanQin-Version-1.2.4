package com.pbids.sanqin.presenter;

import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.pbids.sanqin.common.BaseFragment;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.model.db.UserInfoManager;
import com.pbids.sanqin.ui.activity.me.MeBindingVerfyCodeView;
import com.pbids.sanqin.utils.OkGoUtil;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

import static com.pbids.sanqin.ui.activity.me.MeBindingVerfyCodeFragment.BINDING_PHONE_FIRST;
import static com.pbids.sanqin.ui.activity.me.MeBindingVerfyCodeFragment.BINDING_PHONE_NEW;
import static com.pbids.sanqin.ui.activity.me.MeBindingVerfyCodeFragment.BINDING_PHONE_NEW_REQUEST;
import static com.pbids.sanqin.ui.activity.me.MeBindingVerfyCodeFragment.VERIFICATION_PHONE_NUMBER;
import static com.pbids.sanqin.ui.activity.me.MeBindingVerfyCodeFragment.VERIFICATION_PHONE_NUMBER_REQUEST;
import static com.pbids.sanqin.ui.activity.me.MeBindingVerfyCodeFragment.WITHDRAWLS;

/**
 * Created by pbids903 on 2017/12/29.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:54
 * @desscribe 类描述:我的界面，验证码输入
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.me.MeBindingVerfyCodeFragment
 */
public class MeBindingVerfyCodePresenter extends BasePresenter{
    MeBindingVerfyCodeView meBindingVerfyCodeView;
    public MeBindingVerfyCodePresenter(MeBindingVerfyCodeView meBindingVerfyCodeView){
        this.meBindingVerfyCodeView = meBindingVerfyCodeView;
    }

    public DisposableObserver<Response<String>> submitInformation(final String url, final HttpParams httpParams, final String type){
        DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(@NonNull Response<String> stringResponse) {
                Log.i("wzh","stringResponse.body(): "+stringResponse.body());
                JsonObject jsonObject = new JsonParser().parse(stringResponse.body()).getAsJsonObject();
                int status = jsonObject.get("status").getAsInt();
                if(status == MyApplication.OK){
                    if(type.equals(BINDING_PHONE_FIRST)){
                        JsonObject data = jsonObject.get("data").getAsJsonObject();
                        String SMSCode = data.get("SMSCode").getAsString();
//                        String code = jsonObject.get("data").getAsString();
                        meBindingVerfyCodeView.returnVerifyCode(SMSCode);
                    }else if(type.equals(WITHDRAWLS)) {
                        JsonObject data = jsonObject.get("data").getAsJsonObject();
                        String SMSCode = data.get("SMSCode").getAsString();
                        meBindingVerfyCodeView.returnVerifyCode(SMSCode);
                    }else if(type.equals("1")){
                        JsonObject data = jsonObject.get("data").getAsJsonObject();
                        String phone = data.get("phone").getAsString();
                        MyApplication.getUserInfo().setPhone(phone);
                        UserInfoManager.updateUserInfo(((BaseFragment)meBindingVerfyCodeView).getActivity(),MyApplication.getUserInfo());
                    }else if(type.equals("3")){
                        JsonObject data = jsonObject.get("data").getAsJsonObject();
                        float balance = data.get("accountBalance").getAsFloat();
                        MyApplication.getUserInfo().setAccountBalance(balance);
                        UserInfoManager.updateUserInfo(((BaseFragment)meBindingVerfyCodeView).getActivity(),MyApplication.getUserInfo());
                    }else if(type.equals(BINDING_PHONE_NEW)){
                        JsonObject data = jsonObject.get("data").getAsJsonObject();
                        String phone = data.get("phone").getAsString();
                        String SMSCode = data.get("SMSCode").getAsString();
                        MyApplication.getUserInfo().setPhone(phone);
                        UserInfoManager.updateUserInfo(((BaseFragment)meBindingVerfyCodeView).getActivity(),MyApplication.getUserInfo());
                        meBindingVerfyCodeView.returnVerifyCode(SMSCode);
                    }else if(type.equals(VERIFICATION_PHONE_NUMBER)){
                        JsonObject data = jsonObject.get("data").getAsJsonObject();
                        String SMSCode = data.get("SMSCode").getAsString();
                        meBindingVerfyCodeView.returnVerifyCode(SMSCode);
                    }else if(type.equals(VERIFICATION_PHONE_NUMBER_REQUEST)){

                    }else if(type.equals(BINDING_PHONE_NEW_REQUEST)){

                    }
                    meBindingVerfyCodeView.onHttpSuccess(type);
                }else{
                    meBindingVerfyCodeView.onHttpError(jsonObject.get("message").getAsString());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                meBindingVerfyCodeView.onHttpError(Const.REQUEST_ERROR_NETWOR_DISCONNECT_MESSAGE);
            }

            @Override
            public void onComplete() {

            }
        };

        OkGoUtil.getStringObservableForPost(url,httpParams,observer);
        return observer;
    }
}
