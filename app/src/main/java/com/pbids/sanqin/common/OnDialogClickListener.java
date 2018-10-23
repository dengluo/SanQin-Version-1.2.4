package com.pbids.sanqin.common;

import android.view.View;

import com.pbids.sanqin.ui.view.dialog.BaseDialog;
import com.pbids.sanqin.ui.view.dialog.OneImageOneBtDialog;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:37
 * @desscribe 类描述:dialog的onclick回调接口
 * @remark 备注:
 * @see
 */

public interface OnDialogClickListener {
    void confirm(View view);
    void cancel(View view);
//    void confirm(BaseDialog dialog , View view);
//    void cancel(BaseDialog dialog , View view);

}
