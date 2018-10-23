package com.pbids.sanqin.ui.recyclerview.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pbids.sanqin.R;
import com.pbids.sanqin.model.entity.ZCRankingInfo;
import com.pbids.sanqin.ui.recyclerview.adapter.base.GroupedRecyclerViewAdapter;
import com.pbids.sanqin.ui.recyclerview.holder.BaseViewHolder;
import com.pbids.sanqin.ui.view.CircleImageView;

import java.util.List;


/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:03
 * @desscribe 类描述:宗祠排行的设配器
 * @remark 备注:
 * @see
 */
public class ZCRankingListAdapter extends GroupedRecyclerViewAdapter {
    Context mContext;
    List<ZCRankingInfo> rankingInfos;
    public ZCRankingListAdapter(Context context, List<ZCRankingInfo> rankingInfos) {
        super(context);
        mContext = context;
        this.rankingInfos = rankingInfos;
    }

    public void clear(){
        rankingInfos.clear();
        notifyDataSetChanged();
    }

    public List<ZCRankingInfo> getRankingInfos() {
        return rankingInfos;
    }

    public void setRankingInfos(List<ZCRankingInfo> rankingInfos) {
        this.rankingInfos = rankingInfos;
    }

    @Override
    public int getGroupCount() {
        return rankingInfos==null?0:1;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return rankingInfos==null?0:rankingInfos.size();
    }

    @Override
    public boolean hasHeader(int groupPosition) {
        return false;
    }

    @Override
    public boolean hasFooter(int groupPosition) {
        return false;
    }

    @Override
    public int getHeaderLayout(int viewType) {
        return 0;
    }

    @Override
    public int getFooterLayout(int viewType) {
        return 0;
    }

    @Override
    public int getChildLayout(int viewType) {
        return R.layout.adapter_zc_ranking;
    }

    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {

    }

    @Override
    public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {

    }

    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {
        //信息
        ZCRankingInfo rankingInfo =  rankingInfos.get(childPosition);

        TextView rankingNumber = holder.get(R.id.zc_ranking_number);
        TextView tvVip = holder.get(R.id.tv_rank_vip);
        CircleImageView youxiang = holder.get(R.id.zongquan_fried_list_touxiang);
        TextView name = holder.get(R.id.tv_rank_name);
        TextView userPosition = holder.get(R.id.zc_ranking_position);
        TextView brickNumber = holder.get(R.id.brick_number);
        ImageView arrow = holder.get(R.id.zc_ranking_arrow);
        ImageView imgZong = holder.get(R.id.tv_rank_zong);

        rankingNumber.setText(""+rankingInfo.getRank()); //排行
        Glide.with(mContext).load(rankingInfo.getFaceUrl()).into(youxiang); //头像
        name.setText(rankingInfo.getName()); //用户名
        userPosition.setText(rankingInfo.getLevelName());//等级名称
        brickNumber.setText(""+rankingInfo.getDonateBrickCount());//贡献砖块数

        switch (rankingInfo.getRank()){
            case 1:
                rankingNumber.setTextColor(mContext.getResources().getColor(R.color.main_remind_color_aother));
                rankingNumber.setTextSize(mContext.getResources().getDimension(R.dimen.sp_7));
                break;
            case 2:
                rankingNumber.setTextColor(mContext.getResources().getColor(R.color.main_remind_color_aother));
                rankingNumber.setTextSize(mContext.getResources().getDimension(R.dimen.sp_7));
                break;
            case 3:
                rankingNumber.setTextColor(mContext.getResources().getColor(R.color.main_remind_color_aother));
                rankingNumber.setTextSize(mContext.getResources().getDimension(R.dimen.sp_7));
                break;
            default:
                break;
        }

        //vip 信息
        int vip = rankingInfo.getVip();
        if(vip>0){
            tvVip.setVisibility(View.VISIBLE);
            tvVip.setText("VIP"+vip);
        }else{
            tvVip.setVisibility(View.GONE);
        }

        //宗
        if(rankingInfo.getClanStatus()>0){
            imgZong.setVisibility(View.VISIBLE);
        }else{
            imgZong.setVisibility(View.GONE);
        }

        //排行 均势
        switch (rankingInfo.getRankStatus()){
            case -1:
                arrow.setImageResource(R.drawable.new_icon_xiangxia_default);
                break;
            case 0:
                arrow.setImageResource(R.drawable.new_icon_xiangxia_default);
                break;
            case 1:
                arrow.setImageResource(R.drawable.new_icon_xiangshang_default);
                break;
        }
    }
}
