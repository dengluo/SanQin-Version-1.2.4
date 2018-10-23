package com.pbids.sanqin.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.lzy.okgo.model.HttpParams;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BaseFragment;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.CommonGlideInstance;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.presenter.LoginPagePresenter;
import com.pbids.sanqin.ui.view.CountDownButton;
import com.pbids.sanqin.ui.view.PopupAboveView;
import com.pbids.sanqin.ui.view.VerifyCode;
import com.pbids.sanqin.utils.SharedUtils;
import com.pbids.sanqin.utils.ToastUtil;
import com.pbids.sanqin.utils.ValidatorUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;


/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:20
 * @desscribe 类描述:登录界面
 * @remark 备注:
 * @see
 */
public class LoginPageFragment extends BaseFragment implements LoginPageView {

    private static final String Tag = "LOGIN_PAGE";


    @Bind(R.id.login_bg)                       //背景图
            ImageView loginBg;
    @Bind(R.id.login_phone_number)            //手机号码编辑框
            EditText loginPhoneNumber;
    @Bind(R.id.login_et_test_code)            //验证码编辑框
            EditText loginEtTestCode;
    @Bind(R.id.login_bt_test_code)            //验证码发送按钮
            CountDownButton loginBtTestCode;
    @Bind(R.id.login_et_picture_code)        //图片验证码编辑框
            EditText loginEtPictureCode;
    @Bind(R.id.login_bt_picture_code)       //图片验证码
            VerifyCode loginBtPictureCode;
    @Bind(R.id.login_land)                   //登陆按钮
            Button loginLand;
    @Bind(R.id.login_first_way)             //第一种登陆方式
            ImageView loginFirst;
    @Bind(R.id.login_second_way)           //第二中登陆方式
            ImageView loginSecond;
    @Bind(R.id.login_more)                  //更多的登陆方式
            ImageView loginMore;
    @Bind(R.id.login_picture_code_layout) //图片验证码所在布局
            LinearLayout loginPictureCodeLayout;
    @Bind(R.id.login_logo_another_bg)     //另一种登陆方式logo
            ImageView loginLogoAnotherBg;
    @Bind(R.id.login_land_another)        //另一种登陆方式按钮
            Button loginLandAnother;
    @Bind(R.id.login_another_layout)      //另一种登陆方式所在布局
            LinearLayout loginAnotherLayout;
    @Bind(R.id.login_logo)                 //登陆界面logo
            ImageView loginLogo;
    @Bind(R.id.login_land_phone_layout)
    LinearLayout loginLandPhoneLayout;
    @Bind(R.id.login_first_way_tv)
    TextView loginFirstWayTv;
    @Bind(R.id.login_second_way_tv)
    TextView loginSecondWayTv;


    LoginPagePresenter mLoginPagePresenter;

    int loginNumber = 0;//登录次数
    String serverCode = "";//验证码

    String info;
    String platformName;
    @Bind(R.id.weixin_login_lin)
    LinearLayout weixinLoginLin;
    @Bind(R.id.tel_login_lin)
    LinearLayout telLoginLin;
    @Bind(R.id.login_back_up_fra)
    FrameLayout loginBackUpFra;

    private boolean onClickedWx = false;

