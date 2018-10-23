package com.pbids.sanqin.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pbids.sanqin.R;
import com.pbids.sanqin.base.BaaseGroupRecycerViewAdapter;
import com.pbids.sanqin.base.ComonRecycerGroup;
import com.pbids.sanqin.model.entity.Accounts;
import com.pbids.sanqin.ui.recyclerview.holder.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:45
 * @desscribe 类描述:我的资金列表list设配器
 * @remark 备注:
 * @see
 */
public class NewsMoneyAdapter extends BaaseGroupRecycerViewAdapter<ComonRecycerGroup<Accounts>> {

    public static final int MONEY_TEXT_BALCK = 1;
    public static final int MONEY_TEXT_GREEN = 2;
    public static final int MONEY_TEXT_RED = 3;

    //Context context;
    //List<Accounts> moneyHistories;

    public NewsMoneyAdapter(Context context) {
        super(context, new ArrayList<ComonRecycerGroup<Accounts>>());
        //this.context = context;
        //this.moneyHistories = moneyHistories;
    }

   /* public void setListData(List<Accounts> moneyHistories){
        this.moneyHistories.clear();
        this.moneyHistories.addAll(moneyHistories);
        notifyDataSetChanged();
    }*/

/*    @Override
    public NewsMoneyAdapter.MeMoneyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.me_money_cash_list_item,parent,false);
        return new MeMoneyViewHolder(view);
    }*/

/*    @Override
    public void onBindViewHolder(MeMoneyViewHolder holder, int position) {
        holder.bindingDate(moneyHistories.get(position));
    }
    */


    @Override
    public boolean hasHeader(int groupPosition) {
        ComonRecycerGroup item = gLists.get(groupPosition);
        return item.getHeader() != null;
    }

    @Override
    public boolean hasFooter(int groupPosition) {
        ComonRecycerGroup item = gLists.get(groupPosition);
        return item.getFooter() != null;
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
        //组头
        ComonRecycerGroup item = gLists.get(groupPosition);
        holder.setText(R.id.tv_header, item.getHeader());
    }

    @Override
    public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {

    }

    // 设置颜色
    private void setTextColor(TextView tv, int c) {
        int color = 0;
        switch (c) {
            case MONEY_TEXT_BALCK:
                //黑色
                color = mContext.getResources().getColor(R.color.black);
                break;
            case MONEY_TEXT_GREEN:
                //绿色
                color = mContext.getResources().getColor(R.color.money_tixian);
                break;
            case MONEY_TEXT_RED:
                color = Color.RED;
                break;
        }
        tv.setTextColor(color);
    }

