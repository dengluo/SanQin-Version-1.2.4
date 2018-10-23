package com.pbids.sanqin.ui.activity.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.ui.view.AppToolBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pbids903 on 2018/1/22.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 14:41
 * @desscribe 类描述:支付密码设置-修改支付密码首页
 * @remark 备注:
 * @see
 */
public class MePayPasswordSetOverFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear {


    @Bind(R.id.pay_setpassword_bt)
    Button paySetpasswordBt;

    public static MePayPasswordSetOverFragment newInstance() {
        MePayPasswordSetOverFragment fragment = new MePayPasswordSetOverFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_paypassword_setover, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {

    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftArrowCenterTextTitle("支付设置", _mActivity);
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
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
        start(MePayPasswordReviseFirstFragment.newInstance(),SINGLETOP);
    }
}
