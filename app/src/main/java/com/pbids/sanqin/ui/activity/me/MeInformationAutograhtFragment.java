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

import com.lzy.okgo.model.HttpParams;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.presenter.UserInformationPresenter;
import com.pbids.sanqin.ui.activity.MainFragment;
import com.pbids.sanqin.ui.view.AppToolBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pbids903 on 2017/11/29.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 14:25
 * @desscribe 类描述:我的个人信息-个性签名界面
 * @remark 备注:
 * @see
 */
public class MeInformationAutograhtFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear,MePageView{

//    @Bind(R.id.me_title_left_layout)
//    View meTitleLeftLayout;
//    @Bind(R.id.me_title_left_text)
//    TextView meTitleLeftText;
//    @Bind(R.id.me_title_text)
//    TextView meTitleText;
    @Bind(R.id.me_information_autopragh_et)
    EditText meInformationAutopraghEt;
    @Bind(R.id.me_information_autopragh_tv)
    TextView meInformationAutopraghTv;
    @Bind(R.id.me_information_autopragh_bt)
    Button meInformationAutopraghBt;

    UserInformationPresenter mePresenter;

    public static MeInformationAutograhtFragment newInstance() {
        MeInformationAutograhtFragment fragment = new MeInformationAutograhtFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.me_information_autograht, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftTextCenterTextTitle("取消","我的个性签名",_mActivity);
    }

    public void initView(){
        meInformationAutopraghBt.setClickable(false);
        meInformationAutopraghBt.setBackgroundResource(R.drawable.selector_app_cancel);
//        meTitleText.setText("我的个性签名");

        meInformationAutopraghEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                meInformationAutopraghTv.setText(""+s.length()+"/25");
                if(!meInformationAutopraghBt.isClickable()){
                    meInformationAutopraghBt.setClickable(true);
                    meInformationAutopraghBt.setBackgroundResource(R.drawable.selector_app_comfirm);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public BasePresenter initPresenter() {
        return mePresenter = new UserInformationPresenter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.me_information_autopragh_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
//            case R.id.me_title_left_layout:
//                pop();
//                break;
            case R.id.me_information_autopragh_bt:
                hideSoftInput();
                HttpParams params = new HttpParams();
                params.put("userId",MyApplication.getUserInfo().getUserId());
                params.put("signature",meInformationAutopraghEt.getText().toString());
                mePresenter.reviseUserInformation(params,ME_REVICE_AUTOGRAHT);
                break;
        }
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
    public void reviseSuccess(String type) {
        MainFragment mainFragment = findFragment(MainFragment.class);
        MeFragment meFragment = mainFragment.findChildFragment(MeFragment.class);
        meFragment.updateUserHobby();

        MeInformationFragment fragment = findFragment(MeInformationFragment.class);
        fragment.updateAutograph();
        pop();
    }

    @Override
    public void reviseError(String type) {

    }
}
