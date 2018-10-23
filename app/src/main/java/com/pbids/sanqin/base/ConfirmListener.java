package com.pbids.sanqin.base;

import android.view.View;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:37
 * @desscribe 类描述:dialog的onclick回调接口
 * @remark 备注:
 * @see
 */

public interface ConfirmListener {
    void confirm(ConfirmDialogView dialog , View view);
    void cancel(ConfirmDialogView dialog , View view);
}
