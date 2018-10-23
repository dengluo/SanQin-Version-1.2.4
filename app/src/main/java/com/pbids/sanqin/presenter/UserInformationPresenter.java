package com.pbids.sanqin.presenter;

import android.content.Context;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.lzy.okrx2.adapter.ObservableResponse;
import com.pbids.sanqin.common.BaseFragment;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.model.db.UserInfoManager;
import com.pbids.sanqin.ui.activity.me.MePageView;
import com.pbids.sanqin.ui.view.MeLoadingPop;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static com.pbids.sanqin.utils.AddrConst.ADDRESS_ME_UPDATE_USERINFORMATION;
import static com.pbids.sanqin.utils.AddrConst.SERVER_ADDRESS_USER;

/**
 * Created by pbids903 on 2017/12/11.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 14:09
 * @desscribe 类描述:我的界面-个人信息
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.me.MeInformationAutograhtFragment,com.pbids.sanqin.ui.activity.me.MeInformationFragment,com.pbids.sanqin.ui.activity.me.MeReviseNameFragment
 */
public class UserInformationPresenter extends BasePresenter{
    MePageView mePageView;
    public UserInformationPresenter(MePageView view){
        mePageView = view;
    }

    public DisposableObserver reviseUserInformation(HttpParams httpParams, final String type){
        final Context context = ((BaseFragment)mePageView).getActivity();
        if(context==null){
            return null;
        }
        final MeLoadingPop[] meLoadingPops = new MeLoadingPop[1];
        meLoadingPops[0] = new MeLoadingPop(context,"正在保存");
//        meLoadingPops[0].setBgColor(context.getResources().getColor(R.color.transparent));
        meLoadingPops[0].setIsAnimation(false);
        meLoadingPops[0].setCancelable(false);
        meLoadingPops[0].show();

        DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(@NonNull Response<String> stringResponse) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(stringResponse.body());
                    Log.i("wzh","stringResponse.body() :"+stringResponse.body());
                    int status = jsonObject.getInt("status");
                    if(status == MyApplication.OK){
                        JSONObject data = jsonObject.getJSONObject("data");
                        Log.i("wzh","jsonObject.getJSONObject(\"data\"): "+jsonObject.getJSONObject("data").toString());
                        Log.i("wzh","data :"+data.toString());
                        switch (type){
                            case MePageView.ME_REVICE_NAME:
                                MyApplication.getUserInfo().setName(data.getString("name"));
                                break;
                            case MePageView.ME_REVICE_PICTURE:
                                MyApplication.getUserInfo().setFaceUrl(data.getString("faceUrl"));
                                break;
                            case MePageView.ME_REVICE_AUTOGRAHT:
                                MyApplication.getUserInfo().setSignature(data.getString("signature"));
                                break;
                            case MePageView.ME_REVICE_SEX:
                                MyApplication.getUserInfo().setSex(data.getString("sex"));
                                break;
                            case MePageView.ME_REVICE_LOCATION:
                                MyApplication.getUserInfo().setLocation(data.getString("location"));
//                            MyApplication.getUserInfo().setLocationId(data.getString("locationId"));
                                break;
                            case MePageView.ME_REVICE_PHONE:
                                MyApplication.getUserInfo().setPhone(data.getString("phone"));
//                            MyApplication.getUserInfo().setLocationId(data.getString("locationId"));
                                break;
                            case MePageView.ME_REVICE_NATIVEPLACE:
                                MyApplication.getUserInfo().setNativePlace(data.getString("nativePlace"));
//                            MyApplication.getUserInfo().setLocationId(data.getString("locationId"));
                                break;
                        }
                        mePageView.reviseSuccess(type);
                        UserInfoManager.updateUserInfo(context,MyApplication.getUserInfo());
                    }else{
                        Log.i("wzh",""+jsonObject.getString("message"));
                        mePageView.reviseError(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                meLoadingPops[0].dismiss();
                mePageView.reviseError(Const.REQUEST_ERROR_NETWOR_DISCONNECT_MESSAGE);
            }

            @Override
            public void onComplete() {
                meLoadingPops[0].dismiss();
//                mePageView.reviseSuccess(type);
            }
        };

        OkGo.<String>post(SERVER_ADDRESS_USER + ADDRESS_ME_UPDATE_USERINFORMATION)
                .headers("token", MyApplication.getUserInfo().getToken())
                .params(httpParams)
                .converter(new StringConvert())
                .adapt(new ObservableResponse<String>())
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Response<String>>() {
                    @Override
                    public void accept(@NonNull Response<String> stringResponse) throws Exception {
//                        meLoadingPop.show();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<Response<String>>() {
            @Override
            public void accept(@NonNull Response<String> stringResponse) throws Exception {

            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
        return observer;
    }
}
