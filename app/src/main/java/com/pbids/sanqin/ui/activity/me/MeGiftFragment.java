package com.pbids.sanqin.ui.activity.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lzy.okgo.model.HttpParams;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.model.entity.Gift;
import com.pbids.sanqin.presenter.MeGiftPresenter;
import com.pbids.sanqin.ui.activity.zhizong.ZhiZongWebFragment;
import com.pbids.sanqin.ui.recyclerview.adapter.MeGiftGroupListAdapter;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.ui.view.OneImageOneBtPop;
import com.pbids.sanqin.utils.AddrConst;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 模块：我的 -> 我的礼券
 * Created by pbids903 on 2017/12/9.
 */

public class MeGiftFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear,MeGiftView{

    public static final int SHOW_SPECIFIC_GIFT  = 1647;

    @Bind(R.id.rv_list)
    RecyclerView rv_list; //列表

    MeGiftPresenter meGiftPresenter;
    MeGiftGroupListAdapter meGiftGroupListAdapter;
    String giftCode1;

    public static MeGiftFragment newInstance(){
        MeGiftFragment meGift = new MeGiftFragment();
        Bundle bundle = new Bundle();
        meGift.setArguments(bundle);
        return meGift;
    }

    //设置toolbar 视图
    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.me_gift, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftArrowCenterTextTitleRightText("我的礼券","使用规则",_mActivity);
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        //super.onFragmentResult(requestCode, resultCode, data);
        if(requestCode==SHOW_SPECIFIC_GIFT){
            if(rv_list!=null){
                meGiftPresenter.clearListData();
                meGiftGroupListAdapter.notifyDataSetChanged();
                loadList();
            }
        }
    }

    public void loadList(){
        // 请求列表数据
        HttpParams httpParams = new HttpParams();
        String url = AddrConst.SERVER_ADDRESS_USER + AddrConst.ADDRESS_ACCOUNTCASH;
        addDisposable(meGiftPresenter.submitInformationGiftList(url, httpParams, "-2"));
    }

    //init view_donate_records
    public void initView(){
        setContentLayoutGone();
        // list
        rv_list.setLayoutManager(new LinearLayoutManager(_mActivity));
        meGiftGroupListAdapter = new MeGiftGroupListAdapter(_mActivity, meGiftPresenter.getGiftGroup());
        meGiftGroupListAdapter.setOnItemViewClick(new MeGiftGroupListAdapter.OnItemViewClick(){
            /**
             * 兑换礼券
             * @param view
             * @param groupPosition
             * @param childPosition
             */
            @Override
            public void click(View view, int groupPosition, int childPosition,String giftCode) {
                Gift gift = meGiftPresenter.getGiftAvaliable(childPosition);
                if("GOODS".equals(giftCode)){
                    MeSpecificGiftFragment fragment = MeSpecificGiftFragment.newInstance();
                    fragment.getArguments().putString("redemptionCode",gift.getRedemptionCode());
                    fragment.getArguments().putFloat("totalAmount",gift.getTotalAmount());
                    fragment.getArguments().putLong("id",gift.getId());
                    startForResult(fragment,SHOW_SPECIFIC_GIFT);
                    return;
                }
                if(groupPosition==0){
                    if("CASH".equals(giftCode)){
                        giftCode1 = giftCode;
                    }else if("VIRTUAL".equals(giftCode)){
                        giftCode1 = giftCode;
                    }else{
                        giftCode1 = giftCode;
                    }
                    //Toast.makeText(_mActivity,"兑换中",Toast.LENGTH_SHORT).show();
                    getLoadingPop("兑换中").show();
                    meGiftPresenter.useCoupon(groupPosition,childPosition);
                }else{
                    Toast.makeText(_mActivity,"错误",Toast.LENGTH_SHORT).show();
                }
            }
        });
        rv_list.setAdapter(meGiftGroupListAdapter);
        loadList();
    }

    @Override
    public BasePresenter initPresenter() {
        return meGiftPresenter = new MeGiftPresenter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dispose();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_left_layout:
                //返回
                pop();
                break;
            case R.id.main_right_layout:
                //打开使用规则
                ZhiZongWebFragment fragment1 = ZhiZongWebFragment.newInstance();
                fragment1.getArguments().putString("link", AddrConst.SERVER_ADDRESS_NEWS + "/protocol/coupon");
                start(fragment1);
                break;
        }
    }

    /**
     * 数据请求成功
     * @param type
     */
    @Override
    public void onHttpSuccess(String type) {
        dismiss();
        if(!type.equals("-2")){
            OneImageOneBtPop imageOneBtPop = new OneImageOneBtPop(_mActivity,OneImageOneBtPop.POP_GIFR_EXCHANGE,giftCode1);
            imageOneBtPop.setIsAnimation(false);
            imageOneBtPop.setCancelable(false);
            imageOneBtPop.show();
        }else{
            //列表数据请求返回
            if(meGiftPresenter.hasGiftData()){
                setContentLayoutVisible();
            }else{
                setNoDataLayoutVisible();
                return;
            }
        }
    }

    @Override
    public void onHttpError(String type){
        dismiss();
        Toast.makeText(_mActivity,type,Toast.LENGTH_SHORT).show();
    }

    @Override
    public MeGiftGroupListAdapter getMeGiftGroupListAdapter( ) {
        return meGiftGroupListAdapter;
    }

}