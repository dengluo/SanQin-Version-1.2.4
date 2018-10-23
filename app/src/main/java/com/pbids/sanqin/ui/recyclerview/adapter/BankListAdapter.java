package com.pbids.sanqin.ui.recyclerview.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pbids.sanqin.R;
import com.pbids.sanqin.common.CommonUtil;
import com.pbids.sanqin.model.entity.Bank;
import com.pbids.sanqin.ui.recyclerview.adapter.base.GroupedRecyclerViewAdapter;
import com.pbids.sanqin.ui.recyclerview.holder.BaseViewHolder;
import com.pbids.sanqin.utils.NumberUtil;

import java.util.List;

/**
 * Created by pbids903 on 2018/1/15.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:03
 * @desscribe 类描述:显示银行卡的设配器
 * @remark 备注:
 * @see
 */
public class BankListAdapter extends GroupedRecyclerViewAdapter {
    Context mContext;
    List<Bank> banks;
    public BankListAdapter(Context context, List<Bank> banks) {
        super(context);
        mContext = context;
        this.banks = banks;
    }

    public void clear(){
        banks.clear();
        notifyDataSetChanged();
    }

    public List<Bank> getBanks() {
        return banks;
    }

    public void setBanks(List<Bank> banks) {
        this.banks = banks;
    }

    @Override
    public int getGroupCount() {
        return banks==null?0:banks.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 0;
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
        return R.layout.adapter_bank;
    }

    @Override
    public int getFooterLayout(int viewType) {
        return 0;
    }

