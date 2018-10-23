package com.pbids.sanqin.ui.activity.me;

import com.pbids.sanqin.common.BaseView;
import com.pbids.sanqin.model.entity.Bank;

/**
 * Created by pbids903 on 2018/1/20.
 */

public interface MeScanBankView extends BaseView{
    void getBank(Bank bank);
    void getIsBindCard(int isBindCard);
}
