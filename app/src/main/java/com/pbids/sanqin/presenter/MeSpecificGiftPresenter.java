package com.pbids.sanqin.presenter;

import com.lzy.okgo.model.HttpParams;
import com.pbids.sanqin.base.BaasePresenter;
import com.pbids.sanqin.base.HttpJsonResponse;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.ui.activity.me.MeSpecificGiftView;
import com.pbids.sanqin.utils.AddrConst;

/**
 * presenter
 * 模块：我的 -> 实物礼券
 * Created by pbids903 on 2018/1/5.
 */

public class MeSpecificGiftPresenter extends BaasePresenter<MeSpecificGiftView>{

    public static final int REQUEST_USE_COUPON_ME_SPECIFIC_GIFT  = 12047;


    @Override
    public void onHttpError(int resultCode, int requestCode, int errorCode, String errorMessage) {
        //super.onHttpError(resultCode, requestCode, errorCode, errorMessage);
        mView.showToast(errorMessage);
        mView.loadComplete();
    }

    @Override
    public void onHttpSuccess(int resultCode, int requestCode, HttpJsonResponse rescb) {
        //super.onHttpSuccess(resultCode, requestCode, rescb);
        if(requestCode == REQUEST_USE_COUPON_ME_SPECIFIC_GIFT){
            mView.showToast("兑换成功");
            mView.loadComplete();
        }
    }

    /**
     * 使用礼券
     * @param gid
     */
    public void useCoupon(Long gid) {
        HttpParams params = new HttpParams();
        params.put("id",gid);
        String url = AddrConst.SERVER_ADDRESS_USER + AddrConst.ADDRESS_USECOUPON;
        requestHttpPost(url,params,REQUEST_USE_COUPON_ME_SPECIFIC_GIFT);
    }

}
