package com.pbids.sanqin.common;

import android.view.View;

import com.pbids.sanqin.ui.recyclerview.adapter.base.GroupedRecyclerViewAdapter;

/**
 * Created by pbids903 on 2018/3/23.
 */

public abstract class OnItemClickListener<T extends GroupedRecyclerViewAdapter> {
    public void onItemLongClick(T adapter, View view, int position) {}

    public void onItemChildClick(T adapter, View view, int position) {}

    public void onItemChildLongClick(T adapter, View view, int position) {}
}
