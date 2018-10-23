package com.pbids.sanqin.ui.activity.me;

import com.pbids.sanqin.common.BaseView;
import com.pbids.sanqin.model.entity.Bank;

import java.util.List;

/**
 * Created by pbids903 on 2018/1/15.
 */

public interface MeBindingBankView extends BaseView{
    void getBanks(List<Bank> banks);
}
