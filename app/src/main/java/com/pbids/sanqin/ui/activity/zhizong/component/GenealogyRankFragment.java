package com.pbids.sanqin.ui.activity.zhizong.component;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.alibaba.fastjson.JSONObject;

import com.bumptech.glide.Glide;
import com.pbids.sanqin.R;
import com.pbids.sanqin.base.BaaseFragment;
import com.pbids.sanqin.base.ComonGroupRecycerAdapter;
import com.pbids.sanqin.model.entity.GenealogyRank;
import com.pbids.sanqin.presenter.GenealogyRankPresenter;
import com.pbids.sanqin.ui.activity.zhizong.GenealogyView;
import com.pbids.sanqin.ui.recyclerview.holder.BaseViewHolder;
import com.pbids.sanqin.utils.NumberUtil;

import java.util.ArrayList;
import java.util.List;

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
public class GenealogyRankFragment extends BaaseFragment<GenealogyRankPresenter> implements GenealogyRankView {

    @Bind(R.id.rv_list)
    RecyclerView rvList;

    ComonGroupRecycerAdapter<GenealogyRank> mAdpater;
    GenealogyView genealogyView;

    //private List<GenealogyRank> ranks;

    public GenealogyRankFragment(GenealogyView genealogyView) {
        this.genealogyView = genealogyView;
    }

    @Override
    public GenealogyRankPresenter initPresenter() { return  new GenealogyRankPresenter(); }

    //更新数据
    public void updateRankData(List<GenealogyRank> ranks){
        //this.ranks = ranks;
        if(mAdpater!=null){
            mAdpater.getFirstGroup().getList().clear();
            mAdpater.getFirstGroup().getList().addAll(ranks);
            mAdpater.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            mAdpater = new ComonGroupRecycerAdapter(_mActivity,R.layout.fragment_genealogy_rank_rv_item);
            ArrayList<GenealogyRank> rank = genealogyView.getData().getRank();
            //ToDo 发生空指针异常 74
            if(!(rank.size() <=0)){
                updateRankData(rank);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_genealogy_rank, container, false);
        ButterKnife.bind(this, view);

        mAdpater.setViewHolder(new ComonGroupRecycerAdapter.ViewHolder() {
            @Override
            public void onBindHeaderViewHolder(BaseViewHolder holder,int groupPosition) {

            }

            @Override
            public void onBindFooterViewHolder(BaseViewHolder holder,int groupPosition) {

            }

            @Override
            public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {
                GenealogyRank rank = mAdpater.getChild(groupPosition,childPosition);
                //Log.v("cgl", JSONObject.toJSONString(rank));
                TextView tvIndex = holder.get(R.id.tv_rank_index);
                TextView tvOrganization = holder.get(R.id.tv_rank_organization);
                TextView tvMoney = holder.get(R.id.tv_rank_money);
                ImageView imgSrunameIcon = holder.get(R.id.img_rank_sruname_icon);
                ImageView imgStatus = holder.get(R.id.img_rank_status);

                tvIndex.setText(rank.getRank()+"");
                tvOrganization.setText(rank.getOrganize());
                tvMoney.setText(NumberUtil.scalerWan(rank.getWealth()));
                //姓氏图标
                Glide.with(_mActivity).load(rank.getSurnameIcon()).into(imgSrunameIcon);
            }
        });
        initView();
        return view;
    }


    public void initView() {
        rvList.setLayoutManager(new LinearLayoutManager(_mActivity));
        rvList.setAdapter(mAdpater);
        //mAdpater.notifyDataSetChanged();
        //viewHeight = ScreenUtils.getScreenHeight() - (int) getResources().getDimension(R.dimen.dp_40) - StatusBarUtils.getStatusBarHeight(_mActivity);
        //pageFactory = new PageFactory(_mActivity,ScreenUtils.getScreenWidth(),viewHeight,50);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}
