package com.pbids.sanqin.ui.activity.me;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BaseFragment;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.CommonGlideInstance;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.common.OnDialogClickListener;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.model.entity.UserInfo;
import com.pbids.sanqin.ui.activity.MainFragment;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.ui.view.CircleImageView;
import com.pbids.sanqin.ui.view.dialog.OneTextTwoBtDialog;
import com.pbids.sanqin.utils.RequestCode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 14:24
 * @desscribe 类描述:我的界面
 * @remark 备注:
 * @see
 */
public class MeFragment extends ToolbarFragment {

    @Bind(R.id.me_home_autopragh)
    CircleImageView meHomeAutopragh;
    @Bind(R.id.me_home_name)
    TextView meHomeName;
    @Bind(R.id.me_home_user_hobby)
    TextView meHomeUserHobby;
    @Bind(R.id.me_home_money_number)
    TextView meHomeMoneyNumber;
    @Bind(R.id.me_home_binding_number)
    TextView meHomeBindingNumber;
    @Bind(R.id.me_home_person_layout)
    RelativeLayout meHomePersonLayout;
    @Bind(R.id.me_home_money_layout)
    RelativeLayout meHomeMoneyLayout;
    @Bind(R.id.me_home_binding_layout)
    RelativeLayout meHomeBindingLayout;
    @Bind(R.id.me_home_authentication_layout)
    RelativeLayout meHomeAuthenticationLayout;
    @Bind(R.id.me_home_campaign_layout)
    RelativeLayout meHomeCampaignLayout;
    @Bind(R.id.me_home_gift_layout)
    RelativeLayout meHomeGiftLayout;
    @Bind(R.id.me_home_collection_layout)
    RelativeLayout meHomeCollectionLayout;
    @Bind(R.id.me_home_history_layout)
    RelativeLayout meHomeHistoryLayout;
    @Bind(R.id.me_home_setting_layout)
    RelativeLayout meHomeSettingLayout;
    @Bind(R.id.me_home_family_layout)
    RelativeLayout meHomeFamilyLayout;
    @Bind(R.id.me_home_idcard_number)
    TextView meHomeIdcardNumber;
    @Bind(R.id.me_home_idcard_layout)
    RelativeLayout meHomeIdcardLayout;
    @Bind(R.id.me_home_authen_status)
    TextView meHomeAuthenStatus;
    @Bind(R.id.me_home_person_firstname)
    TextView meHomePersonFirstname;
    @Bind(R.id.me_home_zong)
    ImageView meHomeZong;
    @Bind(R.id.me_information_qr_vip)
    TextView meInformationQrVip;
    @Bind(R.id.ll3)
    LinearLayout ll3;
    @Bind(R.id.me_home_follow_layout)
    RelativeLayout meHomeFollowLayout;
    @Bind(R.id.ly_home_family)
    LinearLayout lyHomeFamile;
    @Bind(R.id.me_home_invite_picture)
    ImageView meHomeInvitePicture;
    @Bind(R.id.me_home_invite)
    TextView meHomeInvite;
    @Bind(R.id.me_name_invite_right_arrow)
    ImageView meNameInviteRightArrow;
    @Bind(R.id.me_home_invite_layout)
    RelativeLayout meHomeInviteLayout;
    private OneTextTwoBtDialog oneTextTwoBtDialog;

