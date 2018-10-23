package com.pbids.sanqin.ui.activity.me;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.model.HttpParams;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.model.entity.BindPhoneBack;
import com.pbids.sanqin.presenter.MeBindingVerfyCodePresenter;
import com.pbids.sanqin.ui.activity.MainFragment;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.ui.view.pay.PayPsdInputView;
import com.pbids.sanqin.ui.view.pay.XNumberKeyboardView;
import com.pbids.sanqin.utils.AddrConst;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 14:17
 * @desscribe 类描述:输入验证码界面
 * @remark 备注:
 * @see
 */
public class MeBindingVerfyCodeFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear, MeBindingVerfyCodeView, XNumberKeyboardView.IOnKeyboardListener,PayPsdInputView.onPasswordListener {

    public static final String BINDING_PHONE_FIRST = "binding_phone_first";
    public static final String WITHDRAWLS = "withdrawls";
    public static final int WITHDRAWLS_REQUEST_CODE_SMS = 3201;
    public static final int WITHDRAWLS_REQUEST_CODE_SUBMIT = 3202;
    public static final String VERIFICATION_PHONE_NUMBER = "verification_phone_number";
    public static final String BINDING_PHONE_NEW = "binding_phone_new";
    public static final String VERIFICATION_PHONE_NUMBER_REQUEST = "verification_phone_number_request";
    public static final String BINDING_PHONE_NEW_REQUEST = "binding_phone_new_request";

    float balance = -1;
    private String phone = "";
    CountDownTimer countDownTimer;
    MeBindingVerfyCodePresenter presenter;
    String key="";

    @Bind(R.id.me_binding_verifycode_send)
    TextView meBindingVerifycodeSend;
    @Bind(R.id.me_binding_phone)
    TextView meBindingPhone;
    @Bind(R.id.me_binding_verifycode)
    PayPsdInputView meBindingVerifycode;
    @Bind(R.id.pay_password_keyboardview)
    XNumberKeyboardView payPasswordKeyboardview;

    public static MeBindingVerfyCodeFragment newInstance() {
        MeBindingVerfyCodeFragment fragment = new MeBindingVerfyCodeFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static MeBindingVerfyCodeFragment newInstance(int type) {
        MeBindingVerfyCodeFragment fragment = new MeBindingVerfyCodeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPEFRAGMENT,type);
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

        String url ;
        //http://www.pbids.com:9514/account/getWithdrawSMSCode
        if (WITHDRAWLS.equals(key)) {
            //提现
            phone = MyApplication.getUserInfo().getPhone();
            HttpParams params = new HttpParams();
            params.put("amount", "" + balance);
            url = AddrConst.SERVER_ADDRESS_PAYMENT + "/account/getWithdrawSMSCode";
            addDisposable(presenter.submitInformation(url, params, WITHDRAWLS));//WITHDRAWLS
//        meTitleText.setText("输入验证码");
            meBindingPhone.setText("+86 " + MyApplication.getUserInfo().getPhone());
        } else if(BINDING_PHONE_FIRST.equals(key)) {
            //绑定手机号
            HttpParams params = new HttpParams();
            params.put("phone", phone);
            url = AddrConst.SERVER_ADDRESS_USER + AddrConst.ADDRESS_ME_BINDING_PGONE;
            addDisposable(presenter.submitInformation(url, params, BINDING_PHONE_FIRST));//BINDING_PHONE_FIRST
//        meTitleText.setText("输入验证码");
            meBindingPhone.setText("+86 " + phone);
//        AppUtils.showSoftInputFromWindow(_mActivity);
        }else if(BINDING_PHONE_NEW.equals(key)){
            //绑定手机号
            HttpParams params = new HttpParams();
            params.put("phone", phone);
            params.put("changeStatus", 2);
            url = AddrConst.SERVER_ADDRESS_USER + AddrConst.ADDRESS_USER_CHANGEBINDPHONE;
            addDisposable(presenter.submitInformation(url, params, BINDING_PHONE_NEW));//BINDING_PHONE_NEW
//        meTitleText.setText("输入验证码");
            meBindingPhone.setText("+86 " + phone);
        }else if(VERIFICATION_PHONE_NUMBER.equals(key)){
            //绑定手机号
            phone = MyApplication.getUserInfo().getPhone();
            HttpParams params = new HttpParams();
            params.put("phone", phone);
            params.put("changeStatus", 1);
            addDisposable(presenter.submitInformation(
                    AddrConst.SERVER_ADDRESS_USER + AddrConst.ADDRESS_USER_CHANGEBINDPHONE, params, VERIFICATION_PHONE_NUMBER));//VERIFICATION_PHONE_NUMBER
//        meTitleText.setText("输入验证码");
            meBindingPhone.setText("+86 " + MyApplication.getUserInfo().getPhone());
        }
        startCountDown();
    }

//    public String realVerifyCode = "";

