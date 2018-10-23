package com.pbids.sanqin.ui.activity.common;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.listener.OnDismissListener;
import com.lzy.okgo.model.HttpParams;
import com.pbids.sanqin.R;
import com.pbids.sanqin.base.BaaseToolBarFragment;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.presenter.SMSVerfyPresenter;
import com.pbids.sanqin.ui.activity.me.MeBindingVerfyCodeFragment;
import com.pbids.sanqin.ui.activity.me.MeMoneyListFragment;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.ui.view.OneImageOneBtPop;
import com.pbids.sanqin.ui.view.pay.PayPsdInputView;
import com.pbids.sanqin.ui.view.pay.XNumberKeyboardView;
import com.pbids.sanqin.utils.AddrConst;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SMSVerfyFragment extends BaaseToolBarFragment<SMSVerfyPresenter> implements PayPsdInputView.onPasswordListener, XNumberKeyboardView.IOnKeyboardListener, SMSVerfyView {

    float balance = -1;
    private String phone = "";
    CountDownTimer countDownTimer;

    String key="";

    @Bind(R.id.me_binding_verifycode_send)
    TextView meBindingVerifycodeSend;
    @Bind(R.id.me_binding_phone)
    TextView meBindingPhone;
    @Bind(R.id.me_binding_verifycode)
    PayPsdInputView meBindingVerifycode;
    @Bind(R.id.pay_password_keyboardview)
    XNumberKeyboardView payPasswordKeyboardview;

    public static SMSVerfyFragment newInstance() {
        SMSVerfyFragment fragment = new SMSVerfyFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.me_binding_verifycode, container, false);
        ButterKnife.bind(this, view);
        showSoftInput(view);
        initView();
        return view;
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftArrowCenterTextTitle("输入验证码", _mActivity);
    }

    public void initView() {
        hideSoftInput();
        meBindingVerifycode.setonPasswordListener(this);
        payPasswordKeyboardview.setIOnKeyboardListener(this);
//        sendVerifyCode();
        key = getArguments().getString("key");
        phone = getArguments().getString("phone");
        balance = getArguments().getFloat("balance");
        //http://www.pbids.com:9514/account/getWithdrawSMSCode
        switch (key){
            case MeBindingVerfyCodeFragment.WITHDRAWLS:
                //提现
                phone = MyApplication.getUserInfo().getPhone();
                HttpParams params = new HttpParams();
                params.put("amount", "" + balance);
                String url = AddrConst.SERVER_ADDRESS_PAYMENT + "/account/getWithdrawSMSCode";
                mPresenter.requestHttpPost(url,params, MeBindingVerfyCodeFragment.WITHDRAWLS_REQUEST_CODE_SMS);
                //addDisposable(presenter.submitInformation(url, params, WITHDRAWLS));//WITHDRAWLS
                meBindingPhone.setText("+86 " + MyApplication.getUserInfo().getPhone());
                break;
        }
        startCountDown();
    }



    @Override
    public SMSVerfyPresenter initPresenter() {
        return  new SMSVerfyPresenter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.me_binding_verifycode_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.me_binding_verifycode_send:
                switch (key){
                    case MeBindingVerfyCodeFragment.WITHDRAWLS:
                        //申请提现
                        phone = MyApplication.getUserInfo().getPhone();
                        HttpParams params = new HttpParams();
                        params.put("amount", "" + balance);
                        String url = AddrConst.SERVER_ADDRESS_PAYMENT + "/account/getWithdrawSMSCode";
                        // meTitleText.setText("输入验证码");
                        meBindingPhone.setText("+86 " + MyApplication.getUserInfo().getPhone());
                        break;
                }
                startCountDown();
                break;
        }
    }

    //开始计时
    private void startCountDown() {
        meBindingVerifycodeSend.setClickable(false);
        meBindingVerifycodeSend.setTextColor(
                _mActivity.getResources().getColor(R.color.main_hit_text_color));
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(60000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    meBindingVerifycodeSend.setText(millisUntilFinished / 1000 + "秒后重新发送");
                }
                @Override
                public void onFinish() {
                    meBindingVerifycodeSend.setText("重新发送验证码");
                    meBindingVerifycodeSend.setClickable(true);
                    meBindingVerifycodeSend.setTextColor(
                            _mActivity.getResources().getColor(R.color.main_link_text_color));
                }
            };
        }
        countDownTimer.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_left_layout:
                pop();
                break;
        }
    }

    //显示提现请求结果
    @Override
    public void onWithdrawCashCb(boolean sta){
        dismiss(); //关闭loader

/*        final SuperDialog superDialog = new SuperDialog(_mActivity);
        superDialog.setContent("申请提现已提交").
                setListener(new SuperDialog.onDialogClickListener() {
                    @Override
                    public void click(boolean isButtonClick, int position) {

                    }
                })
                .setShowImage()
                .setImageListener(new SuperDialog.onDialogImageListener() {
                    @Override
                    public void onInitImageView(ImageView imageView) {
                        //me_icon_yitijiao_default
                        //_mActivity.getResources().getDimension(R.dimen.dp_10)
                        imageView.setAdjustViewBounds(true);
                        imageView.setMaxWidth(R.dimen.dp_6);
                        imageView.setMaxHeight(R.dimen.dp_6);
                        new CommonGlideInstance().setImageViewBackgroundForUrl(_mActivity,imageView,R.drawable.me_icon_yitijiao_default);

                    }
                }).setButtonTexts(new String[]{"我知道了"}).show();

        superDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Toast.makeText(_mActivity, "cancel", Toast.LENGTH_SHORT).show();
            }
        });*/


        OneImageOneBtPop imageOneBtPop = new OneImageOneBtPop(_mActivity,OneImageOneBtPop.POP_WITHDRAW_CASH);
        imageOneBtPop.setIsAnimation(false);
        imageOneBtPop.setCancelable(false);
        if(sta){
            imageOneBtPop.setContent("申请提现已提交");//成功
        }else {
            imageOneBtPop.setContent("申请提现提交失败");//失败
        }
        imageOneBtPop.setNoTitle();
        imageOneBtPop.show();
        imageOneBtPop.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(Object o) {
                Bundle bundle = new Bundle();
                bundle.putInt("con",1);
                //完成后要结束
                setFragmentResult(MeMoneyListFragment.ME_MONEY_LIST_REQUEST_CODE,bundle);
                pop();
            }
        });
    }

    @Override
    public void onHttpError(int resultCode, int requestCode, int errorCode, String errorMessage) {
        dismiss(); //关闭loader
        showToast(errorMessage);
    }

    @Override
    public void onValError(String message) {
        showToast(message);
        dismiss();
        hideSoftInput();
        meBindingVerifycode.invalidate();
        meBindingVerifycode.setText("");
    }

    @Override
    public void onInsertKeyEvent(String text) {
        meBindingVerifycode.append(text);
    }

    @Override
    public void onDeleteKeyEvent() {
        int start = meBindingVerifycode.length() - 1;
        if (start >= 0) {
            meBindingVerifycode.getText().delete(start, start + 1);
        }
    }


    @Override
    public void onOver(String code) {
        Log.d(SMSVerfyFragment.class.getSimpleName(), "验证码："+code);
        switch (key) {
            case MeBindingVerfyCodeFragment.WITHDRAWLS:
                //处理提现申请
//                if (mPresenter.SMSVerify(code)) {
                    switch (key) {
                        case MeBindingVerfyCodeFragment.WITHDRAWLS:
                            getLoadingPop("正在提交").show();
                            mPresenter.onWithdrawCashSubmit(balance,code);
                            break;
                    }
//                } else {
//                    meBindingVerifycode.invalidate();
//                    showToast("验证码错误");
//                    meBindingVerifycode.setText("");
//                }
                break;
        }
    }
}
