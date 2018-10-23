package com.pbids.sanqin.presenter;

import com.google.gson.GsonBuilder;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.model.entity.NewsArticle;
import com.pbids.sanqin.ui.activity.me.MeFamilyManageView;
import com.pbids.sanqin.utils.OkGoUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.observers.DisposableObserver;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:58
 * @desscribe 类描述:我的界面-家族管理
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.me.MeFamilyManageFragment
 */
public class MeFamilyManagePresenter extends BasePresenter{
    MeFamilyManageView meFamilyManageView;
    public MeFamilyManagePresenter(MeFamilyManageView meFamilyManageView){
        this.meFamilyManageView = meFamilyManageView;
    }

    public DisposableObserver<Response<String>> submitInformation(String url, HttpParams params, final String type){
        DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(@android.support.annotation.NonNull Response<String> stringResponse) {
                try {
                    JSONObject jsonObject = new JSONObject(stringResponse.body());
                    int status = jsonObject.getInt("status");
                    if(status== MyApplication.OK){
                        JSONObject data = jsonObject.getJSONObject("data");
                        int peopleMonthlyGrow = data.getInt("peopleMonthlyGrow");
                        int peopleNum = data.getInt("peopleNum");
                        int wealth = data.getInt("wealth");
                        int wealthMonthlyGrow = data.getInt("wealthMonthlyGrow");
                        String surname = data.getString("surname");
                        meFamilyManageView.getFamilyManageInformation(peopleMonthlyGrow,peopleNum,wealth,wealthMonthlyGrow,surname);
                        meFamilyManageView.onHttpSuccess(type);
                    }else {
                        meFamilyManageView.onHttpError(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    meFamilyManageView.onHttpError(Const.JSONERROR);
                }
            }

            @Override
            public void onError(@android.support.annotation.NonNull Throwable e) {
                meFamilyManageView.onHttpError(Const.REQUEST_ERROR_NETWOR_DISCONNECT_MESSAGE);
            }

            @Override
            public void onComplete() {

            }
        };

        OkGoUtil.getStringObservableForPost(url,params,observer);
        return observer;
    }
}
