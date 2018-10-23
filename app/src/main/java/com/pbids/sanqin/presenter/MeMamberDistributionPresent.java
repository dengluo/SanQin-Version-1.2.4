package com.pbids.sanqin.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.model.entity.Gift;
import com.pbids.sanqin.model.entity.Province;
import com.pbids.sanqin.ui.activity.me.MeMamberDistributionView;
import com.pbids.sanqin.utils.OkGoUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 12:01
 * @desscribe 类描述:我的界面-家族管理-人员分布
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.me.MeMamberDistributionFragment
 */
public class MeMamberDistributionPresent extends BasePresenter{
    MeMamberDistributionView meMamberDistributionView;
    public MeMamberDistributionPresent(MeMamberDistributionView meMamberDistributionView){
        this.meMamberDistributionView = meMamberDistributionView;
    }

    public DisposableObserver<Response<String>> submitInformation(final String url, final HttpParams params, final String type){
        final DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(@NonNull Response<String> stringResponse) {
                Log.i("wzh","stringResponse: "+stringResponse.body());
                try {
                    JSONObject jsonObject = new JSONObject(stringResponse.body());
                    int status = jsonObject.getInt("status");
                    if(status== MyApplication.OK){
                        Gson gson = new GsonBuilder().create();
                        JSONArray data = jsonObject.getJSONArray("data");
                        List<Province> provinces = new ArrayList<>();
                        for(int i=0;i<data.length();i++){
                            Province province = gson.fromJson(data.get(i).toString(), Province.class);
                            provinces.add(province);
                        }
                        meMamberDistributionView.onHttpSuccess(type);
                        meMamberDistributionView.getProvinceList(provinces);
                    }else {
                        meMamberDistributionView.onHttpError(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    meMamberDistributionView.onHttpError(e.getMessage());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                meMamberDistributionView.onHttpError(Const.REQUEST_ERROR_NETWOR_DISCONNECT_MESSAGE);
            }

            @Override
            public void onComplete() {

            }
        };

        OkGoUtil.getStringObservableForPost(url,params,observer);
        return observer;
    }
}
