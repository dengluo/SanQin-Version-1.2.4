package com.pbids.sanqin.ui.adapter;

import android.content.Context;

import com.pbids.sanqin.base.ComonGroupRecycerAdapter;
import com.pbids.sanqin.base.ComonRecycerGroup;
import com.pbids.sanqin.model.entity.GroupList;
import com.pbids.sanqin.model.entity.TeamGroupInfo;
import com.pbids.sanqin.presenter.ZhiZongCasesCrowdPresenter;
import com.pbids.sanqin.ui.recyclerview.holder.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : 上官名鹏
 * Description : 宗人群首页适配器
 * Date :Create in 2018/8/13 11:30
 * Modified By :
 */
public class ZhiZongCasesCrowdAdpter extends ComonGroupRecycerAdapter{

    private ComonRecycerGroup<GroupList> comonRecycerGroup = new ComonRecycerGroup<>();

    private ComonRecycerGroup<TeamGroupInfo> comonRecycerGroupItem = new ComonRecycerGroup<>();

    private List<ComonRecycerGroup> groupList,groupListItem = new ArrayList<>();

    public ZhiZongCasesCrowdAdpter(Context context, List glist, int childLayoutId, int headerLayoutId) {
        super(context, glist, childLayoutId, headerLayoutId);
        super.setViewHolder(new ViewHolder());
    }

    class ViewHolder implements ComonGroupRecycerAdapter.ViewHolder{

        @Override
        public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {

        }

        @Override
        public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {

        }

        @Override
        public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {

        }
    }


}
