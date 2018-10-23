package com.pbids.sanqin.presenter;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.pbids.sanqin.base.BaasePresenter;
import com.pbids.sanqin.base.HttpJsonResponse;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.model.entity.GenealogyInformation;
import com.pbids.sanqin.ui.activity.zhizong.GenealogyView;

import static com.pbids.sanqin.ui.activity.zhizong.GenealogyFragment.REQUEST_CODE_GENEALOGY;

/**
 * @author caiguoliang
 * @date on 2018/3/2 11:49
 * @desscribe 类描述:我的族谱
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.zhizong.GenealogyFragment
 */
public class GenealogyPresenter extends BaasePresenter<GenealogyView>{

    @Override
    public void onHttpSuccess(int resultCode, int requestCode,  HttpJsonResponse rescb) {
        //super.onHttpSuccess(resultCode, requestCode, body);
        if(resultCode == Const.OK){
            switch (requestCode){
                case REQUEST_CODE_GENEALOGY:
                    //JSONObject jsonBody = (JSONObject) body;
                    //Log.v("genealog",jsonBody.toJSONString());
                    //GenealogyInformation genealogyInformation = JSONObject.parseObject(jsonBody.toJSONString(),GenealogyInformation.class);
                    GenealogyInformation genealogyInformation = (GenealogyInformation) rescb.getJavaData(GenealogyInformation.class);
                    if(genealogyInformation.getGenealogy()==1){
                        //已建设
                        mView.loadData(genealogyInformation);
                    }else{
                        //未建设
                        mView.loadData(null);
                    }
                    break;
            }
        }
    }

    /*public DisposableObserver<Response<String>> submitInformation(String url, HttpParams params, String type){
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
                familyBookView.onHttpError(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        };

        OkGoUtil.getStringObservableForPost(url,params,observer);
        return observer;
    }*/
}
