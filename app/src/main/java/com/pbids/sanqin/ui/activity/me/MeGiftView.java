package com.pbids.sanqin.ui.activity.me;

import android.support.v7.widget.RecyclerView;

import com.pbids.sanqin.common.BaseView;
import com.pbids.sanqin.model.entity.GiftGroup;
import com.pbids.sanqin.ui.recyclerview.adapter.MeGiftGroupListAdapter;

import java.util.List;

/**
 * view_donate_records
 * 模块：我的 -> 我的礼券
 * Created by pbids903 on 2017/12/28.
 */

public interface MeGiftView extends BaseView{

    //void getGiftGroup(List<GiftGroup> giftGroups);
    MeGiftGroupListAdapter getMeGiftGroupListAdapter();

}
