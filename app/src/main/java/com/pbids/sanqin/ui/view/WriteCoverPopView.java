package com.pbids.sanqin.ui.view;

import android.content.Context;
import android.view.LayoutInflater;

import com.pbids.sanqin.R;
import com.pbids.sanqin.common.CustomPopView;

/**
 * Created by pbids903 on 2017/11/24.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:27
 * @desscribe 类描述:姓氏认证覆盖view
 * @remark 备注:
 * @see
 */
public class WriteCoverPopView extends CustomPopView {
    public WriteCoverPopView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.app_first_cover, contentContainer);
    }

}