    // 设置颜色
    private void setItemColor(BaseViewHolder holder, int c) {
        TextView moneyListNumber = holder.get(R.id.me_money_cash_list_item_number);
        TextView moneyListSymbol = holder.get(R.id.me_money_cash_list_item_rmb);
        setTextColor(moneyListNumber, c);
        setTextColor(moneyListSymbol, c);
    }

    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {
        ComonRecycerGroup<Accounts> item = gLists.get(groupPosition);
        Accounts moneyHistory = item.getList().get(childPosition);
        LinearLayout moneyListLin = holder.get(R.id.money_list_lin);
        TextView moneyListStatusTv = holder.get(R.id.me_money_cash_list_item_status);
        TextView moneyListTime = holder.get(R.id.me_money_cash_list_item_time);
        TextView moneyListNumber = holder.get(R.id.me_money_cash_list_item_number);
        TextView payTypeTv = holder.get(R.id.me_money_type);
        TextView statusTv = holder.get(R.id.me_money_status);
        TextView chargeTipsTv = holder.get(R.id.charge_tips_tv);
        //TextView moneyListSymbol = holder.get(R.id.me_money_cash_list_item_rmb);
        SimpleDateFormat createTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String status = "成功";
        String prefix = "-";
        String payType = "支付宝";
        int orderStatus = moneyHistory.getOrderStatus();
        int orderType = moneyHistory.getOrderType();
        String name = moneyHistory.getPurpose();
        float amount = moneyHistory.getTotalAmount();
        String totalAmount;
        switch (orderStatus) {
            case -3:
                status = "待处理";
                break;
            case -2:
                status = "拒绝申请";
                break;
            case -1:
                status = "失败";
                break;
            case 1:
                status = "已放款";
                break;
            case 0:
                status = "待支付";
                break;
        }
        switch (orderType) {
            case -2:
                //奖励
                prefix = "+";
                break;
            case -1:
                //提现 ;
//                if (orderStatus == -1 || orderStatus == -2) {
//                    prefix = "+";
//                }
                prefix="";
                break;
            case 1:
                //打赏
                break;
            case 2:
                //活动支付
                if (orderStatus == -1 || orderStatus == -2) {
                    prefix = "+";
                }
                break;
            case 3:
                //超级表情
                break;
            case 4:
                //手续费
                if (orderStatus == -1 || orderStatus == -2) {
                    prefix = "+";
                }
                break;
            case 6:
                //邀请返现
                prefix = "+";
                break;
        }
        switch (moneyHistory.getPayType()) {
            case 0:
                payType = "支付宝";
                break;
            case 1:
                payType = "微信";
                break;
            case 2:
                //银行卡支付
                payType = "账户";
                break;
            case 3:
                payType = "账户";
                break;
            case 4:
                payType = "苹果支付";
                break;
        }
        if (prefix.equals("-")) {
            setItemColor(holder, MONEY_TEXT_BALCK);
        } else if(prefix.equals("")){
            setItemColor(holder,MONEY_TEXT_BALCK);
        } else{
            setItemColor(holder, MONEY_TEXT_RED);
        }
//            moneyListStatusIv.setImageResource(icon);
        moneyListStatusTv.setText(name);
        payTypeTv.setText(payType);
        //提现显示状态，其他不显示
        if (moneyHistory.getOrderType() == -1) {
            statusTv.setVisibility(View.VISIBLE);
            statusTv.setText(status);
        } else {
            statusTv.setVisibility(View.GONE);
        }
        //time
        Date date = new Date(moneyHistory.getCreateTime());
        moneyListTime.setText(createTime.format(date));
        //显示扣掉的手续费
        if(moneyHistory.getOrderType() == -1&&moneyHistory.getOrderStatus()==1){
            totalAmount = String.format("%.2f", amount+2);
            chargeTipsTv.setVisibility(View.VISIBLE);
        }else{
            totalAmount = String.format("%.2f", amount);
            chargeTipsTv.setVisibility(View.GONE);
        }
        moneyListNumber.setText(prefix + totalAmount);
    }


/*

    public class MeMoneyViewHolder extends RecyclerView.ViewHolder{
        ImageView moneyListStatusIv;
        TextView moneyListStatusTv;
        TextView moneyListTime;
        TextView moneyListNumber;
        TextView moneyListSymbol;

        public MeMoneyViewHolder(View itemView) {
            super(itemView);
            moneyListStatusIv = (ImageView) itemView.findViewById(R.id.me_money_cash_list_item_iv);
            moneyListStatusTv = (TextView) itemView.findViewById(R.id.me_money_cash_list_item_status);
            moneyListTime = (TextView) itemView.findViewById(R.id.me_money_cash_list_item_time);
            moneyListNumber = (TextView) itemView.findViewById(R.id.me_money_cash_list_item_number);
            moneyListSymbol = (TextView) itemView.findViewById(R.id.me_money_cash_list_item_rmb);
        }


        // 设置颜色
        private void setItemColor(int c){
            setTextColor(moneyListNumber,c);
            setTextColor(moneyListSymbol,c);
        }

        public void bindingDate(Accounts moneyHistory){
            SimpleDateFormat createTime = new SimpleDateFormat("yyyy/MM/dd");
            String totalAmount = String.format("%.2f", moneyHistory.getTotalAmount());
            switch (moneyHistory.getOrderType()){

                case -2:
                    moneyListStatusIv.setImageResource(R.drawable.me_icon_jiangli_default);
                    moneyListTime.setText(createTime.format(moneyHistory.getCreateTime()));
                    moneyListNumber.setText("+"+totalAmount);
                    moneyListStatusTv.setText("奖励");
                    setItemColor(MONEY_TEXT_GREEN);
                    break;

                case -1:
                    moneyListStatusIv.setImageResource(R.drawable.me_icon_tixianchenggong_default);
                    moneyListTime.setText(createTime.format(moneyHistory.getCreateTime()));
                    if(moneyHistory.getOrderStatus() == -3){
                        moneyListStatusTv.setText("提现审核中");
                        moneyListNumber.setText("-"+totalAmount);
                    }else if(moneyHistory.getOrderStatus() == -1){
                        moneyListNumber.setText("+"+totalAmount);
                        moneyListStatusTv.setText("提现失败");
                        setItemColor(MONEY_TEXT_GREEN);
                    }else if(moneyHistory.getOrderStatus() == 1){
                        moneyListNumber.setText("-"+totalAmount);
                        moneyListStatusTv.setText("提现成功");
                    }else if(moneyHistory.getOrderStatus() == -2){
                        moneyListNumber.setText("-"+totalAmount);
                        moneyListStatusTv.setText("提现审核失败");
                        setItemColor(MONEY_TEXT_GREEN);
                    }
                    break;

                case 1:
                    moneyListStatusIv.setImageResource(R.drawable.me_icon_dashang_default);
                    moneyListTime.setText(createTime.format(moneyHistory.getCreateTime()));
                    moneyListNumber.setText("-"+totalAmount);
                    moneyListStatusTv.setText("打赏");
                    break;

                case 2:
                    moneyListStatusIv.setImageResource(R.drawable.me_icon_chaojibiaoqingzhifu_default);
                    moneyListTime.setText(createTime.format(moneyHistory.getCreateTime()));
                    moneyListNumber.setText("-"+totalAmount);
                    moneyListStatusTv.setText("活动支付");
                    break;

                case 3:
                    moneyListStatusIv.setImageResource(R.drawable.me_icon_chaojibiaoqingzhifu_default);
                    moneyListTime.setText(createTime.format(moneyHistory.getCreateTime()));
                    moneyListNumber.setText("-"+totalAmount);
                    moneyListStatusTv.setText("超级表情");
                    break;
            }
        }
    }
    */
}
