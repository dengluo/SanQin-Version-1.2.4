package com.pbids.sanqin.ui.activity.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.common.OnDialogClickListener;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.ui.view.dialog.OneTextTwoBtDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author 巫哲豪
 * @date on 2018/3/7 9:43
 * @desscribe 类描述:
 * @remark 备注:
 * @see
 */

public class MeBindingPhoneOverFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear {

    OneTextTwoBtDialog twoBtDialog;

    @Bind(R.id.phone_over_tv)
    TextView phoneOverTv;
    @Bind(R.id.phone_over_bt)
    Button phoneOverBt;

    public static MeBindingPhoneOverFragment newInstance() {
        MeBindingPhoneOverFragment fragment = new MeBindingPhoneOverFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_binding_phone_over, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        phoneOverTv.setText(MyApplication.getUserInfo().getPhone());
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftArrowCenterTextTitle("绑定手机号", _mActivity);
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

    @OnClick(R.id.phone_over_bt)
    public void onViewClicked() {
        if(twoBtDialog==null){
            twoBtDialog = new OneTextTwoBtDialog(_mActivity);
            twoBtDialog.setContentText("是否更换这个手机号?");
            twoBtDialog.setOnDialogClickLisenrar(new OnDialogClickListener() {
                @Override
                public void confirm(View view) {
                    twoBtDialog.dismiss();
//                    start(MeBindingNumberFragment.newInstance());
                    MeBindingVerfyCodeFragment fragment = MeBindingVerfyCodeFragment.newInstance();
                    fragment.getArguments().putString("key",MeBindingVerfyCodeFragment.VERIFICATION_PHONE_NUMBER);
                    start(fragment);
                }

                @Override
                public void cancel(View view) {
                    twoBtDialog.dismiss();
                }
            });
        }
        twoBtDialog.show();
    }
}