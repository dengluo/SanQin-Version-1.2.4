package com.pbids.sanqin.ui.view.letter;

import android.content.Context;

import com.pbids.sanqin.ui.recyclerview.adapter.base.SwipeGroupedRecyclerViewAdapter;
import com.pbids.sanqin.ui.recyclerview.holder.BaseViewHolder;

/**
 * Created by pbids903 on 2018/2/22.
 */

public class GroupingRecylerViewAdapter extends SwipeGroupedRecyclerViewAdapter {

    public GroupingRecylerViewAdapter(Context context) {
        super(context);
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return 0;
    }

    @Override
    public int getGroupCount() {
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 0;
    }

    @Override
    public boolean hasHeader(int groupPosition) {
        return false;
    }

    @Override
    public boolean hasFooter(int groupPosition) {
        return false;
    }

    @Override
    public int getHeaderLayout(int viewType) {
        return 0;
    }

    @Override
    public int getFooterLayout(int viewType) {
        return 0;
    }

    @Override
    public int getChildLayout(int viewType) {
        return 0;
    }

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
