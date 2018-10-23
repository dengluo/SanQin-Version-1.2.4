package com.pbids.sanqin.ui.activity.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.pbids.sanqin.R;
import com.pbids.sanqin.base.BaaseToolBarFragment;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.presenter.MeNickSavaPresenter;
import com.pbids.sanqin.presenter.UserInformationPresenter;
import com.pbids.sanqin.ui.activity.MainFragment;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.utils.AddrConst;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author : 上官名鹏
 * Description : 别称页面
 * Date :Create in 2018/8/9 10:19
 * Modified By :
 */
public class MeNickSavaFragment extends BaaseToolBarFragment<MeNickSavaPresenter> implements MeNickSaveView{

    @Bind(R.id.me_nick_name_et)
    EditText nickNameEt;

    private MeNickSavaPresenter meNickSavaPresenter;

    public static MeNickSavaFragment instance(){
        MeNickSavaFragment fragment = new MeNickSavaFragment();
        return fragment;
    }


    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.me_information_nick_name,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setLeftArrowCenterTextTitle("我的别称",getContext());
        toolBar.setOnToolBarClickLisenear(this);
    }


    @Override
    public void success() {
        showToast("修改成功");
//        MainFragment mainFragment = findFragment(MainFragment.class);
//        MeFragment meFragment = mainFragment.findChildFragment(MeFragment.class);
        MeInformationFragment meInformationFragment = findFragment(MeInformationFragment.class);
        meInformationFragment.updateNikcName();
        pop();
    }

    @Override
    public void failed() {
        showToast("修改失败");

    }

    @Override
    public void onClick(View v) {
        pop();
    }

    @OnClick(R.id.me_nick_name_save_bt)
    public void OnClickView(){
        String url = AddrConst.SERVER_ADDRESS_USER+AddrConst.ADDRESS_UPDATE_NICK_NAME;
        String nickName = nickNameEt.getText().toString().trim();
        if("".equals(nickName)){
            showToast("内容不能为空！");
        }else{
            meNickSavaPresenter.updateNickName(url,nickName);
        }
    }

    @Override
    protected MeNickSavaPresenter initPresenter() {
        return this.meNickSavaPresenter = new MeNickSavaPresenter(this);
    }
}
