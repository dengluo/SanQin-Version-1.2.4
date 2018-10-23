package com.pbids.sanqin.ui.activity.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.lzy.okgo.model.HttpParams;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.model.db.UserInfoManager;
import com.pbids.sanqin.model.entity.Bank;
import com.pbids.sanqin.model.entity.UserInfo;
import com.pbids.sanqin.presenter.MeBindingBankPresenter;
import com.pbids.sanqin.ui.activity.MainFragment;
import com.pbids.sanqin.ui.recyclerview.adapter.BankListAdapter;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.utils.AddrConst;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 14:16
 * @desscribe 类描述:已绑定银行卡界面
 * @remark 备注:
 * @see
 */
public class MeBindingBankFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear, MeBindingBankView {

    MeBindingBankPresenter meBindingBankPresenter;
    @Bind(R.id.me_binding_bank_rv)
    RecyclerView meBindingBankRv;
    @Bind(R.id.me_binding_bank_bt)
    Button meBindingBankBt;

    BankListAdapter bankListAdapter;

    public static MeBindingBankFragment newInstance() {
        MeBindingBankFragment fragment = new MeBindingBankFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_binding_bank, container, false);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }

    private void initView() {
        setContentLayoutGone();
        HttpParams params = new HttpParams();
        addDisposable(meBindingBankPresenter.submitInformation(AddrConst.SERVER_ADDRESS_USER + AddrConst.ADDRESS_BINDCARD_LIST, params, "1"));
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftArrowCenterTextTitle("绑定的银行卡", _mActivity);
    }

    @Override
    public BasePresenter initPresenter() {
        return meBindingBankPresenter = new MeBindingBankPresenter(this);
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
        dispose();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.me_binding_bank_bt)
    public void onViewClicked() {
        if("解除绑定".equals(meBindingBankBt.getText().toString())){
            // 解除绑定
            if(bankListAdapter.getBanks().size()!=0){
                getLoadingPop("正在解绑").show();
                HttpParams params = new HttpParams();
                params.put("id",bankListAdapter.getBanks().get(0).getId());
                addDisposable(meBindingBankPresenter.submitInformationForUnbundling(
                        AddrConst.SERVER_ADDRESS_USER + AddrConst.ADDRESS_BINDCARD_DEL, params, "2"));
            }
        }
    }

    @Override
    public void onHttpSuccess(String type) {
        dismiss();
        if("2".equals(type)){
//            if(bankListAdapter!=null){
//                bankListAdapter.clear();
//            }
//            meBindingBankBt.setText("绑定银行卡");
            UserInfo userInfo = MyApplication.getUserInfo();
            userInfo.setIsBindCard(0);
            userInfo.setCardNumber("");
            UserInfoManager.updateUserInfo(_mActivity,userInfo);

            MainFragment mainFragment = findFragment(MainFragment.class);
            MeFragment meFragment = mainFragment.findChildFragment(MeFragment.class);
            meFragment.updateIdcard();
            pop();
        }else if("1".equals(type)){
            setContentLayoutVisible();
        }
    }

    @Override
    public void onHttpError(String type) {
        dismiss();
        Toast.makeText(_mActivity,type,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getBanks(List<Bank> banks) {
        if(banks.size()>0){
            meBindingBankBt.setText("解除绑定");
        }else{
            meBindingBankBt.setText("绑定银行卡");
        }
        //Log.i("wzh","getBanksgetBanksgetBanks");
        LinearLayoutManager manager = new LinearLayoutManager(_mActivity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        bankListAdapter = new BankListAdapter(_mActivity, banks);
        meBindingBankRv.addItemDecoration(new HorizontalDividerItemDecoration.Builder(_mActivity)
                .color(_mActivity.getResources().getColor(R.color.transparent))
                .sizeResId(R.dimen.dp_10)
                .build());
        meBindingBankRv.setLayoutManager(manager);
        meBindingBankRv.setAdapter(bankListAdapter);
    }
}
