package com.pbids.sanqin.base;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

public class BaaseRecyclerViewHolder extends RecyclerView.ViewHolder {


    /**
     * Views indexed with their IDs
     */
    protected final SparseArray<View> views;

    protected View mItemView;


    public BaaseRecyclerViewHolder(View itemView) {
        super(itemView);
        this.views = new SparseArray<View>();
        mItemView = itemView;
    }


    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = mItemView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }
}
