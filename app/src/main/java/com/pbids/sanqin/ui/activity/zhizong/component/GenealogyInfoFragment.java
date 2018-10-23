package com.pbids.sanqin.ui.activity.zhizong.component;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.pbids.sanqin.R;
import com.pbids.sanqin.base.BaaseFragment;
import com.pbids.sanqin.model.entity.GenealogyInfo;
import com.pbids.sanqin.model.entity.GenealogyInformation;
import com.pbids.sanqin.presenter.GenealogyInfoPresenter;
import com.pbids.sanqin.utils.NumberUtil;
import com.pbids.sanqin.utils.TimeUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author caiguoliang
 * @date on 2018/3/2 15:08
 * @desscribe 类描述:我的族谱界面 ->组件-排行
 * @remark 备注:
 * @see
 */
@SuppressLint("ValidFragment")
public class GenealogyInfoFragment extends BaaseFragment<GenealogyInfoPresenter> implements GenealogyInfoView {

    @Bind(R.id.pager_first_familiy_number)
    TextView tvFamilyNumber; //人数
    @Bind(R.id.pager_first_fund_number)
    TextView tvFundNumber; //财富
    @Bind(R.id.tv_year)
    TextView tvYear; //年

    @Bind(R.id.pager_first_name)
    TextView tvSurname ;

    private GenealogyInformation genealogyInformation;

    /*private GenealogyView genealogyView;

    public GenealogyInfoFragment(GenealogyView genealogyView){
        this.genealogyView = genealogyView;
    }*/

    public static GenealogyInfoFragment newInstance(GenealogyInformation genealogyInformation){
        GenealogyInfoFragment fragment = new GenealogyInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(GenealogyPaperFragment.DATA,genealogyInformation);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public GenealogyInfoPresenter initPresenter() { return  new GenealogyInfoPresenter(); }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_genealogy_info, container, false);
        //设置背景
        SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                view.setBackground(resource);
            }
        };
        Glide.with(this).load(R.drawable.pager_first_bg).into(simpleTarget);
        //---end--

        ButterKnife.bind(this, view);
        initData();
        initView();
        return view;
    }

    private void initData() {
        Bundle arguments = getArguments();
        genealogyInformation = (GenealogyInformation) arguments.getSerializable(GenealogyPaperFragment.DATA);
    }


    public void initView() {
        initTypeface(); // 初始化字体
        tvSurname.getPaint().setTypeface(typeface);

        if(genealogyInformation==null){
            return;
        }
        if (genealogyInformation.getGenealogy()>0){
            GenealogyInfo genealogyInfo = genealogyInformation.getInfo();
            //tvFamilyNumber.setText(genealogyInfo.getPeopleNum()+"人");
            tvFamilyNumber.setText(NumberUtil.scalerWan(genealogyInfo.getPeopleNum())+"人");
            tvFundNumber.setText(NumberUtil.scalerWan(genealogyInfo.getWealth()));
            tvYear.setText(TimeUtil.getGenealogyTimeFormat(genealogyInfo.getCreateTime()));
            tvSurname.setText(genealogyInfo.getSurname()+"氏族谱");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
