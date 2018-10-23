package com.pbids.sanqin.ui.activity.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.CommonUtil;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.ui.view.AppToolBar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by pbids903 on 2018/1/31.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 14:15
 * @desscribe 类描述:实名认证结束，显示界面
 * @remark 备注:
 * @see
 */
public class MeAuthenticationOverFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear {

    @Bind(R.id.name_tv)
    TextView nameTv;
    @Bind(R.id.idcard_tv)
    TextView idcardTv;
    @Bind(R.id.r1)
    RelativeLayout r1;

    public static MeAuthenticationOverFragment newInstance() {
        MeAuthenticationOverFragment fragment = new MeAuthenticationOverFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me_authentication_over, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        nameTv.setText(MyApplication.getUserInfo().getName());
        idcardTv.setText(CommonUtil.shieldIdNumberSpace(MyApplication.getUserInfo().getIdNumber()));
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftArrowCenterTextTitle("实名认证", _mActivity);
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

    public void clear(){
        r1.setBackground(null);
        r1 = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        clear();
        ButterKnife.unbind(this);
    }

}
