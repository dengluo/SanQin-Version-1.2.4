package com.pbids.sanqin.session.action;

import android.widget.Toast;

import com.pbids.sanqin.R;
import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;

/**
 * Created by huangjun on 2015/7/7.
 */
public class RTSAction extends BaseAction {

    public RTSAction() {
        super(R.drawable.message_plus_rts_selector, R.string.input_panel_RTS);
    }

    @Override
    public void onClick() {
        if (NetworkUtil.isNetAvailable(getActivity())) {
//            RTSActivity.startSession(getActivity(), getAccount(), RTSActivity.FROM_INTERNAL);
        } else {
            Toast.makeText(getActivity(), R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
        }

    }
}
