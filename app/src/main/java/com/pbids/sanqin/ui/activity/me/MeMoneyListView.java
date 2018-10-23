package com.pbids.sanqin.ui.activity.me;

import com.pbids.sanqin.base.BaaseView;
import com.pbids.sanqin.model.entity.Accounts;

import java.util.List;

/**
 * caiguoliang
 * 我的资金
 */

public interface MeMoneyListView extends BaaseView {

    void getMoneyHistoriesInfo(long id,float balance,int fee,int minAmount,List<Accounts> moneyHistories ,int withdrawalThreshold);

    void isLastData();

    void loadError();

    void allowWithDraw();

    void errorWithDraw(String message);

}
