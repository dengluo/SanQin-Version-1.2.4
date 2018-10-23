package com.pbids.sanqin.ui.activity.zongquan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pbids.sanqin.R;
import com.pbids.sanqin.base.BaaseToolBarFragment;
import com.pbids.sanqin.presenter.SayHelloPresenter;
import com.pbids.sanqin.ui.view.AppToolBar;

import butterknife.ButterKnife;

public class SayHelloFragment extends BaaseToolBarFragment <SayHelloPresenter> implements AppToolBar.OnToolBarClickLisenear, SayHelloView {

    public static SayHelloFragment instance() {
        SayHelloFragment fragment = new SayHelloFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_say_hello, container, false);
        initView();
        return view;
    }

    private void initView() {
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setLeftArrowCenterTextTitle("跟我打招呼的人", _mActivity);
        toolBar.setOnToolBarClickLisenear(this);
    }

    @Override
    protected SayHelloPresenter initPresenter() {
        return new SayHelloPresenter();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_left_layout:
                //返回
                pop();
                break;
        }
    }
}
