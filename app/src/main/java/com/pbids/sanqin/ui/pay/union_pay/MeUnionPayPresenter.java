package com.pbids.sanqin.ui.pay.union_pay;

import android.content.Context;
import android.util.Log;

import com.lzy.okgo.model.HttpParams;
import com.pbids.sanqin.base.BaasePresenter;
import com.pbids.sanqin.base.BaaseView;
import com.pbids.sanqin.base.HttpJsonResponse;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.utils.AddrConst;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 14:07
 * @desscribe 类描述:银联支付-银生宝
 * @remark 备注:
 * @see MeUnionPayFragment
 */
public class MeUnionPayPresenter extends BaasePresenter<MeUnionPayView> {

    public static final int HTTP_REUEST_UNION_PAY_ORDER_CODE = 641 ;

    public boolean requestPayOrder(HttpParams params){
        String url = AddrConst.SERVER_ADDRESS_PAYMENT +"/pay/getPayOrder";
        Log.d("cgl","url:"+url);
        //请求服务器
        this.requestHttpPost(url,params,HTTP_REUEST_UNION_PAY_ORDER_CODE);
        return true;
    }

    @Override
    public void onHttpSuccess(int resultCode, int requestCode, HttpJsonResponse rescb) {
        if (resultCode == Const.OK) {
            switch (requestCode) {
                case MeUnionPayPresenter.HTTP_REUEST_UNION_PAY_ORDER_CODE:
                    // 银生宝支付 向服务器请求生成定单
                    String payUrl = rescb.getData().toString();
                    Log.d("cgl", "payurl:"+payUrl);
                    mView.openPayUrl(payUrl);
                    break;
                default:
                    break;
            }
        }else {
            mView.showToast("请求失败");
        }
    }

/*    @Override
    public void onHttpError(int resultCode, int requestCode, int errorCode, String errorMessage) {
        super.onHttpError(resultCode, requestCode, errorCode, errorMessage);
        mView.showToast("请求失败:" + errorMessage);
    }*/

    /* MeUnionPayView meAuthenticationView;
    public MeUnionPayPresenter(MeUnionPayView meAuthenticationView){
        this.meAuthenticationView = meAuthenticationView;
    }

    public DisposableObserver<Response<String>> getInformation(String url, HttpParams httpParams, final String type){
        DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(@NonNull Response<String> stringResponse) {
                Log.i("wzh","stringResponse: "+stringResponse.body());
                try {
                    JSONObject jsonObject = new JSONObject(stringResponse.body());
                    int status = jsonObject.getInt("status");
                    if(status== MyApplication.OK){
                        if("1".equals(type)){
                            meAuthenticationView.getPayInformationForYSB(jsonObject.getString("data"));
                        }
                        meAuthenticationView.onHttpSuccess(type);
                    }else {
                        meAuthenticationView.onHttpError(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        OkGoUtil.getStringObservableForPost(url,httpParams,observer);
        return observer;
    }*/
}
