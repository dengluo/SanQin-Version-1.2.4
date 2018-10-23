package com.pbids.sanqin.ui.activity.me;

import com.pbids.sanqin.base.BaaseView;
import com.pbids.sanqin.base.ComonRecycerGroup;
import com.pbids.sanqin.model.entity.Accounts;
import com.pbids.sanqin.model.entity.AccountsGroup;

import java.util.List;
import java.util.Map;


/**
 * @author 巫哲豪
 * @date on 2018/3/2 14:14
 * @desscribe 类描述:
 * @remark 备注:流水view接口
 * @see
 */
public interface MeAccountsView extends BaaseView {
    void getAccounts(Map<String,List<Accounts>> accountsMap);
    void getAccountsGroup(List<ComonRecycerGroup<Accounts>> accountsGroups);
}
