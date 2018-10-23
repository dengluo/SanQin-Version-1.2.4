package com.pbids.sanqin.ui.pay.union_pay;

import com.pbids.sanqin.base.BaaseView;

/**
 * 银生宝支付
 */

public interface MeUnionPayView extends BaaseView {
    //    void getPayInformationForYSB(String info);
    void openPayUrl(String payUrl);

}

