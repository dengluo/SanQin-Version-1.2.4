package com.pbids.sanqin.ui.recyclerview.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pbids.sanqin.R;
import com.pbids.sanqin.model.entity.Gift;
import com.pbids.sanqin.model.entity.GiftGroup;
import com.pbids.sanqin.ui.recyclerview.adapter.base.GroupedRecyclerViewAdapter;
import com.pbids.sanqin.ui.recyclerview.holder.BaseViewHolder;
import com.pbids.sanqin.utils.TimeUtil;

import java.util.List;

/**
 * adapter
 * 模块：我的 -> 我的礼券  
 * Created by caiguoliang 2018-3-2
 */

public class MeGiftGroupListAdapter extends GroupedRecyclerViewAdapter {

    //列表数据
    private List<GiftGroup> giftGroups;
    Context mContext;

    OnItemViewClick onItemViewClick ;

    public MeGiftGroupListAdapter(Context context, List<GiftGroup> giftGroups) {
        super(context);
        mContext = context;
        this.giftGroups = giftGroups;
    }

    public List<GiftGroup> getGiftGroups(){
        return giftGroups;
    }

    /**
     * 分组数量
     * @return  
     */
    @Override
    public int getGroupCount() {
        return giftGroups == null ? 0 : giftGroups.size();
    }

    /**
     * 列表总数
     * @param groupPosition
     * @return
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return giftGroups.get(groupPosition) == null ? 0 : giftGroups.get(groupPosition).getGifts().size();
    }

    /**
     * 分组是否有头部
     * @param groupPosition  
     * @return
     */
    @Override
    public boolean hasHeader(int groupPosition) {
        if (groupPosition == 0) {
            return false;
        }
        //如果列表为空不显示表头
        if (groupPosition == 1 && giftGroups.get(1).getGifts().size() == 0) {
            return false;
        }
        return true;
    }

    /**
     * 分组无底部
     * @param groupPosition
     * @return
     */
    @Override
    public boolean hasFooter(int groupPosition) {
        return false;
    }

    @Override
    public int getHeaderLayout(int viewType) {
        return R.layout.me_gift_list_header;
    }

    @Override
    public int getFooterLayout(int viewType) {
        return R.layout.adapter_footer;
    }

    @Override
    public int getChildLayout(int viewType) {
        return R.layout.me_gift_available;
    }

    //bind header view_donate_records
    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {
        GiftGroup giftGroup = giftGroups.get(groupPosition);
        holder.setText(R.id.tv_header, giftGroup.getHeader());
    }

    //bind footer view_donate_records
    @Override
    public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {
        //没有footer 不需要绑定
    }

    //bind 列表视图
    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, final int groupPosition, final int childPosition) {
        Gift gift = giftGroups.get(groupPosition).getGifts().get(childPosition);
        //view_donate_records
        TextView name = (TextView) holder.get(R.id.me_gift_available_name);
        TextView information = (TextView) holder.get(R.id.me_gift_available_information);
        TextView start = (TextView) holder.get(R.id.me_gift_available_start);
        TextView end = (TextView) holder.get(R.id.me_gift_available_end);
        ImageView bg = (ImageView) holder.get(R.id.me_gift_available_bg);
        TextView  right_top =(TextView)holder.get(R.id.rv_right_top);

        Button bt = (Button) holder.get(R.id.me_gift_available_bt);

        int txt_color = Color.parseColor("#FFFFFF") ;
        if(gift.getState()>0){
            bt.setVisibility(View.VISIBLE);//显示兑换按钮
            //可使用的
            String giftCode="";
            if(gift.getCode().contains("VIRTUAL")){//红色
                giftCode="VIRTUAL";
                Glide.with(mContext).load(R.drawable.me_bg_cengse_default).into(bg);
                bt.setTextColor(mContext.getResources().getColor(R.color.gift_text_red));
            }else if(gift.getCode().contains("CASH")){//黄色
                giftCode="CASH";
                Glide.with(mContext).load(R.drawable.me_bg_huangse_default).into(bg);
                bt.setTextColor(mContext.getResources().getColor(R.color.gift_text_huang));
            }else if(gift.getCode().contains("GOODS")){//蓝色
                giftCode="GOODS";
                Glide.with(mContext).load(R.drawable.me_bg_blue_default).into(bg);
                bt.setTextColor(mContext.getResources().getColor(R.color.gift_text_blue));
                bt.setText("查看详情");
            }

            //兑换按钮
            final int finalI = childPosition;
            final String finalGiftCode = giftCode;
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemViewClick!=null){
                        onItemViewClick.click(v,groupPosition,childPosition, finalGiftCode);
                    }
                }
            });

        }else{
            bt.setVisibility(View.GONE);//不显示兑换按钮
            txt_color =  Color.parseColor("#88373132") ;//文字颜色
            //已过期
            if(gift.getCode().contains("VIRTUAL")){//红色
                Glide.with(mContext).load(R.drawable.me_bg_cengse_disabled).into(bg);
            }else if(gift.getCode().contains("CASH")){//黄色
                Glide.with(mContext).load(R.drawable.me_bg_huangse_disabled).into(bg);
            }else if(gift.getCode().contains("GOODS")){//蓝色
                Glide.with(mContext).load(R.drawable.me_bg_blue_disabled).into(bg);
            }
        }

        //调整文字颜色
        name.setTextColor(txt_color);
        information.setTextColor(txt_color);
        start.setTextColor(txt_color);
        end.setTextColor(txt_color);
        right_top.setTextColor(txt_color);

        //礼券信息
        name.setText(gift.getCouponName());
        information.setText(gift.getRemarks());
        start.setText(TimeUtil.getGiftTimeFormat(gift.getStartDate()));
        end.setText(TimeUtil.getGiftTimeFormat(gift.getEndDate()));
    }


    public interface OnItemViewClick{
        void click(View view,int groupPosition,int childPosition,String giftCode);
    }

    public void setOnItemViewClick(OnItemViewClick onItemViewClick){
        this.onItemViewClick = onItemViewClick;
    }
}
