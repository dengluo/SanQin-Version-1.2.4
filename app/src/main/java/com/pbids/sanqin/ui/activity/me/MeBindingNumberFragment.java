package com.pbids.sanqin.ui.activity.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.OnDialogClickListener;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.ui.activity.zhizong.ZhiZongWebFragment;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.utils.AddrConst;
import com.pbids.sanqin.utils.ValidatorUtil;
import com.pbids.sanqin.utils.ButtonUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pbids903 on 2017/11/29.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 14:16
 * @desscribe 类描述:发送验证码界面
 * @remark 备注:
 * @see
 */
public class MeBindingNumberFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear {

    String key="";

    @Bind(R.id.me_binding_text)
    TextView meBindingText;
    @Bind(R.id.me_binding_bt)
    Button meBindingBt;
    @Bind(R.id.me_binding_seletor_box)
    ImageView meBindingSeletorBox;
    @Bind(R.id.me_binding_et)
    EditText meBindingEt;
    @Bind(R.id.tv_bind_phone_protocol)
    TextView tvBindProtocol;

    public static MeBindingNumberFragment newInstance() {
        MeBindingNumberFragment meBindingNumber = new MeBindingNumberFragment();
        Bundle bundle = new Bundle();
        meBindingNumber.setArguments(bundle);
        return meBindingNumber;
    }

    public static MeBindingNumberFragment newInstance(int type) {
        MeBindingNumberFragment meBindingNumber = new MeBindingNumberFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPEFRAGMENT,type);
        meBindingNumber.setArguments(bundle);
        return meBindingNumber;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.me_binding_number, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftArrowCenterTextTitle("绑定手机号",_mActivity);
    }

    public void initView() {
        key = getArguments().getString("key","");
        ButtonUtil.setOnClickFalse(meBindingBt);
        meBindingEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(ValidatorUtil.isMobile(meBindingEt.getText().toString()) && meBindingSeletorBox.isSelected()){
                    ButtonUtil.setOnClickTrue(meBindingBt);
                }else{
                    ButtonUtil.setOnClickFalse(meBindingBt);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        meBindingEt.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        meBindingSeletorBox.setSelected(true);
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public boolean onBackPressedSupport() {
        return super.onBackPressedSupport();
    }

    @OnClick({R.id.me_binding_seletor_box,R.id.me_binding_bt,R.id.tv_bind_phone_protocol})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.tv_bind_phone_protocol:
                //查看华亲池协议
                ZhiZongWebFragment webFragment = ZhiZongWebFragment.newInstance();
                webFragment.getArguments().putString("link", AddrConst.SERVER_ADDRESS_NEWS+ "/protocol/bindphone");
                start(webFragment);
                break;

            case R.id.me_binding_seletor_box:
                //
                if (meBindingSeletorBox.isSelected()) {
                    meBindingSeletorBox.setSelected(false);
                    ButtonUtil.setOnClickFalse(meBindingBt);
                } else {
                    meBindingSeletorBox.setSelected(true);
                    if(ValidatorUtil.isMobile(meBindingEt.getText().toString()) && meBindingSeletorBox.isSelected()){
                        ButtonUtil.setOnClickTrue(meBindingBt);
                    }
                }
                break;

            case R.id.me_binding_bt:
                //绑定提交
                int type = getArguments().getInt(TYPEFRAGMENT);
                MeBindingVerfyCodeFragment fragment = MeBindingVerfyCodeFragment.newInstance(type);
                fragment.getArguments().putString("phone",meBindingEt.getText().toString().trim());
                if(MeBindingVerfyCodeFragment.BINDING_PHONE_NEW.equals(key)){
                    fragment.getArguments().putString("key",MeBindingVerfyCodeFragment.BINDING_PHONE_NEW);
                }else{
                    fragment.getArguments().putString("key",MeBindingVerfyCodeFragment.BINDING_PHONE_FIRST);
                }
                start(fragment);
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
}
