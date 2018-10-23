package com.pbids.sanqin.ui.activity.me;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lzy.okgo.model.HttpParams;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.model.db.UserInfoManager;
import com.pbids.sanqin.model.entity.UserInfo;
import com.pbids.sanqin.presenter.MeAuthenticationPresenter;
import com.pbids.sanqin.ui.activity.MainFragment;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.ui.view.dialog.OneImageOneBtDialog;
import com.pbids.sanqin.ui.view.dialog.OneTextTwoBtDialog;
import com.pbids.sanqin.utils.AddrConst;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.pbids.sanqin.ui.view.dialog.OneImageOneBtDialog.POP_ME_AUTHENTICATION;

/**
 * Created by pbids903 on 2018/1/29.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 14:44
 * @desscribe 类描述:选择姓氏界面
 * @remark 备注:
 * @see
 */
public class MeSelectFirstNameFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear, MeAuthenticationView {

    MeAuthenticationPresenter presenter;
    @Bind(R.id.one_text_bt)
    Button oneTextBt;
    @Bind(R.id.two_text_bt)
    Button twoTextBt;
    @Bind(R.id.submit_bt)
    Button submitBt;
    OneImageOneBtDialog oneImageOneBtDialog;
    private String username="";
    private String idcard="";
    private OneTextTwoBtDialog oneTextTwoBtDialog;
    private int toFragmentType;

    public static final int RESULTCODEREALNAME = 20013;

    public static MeSelectFirstNameFragment newInstance() {
        MeSelectFirstNameFragment fragment = new MeSelectFirstNameFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static MeSelectFirstNameFragment newInstance(int isValuePage) {
        MeSelectFirstNameFragment fragment = new MeSelectFirstNameFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPEFRAGMENT,isValuePage);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me_selectfirstname, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        username = getArguments().getString("username");
        idcard = getArguments().getString("idcard");
        oneTextBt.setText(username.substring(0,1));
        twoTextBt.setText(username.substring(0,2));
        oneTextBt.setSelected(true);
        twoTextBt.setSelected(false);
        //判断实名认证页面是从哪个页面点击，做相应不同的跳转
        toFragmentType = (int) getArguments().get(TYPEFRAGMENT);
    }

    @Override
    public void setToolBar(AppToolBar toolBar){
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftTextCenterTextTitle("取消", "选择姓氏", _mActivity);
    }

    @Override
    public BasePresenter initPresenter() {
        return presenter = new MeAuthenticationPresenter(this);
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
    public void onHttpSuccess(String type){
        dismiss();
//        oneImageOneBtDialog = new OneImageOneBtDialog(_mActivity);
//        oneImageOneBtDialog.setGrayCenter();
//        oneImageOneBtDialog.setType(POP_ME_AUTHENTICATION);
//
//        if(MyApplication.getUserInfo().getName().equals(username)){
//            oneImageOneBtDialog.setBlewText("认证姓名与用户名一致");
//        }else{
//            oneImageOneBtDialog.setBlewText("认证姓名与用户名不一致 系统将自动更改用户名");
//            oneImageOneBtDialog.setBlewTextColor(getContext().getResources().getColor(R.color.authentication_success_text));
//        }
//        oneImageOneBtDialog.show();
    }

    @Override
    public void onHttpError(String type) {
        dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dispose();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.submit_bt)
    public void onViewClicked() {
//        addDisposable(presenter.submitInformation());
    }

    @OnClick({R.id.one_text_bt, R.id.two_text_bt, R.id.submit_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.one_text_bt:
                oneTextBt.setSelected(true);
                twoTextBt.setSelected(false);
                break;
            case R.id.two_text_bt:
                oneTextBt.setSelected(false);
                twoTextBt.setSelected(true);
                break;
            case R.id.submit_bt:
                getLoadingPop("正在保存").show();
                HttpParams params = new HttpParams();
                params.put("idNumber",idcard);
                params.put("name",username);
                UserInfo userInfo = MyApplication.getUserInfo();
                if(oneTextBt.isSelected()){
                    params.put("surname",oneTextBt.getText().toString());
                }else{
                    params.put("surname",twoTextBt.getText().toString());
                    params.put("Id",userInfo.getIdNumber());
                    params.put("reId",userInfo.getReid());
                }
                addDisposable(presenter.submitInformation(AddrConst.SERVER_ADDRESS_USER + AddrConst.ADDRESS_NAME_VERIFIED,params,""));
//                MainFragment meFragment = findFragment(MainFragment.class);
//                start(meFragment);
//                popTo(MainFragment.class,false);
                break;
        }
    }

    @Override
    public void reviceUserNmae(String name, String surname, int surnameId,int isRealName,String idNumber) {
        String mUserName = MyApplication.getUserInfo().getName();

        UserInfo userInfo = MyApplication.getUserInfo();
        userInfo.setName(username);
        userInfo.setSurname(surname);
        userInfo.setSurnameId(surnameId);
        userInfo.setIsRealName(isRealName);
        userInfo.setIdNumber(idNumber);
        UserInfoManager.updateUserInfo(_mActivity,userInfo);

        MainFragment mainFragment = findFragment(MainFragment.class);
        //更新博古架姓名内容
        mainFragment.updateVPNameLayout(userInfo);

//        MainFragment mainFragment = findFragment(MainFragment.class);
        MeFragment meFragment = mainFragment.findChildFragment(MeFragment.class);
        meFragment.updateName();
        meFragment.updateAuthen();
        meFragment.updateHeadPortrait();

//        meFragment.updateIdcard();

        oneImageOneBtDialog = new OneImageOneBtDialog(_mActivity);
        oneImageOneBtDialog.setGrayCenter();
        oneImageOneBtDialog.setType(POP_ME_AUTHENTICATION);
        if(mUserName.equals(username)){
            oneImageOneBtDialog.setBlewText("认证姓名与用户名一致");
        }else{
            oneImageOneBtDialog.setBlewText("认证姓名与用户名不一致 已修改成功!");
            oneImageOneBtDialog.setBlewTextColor(getContext().getResources().getColor(R.color.authentication_success_text));
        }
        oneImageOneBtDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                MeSelectFirstNameFragment.this.popTo(MainFragment.class, false);
//                start(MeInviteFragment.instance());
//                oneImageOneBtDialog.dismiss();
            }
        });
        oneImageOneBtDialog.setInviteTips(new InviteTips() {
            @Override
            public void confirm(View view) {
                oneImageOneBtDialog.dismiss();
                //新人邀请弹框
                    pop();
                    Bundle bundle = new Bundle();
                    bundle.putInt(TYPEFRAGMENT,(int)getArguments().get(TYPEFRAGMENT));
                    setFragmentResult(RESULTCODEREALNAME, bundle);
            }
        });
        oneImageOneBtDialog.show();
    }
}
