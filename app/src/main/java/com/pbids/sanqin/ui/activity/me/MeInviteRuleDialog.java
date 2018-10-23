package com.pbids.sanqin.ui.activity.me;

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
 *         Description : 邀请规则
 *         Date :Create in 2018/6/29 15:23
 *         Modified By :
 */

public class MeInviteRuleDialog extends Dialog {

    private Context mContext;

    public MeInviteRuleDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.dialog_me_invite_rule, null);
        ButterKnife.bind(this, view);
        setContentView(view);
    }

    @OnClick(R.id.invite_rule_dismiss)
    public void onViewClicked() {
        dismiss();
    }
}
