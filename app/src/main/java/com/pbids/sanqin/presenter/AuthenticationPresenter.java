package com.pbids.sanqin.presenter;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.pbids.sanqin.common.BaseFragment;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.model.db.UserInfoManager;
import com.pbids.sanqin.model.entity.FamilyRank;
import com.pbids.sanqin.model.entity.UserInfo;
import com.pbids.sanqin.ui.activity.AuthenticationView;
import com.pbids.sanqin.utils.OkGoUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:46
 * @desscribe 类描述:
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.AuthenticationFragment
 */
public class AuthenticationPresenter extends BasePresenter{
    AuthenticationView authenticationView;
    public AuthenticationPresenter(AuthenticationView authenticationView){
        this.authenticationView = authenticationView;
    }

    public DisposableObserver<Response<String>> submitInformation(final String url, final HttpParams params, final String type){
        final DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(@NonNull Response<String> stringResponse) {
                try {
                    JSONObject jsonObject = new JSONObject(stringResponse.body());
                    int status = jsonObject.getInt("status");
                    if("1".equals(type)){
                        if(status== MyApplication.OK){
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            jsonArray.length();
                            ArrayList<FamilyRank> familyRanks = new ArrayList<FamilyRank>();
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            for(int i = 0;i<jsonArray.length();i++){
                                FamilyRank familyRank = gsonBuilder.create().fromJson(jsonArray.get(i).toString(), FamilyRank.class);
                                familyRanks.add(familyRank);
//                            Log.i("wzh","familyRank: "+familyRank.toString());
                            }
                            authenticationView.getFamilyRanks(familyRanks);
                            authenticationView.onHttpSuccess(type);
                        }else {
                            authenticationView.onHttpError(jsonObject.getString("message"));
                        }
                    }else if("2".equals(type)){
                        if(status== MyApplication.OK){
                            Log.i("wzh","type1: "+type);
                            JSONObject data = jsonObject.getJSONObject("data");
                            String nativePlace = data.getString("nativePlace");
                            String surname = data.getString("surname");
                            int surnameId = data.getInt("surnameId");
                            int surnameStatus = data.getInt("surnameStatus");
                            UserInfo userInfo= MyApplication.getUserInfo();
                            userInfo.setNativePlace(nativePlace);
                            userInfo.setSurname(surname);
                            userInfo.setSurnameId(surnameId);
                            userInfo.setSurnameStatus(surnameStatus);
                            MyApplication.setUserInfo(userInfo);
                            UserInfoManager.updateUserInfo(((BaseFragment)authenticationView).getActivity(),userInfo);
                            authenticationView.onHttpSuccess(type);
                            Log.i("wzh","type2: "+type);
                        }else if(status== MyApplication.ERROR){
                            authenticationView.onHttpError(jsonObject.getString("message"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                authenticationView.onHttpError(Const.REQUEST_ERROR_NETWOR_DISCONNECT_MESSAGE);
            }

            @Override
            public void onComplete() {

            }
        };

        OkGoUtil.getStringObservableForPost(url,params,observer);
        return observer;
    }
}
