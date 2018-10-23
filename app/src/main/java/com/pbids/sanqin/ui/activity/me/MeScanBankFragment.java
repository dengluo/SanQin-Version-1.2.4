package com.pbids.sanqin.ui.activity.me;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.model.HttpParams;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BaseFragment;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.model.db.UserInfoManager;
import com.pbids.sanqin.model.entity.Bank;
import com.pbids.sanqin.model.entity.UserInfo;
import com.pbids.sanqin.presenter.MeScanBankPresenter;
import com.pbids.sanqin.ui.activity.MainFragment;
import com.pbids.sanqin.ui.activity.zhizong.ZhiZongWebFragment;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.ui.view.dialog.OneImageOneBtDialog;
import com.pbids.sanqin.utils.AddrConst;
import com.pbids.sanqin.utils.ButtonUtil;
import com.pbids.sanqin.utils.ValidatorUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.pbids.sanqin.ui.view.dialog.OneImageOneBtDialog.POP_ME_AUTHENTICATION;

/**
 * 提现绑卡
 */

public class MeScanBankFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear, MeScanBankView {

    MeScanBankPresenter meNewAuthenticationPresenter;
    @Bind(R.id.authem_new_et)
    EditText authemNewEt;
    @Bind(R.id.authem_new_next)
    Button authemNewNext;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.idcard)
    TextView idcard;
    @Bind(R.id.tv_bindcard)
    TextView tvBindCard;
    @Bind(R.id.bank_seletor_box)
    ImageView bankSeletorBox;

    public static MeScanBankFragment newInstance() {
        MeScanBankFragment fragment = new MeScanBankFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static MeScanBankFragment newInstance(int type) {
        MeScanBankFragment fragment = new MeScanBankFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(BaseFragment.TYPEFRAGMENT,type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_binding_card, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        name.setText(MyApplication.getUserInfo().getName());
        idcard.setText(MyApplication.getUserInfo().getIdNumber());
        bankSeletorBox.setSelected(true);
        ButtonUtil.setOnClickFalse(authemNewNext);
        authemNewEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (ValidatorUtil.isBankIDCard(s.toString())) {
//                    ButtonUtil.setOnClickTrue(authemNewNext);
//                } else {
//                    ButtonUtil.setOnClickFalse(authemNewNext);
//                }
                setButtonStatus(s.toString().trim());
//                if(" ".equals(s.toString().substring(s.length()-1,s.length()))){
//                    return;
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {
//                authemNewEt.setText(NumberUtil.spaceAt4(s.toString()));
            }
        });


    }

    private void setButtonStatus(String text){
        if(ValidatorUtil.isBankIDCard(text) && bankSeletorBox.isSelected()){
            ButtonUtil.setOnClickTrue(authemNewNext);
        }else{
            ButtonUtil.setOnClickFalse(authemNewNext);
        }
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setLeftArrowCenterTextTitle("绑定银行卡", _mActivity);
        toolBar.setOnToolBarClickLisenear(this);
    }

    @Override
    public BasePresenter initPresenter() {
        return meNewAuthenticationPresenter = new MeScanBankPresenter(this);
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
    }

    @Override
    public void onHttpError(String type) {
        dismiss();
        Toast.makeText(_mActivity, type, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dispose();
        ButterKnife.unbind(this);
    }

    @Override
    public void getBank(Bank bank) {
        UserInfo userInfo = MyApplication.getUserInfo();
        userInfo.setIsBindCard(1);
        userInfo.setCardNumber(bank.getCardNumber());
        UserInfoManager.updateUserInfo(_mActivity,userInfo);

        MainFragment mainFragment = findFragment(MainFragment.class);
        MeFragment meFragment = mainFragment.findChildFragment(MeFragment.class);
        meFragment.updateIdcard();

        OneImageOneBtDialog oneImageOneBtDialog = new OneImageOneBtDialog(_mActivity);
        oneImageOneBtDialog.setGrayCenter();
        oneImageOneBtDialog.setType(POP_ME_AUTHENTICATION);
        oneImageOneBtDialog.setAboveText("您已成功绑定");

        oneImageOneBtDialog.setBlewText(bank.getBankName()+bank.getCardType());
        oneImageOneBtDialog.setBlewTextColor(getContext().getResources().getColor(R.color.main_title_text_color));
//        oneImageOneBtDialog.setBlewTextSize(getContext().getResources().getDimension(R.dimen.sp_19));
//        if(mUserName.equals(meAuthenticationNameEt.getText().toString())){
//            oneImageOneBtDialog.setBlewText("认证姓名与用户名一致");
//        }else{
//            oneImageOneBtDialog.setBlewText("认证姓名与用户名不一致 系统将自动更改用户名");
//            oneImageOneBtDialog.setBlewTextColor(getContext().getResources().getColor(R.color.authentication_success_text));
//        }
        oneImageOneBtDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Bundle bundle = getArguments();
                Object object = bundle.get(BaseFragment.TYPEFRAGMENT);
                if(bundle!=null&&object!=null){
                    int type = (int)object;
                    if(type==BaseFragment.TOBANKTYPE) pop();
                }else{
                    MeScanBankFragment.this.popTo(MainFragment.class, false);
                }
            }
        });
        oneImageOneBtDialog.show();
//        MeScanBankInfoFragment fragment_zc_rangking = MeScanBankInfoFragment.newInstance();
//        fragment_zc_rangking.getArguments().putString("bankName", bank.getBankName());
//        fragment_zc_rangking.getArguments().putString("cardType", bank.getCardType());
//        fragment_zc_rangking.getArguments().putString("cardNumber", bank.getCardNumber());
//        fragment_zc_rangking.getArguments().putString("bankCode", bank.getBankCode());
//        start(fragment_zc_rangking);
    }

    @Override
    public void getIsBindCard(int isBindCard) {

    }

    @OnClick({R.id.bank_seletor_box, R.id.authem_new_next,R.id.tv_bindcard})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bank_seletor_box:
                if (bankSeletorBox.isSelected()) {
                    bankSeletorBox.setSelected(false);
                    ButtonUtil.setOnClickFalse(authemNewNext);
                } else {
                    bankSeletorBox.setSelected(true);
                    if(ValidatorUtil.isBankIDCard(authemNewEt.getText().toString().trim())
                            && bankSeletorBox.isSelected()){
                        ButtonUtil.setOnClickTrue(authemNewNext);
                    }
                }
                break;
            case R.id.authem_new_next:
                //添加银行卡请求
                getLoadingPop("正在提交").show();
                HttpParams params = new HttpParams();
                params.put("cardNumber", authemNewEt.getText().toString().trim());
                String url = AddrConst.SERVER_ADDRESS_USER + AddrConst.ADDRESS_SAVE_BINDCARD;
                addDisposable(meNewAuthenticationPresenter.submitInfotmation( url , params, "1"));
                break;
            case R.id.tv_bindcard:
                //打开用户协议
                ZhiZongWebFragment fragment1 = ZhiZongWebFragment.newInstance();
                fragment1.getArguments().putString("link", AddrConst.SERVER_ADDRESS_NEWS+ "/protocol/bindbankcard");
                start(fragment1);
                break;
        }
    }
}
