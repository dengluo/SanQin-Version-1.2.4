package com.pbids.sanqin.presenter;

import com.lzy.okgo.model.HttpParams;
import com.pbids.sanqin.base.BaasePresenter;
import com.pbids.sanqin.base.HttpJsonResponse;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.ui.activity.common.SMSVerfyView;
import com.pbids.sanqin.ui.activity.me.MeBindingVerfyCodeFragment;
import com.pbids.sanqin.utils.AddrConst;


/**
 * @author caiguoliang
 * @date on 2018/3/2 11:54
 * @desscribe 类描述:我的界面，验证码输入
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.me.MeBindingVerfyCodeFragment
 */
public class SMSVerfyPresenter  extends BaasePresenter<SMSVerfyView> {

    //保存收到的验证码
    public String realVerifyCode = "";

    @Override
    public void onHttpSuccess(int resultCode, int requestCode,  HttpJsonResponse rescb) {
        //super.onHttpSuccess(resultCode, requestCode, body);
        switch (requestCode){
            case MeBindingVerfyCodeFragment.WITHDRAWLS_REQUEST_CODE_SMS:
                if(resultCode==Const.OK) {
                    mView.showToast(rescb.getMessage());
                }
//                    //提现验证码请求返回
//                    try {
//                        //String phone = body.getString("phone");
//                        JSONObject jsonBody = rescb.getJsonData();
//                        realVerifyCode = jsonBody.getString("SMSCode");
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                    if (realVerifyCode == null || realVerifyCode.isEmpty()) {
//                        realVerifyCode = "";
//                        mView.showToast("请求短信验证码错误 code:" + ErrorCode.ERROR_SMS_REQUEST);
//                    }
//                }else{
//                    //提现验证码请求返回
//                    mView.showToast("请求验证码失败 code:" + ErrorCode.ERROR_SMS_REQUEST);
//                }
                break;

            case MeBindingVerfyCodeFragment.WITHDRAWLS_REQUEST_CODE_SUBMIT:
                //提现提交返回
//                mView.onWithdrawCashCb(resultCode==Const.OK);
                if(resultCode==Const.OK){
                    mView.onWithdrawCashCb(true);
                }else{
                    mView.onValError(rescb.getMessage());
                }
                break;
        }

    }

    //请求数据错误
    @Override
    public void onHttpError(int resultCode, int requestCode, int errorCode, String errorMessage) {
        mView.onHttpError(resultCode,requestCode,errorCode,errorMessage);
        switch (requestCode){
            case MeBindingVerfyCodeFragment.WITHDRAWLS_REQUEST_CODE_SMS:
                break;

            case MeBindingVerfyCodeFragment.WITHDRAWLS_REQUEST_CODE_SUBMIT:
                break;
        }
    }

    //检验短信验证码
    public boolean SMSVerify(String code){
        if(realVerifyCode.equals(code)){
            return true;
        }
        return false;
    }

    //取短信
    public String getSMSCode(){
        return  realVerifyCode;
    }

    //取短信验证码后--完成提现申请
    public void onWithdrawCashSubmit(float balance,String code ){
        HttpParams params = new HttpParams();
        params.put("amount", "" + balance);
        params.put("phone", MyApplication.getUserInfo().getPhone());
        params.put("SMSCode", code);

        String url = AddrConst.SERVER_ADDRESS_PAYMENT + "/account/applyWithdraw";
        requestHttpPost(url,params,MeBindingVerfyCodeFragment.WITHDRAWLS_REQUEST_CODE_SUBMIT);

    }
}
