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
import android.widget.Toast;

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
 * @author 巫哲豪
 * @date on 2018/3/2 14:43
 * @desscribe 类描述:我的个人信息-修改名字界面
 * @remark 备注:
 * @see
 */
public class MeReviseNameFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear,MePageView{
    //requestCode
    public static final int REQUEST_CODE = 701;
    public static final String BUNDLE_KEY = "701";
//    @Bind(R.id.me_title_left_layout)
//    View meTitleLeftLayout;
//    @Bind(R.id.me_title_left_text)
//    TextView meTitleLeftText;
//    @Bind(R.id.me_title_text)
//    TextView meTitleText;
    @Bind(R.id.me_revise_name_et)
    EditText meReviseNameEt;
    @Bind(R.id.me_revise_name_iv)
    ImageView meReviseNameIv;
    @Bind(R.id.me_revise_name_bt)
    Button meReviseNameBt;
    UserInformationPresenter mePresenter;

    public static MeReviseNameFragment newInstance() {
        MeReviseNameFragment fragment = new MeReviseNameFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.me_revise_name, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftTextCenterTextTitle("取消","我的名字",_mActivity);
    }

    public void initView(){
//        getArguments().getString("username");
        Log.i("wzh","getArguments().getString(\"username\"): "+getArguments().getString("username"));
        meReviseNameEt.setText(getArguments().getString("username"));
        meReviseNameBt.setClickable(false);
        meReviseNameBt.setBackgroundResource(R.drawable.selector_app_cancel);

        meReviseNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if("".equals(s.toString())){
                    meReviseNameBt.setBackgroundResource(R.drawable.selector_app_cancel);
                    meReviseNameBt.setClickable(false);
                }
                if(!meReviseNameBt.isClickable() && !"".equals(s.toString())){
                    meReviseNameBt.setBackgroundResource(R.drawable.selector_app_comfirm);
                    meReviseNameBt.setClickable(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        meReviseNameIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meReviseNameEt.setText("");
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
//        hideSoftInput();
    }

    @OnClick({R.id.me_revise_name_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
//            case R.id.me_title_left_layout:
//                pop();
//                break;
            case R.id.me_revise_name_bt:
//                mePresenter.reviseUserName(MyApplication.getUserInfo().getName(),MyApplication.getUserInfo().getId());
                hideSoftInput();
                HttpParams params = new HttpParams();
                params.put("userId",MyApplication.getUserInfo().getUserId());
                params.put("name",meReviseNameEt.getText().toString().trim());
                mePresenter.reviseUserInformation(params,MePageView.ME_REVICE_NAME);
                break;
        }
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
    public void reviseSuccess(String type) {
        MainFragment mainFragment = findFragment(MainFragment.class);
        MeFragment meFragment = mainFragment.findChildFragment(MeFragment.class);
        meFragment.updateName();

        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY,meReviseNameEt.getText().toString().trim());
        setFragmentResult(REQUEST_CODE,bundle);
        pop();
    }

    @Override
    public void reviseError(String type) {
        Toast.makeText(_mActivity,type,Toast.LENGTH_SHORT).show();
    }
}