    public static LoginPageFragment newInstance() {
        LoginPageFragment fragment = new LoginPageFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login_page, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    public void initView() {
//        initLoginWay();

    }

    @Override
    public void onResume() {
        super.onResume();
        //应用双开点击取消选择，需要关掉弹窗
        if (onClickedWx) {
            dismiss();
        }
    }

    public void initLoginWay() {
        SharedPreferences sp = _mActivity.getSharedPreferences("sanqin", Context.MODE_PRIVATE);
        String loginWay = sp.getString("login_way", "WeChat");
        int size = (int) getResources().getDimension(R.dimen.dp_40);
        int size2 = (int) getResources().getDimension(R.dimen.dp_220);
        switch (loginWay) {
            case "WeChat":
                loginFirstWayTv.setText("手机");
                loginSecondWayTv.setText("QQ");
                new CommonGlideInstance().setImageViewBackgroundForUrl(_mActivity, loginLogoAnotherBg, R.drawable.login_qq);
//                Glide.with(_mActivity).load(R.drawable.login_qq)
//                        .override(size,size).into(loginSecond);
                new CommonGlideInstance().setImageViewBackgroundForUrl(_mActivity, loginLogoAnotherBg, R.drawable.login_weixin_logo);
//                Glide.with(_mActivity).load(R.drawable.login_weixin_logo)
//                        .override(size2,size2).into(loginLogoAnotherBg);
                loginLandAnother.setText("微信授权登录");
                loginLandAnother.setTextColor(getResources().getColor(R.color.login_weixin_bt_text));
                loginLandAnother.setBackgroundResource(R.drawable.login_weixin_land);
                break;
            case "QQ":
                loginFirstWayTv.setText("手机");
                loginSecondWayTv.setText("微信");
                new CommonGlideInstance().setImageViewBackgroundForUrl(_mActivity, loginSecond, R.drawable.login_weixin);
//                Glide.with(_mActivity).load(R.drawable.login_weixin)
//                        .override(size,size).into(loginSecond);
                new CommonGlideInstance().setImageViewBackgroundForUrl(_mActivity, loginLogoAnotherBg, R.drawable.login_qq_logo);
//                Glide.with(_mActivity).load(R.drawable.login_qq_logo)
//                        .override(size2,size2).into(loginLogoAnotherBg);
                loginLandAnother.setText("QQ授权登录");
                loginLandAnother.setTextColor(getResources().getColor(R.color.login_qq_bt_text));
                loginLandAnother.setBackgroundResource(R.drawable.login_qq_land);
                break;
            case "phone":
                loginFirstWayTv.setText("微信");
                loginSecondWayTv.setText("QQ");
                new CommonGlideInstance().setImageViewBackgroundForUrl(_mActivity, loginFirst, R.drawable.login_weixin);
//                Glide.with(_mActivity).load(R.drawable.login_weixin)
//                        .override(size,size).into(loginFirst);
                new CommonGlideInstance().setImageViewBackgroundForUrl(_mActivity, loginSecond, R.drawable.login_qq);
//                Glide.with(_mActivity).load(R.drawable.login_qq)
//                        .override(size,size).into(loginSecond);
                loginLandPhoneLayout.setVisibility(View.VISIBLE);
                loginBg.setVisibility(View.VISIBLE);
                loginAnotherLayout.setVisibility(View.GONE);
                break;
        }
    }


    @Override
    public boolean onBackPressedSupport() {
        return super.onBackPressedSupport();
    }

    @Override
    public BasePresenter initPresenter() {
        return mLoginPagePresenter = new LoginPagePresenter(this);
    }

    //得到验证码
    @Override
    public void returnVerifyCode(String serverCode) {
        this.serverCode = serverCode;
        if(serverCode!=null){
            loginBtTestCode.startCountDown();
        }
        Log.d("验证码", "returnVerifyCode: " + serverCode);
    }

    @OnClick({R.id.login_bt_test_code, R.id.login_land, R.id.login_first_way
            , R.id.login_second_way, R.id.login_more, R.id.login_land_another})
    public void onViewClicked(View view) {
        String telPhone = loginPhoneNumber.getText().toString();//手机号
        String smsCode = loginEtTestCode.getText().toString();//输入的验证码
        String valCode = loginEtPictureCode.getText().toString().trim();//输入图形验证码
        String valCodePicture = loginBtPictureCode.getCodeText();//生成的图形验证码
        switch (view.getId()) {
            case R.id.login_bt_test_code:
                if ("".equals(telPhone)) {
                    onHttpError(LoginPageView.LONGIN_EMPTY_PHONE_NUMBER);
                    return;
                } else if (!ValidatorUtil.isMobile(telPhone)) {
                    onHttpError(LoginPageView.LONGIN_ERROR_PHONE_NUMBER);
                    return;
                }
                mLoginPagePresenter.loginSendTestCode(telPhone);
                break;
            case R.id.login_land:
//                if (telPhone.equals("14796635306") || telPhone.equals("13617200507")) {
//                    serverCode = "1234";
//                }
                if (loginNumber >= 1) {
                    boolean textResult = testEtCode(telPhone, smsCode, serverCode, valCode, valCodePicture);
//                    getLoadingPop("正在登录").show();
                    if (!textResult) return;
                    getLoadingPop("正在登录").show();
                    mLoginPagePresenter.localLogin(telPhone, smsCode, serverCode, valCode, valCodePicture);
                } else {
                    boolean textResult = testEtCode(telPhone, smsCode, serverCode, valCodePicture, null);
                    if (!textResult) return;
                    getLoadingPop("正在登录").show();
                    mLoginPagePresenter.localLogin(telPhone, smsCode, serverCode, valCodePicture, null);
                }
                break;
            case R.id.login_first_way:
//                if(loginFirstWayTv.getText().equals("手机")){
//                    changeToPhonePage();
//                    loginFirstWayTv.setText("微信");
//                    new CommonGlideInstance().setImageViewBackgroundForUrl(_mActivity,loginFirst,R.drawable.login_weixin);
////                    Glide.with(_mActivity).load(R.drawable.login_weixin)
////                            .override(loginFirst.getWidth(),loginFirst.getHeight()).into(loginFirst);
//                    loginSecondWayTv.setText("QQ");
//                    new CommonGlideInstance().setImageViewBackgroundForUrl(_mActivity,loginSecond,R.drawable.login_qq);
////                    Glide.with(_mActivity).load(R.drawable.login_qq)
////                            .override(loginFirst.getWidth(),loginFirst.getHeight()).into(loginSecond);
//                }else if(loginFirstWayTv.getText().equals("微信")){
//                    Log.i("wzh","login_first_way_weixin");
//                    getLoadingPop("正在登录").show();
//                    SharedUtils.weixinGetInformation(platformActionListener);
//                }
                loginAnotherLayout.setVisibility(View.VISIBLE);
                loginLandPhoneLayout.setVisibility(View.GONE);
                telLoginLin.setVisibility(View.VISIBLE);
                weixinLoginLin.setVisibility(View.GONE);
                loginBackUpFra.setBackground(null);
                break;
            case R.id.login_second_way:
//                Log.i("wzh","login_second_way_qq");
//                if(loginSecondWayTv.getText().equals("微信")){
//                    getLoadingPop("正在登录").show();
//                    SharedUtils.weixinGetInformation(platformActionListener);
//                }else if(loginSecondWayTv.getText().equals("QQ")){
//                    getLoadingPop("正在登录").show();
//                    SharedUtils.qqGetInformation(platformActionListener);
//                }
                loginAnotherLayout.setVisibility(View.GONE);
                loginLandPhoneLayout.setVisibility(View.VISIBLE);
                telLoginLin.setVisibility(View.GONE);
                weixinLoginLin.setVisibility(View.VISIBLE);
                loginBackUpFra.setBackgroundResource(R.drawable.login_back_up);
//                getLoadingPop("正在登录").show();
//                SharedUtils.qqGetInformation(platformActionListener);
                break;
            case R.id.login_more:
                PopupAboveView popupAboveView = new PopupAboveView(_mActivity, 20, true);
                popupAboveView.showUp(loginMore);
                break;
            case R.id.login_land_another:
                getLoadingPop("正在登录").show();
                onClickedWx = true;
                SharedUtils.weixinGetInformation(platformActionListener);
                /*if(loginLandAnother.getText().equals("微信授权登录")){
                    getLoadingPop("正在登录").show();
                    SharedUtils.weixinGetInformation(platformActionListener);
                }else if(loginLandAnother.getText().equals("QQ授权登录")){
                    getLoadingPop("正在登录").show();
                    SharedUtils.qqGetInformation(platformActionListener);
                }*/
                break;
        }
    }

    private void changeToPhonePage() {
        int enter = me.yokeyword.fragmentation.R.anim.h_fragment_enter;
        int exit = me.yokeyword.fragmentation.R.anim.h_fragment_exit;
        int popEnter = me.yokeyword.fragmentation.R.anim.h_fragment_pop_enter;
        int popExit = me.yokeyword.fragmentation.R.anim.h_fragment_pop_exit;

        Animation leftOut = AnimationUtils.loadAnimation(_mActivity, exit);
        leftOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                loginLandPhoneLayout.setVisibility(View.VISIBLE);
                loginBg.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        loginAnotherLayout.startAnimation(leftOut);
        Animation rightIn = AnimationUtils.loadAnimation(_mActivity, enter);
        rightIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                loginLandPhoneLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                loginAnotherLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        loginLandPhoneLayout.startAnimation(rightIn);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Glide.get(_mActivity).clearMemory();
        loginSecond.setBackground(null);
        loginSecond.setBackgroundResource(0);
        loginSecond.setImageResource(0);
        loginSecond = null;
        ButterKnife.unbind(this);
    }

    private void clearView(View view) {
        if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;
        }
    }