    public static MeFragment newInstance() {
        MeFragment fragment = new MeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        View view = inflater.inflate(R.layout.me_home, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setCenterTextTitle("我的", _mActivity);
    }

    public void initView() {
        //家族显示与不显示
        if (MyApplication.getUserInfo().getClanStatus() > 0) {
            lyHomeFamile.setVisibility(View.VISIBLE);
        } else {
            lyHomeFamile.setVisibility(View.GONE);
        }

        int size = (int) getResources().getDimension(R.dimen.dp_60);
//        Glide.with(_mActivity).load(MyApplication.getUserInfo().getFaceUrl())
//                .placeholder(R.drawable.me_avatar_moren_default)
//                .error(R.drawable.me_avatar_moren_default)
//                .dontAnimate().override(size, size).into(meHomeAutopragh);
        updateHeadPortrait();
        meHomeName.setText(MyApplication.getUserInfo().getName());
        meHomeUserHobby.setText(MyApplication.getUserInfo().getSignature());
        meHomeMoneyNumber.setText(MyApplication.getUserInfo().getAccountBalance() + "元");
        if ("".equals(MyApplication.getUserInfo().getPhone())) {
            meHomeBindingNumber.setText("未绑定");
        } else {
//            meHomeBindingNumber.setText(MyApplication.getUserInfo().getPhone());
            meHomeBindingNumber.setText("已绑定");
        }
        if (MyApplication.getUserInfo().getIsBindCard() == 1) {
//            meHomeIdcardNumber.setText(CommonUtil.shieldCardNumber(MyApplication.getUserInfo().getCardNumber()));
            meHomeIdcardNumber.setText("已绑定");
        } else {
            meHomeIdcardNumber.setText("未绑定");
        }
        if (MyApplication.getUserInfo().getIsRealName() == 1) {
            meHomeAuthenStatus.setText("已认证");
        } else {
            meHomeAuthenStatus.setText("未认证");
        }
        if (MyApplication.getUserInfo().getClanStatus() == 1) {
            meHomeZong.setVisibility(View.VISIBLE);
        }
        if (MyApplication.getUserInfo().getVip() != 0) {
            ll3.setVisibility(View.VISIBLE);
            meInformationQrVip.setText("VIP" + MyApplication.getUserInfo().getVip());
        }
    }

    public void updateAuthen() {
        if (MyApplication.getUserInfo().getIsRealName() == 1) {
            meHomeAuthenStatus.setText("已认证");
        } else {
            meHomeAuthenStatus.setText("未认证");
        }
//        meHomeAuthenStatus.setText("已认证");
    }

    public void updateHeadPortrait() {
//        int size = (int) getResources().getDimension(R.dimen.dp_60);
        new CommonGlideInstance().setImageViewBackgroundForUrl(_mActivity, meHomeAutopragh, MyApplication.getUserInfo().getFaceUrl(), new RequestListener() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                meHomePersonFirstname.setVisibility(View.VISIBLE);
                Typeface typeface = Typeface.createFromAsset(_mActivity.getAssets(), "fonts/汉仪颜楷繁.ttf");
                meHomePersonFirstname.getPaint().setTypeface(typeface);
//                MyApplication.getUserInfo().setSurname("速度");
                if (MyApplication.getUserInfo().getSurname().length() == 1) {
                    meHomePersonFirstname.setTextSize(_mActivity.getResources().getDimension(R.dimen.sp_7));
                    meHomePersonFirstname.setLineSpacing(0, 0.8F);
                } else if (MyApplication.getUserInfo().getSurname().length() == 2) {
                    meHomePersonFirstname.setTextSize(_mActivity.getResources().getDimension(R.dimen.sp_6));
                    meHomePersonFirstname.setLineSpacing(0, 0.8F);
                }
                meHomePersonFirstname.setText(MyApplication.getUserInfo().getSurname() + "府");
                return false;
            }

            @Override
            public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                meHomePersonFirstname.setVisibility(View.INVISIBLE);
                return false;
            }
        });