    @Override
    public int getChildLayout(int viewType) {
        return 0;
    }

    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {
        ImageView bankIcon =  holder.get(R.id.me_bank_icon);
        TextView bankName =  holder.get(R.id.me_bank_name);
        TextView bankType =  holder.get(R.id.me_bank_type);
        TextView meBankCard =  holder.get(R.id.me_bank_card);
        ImageView bankLayout =  holder.get(R.id.me_bank_bg);

        bankName.setText(banks.get(groupPosition).getBankName());
        String cardType = banks.get(groupPosition).getCardType();
        bankType.setText(cardType);
//        if(cardType==1){
//            bankType.setText("借记卡");
//        }else if(cardType==2){
//            bankType.setText("信用卡");
//        }spaceAt4
        String cardNumber = banks.get(groupPosition).getCardNumber();
//        spaceAt4(cardNumber);

//        meBankCard.setText(spaceAt4(cardNumber));
        meBankCard.setText(NumberUtil.spaceAt4(CommonUtil.shieldCardNumber(cardNumber)));

        switch (banks.get(groupPosition).getBankCode()){
            case "ICBC"://工商银行
                bankIcon.setImageResource(R.drawable.me_icon_gongshang_default);
                bankLayout.setImageResource(R.drawable.me_bg_gongshang_default);
                bankLayout.setBackgroundResource(R.drawable.bank_gongshang);
                break;
            case "ABC"://农业银行
                bankIcon.setImageResource(R.drawable.me_icon_nongye_default);
                bankLayout.setImageResource(R.drawable.me_bg_nongye_default);
                bankLayout.setBackgroundResource(R.drawable.bank_nongye);
                break;
            case "BOC"://中国银行
                bankIcon.setImageResource(R.drawable.me_icon_china_default);
                bankLayout.setImageResource(R.drawable.me_bg_china_default);
                bankLayout.setBackgroundResource(R.drawable.bank_zhongguo);
                break;
            case "CCB"://建设银行
                bankIcon.setImageResource(R.drawable.me_icon_jianshe_default);
                bankLayout.setImageResource(R.drawable.me_bg_jianshe_default);
                bankLayout.setBackgroundResource(R.drawable.bank_jianshe);
                break;
            case "SPABANK"://平安银行
                bankIcon.setImageResource(R.drawable.me_icon_pingan_default);
                bankLayout.setImageResource(R.drawable.me_bg_pingan_default);
                bankLayout.setBackgroundResource(R.drawable.bank_pingan);
                break;
            case "CIB"://兴业银行
                bankIcon.setImageResource(R.drawable.me_icon_xingye_default);
                bankLayout.setImageResource(R.drawable.me_bg_xingye_default);
                bankLayout.setBackgroundResource(R.drawable.bank_xingye);
                break;
            case "CEB"://光大银行
                bankIcon.setImageResource(R.drawable.me_icon_guangda_default);
                bankLayout.setImageResource(R.drawable.me_bg_guangda_default);
                bankLayout.setBackgroundResource(R.drawable.bank_guangda);
                break;
            case "SHBANK"://上海银行
                bankIcon.setImageResource(R.drawable.me_icon_shenzhenfazhan_default);
                bankLayout.setImageResource(R.drawable.me_bg_shenzhenfazhan_default);
                bankLayout.setBackgroundResource(R.drawable.bank_shanghai);
                break;
            case "SPDB"://浦发银行
                bankIcon.setImageResource(R.drawable.me_icon_shanghaipudong_default);
                bankLayout.setImageResource(R.drawable.me_bg_shanghaipudong_default);
                bankLayout.setBackgroundResource(R.drawable.bank_shanghaipufa);
                break;
            case "CITIC"://中信银行
                bankIcon.setImageResource(R.drawable.me_icon_zhongxin_default);
                bankLayout.setImageResource(R.drawable.me_bg_zhongxin_default);
                bankLayout.setBackgroundResource(R.drawable.bank_zhongxin);
                break;
            case "HXBANK"://华夏银行
                bankIcon.setImageResource(R.drawable.me_icon_huaxia_default);
                bankLayout.setImageResource(R.drawable.me_bg_huaxia_default);
                bankLayout.setBackgroundResource(R.drawable.bank_huaxia);
                break;
            case "BJBANK"://北京银行
                bankIcon.setImageResource(R.drawable.me_icon_beijing_default);
                bankLayout.setImageResource(R.drawable.me_bg_beijing_default);
                bankLayout.setBackgroundResource(R.drawable.bank_beijing);
                break;
            case "COMM"://交通银行
                bankIcon.setImageResource(R.drawable.me_icon_jiaotong_default);
                bankLayout.setImageResource(R.drawable.me_bg_jiaotong_default);
                bankLayout.setBackgroundResource(R.drawable.bank_china_jiaotong);
                break;
            case "CMB"://招商银行
                bankIcon.setImageResource(R.drawable.me_icon_zhaoshang_default);
                bankLayout.setImageResource(R.drawable.me_bg_zhaoshang_default);
                bankLayout.setBackgroundResource(R.drawable.bank_zhaoshang);
                break;
            case "PSBC"://邮政储蓄银行
                bankIcon.setImageResource(R.drawable.me_icon_youzheng_default);
                bankLayout.setImageResource(R.drawable.me_bg_youzheng_default);
                bankLayout.setBackgroundResource(R.drawable.bank_youzheng);
                break;
            case "CMBC"://民生银行
                bankIcon.setImageResource(R.drawable.me_icon_minsheng_default);
                bankLayout.setImageResource(R.drawable.me_bg_minsheng_default);
                bankLayout.setBackgroundResource(R.drawable.bank_minsheng);
                break;
            case "GDB"://广发银行
                bankIcon.setImageResource(R.drawable.me_icon_guangdongfazhan_default);
                bankLayout.setImageResource(R.drawable.me_bg_guangdongfazhan_default);
                bankLayout.setBackgroundResource(R.drawable.bank_guangdongfazhan);
                break;
//            case 18://花旗银行
//                bankIcon.setImageResource(R.drawable.me_icon_huaqi_default);
//                bankLayout.setImageResource(R.drawable.me_bg_huaqi_default);
//                bankLayout.setBackgroundResource(R.drawable.bank_huaqi);
//                break;
            default:
                bankIcon.setVisibility(View.INVISIBLE);
                bankLayout.setBackgroundResource(R.drawable.bank_huaqi);
                break;
        }
    }

    @Override
    public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {

    }

    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {

    }
}
