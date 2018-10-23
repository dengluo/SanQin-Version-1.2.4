package com.pbids.sanqin.ui.activity.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.model.db.UserInfoManager;
import com.pbids.sanqin.model.entity.Bank;
import com.pbids.sanqin.model.entity.UserInfo;
import com.pbids.sanqin.presenter.MeScanBankPresenter;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.utils.AddrConst;
import com.pbids.sanqin.utils.ValidatorUtil;
import com.pbids.sanqin.utils.ButtonUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pbids903 on 2018/1/20.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 14:05
 * @desscribe 类描述:我的界面绑定银行卡（过时）
 * @remark 备注:
 * @see
 */
public class MeScanBankInfoFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear,MeScanBankView {


    @Bind(R.id.bank_scan_bankname)
    TextView bankScanBankname;
    @Bind(R.id.bank_scan_username)
    EditText bankScanUsername;
    @Bind(R.id.bank_scan_idcard)
    EditText bankScanIdcard;
    @Bind(R.id.bank_scan_phone)
    EditText bankScanPhone;
    @Bind(R.id.bank_seletor_box)
    ImageView bankSeletorBox;
    @Bind(R.id.authem_new_next)
    Button authemNewNext;

    MeScanBankPresenter meNewAuthenticationPresenter;
    String bankName="";
    String cardNumber="";
    String cardType="";
    String bankCode="";

    public static MeScanBankInfoFragment newInstance() {
        MeScanBankInfoFragment fragment = new MeScanBankInfoFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan_bankinfo, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        bankName = getArguments().getString("bankName");
        cardNumber = getArguments().getString("cardNumber");
        cardType = getArguments().getString("cardType");
        bankCode = getArguments().getString("bankCode");

        bankScanBankname.setText(bankName+" "+cardType);

        bankSeletorBox.setSelected(true);
        ButtonUtil.setOnClickFalse(authemNewNext);
        bankScanUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setButtonStatus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        bankScanIdcard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setButtonStatus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        bankScanPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setButtonStatus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setButtonStatus(){
        if(checkInfo() && bankSeletorBox.isSelected()){
            ButtonUtil.setOnClickTrue(authemNewNext);
        }else{
            ButtonUtil.setOnClickFalse(authemNewNext);
        }
    }

    private boolean checkInfo() {
        boolean isUsername = ValidatorUtil.isChineseName(bankScanUsername.getText().toString().trim());
        boolean isIdcard = ValidatorUtil.isIDCard(bankScanIdcard.getText().toString().trim());
        boolean isPhone = ValidatorUtil.isMobile(bankScanPhone.getText().toString().trim());
        Log.i("wzw","bankScanUsername.getText().toString():"+bankScanUsername.getText().toString());
        Log.i("wzw","isUsername:"+isUsername);
        Log.i("wzw","isIdcard:"+isIdcard);
        Log.i("wzw","isPhone:"+isPhone);
        if (isUsername && isIdcard && isPhone) {
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setLeftArrowCenterTextTitle("填写银行卡信息", _mActivity);
        toolBar.setOnToolBarClickLisenear(this);
    }

    @Override
    public BasePresenter initPresenter() {
        return meNewAuthenticationPresenter = new MeScanBankPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_left_layout:
                pop();
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dispose();
        ButterKnife.unbind(this);
    }


    @OnClick({R.id.bank_seletor_box, R.id.authem_new_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bank_seletor_box:
                if (bankSeletorBox.isSelected()) {
                    bankSeletorBox.setSelected(false);
                    ButtonUtil.setOnClickFalse(authemNewNext);
                } else {
                    bankSeletorBox.setSelected(true);
                    if(checkInfo() && bankSeletorBox.isSelected()){
                        ButtonUtil.setOnClickTrue(authemNewNext);
                    }
                }
                break;
            case R.id.authem_new_next:
                getLoadingPop("正在提交").show();
                HttpParams params = new HttpParams();
                params.put("name",bankScanUsername.getText().toString().trim());
                params.put("phone",bankScanPhone.getText().toString().trim());
                params.put("cardNumber",cardNumber);
                params.put("idNumber",bankScanIdcard.getText().toString().trim());
                params.put("cardType",cardType);
                params.put("bankCode",bankCode);
                params.put("bankName",bankName);
                addDisposable(meNewAuthenticationPresenter.submitInfotmation(
                        AddrConst.SERVER_ADDRESS_USER + AddrConst.ADDRESS_SAVE_BINDCARD,params,"2"));
//                start();
                break;
        }
    }

    @Override
    public void onHttpSuccess(String type) {
        dismiss();
        Toast.makeText(_mActivity,type,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onHttpError(String type) {
        dismiss();
        Toast.makeText(_mActivity,type,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getBank(Bank bank) {

    }

    @Override
    public void getIsBindCard(int isBindCard) {
        UserInfo userInfo = MyApplication.getUserInfo();
        userInfo.setIsBindCard(isBindCard);
        UserInfoManager.updateUserInfo(_mActivity,userInfo);

        MeScanBankFragment meScanBankFragment = findFragment(MeScanBankFragment.class);
        meScanBankFragment.pop();
        pop();
    }
}
