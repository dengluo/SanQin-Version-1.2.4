package com.pbids.sanqin.ui.recyclerview.adapter.base;

import android.content.Context;
//import com.pbids.sanqin.ui.recyclerview.swipe.Attributes;
//import com.pbids.sanqin.ui.recyclerview.swipe.SwipeAdapterInterface;
//import com.pbids.sanqin.ui.recyclerview.swipe.SwipeItemMangerInterface;
//import com.pbids.sanqin.ui.recyclerview.swipe.SwipeItemRecyclerMangerImpl;
//import com.pbids.sanqin.ui.recyclerview.swipe.SwipeLayout;
//import com.daimajia.swipe.SwipeLayout;
//import com.daimajia.swipe.implments.SwipeItemRecyclerMangerImpl;
//import com.daimajia.swipe.interfaces.SwipeAdapterInterface;
//import com.daimajia.swipe.interfaces.SwipeItemMangerInterface;
//import com.daimajia.swipe.util.Attributes;

import com.pbids.sanqin.ui.recyclerview.adapter.QinQinChiFriendAdapter;
import com.pbids.sanqin.ui.recyclerview.swipe.SwipeLayout;
import com.pbids.sanqin.ui.recyclerview.swipe.implments.SwipeItemRecyclerMangerImpl;
import com.pbids.sanqin.ui.recyclerview.swipe.interfaces.SwipeAdapterInterface;
import com.pbids.sanqin.ui.recyclerview.swipe.interfaces.SwipeItemMangerInterface;
import com.pbids.sanqin.ui.recyclerview.swipe.util.Attributes;

import java.util.List;

/**
 * Created by pbids903 on 2018/1/24.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:00
 * @desscribe 类描述:带侧滑，带头尾的RecyclerView设配器（公用）
 * @remark 备注:下面的是列子
 * @see QinQinChiFriendAdapter
 */
public abstract class SwipeGroupedRecyclerViewAdapter extends GroupedRecyclerViewAdapter implements SwipeItemMangerInterface, SwipeAdapterInterface {
    public SwipeItemRecyclerMangerImpl mItemManger = new SwipeItemRecyclerMangerImpl(this);

    public SwipeGroupedRecyclerViewAdapter(Context context) {
        super(context);
    }

    public void openItem(int position) {
        this.mItemManger.openItem(position);
    }

    public void closeItem(int position) {
        this.mItemManger.closeItem(position);
    }

    public void closeAllExcept(SwipeLayout layout) {
        this.mItemManger.closeAllExcept(layout);
    }

    public void closeAllItems() {
        this.mItemManger.closeAllItems();
    }

    public List<Integer> getOpenItems() {
        return this.mItemManger.getOpenItems();
    }

    public List<SwipeLayout> getOpenLayouts() {
        return this.mItemManger.getOpenLayouts();
    }

    public void removeShownLayouts(SwipeLayout layout) {
        this.mItemManger.removeShownLayouts(layout);
    }

    public boolean isOpen(int position) {
        return this.mItemManger.isOpen(position);
    }

    public Attributes.Mode getMode() {
        return this.mItemManger.getMode();
    }

    public void setMode(Attributes.Mode mode) {
        this.mItemManger.setMode(mode);
    }

}
