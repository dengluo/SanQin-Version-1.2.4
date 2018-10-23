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
import android.widget.Toast;

import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.presenter.MeCooperationPresenter;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.utils.AddrConst;
import com.pbids.sanqin.utils.ButtonUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by pbids903 on 2017/12/14.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 14:20
 * @desscribe 类描述:商务合作界面
 * @remark 备注:
 * @see
 */
public class MeCooperationFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear,MeCooperationView{

//    @Bind(R.id.me_title_left_layout)
//    View meTitleLeftLayout;
//    @Bind(R.id.me_title_text)
//    TextView meTitleText;
    @Bind(R.id.me_cooperation_name_et)
    EditText meCooperationNameEt;
    @Bind(R.id.me_cooperation_phone_et)
    EditText meCooperationPhoneEt;
    @Bind(R.id.me_cooperation_event_et)
    EditText meCooperationEventEt;
    @Bind(R.id.me_cooperation_bt)
    Button meCooperationBt;
    MeCooperationPresenter meCooperationPresenter;

    public static MeCooperationFragment newInstance() {
        MeCooperationFragment fragment = new MeCooperationFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view_donate_records = inflater.inflate(R.layout.fragment_me_cooperation, container, false);
//        ButterKnife.bind(this, view_donate_records);
//        initView();
//        return view_donate_records;
//    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me_cooperation, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftArrowCenterTextTitle("商务合作",_mActivity);
    }

    public void initView(){
//        meTitleText.setText("商务合作");
        ButtonUtil.setOnClickFalse(meCooperationBt);
        meCooperationNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buttonStatus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        meCooperationPhoneEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buttonStatus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        meCooperationEventEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buttonStatus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public BasePresenter initPresenter() {
        return meCooperationPresenter = new MeCooperationPresenter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dispose();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.me_cooperation_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.me_cooperation_bt:
                hideSoftInput();
                HttpParams params = new HttpParams();
                params.put("userId", MyApplication.getUserInfo().getUserId());
                params.put("name",meCooperationNameEt.getText().toString().trim());
                params.put("phone",meCooperationPhoneEt.getText().toString().trim());
                params.put("content",meCooperationEventEt.getText().toString().trim());
                getLoadingPop("正在提交").show();
                DisposableObserver<Response<String>> observer = meCooperationPresenter.submitInformation(
                        AddrConst.SERVER_ADDRESS_USER + AddrConst.ADDRESS_ME_BUSINESS_COOPERATION,params);
                if(observer!=null){
                    addDisposable(observer);
                }
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
    public void submitInformation() {

    }

    public void buttonStatus(){
         if(meCooperationNameEt.getText().toString().trim().equals("")
                 || meCooperationPhoneEt.getText().toString().trim().equals("")
                 || meCooperationEventEt.getText().toString().trim().equals("")){
             ButtonUtil.setOnClickFalse(meCooperationBt);
         }else {
             ButtonUtil.setOnClickTrue(meCooperationBt);
         }
    }

    @Override
    public void onHttpSuccess(String type) {
        getLoadingPop(null).dismiss();
        pop();
    }

    @Override
    public void onHttpError(String type) {
        getLoadingPop(null).dismiss();
        Toast.makeText(_mActivity,type,Toast.LENGTH_SHORT).show();
    }
}
