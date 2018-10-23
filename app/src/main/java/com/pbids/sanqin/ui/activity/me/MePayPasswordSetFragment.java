package com.pbids.sanqin.ui.activity.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.lzy.okgo.model.HttpParams;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BaseFragment;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.model.db.UserInfoManager;
import com.pbids.sanqin.model.entity.UserInfo;
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
 * @desscribe 类描述:支付密码设置-设置支付密码界面
 * @remark 备注:
 * @see
 */
public class MePayPasswordSetFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear,MePayPasswordView {

    @Bind(R.id.pay_setpassword1)
    PayPsdInputView paySetpassword1;
    @Bind(R.id.pay_setpassword2)
    PayPsdInputView paySetpassword2;
    @Bind(R.id.pay_setpassword_bt)
    Button paySetpasswordBt;

    MePayPasswordPresenter mePayPasswordPresenter;
    String type ="";
    String jump ="";

    public static MePayPasswordSetFragment newInstance() {
        MePayPasswordSetFragment fragment = new MePayPasswordSetFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_paypassword_set, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        paySetpassword1.setonPasswordListener(new PayPsdInputView.onPasswordListener() {
            @Override
            public void onOver(String password) {
                paySetpassword2.requestFocus();
            }
        });
        paySetpassword2.setonPasswordListener(new PayPsdInputView.onPasswordListener() {
            @Override
            public void onOver(String password) {

            }
        });
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        type = getArguments().getString("type");
        jump = getArguments().getString("jump");
        toolBar.setOnToolBarClickLisenear(this);
        if("revise".equals(type)){
            toolBar.setLeftArrowCenterTextTitle("修改支付密码", _mActivity);
        }else if("set".equals(type)){
            toolBar.setLeftArrowCenterTextTitle("支付设置", _mActivity);
        }
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
        dispose();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.pay_setpassword_bt)
    public void onViewClicked() {
        String passwrod1 = paySetpassword1.getText().toString().trim();
        String passwrod2 =paySetpassword2.getText().toString().trim();
        Log.i("wzh","passwrod1.length():"+passwrod1.length());
        if("".equals(passwrod1)){
            Toast.makeText(_mActivity,"第一次填写不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if(passwrod1.length()!=6){
            Toast.makeText(_mActivity,"第一次填写未完成",Toast.LENGTH_SHORT).show();
            return;
        }
        if("".equals(passwrod2)){
            Toast.makeText(_mActivity,"第二次填写不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if(passwrod2.length()!=6){
            Toast.makeText(_mActivity,"第二次填写未完成",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!passwrod1.equals(passwrod2)){
            Toast.makeText(_mActivity,"两次输入的密码不一致",Toast.LENGTH_SHORT).show();
            return;
        }
        getLoadingPop("正在提交").show();
        HttpParams params = new HttpParams();
        params.put("payword",passwrod1);
        addDisposable(mePayPasswordPresenter.submitInfotmation(AddrConst.SERVER_ADDRESS_PAYMENT + AddrConst.ADDRESS_UPDATE_PAYWORD,params,"1"));
    }

    @Override
    public void onHttpSuccess(String type) {
        dismiss();
        if("revise".equals(type)){
            Toast.makeText(_mActivity,"密码修改成功",Toast.LENGTH_SHORT).show();
        }else if("set".equals(type)){
            Toast.makeText(_mActivity,"密码设置成功",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onHttpError(String type) {
        dismiss();
        Toast.makeText(_mActivity,type,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onIsSetPayword(int isSetPayword) {
        UserInfo userInfo = MyApplication.getUserInfo();
        userInfo.setIsSetPayword(1);
        UserInfoManager.updateUserInfo(_mActivity,userInfo);
        if(null!=findFragment(MePayPasswordSetOverFragment.class) || "1".equals(jump)){
            BaseFragment preFragment = (BaseFragment) getPreFragment();
            if(preFragment.getOnPayPasswordSetLisenear()!=null){
                Log.i("wzh","preFragment.getOnPayPasswordSetLisenear()!=null");
                preFragment.getOnPayPasswordSetLisenear().setOver();
            }
            pop();
        }else{
//        start(MePayPasswordSetOverFragment.newInstance(),SINGLETOP);
            startWithPop(MePayPasswordSetOverFragment.newInstance());
        }
    }
}
