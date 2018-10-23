package com.pbids.sanqin.ui.activity.common;

import com.pbids.sanqin.base.BaaseView;

/*
 * view -- 短信验证
 *
 */
public interface SMSVerfyView extends BaaseView {

    //提现申请返回
    void onWithdrawCashCb(boolean sta);

    void onHttpError(int resultCode, int requestCode, int errorCode, String errorMessage) ;

    void onValError(String message);

}