    @Override
    public BasePresenter initPresenter() {
        return presenter = new MeBindingVerfyCodePresenter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        dispose();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.me_binding_verifycode_send})
    public void onViewClicked(View view) {
        String url ;
        switch (view.getId()) {
            case R.id.me_binding_verifycode_send:
                if (WITHDRAWLS.equals(key)) {
                    phone = MyApplication.getUserInfo().getPhone();
                    HttpParams params = new HttpParams();
                    params.put("amount", "" + balance);
                    url = AddrConst.SERVER_ADDRESS_PAYMENT + "/account/getWithdrawSMSCode";
                    addDisposable(presenter.submitInformation(url, params, WITHDRAWLS));//WITHDRAWLS
//        meTitleText.setText("输入验证码");
                    meBindingPhone.setText("+86 " + MyApplication.getUserInfo().getPhone());
                } else if(BINDING_PHONE_FIRST.equals(key)) {
                    HttpParams params = new HttpParams();
                    params.put("phone", phone);
                    url = AddrConst.SERVER_ADDRESS_USER + AddrConst.ADDRESS_ME_BINDING_PGONE;
                    addDisposable(presenter.submitInformation(url, params, BINDING_PHONE_FIRST));//BINDING_PHONE_FIRST
//        meTitleText.setText("输入验证码");
                    meBindingPhone.setText("+86 " + phone);
//        AppUtils.showSoftInputFromWindow(_mActivity);
                }else if(BINDING_PHONE_NEW.equals(key)){
                    HttpParams params = new HttpParams();
                    params.put("phone", phone);
                    params.put("changeStatus", 2);
                    url = AddrConst.SERVER_ADDRESS_USER + AddrConst.ADDRESS_USER_CHANGEBINDPHONE;
                    addDisposable(presenter.submitInformation(url, params, BINDING_PHONE_NEW));//BINDING_PHONE_NEW
//        meTitleText.setText("输入验证码");
                    meBindingPhone.setText("+86 " + phone);
                }else if(VERIFICATION_PHONE_NUMBER.equals(key)){
                    phone = MyApplication.getUserInfo().getPhone();
                    HttpParams params = new HttpParams();
                    params.put("phone", phone);
                    params.put("changeStatus", 1);
                    url = AddrConst.SERVER_ADDRESS_USER + AddrConst.ADDRESS_USER_CHANGEBINDPHONE;
                    addDisposable(presenter.submitInformation(url, params, VERIFICATION_PHONE_NUMBER));//VERIFICATION_PHONE_NUMBER
//        meTitleText.setText("输入验证码");
                    meBindingPhone.setText("+86 " + MyApplication.getUserInfo().getPhone());
                }
                startCountDown();
                break;
        }
    }

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

    @Override
    public void onHttpSuccess(String type) {
        dismiss();
        if (type.equals("1")) {
            MainFragment mainFragment = findFragment(MainFragment.class);
            MeFragment meFragment = mainFragment.findChildFragment(MeFragment.class);
            meFragment.updatePhone();
            popTo(MainFragment.class, false);
            EventBus.getDefault().post(new BindPhoneBack((int)getArguments().get(TYPEFRAGMENT)));
        } else if (type.equals("3")) {
            MainFragment mainFragment = findFragment(MainFragment.class);
            MeFragment meFragment = mainFragment.findChildFragment(MeFragment.class);
            meFragment.updateAccountBalance();
            popTo(MainFragment.class, false);
        } else if(type.equals(VERIFICATION_PHONE_NUMBER_REQUEST)) {
            MeBindingNumberFragment fragment = MeBindingNumberFragment.newInstance();
            fragment.getArguments().putString("key",BINDING_PHONE_NEW);
            //  start(fragment);
            startWithPop(fragment);
        } else if(type.equals(BINDING_PHONE_NEW_REQUEST)){
            MainFragment mainFragment = findFragment(MainFragment.class);
            MeFragment meFragment = mainFragment.findChildFragment(MeFragment.class);
            meFragment.updatePhone();
            popTo(MainFragment.class, false);
        }
    }

    @Override
    public synchronized void onHttpError(String type) {
        //ToDo java.lang.IllegalStateException
        try{
            dismiss();
            // Log.i("wzh", "onHttpError");
//        popTo(MainFragment.class,false);
//        pop();
//        MainFragment mainFragment = findFragment(MainFragment.class);
//        MeFragment meFragment = mainFragment.findChildFragment(MeFragment.class);
//        meFragment.updatePhone();
//        getLoadingPop(null).dismiss();
//        countDownTimer.cancel();
//        dismiss();
//        countDownTimer.cancel();
//        countDownTimer = null;
//        dismiss();
//        DisposableObserver observer = new DisposableObserver() {
//            @Override
//            public void onNext(@NonNull Object o) {
//                popTo(MainFragment.class,false);
//            }
//            @Override
//            public void onError(@NonNull Throwable e) {
//            }
//            @Override
//            public void onComplete() {
//            }
//        };
//        Observable.timer(700,TimeUnit.MILLISECONDS).delay(700,TimeUnit.MILLISECONDS).subscribe(observer);
//        addDisposable(observer);
//        Flowable.timer(700, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe();
//        popTo(MeMoneyListFragment.class,true);
            pop();
            Toast.makeText(_mActivity, type, Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //    private String code ="";
    @Override
    public void returnVerifyCode(String code) {
//        realVerifyCode = code;
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
    public void onOver(String password) {
        String url ;
//        if (realVerifyCode.equals(password)) {
            hideSoftInput();
            getLoadingPop("正在提交").show();
            if (BINDING_PHONE_FIRST.equals(key)) {
                HttpParams params = new HttpParams();
                params.put("id", MyApplication.getUserInfo().getUserId());
                params.put("phone", phone);
                params.put("SMSCode", password);
                addDisposable(presenter.submitInformation(AddrConst.SERVER_ADDRESS_USER + AddrConst.ADDRESS_ME_BINDING_PGONE, params, "1"));
            } else if(WITHDRAWLS.equals(key)){
                HttpParams params = new HttpParams();
                params.put("amount", "" + balance);
                params.put("phone", MyApplication.getUserInfo().getPhone());
                params.put("SMSCode", password);
                addDisposable(presenter.submitInformation(AddrConst.SERVER_ADDRESS_PAYMENT + "/account/applyWithdraw", params, "3"));
            } else if(VERIFICATION_PHONE_NUMBER.equals(key)){
                HttpParams params = new HttpParams();
                params.put("phone", "" + phone);
                params.put("changeStatus", 1);
                params.put("SMSCode", password);
                addDisposable(presenter.submitInformation(AddrConst.SERVER_ADDRESS_USER
                        + AddrConst.ADDRESS_USER_CHANGEBINDPHONE, params, VERIFICATION_PHONE_NUMBER_REQUEST));
            }else if(BINDING_PHONE_NEW.equals(key)){
                HttpParams params = new HttpParams();
                params.put("phone", "" + phone);
                params.put("changeStatus", 2);
                params.put("SMSCode", password);
                url = AddrConst.SERVER_ADDRESS_USER + AddrConst.ADDRESS_USER_CHANGEBINDPHONE;
                addDisposable(presenter.submitInformation(url, params, BINDING_PHONE_NEW_REQUEST));
            }
//        } else {
//            meBindingVerifycode.invalidate();
//            Toast.makeText(_mActivity, "验证码错误", Toast.LENGTH_SHORT).show();
//            meBindingVerifycode.setText("");
//        }
    }
}
