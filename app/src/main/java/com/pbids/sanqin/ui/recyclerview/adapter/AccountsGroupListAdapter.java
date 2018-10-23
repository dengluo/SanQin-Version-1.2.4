package com.pbids.sanqin.ui.recyclerview.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.pbids.sanqin.R;
import com.pbids.sanqin.model.entity.Accounts;
import com.pbids.sanqin.model.entity.AccountsGroup;
import com.pbids.sanqin.ui.adapter.NewsMoneyAdapter;
import com.pbids.sanqin.ui.recyclerview.adapter.base.GroupedRecyclerViewAdapter;
import com.pbids.sanqin.ui.recyclerview.holder.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.List;


/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:02
 * @desscribe 类描述:流水界面list设配器
 * @remark 备注:
 * @see
 */
public class AccountsGroupListAdapter extends GroupedRecyclerViewAdapter {

    List<AccountsGroup> accountsGroups;
    Context mContext;

    public AccountsGroupListAdapter(Context context, List<AccountsGroup> accountsGroups) {
        super(context);
        mContext = context;
        this.accountsGroups = accountsGroups;
    }

    @Override
    public int getGroupCount() {
        return accountsGroups == null ? 0 : accountsGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return accountsGroups.get(groupPosition) == null?0:accountsGroups.get(groupPosition).getAccountses().size();
    }

    @Override
    public boolean hasHeader(int groupPosition) {
        return true;
    }

    @Override
    public boolean hasFooter(int groupPosition) {
        return false;
    }

    @Override
    public int getHeaderLayout(int viewType) {
        return R.layout.adapter_header;
    }

    @Override
    public int getFooterLayout(int viewType) {
        return R.layout.adapter_footer;
    }

    @Override
    public int getChildLayout(int viewType) {
        return R.layout.me_money_cash_list_item;
    }


    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {
        AccountsGroup accountsGroup = accountsGroups.get(groupPosition);
        holder.setText(R.id.tv_header, accountsGroup.getHeader());
    }

    @Override
    public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {

    }

    // 设置颜色
    private void setTextColor(TextView tv, int c){
        int color = 0;
        switch (c){
            case NewsMoneyAdapter.MONEY_TEXT_BALCK:
                //黑色
                color = mContext.getResources().getColor(R.color.black);
                break;
            case  NewsMoneyAdapter.MONEY_TEXT_GREEN:
                //绿色
                color = mContext.getResources().getColor(R.color.money_tixian);
                break;
        }
        tv.setTextColor(color);
    }



    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {
        Accounts accounts = accountsGroups.get(groupPosition).getAccountses().get(childPosition);
        ImageView moneyListStatusIv = (ImageView)holder.get(R.id.me_money_cash_list_item_iv);
        TextView moneyListStatusTv = (TextView)holder.get(R.id.me_money_cash_list_item_status);
        TextView moneyListTime = (TextView)holder.get(R.id.me_money_cash_list_item_time);
        TextView moneyListNumber = (TextView)holder.get(R.id.me_money_cash_list_item_number);
        TextView moneyListSymbol = (TextView)holder.get(R.id.me_money_cash_list_item_rmb);

        SimpleDateFormat createTime = new SimpleDateFormat("yyyy/MM/dd");

        String totalAmount = String.format("%.2f", accounts.getTotalAmount())    ;
        switch (accounts.getOrderType()){
            case -2://奖励
                moneyListStatusIv.setImageResource(R.drawable.me_icon_jiangli_default);
                moneyListTime.setText(createTime.format(accounts.getCreateTime()));
                //int totalAmount1 = (int)accounts.getTotalAmount();
                //moneyListNumber.setTextColor(mContext.getResources().getColor(R.color.money_tixian));
                setTextColor(moneyListNumber,NewsMoneyAdapter.MONEY_TEXT_GREEN);
                setTextColor(moneyListSymbol,NewsMoneyAdapter.MONEY_TEXT_GREEN);
                moneyListNumber.setText("+"+totalAmount);
                moneyListStatusTv.setText("奖励");
                break;
            case -1://提现
                moneyListStatusIv.setImageResource(R.drawable.me_icon_tixianchenggong_default);
                moneyListTime.setText(createTime.format(accounts.getCreateTime()));
                //String totalAmount2 = String.format("%.2f", accounts.getTotalAmount())    ;
                if(accounts.getOrderStatus() == -3){
                    moneyListStatusTv.setText("提现审核中");
                    moneyListNumber.setText("-"+totalAmount);
                    //moneyListNumber.setTextColor(mContext.getResources().getColor(R.color.money_tixian));
                }else if(accounts.getOrderStatus() == -1){
                    moneyListNumber.setText("+"+totalAmount);
                    moneyListStatusTv.setText("提现失败");
                    //moneyListNumber.setTextColor(mContext.getResources().getColor(R.color.main_title_text_color));
                    setTextColor(moneyListNumber,NewsMoneyAdapter.MONEY_TEXT_GREEN);
                    setTextColor(moneyListSymbol,NewsMoneyAdapter.MONEY_TEXT_GREEN);
                }else if(accounts.getOrderStatus() == 1){
                    moneyListNumber.setText("-"+totalAmount);
                    moneyListStatusTv.setText("提现成功");
                    //moneyListNumber.setTextColor(mContext.getResources().getColor(R.color.main_title_text_color));

                }else if(accounts.getOrderStatus() == -2){
                    moneyListNumber.setText("+"+totalAmount);
                    moneyListStatusTv.setText("提现审核失败");
                    //moneyListNumber.setTextColor(mContext.getResources().getColor(R.color.main_title_text_color));
                    setTextColor(moneyListNumber,NewsMoneyAdapter.MONEY_TEXT_GREEN);
                    setTextColor(moneyListSymbol,NewsMoneyAdapter.MONEY_TEXT_GREEN);
                }
                break;
            case 1://打赏
                moneyListStatusIv.setImageResource(R.drawable.me_icon_dashang_default);
                moneyListTime.setText(createTime.format(accounts.getCreateTime()));
                //int totalAmount3 = (int)accounts.getTotalAmount();
                moneyListNumber.setText("-"+totalAmount);
                moneyListNumber.setTextColor(mContext.getResources().getColor(R.color.main_title_text_color));
                moneyListStatusTv.setText("打赏");
                break;
            case 2://活动支付
                moneyListStatusIv.setImageResource(R.drawable.me_icon_chaojibiaoqingzhifu_default);
                moneyListTime.setText(createTime.format(accounts.getCreateTime()));
                //int totalAmount4 = (int)accounts.getTotalAmount();
                moneyListNumber.setText("-"+totalAmount);
                moneyListNumber.setTextColor(mContext.getResources().getColor(R.color.main_title_text_color));
                moneyListStatusTv.setText("活动支付");
                break;
            case 3://超级表情支付
                moneyListStatusIv.setImageResource(R.drawable.me_icon_chaojibiaoqingzhifu_default);
                moneyListTime.setText(createTime.format(accounts.getCreateTime()));
                //int totalAmount5 = (int)accounts.getTotalAmount();
                moneyListNumber.setText("-"+totalAmount);
                moneyListNumber.setTextColor(mContext.getResources().getColor(R.color.main_title_text_color));
                moneyListStatusTv.setText("超级表情支付");
                break;
        }
    }
}
