package com.pbids.sanqin.ui.recyclerview.adapter;

import android.content.Context;
import android.widget.TextView;

import com.pbids.sanqin.R;
import com.pbids.sanqin.model.entity.QinQinChiUserGroup;
import com.pbids.sanqin.model.entity.QinQinChiUserGroupItem;
import com.pbids.sanqin.ui.recyclerview.adapter.base.GroupedRecyclerViewAdapter;
import com.pbids.sanqin.ui.recyclerview.holder.BaseViewHolder;

import java.util.List;

/**
 * adapter
 * 模块：亲亲池 -> 分组
 * Created by caiguoliang 2018-3-2
 */

public class QinQinChiGroupListAdapter extends GroupedRecyclerViewAdapter {

    //列表数据
    List<QinQinChiUserGroup> userGroups;
    Context mContext;

    public QinQinChiGroupListAdapter(Context context, List<QinQinChiUserGroup> userGroups) {
        super(context);
        mContext = context;
        this.userGroups = userGroups;
    }

    @Override
    public boolean hasFooter(int groupPosition) {
        return false;
    }
    @Override
    public int getFooterLayout(int viewType) {
        return 0;
    }
    @Override
    public boolean hasHeader(int groupPosition) { return false; }
    @Override
    public int getHeaderLayout(int viewType) {
        return 0;
    }

    /**
     * 分组数量
     * @return
     */
    @Override
    public int getGroupCount() {
        return userGroups == null ? 0 : userGroups.size();
    }

    /**
     * 列表总数
     * @param groupPosition
     * @return
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return userGroups.get(groupPosition) == null ? 0 : userGroups.get(groupPosition).getGroups().size();
    }


    @Override
    public int getChildLayout(int viewType) {
        return R.layout.adapter_zong_quan_group_item;
    }

    //bind header view_donate_records
    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {
        //没有Header 不需要绑定
    }

    //bind footer view_donate_records
    @Override
    public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {
        //没有footer 不需要绑定
    }

    //bind 列表视图
    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, final int groupPosition, final int childPosition) {
    	TextView tvGroupName =  (TextView) holder.get(R.id.tv_group_name);
		QinQinChiUserGroupItem groupItem = userGroups.get(groupPosition).getGroups().get(childPosition);
		tvGroupName.setText(groupItem.getName());

    }

}
