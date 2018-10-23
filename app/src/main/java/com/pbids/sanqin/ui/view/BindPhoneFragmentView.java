package com.pbids.sanqin.ui.view;

import com.pbids.sanqin.base.BaaseView;

public interface BindPhoneFragmentView extends BaaseView{

    void onHttpError(String type);

    void onHttpSuccess();

    void SMSCode(String code);

}
