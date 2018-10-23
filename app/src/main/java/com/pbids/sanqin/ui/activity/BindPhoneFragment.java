package com.pbids.sanqin.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pbids.sanqin.R;
import com.pbids.sanqin.base.BaaseFragment;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.presenter.BindPhoneFragmentPresenter;
import com.pbids.sanqin.ui.view.BindPhoneFragmentView;
import com.pbids.sanqin.ui.view.CountDownButton;
import com.pbids.sanqin.utils.ValidatorUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author : 上官名鹏
 * Description : 登录绑定手机号页面
 * Date :Create in 2018/9/12 19:27
 * Modified By :
 */
public class BindPhoneFragment extends BaaseFragment<BindPhoneFragmentPresenter> implements BindPhoneFragmentView {


    @Bind(R.id.bind_bg)
    ImageView bindBg;
    @Bind(R.id.bind_logo_another_bg)
    ImageView bindLogoAnotherBg;
    @Bind(R.id.bind_land_another)
    Button bindLandAnother;
    @Bind(R.id.bind_another_layout)
    LinearLayout bindAnotherLayout;
    @Bind(R.id.bind_phone_number)
    EditText bindPhoneNumber;
    @Bind(R.id.bind_et_test_code)
    EditText bindEtTestCode;
    @Bind(R.id.bind_land_phone_layout)
    LinearLayout bindLandPhoneLayout;
    @Bind(R.id.bind_bt_test_code)
    CountDownButton bindBtTestCode;
    private BindPhoneFragmentPresenter bindPhoneFragmentPresenter;

    int loginNumber = 0;//登录次数
    String serverCode = "";//验证码

    public static final String INFO = "info";
    public static final String TYPE = "type";

    @Override
    protected BindPhoneFragmentPresenter initPresenter() {
        return bindPhoneFragmentPresenter = new BindPhoneFragmentPresenter(this);
    }

    public static BindPhoneFragment newInstance(String info, String type) {
        BindPhoneFragment fragment = new BindPhoneFragment();
        Bundle bundle = new Bundle();
        bundle.putString(INFO, info);
        bundle.putString(TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bind_phone, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.bind_bt_test_code, R.id.bind_land})
    public void onViewClicked(View view) {
        String telPhone = bindPhoneNumber.getText().toString();
        switch (view.getId()) {
            case R.id.bind_bt_test_code:
                if ("".equals(telPhone)) {
                    onHttpError(LoginPageView.LONGIN_EMPTY_PHONE_NUMBER);
                    return;
                } else if (!ValidatorUtil.isMobile(telPhone)) {
                    onHttpError(LoginPageView.LONGIN_ERROR_PHONE_NUMBER);
                    return;
                }
                bindBtTestCode.startCountDown();
                bindPhoneFragmentPresenter.sendCode(telPhone);
                break;
            case R.id.bind_land:
                String SMSCode = bindEtTestCode.getText().toString().trim();
                if ("".equals(telPhone)) {
                    onHttpError(LoginPageView.LONGIN_EMPTY_PHONE_NUMBER);
                    return;
                }else if(!ValidatorUtil.isMobile(telPhone)){
                    onHttpError(LoginPageView.LONGIN_ERROR_PHONE_NUMBER);
                    return;
                }
                if(SMSCode==null||SMSCode.equals("")){
                    onHttpError(LoginPageView.LONGIN_EMPTY_TEST_CODE);
                    return;
                }
//                getLoadingPop("正在登录").show();
                bindPhoneFragmentPresenter.autoLogin(telPhone, SMSCode, getArguments().getString(INFO), getArguments().getString(TYPE));
                break;
        }
    }

    @Override
    public void onHttpError(final String type) {
        dismiss();
        switch (type) {
            case LoginPageView.LONGIN_EMPTY_PHONE_NUMBER:
                Toast.makeText(_mActivity, "手机号不能为空", Toast.LENGTH_SHORT).show();
                break;
            case LoginPageView.LONGIN_EMPTY_TEST_CODE:
                Toast.makeText(_mActivity, "验证码不能为空", Toast.LENGTH_SHORT).show();
                break;
            case LoginPageView.LONGIN_ERROR_PHONE_NUMBER:
                Toast.makeText(_mActivity, "您输入的不是手机号码", Toast.LENGTH_SHORT).show();
                break;
            case LoginPageView.LONGIN_EMPTY_PICTURE_CODE:
                Toast.makeText(_mActivity, "图形验证码不能为空", Toast.LENGTH_SHORT).show();
                break;
            case LoginPageView.LONGIN_ERROR_TEST_CODE:
                Toast.makeText(_mActivity, "验证码错误", Toast.LENGTH_SHORT).show();
                dismiss();
                break;
            case LoginPageView.LONGIN_ERROR_PICTURE_CODE:
                Toast.makeText(_mActivity, "图形验证码错误", Toast.LENGTH_SHORT).show();
                break;
            case LoginPageView.LONGIN_ERROR_HTTP_CODE:
                Toast.makeText(_mActivity, "网络异常，请检查网络后重试", Toast.LENGTH_SHORT).show();
                break;
            default:
                _mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(_mActivity, type, Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                });
                break;
        }
    }

    @Override
    public void onHttpSuccess() {
        dismiss();
        if (MyApplication.getUserInfo().getSurnameStatus() == 0) {
            start(AuthenticationFragment.newInstance());
        } else if (MyApplication.getUserInfo().getSurnameStatus() == 1) {
            Intent intent = new Intent("com.pbids.sanqin.ui.activity.HomePageActivity");
            //不可返回
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            _mActivity.finish();
        }
        //登陆结束apcivity
    }

    @Override
    public void SMSCode(String code) {
        Log.i("验证码", "SMSCode: " + code);

    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
    }
}
