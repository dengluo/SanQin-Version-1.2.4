package com.pbids.sanqin.presenter;

import android.content.Context;
import android.util.Log;

import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.pbids.sanqin.common.BaseFragment;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.ui.activity.me.MeCooperationView;
import com.pbids.sanqin.utils.OkGoUtil;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;


/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:57
 * @desscribe 类描述:我的界面，关于，商务合作
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.me.MeCooperationFragment
 */
public class MeCooperationPresenter extends BasePresenter{
    MeCooperationView meCooperationView;
    public MeCooperationPresenter(MeCooperationView meCooperationView){
        this.meCooperationView = meCooperationView;
    }

    public DisposableObserver<Response<String>> submitInformation(String url, HttpParams params){
        final Context context = ((BaseFragment)meCooperationView).getActivity();
        if(context==null){
            return null;
        }
        DisposableObserver<Response<String>> disposableObserver = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(@NonNull Response<String> stringResponse) {
                Log.i("wzh","stringResponse.body(): "+stringResponse.body());
                try {
                    JSONObject jsonObject = new JSONObject(stringResponse.body());
                    int status = jsonObject.getInt("status");
                    if(status == MyApplication.OK){
                        meCooperationView.onHttpSuccess("");
                    }else{
                        meCooperationView.onHttpError(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.i("wzh","onError(): ");
                meCooperationView.onHttpError(Const.REQUEST_ERROR_NETWOR_DISCONNECT_MESSAGE);
            }

            @Override
            public void onComplete() {
                Log.i("wzh","onComplete(): ");
//                meCooperationView.onHttpSuccess("");
            }
        };
        OkGoUtil.getStringObservableForPost(url,params,disposableObserver);
        return disposableObserver;
    }
}