    PlatformActionListener platformActionListener = new PlatformActionListener() {
        @Override
        public void onError(Platform arg0, int arg1, Throwable arg2) {
            // TODO Auto-generated method stub
            arg2.printStackTrace();
            //提示微信有没有安装
            if ("Wechat".equals(arg0.getName())) {
                _mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(_mActivity, "登录失败,请先确定手机已经安装微信!", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                });
            }
        }

        @Override
        public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
            // TODO Auto-generated method stub
            //输出所有授权信息
            String identifier = "";
            String openId = "";
            info = arg0.getDb().exportData();
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(info);
            platformName = arg0.getName();
            if (platformName.equals("QQ")) {
                identifier = jsonObject.getString("userID");
            } else {
                openId = jsonObject.getString("unionid");
            }
            addDisposable(mLoginPagePresenter.isExistUser(identifier, openId));
//            if("QQ".equals(platformName)){
//                HttpParams params = parePlatformInformation(info,"QQ");
//                mLoginPagePresenter.loginPlatform(AddrConst.SERVER_ADDRESS_USER + AddrConst.ADDRESS_AUTHLOGIN,params,platformName);
//            }else if("Wechat".equals(platformName)){
////                getLoadingPop("正在登录").show();
//                HttpParams params = parePlatformInformation(info,"WeChat");
//                mLoginPagePresenter.loginPlatform(AddrConst.SERVER_ADDRESS_USER + AddrConst.ADDRESS_AUTHLOGIN,params,platformName);
//            }
        }

        @Override
        public void onCancel(Platform arg0, int arg1) {
            // TODO Auto-generated method stub
            _mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dismiss();
                }
            });
        }
    };

    public boolean testEtCode(String phoneNumber, String smsCode, String realTestCode, String pictureCode, String realPictureCode) {
        if ("".equals(phoneNumber)) {
            onHttpError(LoginPageView.LONGIN_EMPTY_PHONE_NUMBER);
            return false;
        } else if (!ValidatorUtil.isMobile(phoneNumber)) {
            onHttpError(LoginPageView.LONGIN_ERROR_PHONE_NUMBER);
            return false;
        }
        if (realPictureCode != null) {
            if (pictureCode.equals("")) {
                onHttpError(LoginPageView.LONGIN_EMPTY_PICTURE_CODE);
                return false;
            } else if (!pictureCode.equalsIgnoreCase(realPictureCode)) {
                onHttpError(LoginPageView.LONGIN_ERROR_PICTURE_CODE);
                return false;
            }
        }
        if ("".equals(smsCode)) {
            onHttpError(LoginPageView.LONGIN_EMPTY_TEST_CODE);
            return false;
        }
//        if (realTestCode != null && !realTestCode.equals("")) {
//            if (!smsCode.equals(realTestCode)) {
//                onHttpError(LoginPageView.LONGIN_ERROR_TEST_CODE);
//                return false;
//            }
//        }
        return true;
    }

    @Override
    public void onHttpSuccess(String type) {
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
    public void showToastInfo(String mess) {
        Toast.makeText(_mActivity, mess, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void bindPhone() {
        dismiss();
        start(BindPhoneFragment.newInstance(info, platformName));
    }

    @Override
    public void toastStauts(String message) {
        ToastUtil.show(_mActivity,message);
        dismiss();
    }

    @Override
    public void onHttpError(final String type) {
        dismiss();
        if (loginNumber >= 1) {
            loginPictureCodeLayout.setVisibility(View.VISIBLE);
        }
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
        loginNumber = loginNumber + 1;
    }

    public HttpParams parePlatformInformation(String info, String platformName) {
        HttpParams params = new HttpParams();
        try {
            JSONObject jsonObject = new JSONObject(info);
            String nickname = jsonObject.getString("nickname");
            String userID = jsonObject.getString("userID");
            String sex = jsonObject.getString("gender");
            String unionid = jsonObject.getString("unionid");
            if (sex.equals("0")) {
                sex = "男";
            } else if (sex.equals("1")) {
                sex = "女";
            }
            String faceUrl = jsonObject.getString("icon");
            String identityType = platformName;
            params.put("name", nickname);
            params.put("identityType", identityType);
            params.put("sex", sex);
            params.put("faceUrl", faceUrl);
            params.put("deviceId", MyApplication.deviceId);
            if ("WeChat".equals(platformName)) {
                params.put("identifier", unionid);
                params.put("openId", jsonObject.getString("openid"));
            } else if ("QQ".equals(platformName)) {
                params.put("identifier", userID);
                params.put("openId", "");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }
}
