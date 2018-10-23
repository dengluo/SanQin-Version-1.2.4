package com.pbids.sanqin.ui.recyclerview.adapter.base;

import android.content.Context;


import com.pbids.sanqin.ui.recyclerview.adapter.base.GroupedListAdapter;
import com.pbids.sanqin.ui.recyclerview.adapter.base.GroupedRecyclerViewAdapter;
import com.pbids.sanqin.ui.recyclerview.entity.GroupEntity;
import com.pbids.sanqin.ui.recyclerview.holder.BaseViewHolder;

import java.util.ArrayList;

/**
 * 这是不带组头的Adapter。
 *只需要{@link GroupedRecyclerViewAdapter#hasHeader(int)}方法返回false就可以去掉组尾了。
 */
public class NoHeaderAdapter extends GroupedListAdapter {

    public NoHeaderAdapter(Context context, ArrayList<GroupEntity> groups) {
        super(context, groups);
    }

    /**
     * 返回false表示没有组头
     *
     * @param groupPosition
     * @return
     */
    @Override
    public boolean hasHeader(int groupPosition) {
        return false;
    }

    /**
     * 当hasHeader返回false时，这个方法不会被调用。
     *
     * @return
     */
    @Override
    public int getHeaderLayout(int viewType) {
        return 0;
    }

    /**
     * 当hasHeader返回false时，这个方法不会被调用。
     *
     * @param holder
     * @param groupPosition
     */
    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {

    }

}