//        Glide.with(_mActivity).load(MyApplication.getUserInfo().getFaceUrl())
//                .placeholder(R.drawable.me_avatar_moren_default)
//                .error(R.drawable.me_avatar_moren_default_)
//                .dontAnimate()
//                .override(size, size)
//                .listener(new RequestListener<String, GlideDrawable>() {
//            @Override
//            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                meHomePersonFirstname.setVisibility(View.VISIBLE);
//                Typeface typeface = Typeface.createFromAsset(_mActivity.getAssets(), "fonts/华文行楷.ttf");
//                meHomePersonFirstname.getPaint().setTypeface(typeface);
////                MyApplication.getUserInfo().setSurname("速度");
//                if(MyApplication.getUserInfo().getSurname().length()==1){
//                    meHomePersonFirstname.setTextSize(_mActivity.getResources().getDimension(R.dimen.sp_8));
//                    meHomePersonFirstname.setLineSpacing(0,0.8F);
//                }else if(MyApplication.getUserInfo().getSurname().length()==2){
//                    meHomePersonFirstname.setTextSize(_mActivity.getResources().getDimension(R.dimen.sp_6));
//                    meHomePersonFirstname.setLineSpacing(0,0.8F);
//                }
//                meHomePersonFirstname.setText(MyApplication.getUserInfo().getSurname()+"府");
//                return false;
//            }
//
//            @Override
//            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                meHomePersonFirstname.setVisibility(View.INVISIBLE);
//                return false;
//            }
//        }).into(meHomeAutopragh);
    }

    public void updateName() {
        meHomeName.setText(MyApplication.getUserInfo().getName());
    }

    public void updateIdcard() {
        if (MyApplication.getUserInfo().getIsBindCard() == 1) {
            meHomeIdcardNumber.setText("已绑定");
        } else {
            meHomeIdcardNumber.setText("未绑定");
        }
    }

    public void updateUserHobby() {
        meHomeUserHobby.setText(MyApplication.getUserInfo().getSignature());
    }

    public void updateAccountBalance() {
        float balance = MyApplication.getUserInfo().getAccountBalance();
        updateAccountBalance(balance);
//        meHomeMoneyNumber.setText("" + String.format("%.2f", balance) + "元");
    }

    public void updateAccountBalance(float balance) {
//        float balance = MyApplication.getUserInfo().getAccountBalance();
        meHomeMoneyNumber.setText("" + String.format("%.2f", balance) + "元");
        meHomeMoneyNumber.setVisibility(View.VISIBLE);
    }

    public void updatePhone() {
        if ("".equals(MyApplication.getUserInfo().getPhone())) {
            meHomeBindingNumber.setText("未绑定");
        } else {
            meHomeBindingNumber.setText("已绑定");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public static String str;

    @OnClick({R.id.me_home_family_layout, R.id.me_home_person_layout, R.id.me_home_money_layout ,R.id.me_home_binding_layout
            , R.id.me_home_authentication_layout, R.id.me_home_campaign_layout
            , R.id.me_home_gift_layout, R.id.me_home_collection_layout, R.id.me_home_follow_layout
            , R.id.me_home_history_layout, R.id.me_home_setting_layout, R.id.me_home_idcard_layout,R.id.me_home_invite_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.me_home_family_layout:
                ((MainFragment) getParentFragment()).start(MeFamilyManageFragment.newInstance());
                break;
            case R.id.me_home_person_layout:
                ((MainFragment) getParentFragment())
                        .startFragmentForResult(MeInformationFragment.newInstance(), RequestCode.ME_INFORMATION_FRAGMENT_RESULT);
                break;
            case R.id.me_home_money_layout:
                /*final SuperDialog superDialog = new SuperDialog(_mActivity);
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
                                new CommonGlideInstance().setImageViewBackgroundForUrl(_mActivity, imageView, R.drawable.me_icon_yitijiao_default);
                            }
                        }).setButtonTexts(new String[]{"我知道了"}) ;

                superDialog.setButtonTextColor(_mActivity.getResources().getColor(R.color.white));
                superDialog.setButtonViewStyle(R.style.superdialog_button);
                superDialog.setTitleViewStyle(R.style.superdialog_title);
                superDialog.setContentViewStyle(R.style.superdialog_title);
                superDialog.setImageViewStyle(R.style.superdialog_image);
                superDialog.show();
                superDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        Toast.makeText(_mActivity, "cancel", Toast.LENGTH_SHORT).show();
                    }
                });*/
                //我的资金
                ((MainFragment) getParentFragment()).start(MeMoneyListFragment.newInstance());
//                startShot();

                break;
            case R.id.me_home_binding_layout:
                if ("".equals(MyApplication.getUserInfo().getPhone())) {
                    //绑定电话
                    ((MainFragment) getParentFragment()).start(MeBindingNumberFragment.newInstance(BaseFragment.DEFAULTTYPE));
                } else {
                    //绑定电话完成
                    ((MainFragment) getParentFragment()).start(MeBindingPhoneOverFragment.newInstance());
                }
                break;
            case R.id.me_home_authentication_layout:
                if(MyApplication.getUserInfo().getPhone().equals("")){
                    showBindPhoneDialog(this,TORENLNAMETYPE);
                }else{
                    if (MyApplication.getUserInfo().getIsRealName() == 1) {
                        //实名认证完成
                        ((MainFragment) getParentFragment()).start(MeAuthenticationOverFragment.newInstance());
                    } else {
                        //实名认证
                        ((MainFragment) getParentFragment()).start(MeAuthenticationFragment.newInstance(DEFAULTTYPE));
                    }
                }
                break;
            case R.id.me_home_campaign_layout:
                MeCampaignFragment fragment = MeCampaignFragment.newInstance();
                fragment.setFragmentAnimator(null);
                ((MainFragment) getParentFragment()).start(fragment);
                break;
            case R.id.me_home_gift_layout:
                ((MainFragment) getParentFragment()).start(MeGiftFragment.newInstance());
                break;
            case R.id.me_home_collection_layout:
                ((MainFragment) getParentFragment()).start(MeCollectionFragment.newInstance());
                break;
            case R.id.me_home_history_layout:
                ((MainFragment) getParentFragment()).start(MeHistoryFragment.newInstance());
                break;
            case R.id.me_home_setting_layout:
                ((MainFragment) getParentFragment()).start(MeSettingFragment.newInstance());
//                ((MainFragment) getParentFragment()).start(ShakeFragment.newInstance());
                break;
            case R.id.me_home_follow_layout:
                ((MainFragment) getParentFragment()).start(MeTopicSubscribeFragment.newInstance());
                break;
            case R.id.me_home_idcard_layout:
                //提现绑卡
                /*String json = "{\"code\":0,\"data\":{\"userInfo\":{\"accountBalance\":8682.8,\"birthday\":\"19931111\",\"brickCount\":0,\"email\":\"\",\"empiric\":0,\"faceUrl\":\"http://120.79.177.35:9999/group1/M00/00/0D/wKgFCFpnDDGAZ-2KAAAMhF4uglU721.jpg\",\"idNumber\":\"441***********0957\",\"isBindCard\":1,\"isRealName\":1,\"isSetPayword\":1,\"isVIP\":0,\"level\":11,\"levelName\":\"草民\",\"location\":\"天津市\",\"loginIP\":\"\",\"loginTime\":1517192623000,\"name\":\"\",\"nativePlace\":\"北京市,市辖区,东城区\",\"newGiftCount\":0,\"noticeSurnameIds\":\"1,2\",\"noticeSurnames\":\"张,李\",\"phone\":\"18565616772\",\"quickResponseCode\":\"\",\"sex\":\"男\",\"signature\":\"科技局U\",\"surname\":\"巫\",\"surnameId\":220,\"surnameStatus\":1,\"token\":\"1913EB9C4EF6C355EC32CF085E643174\",\"upgradeEx\":99,\"userId\":20,\"username\":\"巫哲豪\"},\"bindCard\":{\"bankCode\":\"ICBC\",\"bankIcon\":\"https://apimg.alipay.com/combo.png?d=cashier&t=ICBC\",\"bankName\":\"工商银行\",\"birthday\":\"19931111\",\"cardNumber\":\"***************2512\",\"cardType\":\"借记卡\",\"createTime\":1517367778694,\"id\":0,\"idNumber\":\"441***********0957\",\"name\":\"巫哲豪\",\"userId\":20}},\"message\":\"姓名、银行卡号、身份证号三者一致\",\"status\":1}";
                JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
                int status = jsonObject.get("status").getAsInt();
                JsonObject data = jsonObject.getAsJsonObject("data");
                if(status == MyApplication.OK){

                    JsonObject bindCard = data.getAsJsonObject("bindCard");
                    Bank bank = new GsonBuilder().create().fromJson(bindCard.toString(),Bank.class);
                }*/
                UserInfo userInfo = MyApplication.getUserInfo();
                final OneTextTwoBtDialog oneTextTwoBtDialog;
                if (userInfo.getIsRealName() == 1) {
                    if (userInfo.getIsBindCard() == 0) {
                        ((MainFragment) getParentFragment()).start(MeScanBankFragment.newInstance());
                    } else if (userInfo.getIsBindCard() == 1) {
                        ((MainFragment) getParentFragment()).start(MeBindingBankFragment.newInstance());
                    }
                    //((MainFragment) getParentFragment()).start(MeScanBankFragment.newInstance());
                } else {
                    /*((MainFragment) getParentFragment()).start(MeScanBankFragment.newInstance());
                    if (MyApplication.getUserInfo().getIsBindCard() == 0) {
                        ((MainFragment) getParentFragment()).start(MeScanBankFragment.newInstance());
                    } else if (MyApplication.getUserInfo().getIsBindCard() == 1) {
                        ((MainFragment) getParentFragment()).start(MeBindingBankFragment.newInstance());
                    }*/
                    if(userInfo.getPhone().equals("")){
                        showBindPhoneDialog(this,TOBANKTYPE);
                    }else if(userInfo.getIsRealName()==0){
                        oneTextTwoBtDialog = new OneTextTwoBtDialog(_mActivity);
                        oneTextTwoBtDialog.setGrayCenter();
                        oneTextTwoBtDialog.setComfirmText("前往");
                        oneTextTwoBtDialog.setOnDialogClickLisenrar(new OnDialogClickListener() {
                            @Override
                            public void confirm(View view) {
                                oneTextTwoBtDialog.dismiss();
                                ((MainFragment) getParentFragment()).start(MeAuthenticationFragment.newInstance(TOBANKTYPE));
                            }

                            @Override
                            public void cancel(View view) {
                                oneTextTwoBtDialog.dismiss();
                            }
                        });
                        oneTextTwoBtDialog.setContentText("绑定银行卡之前必须进行实名认证,是否前往实名认证界面");
                        oneTextTwoBtDialog.show();
                    }
                }
                break;
            case R.id.me_home_invite_layout:
                String phone = MyApplication.getUserInfo().getPhone();
                Integer isRealName = MyApplication.getUserInfo().getIsRealName();
                if(phone.equals("")) {
                    showBindPhoneDialog(this, TOINVTITETYPE);
                }else if(isRealName==0){
                    showRealNameDialog(this, BaseFragment.TOINVTITETYPE);
                }else{
                    ((MainFragment)getParentFragment()).start(MeInviteFragment.instance());
                }
                break;
        }
    }

}
