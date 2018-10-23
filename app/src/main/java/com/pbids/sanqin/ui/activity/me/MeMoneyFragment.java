package com.pbids.sanqin.ui.activity.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pbids.sanqin.R;
import com.pbids.sanqin.base.BaaseToolBarFragment;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.presenter.MeMoneyPresenter;
import com.pbids.sanqin.ui.activity.common.SMSVerfyFragment;
import com.pbids.sanqin.ui.view.AppToolBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author 巫哲豪
 * @date on 2018/3/2 14:38
 * @desscribe 类描述:我的资金界面（提现界面）
 * @remark 备注:
 * @see
 */
public class MeMoneyFragment extends BaaseToolBarFragment<MeMoneyPresenter> implements AppToolBar.OnToolBarClickLisenear,MeMoneyView{

    public static final int ME_MONEY_REQUEST_CODE = 20311;

    @Bind(R.id.me_money_cash_text)
    TextView meMoneyCashText;
    @Bind(R.id.me_money_cash_et)
    EditText meMoneyCashEt;
    @Bind(R.id.me_money_cash_line)
    View meMoneyCashLine;
    @Bind(R.id.me_money_cash_all)
    TextView meMoneyCashAll;
    @Bind(R.id.me_money_cash_bt)
    Button meMoneyCashBt;
    @Bind(R.id.me_money_hint)
    TextView meMoneyHint;

    float balance = -1;
    int fee = -1;
    int minAmount = -1;
    int withdrawalThreshold = 0;
    MeMoneyPresenter meMoneyPresenter;

    public static MeMoneyFragment newInstance() {
        MeMoneyFragment fragment = new MeMoneyFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.me_money, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftArrowCenterTextTitleRightText("我的资金","流水",_mActivity);
    }

    public void initView(){
        balance = getArguments().getFloat("balance");
        fee = getArguments().getInt("fee");
        minAmount = getArguments().getInt("minAmount");
        meMoneyCashEt.setHint("可提现" + String.format("%.2f", balance) + "元");
        meMoneyHint.setText("每次提现金需要收取服务费"+fee+"元"+",最低的提现额度为"+minAmount+"元");
        withdrawalThreshold = getArguments().getInt("withdrawalThreshold");
        //侦听文字改变
        meMoneyCashEt.addTextChangedListener(textWatcher);
//        //限制输入类型
//        meMoneyCashEt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    }
    //侦听文字改变
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            //总提现金额
            float total = balance;
            if (total < 0) {
                total = 0;
            }
            String input = meMoneyCashEt.getText().toString();
            //不能超出总金额
            try {
                if (meMoneyCashEt != null && input != null && Float.valueOf(input) > total) {
//                    Toast.makeText(_mActivity, "申请提现需要收取"+fee+"元手续费,您本次的最高提现金额为("+String.format("%.2f", total)+")元.", Toast.LENGTH_SHORT).show();
                    meMoneyCashEt.setText(String.format("%.2f", total));
                    return;
                }
            } catch (Exception e) {

            }
        }
    };

    @Override
    public MeMoneyPresenter initPresenter() {
        return  meMoneyPresenter = new MeMoneyPresenter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.me_money_cash_bt,R.id.me_money_cash_all})
    public void onViewClicked(View view) {
        //总提现金额
//        float total = balance - 2;
        float total = balance;
        if (total < 0) {
            total = 0;
        }
        switch (view.getId()) {
            case R.id.me_money_cash_bt:
                //确认提现
                if("".equals(meMoneyCashEt.getText().toString())){
                    Toast.makeText(_mActivity,"请输入提现金额",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Float.valueOf(meMoneyCashEt.getText().toString()) < minAmount) {
                    Toast.makeText(_mActivity, "最低的提现额度为" + minAmount + "元", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ("".equals(MyApplication.getUserInfo().getPhone())) {
                    Toast.makeText(_mActivity, "请先绑定手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (total < 0) {
                    total = 0;
                }
                if (Float.valueOf(meMoneyCashEt.getText().toString()) > total) {
                    Toast.makeText(_mActivity, "您提取的金额超出了范围,请重新输入", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(_mActivity, "申请提现需要收取"+fee+"元手续费,您本次的最高提现金额为("+String.format("%.2f", total)+")元,请修改.", Toast.LENGTH_SHORT).show();
                    //meMoneyCashEt.setText("");
                    return;
                }
                if(Float.valueOf(meMoneyCashEt.getText().toString()) > withdrawalThreshold){
                    showToast("提现金额超过最大提现金额");
                    return;
                }
                //跳转到验证码界面
//                MeBindingVerfyCodeFragment fragment = MeBindingVerfyCodeFragment.newInstance();
//                fragment.getArguments().putString("key",MeBindingVerfyCodeFragment.WITHDRAWLS);
//                fragment.getArguments().putFloat("balance",Float.valueOf(meMoneyCashEt.getText().toString().trim()));
//                start(fragment);
                //判断是否在提现时间
//                meMoneyPresenter.isWithDrawTime(Float.valueOf(meMoneyCashEt.getText().toString().trim()));
                SMSVerfyFragment fragment =SMSVerfyFragment.newInstance();
                fragment.getArguments().putString("key",MeBindingVerfyCodeFragment.WITHDRAWLS);
                fragment.getArguments().putFloat("balance",Float.valueOf(meMoneyCashEt.getText().toString().trim()));
                startForResult(fragment,ME_MONEY_REQUEST_CODE);
                break;

            case R.id.me_money_cash_all:
                //全部转出
                //meMoneyCashEt.setText(String.format("%.2f", MyApplication.getUserInfo().getAccountBalance()));

//                Toast.makeText(_mActivity, "申请提现需要收取"+fee+"元手续费,您本次的最高提现金额为("+String.format("%.2f", total)+")元", Toast.LENGTH_SHORT).show();
                meMoneyCashEt.setText(String.format("%.2f", total));
                break;
        }
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == ME_MONEY_REQUEST_CODE && data != null) {
            int con = data.getInt("con");
            if (con == 1) {
                //支付完成后要结束
                setFragmentResult(MeMoneyListFragment.ME_MONEY_LIST_REQUEST_CODE,data);
                pop();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_left_layout:
                //返回
                hideSoftInput();
                pop();
                break;
            case R.id.main_right_layout:
                //查看资金流水
                hideSoftInput(); //收起键盘
                start(MeAccountsFragment.newInstance());
                break;
        }
    }

    @Override
    public void allowWithDraw() {
        SMSVerfyFragment fragment =SMSVerfyFragment.newInstance();
        fragment.getArguments().putString("key",MeBindingVerfyCodeFragment.WITHDRAWLS);
        fragment.getArguments().putFloat("balance",Float.valueOf(meMoneyCashEt.getText().toString().trim()));
        startForResult(fragment,ME_MONEY_REQUEST_CODE);
    }
}