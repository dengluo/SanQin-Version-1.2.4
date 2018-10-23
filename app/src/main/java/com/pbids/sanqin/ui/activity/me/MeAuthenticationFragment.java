package com.pbids.sanqin.ui.activity.me;

import android.content.DialogInterface;
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
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.common.OnDialogClickListener;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.model.db.UserInfoManager;
import com.pbids.sanqin.model.entity.UserInfo;
import com.pbids.sanqin.presenter.MeAuthenticationPresenter;
import com.pbids.sanqin.ui.activity.MainFragment;
import com.pbids.sanqin.ui.activity.zongquan.ZhiZongCasesCrowdFragment;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.ui.view.dialog.OneImageOneBtDialog;
import com.pbids.sanqin.ui.view.dialog.OneTextTwoBtDialog;
import com.pbids.sanqin.utils.AddrConst;
import com.pbids.sanqin.utils.ButtonUtil;
import com.pbids.sanqin.utils.ValidatorUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.pbids.sanqin.ui.view.dialog.OneImageOneBtDialog.POP_ME_AUTHENTICATION;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 14:14
 * @desscribe 类描述:实名认证
 * @remark 备注:
 * @see
 */
public class MeAuthenticationFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear, MeAuthenticationView{

    MeAuthenticationPresenter presenter;
    @Bind(R.id.me_authentication_name_et)
    EditText meAuthenticationNameEt;
    @Bind(R.id.me_authentication_idcard_et)
    EditText meAuthenticationIdcardEt;
    @Bind(R.id.authem_new_next)
    Button authemNewNext;
    OneImageOneBtDialog oneImageOneBtDialog;
    public static final String ISINVITEPAGE="isInvitePage";

    public static MeAuthenticationFragment newInstance(){
        MeAuthenticationFragment fragment = new MeAuthenticationFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static MeAuthenticationFragment newInstance(int toFragmentType){
        MeAuthenticationFragment fragment = new MeAuthenticationFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ISINVITEPAGE,toFragmentType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me_authentication, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        ButtonUtil.setOnClickFalse(authemNewNext);
        meAuthenticationNameEt.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setButtonStatus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        meAuthenticationIdcardEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setButtonStatus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setButtonStatus(){
        if(checkInfo()){
            ButtonUtil.setOnClickTrue(authemNewNext);
        }else{
            ButtonUtil.setOnClickFalse(authemNewNext);
        }
    }

    private boolean checkInfo() {
        boolean isUsername = ValidatorUtil.isChineseName(meAuthenticationNameEt.getText().toString().trim());
        boolean isIdcard = ValidatorUtil.isIDCard(meAuthenticationIdcardEt.getText().toString().trim());
        if (isUsername && isIdcard) {
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftArrowCenterTextTitle("实名认证",_mActivity);
    }

    @Override
    public BasePresenter initPresenter() {
        return presenter = new MeAuthenticationPresenter(this);
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
    public void onHttpSuccess(String type) {
        dismiss();
        if(type.equals("10")){
            MeSelectFirstNameFragment fragment = MeSelectFirstNameFragment.newInstance((int)getArguments().get(ISINVITEPAGE));
            fragment.getArguments().putString("username",meAuthenticationNameEt.getText().toString());
            fragment.getArguments().putString("idcard",meAuthenticationIdcardEt.getText().toString());
//            start(fragment);
            startForResult(fragment,MeSelectFirstNameFragment.RESULTCODEREALNAME);
        }
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        if(requestCode==MeSelectFirstNameFragment.RESULTCODEREALNAME&&data!=null){
            pop();
            int type = (int) data.get(TYPEFRAGMENT);
            switch (type){
                case TOBANKTYPE:
                    start(MeScanBankFragment.newInstance());
                    break;
                case TOINVTITETYPE:
                    start(MeInviteFragment.instance());
                    break;
                case TOTEAMTYPE:
                    start(ZhiZongCasesCrowdFragment.instance());
                    break;
                case DEFAULTTYPE:
                    showInviteDialog();
                    break;
            }

        }
    }

    @Override
    public void onHttpError(String type) {
        dismiss();
        Toast.makeText(_mActivity,type,Toast.LENGTH_SHORT).show();
//        MeSelectFirstNameFragment fragment_zc_rangking = MeSelectFirstNameFragment.newInstance();
//        fragment_zc_rangking.getArguments().putString("username",meAuthenticationNameEt.getText().toString());
//        fragment_zc_rangking.getArguments().putString("idcard",meAuthenticationIdcardEt.getText().toString());
//        start(fragment_zc_rangking);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dispose();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.authem_new_next)
    public void onViewClicked() {
        getLoadingPop("正在提交").show();
        HttpParams params = new HttpParams();
        params.put("name",meAuthenticationNameEt.getText().toString());
        params.put("idNumber",meAuthenticationIdcardEt.getText().toString());
        addDisposable(presenter.submitInformation(AddrConst.SERVER_ADDRESS_USER + AddrConst.ADDRESS_NAME_VERIFIED,params,""));
    }

    @Override
    public void reviceUserNmae(String username, String surname, int surnameId,int isRealName,String idNumber){
//        if(username.length()>2){
//            MeSelectFirstNameFragment fragment_zc_rangking = MeSelectFirstNameFragment.newInstance();
//            fragment_zc_rangking.getArguments().putString("username",meAuthenticationNameEt.getText().toString());
//            fragment_zc_rangking.getArguments().putString("idcard",meAuthenticationIdcardEt.getText().toString());
//            start(fragment_zc_rangking);
//            return;
//        }

        String mUserName = MyApplication.getUserInfo().getName();

        UserInfo userInfo = MyApplication.getUserInfo();
        userInfo.setName(username);
        userInfo.setSurname(surname);
        userInfo.setSurnameId(surnameId);
        userInfo.setIsRealName(isRealName);
        userInfo.setIdNumber(idNumber);
        UserInfoManager.updateUserInfo(_mActivity,userInfo);

        MainFragment mainFragment = findFragment(MainFragment.class);

        MeFragment meFragment = mainFragment.findChildFragment(MeFragment.class);
        meFragment.updateName();
        meFragment.updateAuthen();
//        meFragment.updateIdcard();
        oneImageOneBtDialog = new OneImageOneBtDialog(_mActivity);
        oneImageOneBtDialog.setGrayCenter();
        oneImageOneBtDialog.setType(POP_ME_AUTHENTICATION);
        oneImageOneBtDialog.setInviteTips(new InviteTips() {
            @Override
            public void confirm(View view) {
                oneImageOneBtDialog.dismiss();
                pop();
            }
        });
        if(mUserName.equals(meAuthenticationNameEt.getText().toString())){
            oneImageOneBtDialog.setBlewText("认证姓名与用户名一致");
        }else{
            oneImageOneBtDialog.setBlewText("认证姓名与用户名不一致 系统将自动更改用户名");
            oneImageOneBtDialog.setBlewTextColor(getContext().getResources().getColor(R.color.authentication_success_text));
        }
        oneImageOneBtDialog.show();
    }
}