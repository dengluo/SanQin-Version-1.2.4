package com.pbids.sanqin.ui.activity.zongquan;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.pbids.sanqin.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author : 上官名鹏
 *         Description :
 *         Date :Create in 2018/7/19 15:11
 *         Modified By :
 */

public class ZhiZongAddTeamFaildDialog extends Dialog {

    private Context mContext;

    public ZhiZongAddTeamFaildDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.dialog_add_team_faild, null);
        ButterKnife.bind(this,view);
        setContentView(view);

    }

    @OnClick(R.id.add_team_faild_ok_bt)
    public void onViewClicked() {
        dismiss();
    }
}
