package com.pbids.sanqin.ui.activity.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.lzy.okgo.model.HttpParams;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.presenter.MePayPasswordPresenter;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.ui.view.pay.PayPsdInputView;
import com.pbids.sanqin.utils.AddrConst;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pbids903 on 2018/1/22.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 14:39
 * @desscribe 类描述:修改支付密码-输入旧的密码界面
 * @remark 备注:
 * @see
 */
public class MePayPasswordReviseFirstFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear,MePayPasswordView {


    @Bind(R.id.pay_revisepassword)
    PayPsdInputView payRevisepassword;
    @Bind(R.id.pay_setpassword_bt)
    Button paySetpasswordBt;

    MePayPasswordPresenter mePayPasswordPresenter;

    public static MePayPasswordReviseFirstFragment newInstance() {
        MePayPasswordReviseFirstFragment fragment = new MePayPasswordReviseFirstFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_paypassword_revise1, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        payRevisepassword.setonPasswordListener(new PayPsdInputView.onPasswordListener() {
            @Override
            public void onOver(String password) {

            }
        });
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftArrowCenterTextTitle("修改支付密码", _mActivity);
    }

    @Override
    public BasePresenter initPresenter() {
        return mePayPasswordPresenter = new MePayPasswordPresenter(this);
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
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.pay_setpassword_bt)
    public void onViewClicked() {
        String password = payRevisepassword.getText().toString().trim();
        if("".equals(password)){
            Toast.makeText(_mActivity,"密码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.length()!=6){
            Toast.makeText(_mActivity,"密码填写未完成",Toast.LENGTH_SHORT).show();
            return;
        }
        getLoadingPop("正在提交").show();
        HttpParams params = new HttpParams();
        params.put("payword",password);
        addDisposable(mePayPasswordPresenter.submitInfotmation(AddrConst.SERVER_ADDRESS_PAYMENT + AddrConst.ADDRESS_CHECK_PAYWORD,params,"2"));
    }

    @Override
    public void onHttpSuccess(String type) {
        dismiss();
        if("2".equals(type)){
            MePayPasswordSetFragment fragment = MePayPasswordSetFragment.newInstance();
            fragment.getArguments().putString("type","revise");
            startWithPop(fragment);
        }
    }

    @Override
    public void onHttpError(String type) {
        dismiss();
        Toast.makeText(_mActivity,type,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onIsSetPayword(int isSetPayword) {

    }
}
