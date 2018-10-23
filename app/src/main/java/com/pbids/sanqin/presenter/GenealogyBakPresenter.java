package com.pbids.sanqin.presenter;

import com.google.gson.GsonBuilder;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.model.entity.GenealogyInformation;
import com.pbids.sanqin.ui.activity.zhizong.GenealogyBakView;
import com.pbids.sanqin.ui.activity.zhizong.GenealogyView;
import com.pbids.sanqin.utils.OkGoUtil;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:49
 * @desscribe 类描述:我的族谱
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.zhizong.GenealogyFragment
 */
public class GenealogyBakPresenter extends BasePresenter{
    GenealogyBakView familyBookView;
    public GenealogyBakPresenter(GenealogyBakView familyBookView){
        this.familyBookView = familyBookView;
    }

    public DisposableObserver<Response<String>> submitInformation(String url, HttpParams params, String type){
        DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(@NonNull Response<String> stringResponse) {
                final GenealogyInformation[] genealogyInformations = new GenealogyInformation[1];
                try {
                    JSONObject jsonObject = new JSONObject(stringResponse.body());
                    int status = jsonObject.getInt("status");
                    if(status== MyApplication.OK){
                        JSONObject data = jsonObject.getJSONObject("data");
                        int genealogy = data.getInt("genealogy");
                        if(genealogy==1){
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            GenealogyInformation genealogyInformation = gsonBuilder.create().fromJson(data.toString(), GenealogyInformation.class);
                            genealogyInformations[0] = genealogyInformation;
                        }
                        if(genealogyInformations[0]!=null){
                            familyBookView.updataInformation(genealogyInformations[0]);
                            familyBookView.onHttpSuccess("1");
                        }
                    }else {
                        familyBookView.onHttpError(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    familyBookView.onHttpError(e.getMessage());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                familyBookView.onHttpError(Const.REQUEST_ERROR_NETWOR_DISCONNECT_MESSAGE);
            }

            @Override
            public void onComplete() {

            }
        };

        OkGoUtil.getStringObservableForPost(url,params,observer);
        return observer;
    }
}
