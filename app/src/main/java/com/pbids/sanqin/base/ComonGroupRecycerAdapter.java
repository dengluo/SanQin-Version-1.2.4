package com.pbids.sanqin.base;

import android.content.Context;

import com.pbids.sanqin.ui.recyclerview.holder.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 *  公共 recycerAdapter 组件
 */


public class ComonGroupRecycerAdapter<T> extends BaaseSwipeGroupRecycerViewAdapter<ComonRecycerGroup>  {


    public ComonGroupRecycerAdapter(Context context,  int childLayoutId) {
        super(context, null);
        //默认创建一个组
        gLists = new ArrayList<>();
        gLists.add(new ComonRecycerGroup());
        this.childLayoutId = childLayoutId;
    }

    public ComonGroupRecycerAdapter(Context context, List<ComonRecycerGroup> glist, int childLayoutId) {
        super(context, glist);
        this.childLayoutId = childLayoutId;
    }
    public ComonGroupRecycerAdapter(Context context, List<ComonRecycerGroup> glist, int childLayoutId, int headerLayoutId) {
        super(context, glist);
        this.childLayoutId = childLayoutId;
        this.headerLayoutId = headerLayoutId;
    }
    public ComonGroupRecycerAdapter(Context context, List<ComonRecycerGroup> glist, int childLayoutId, int headerLayoutId,int footerLayoutId) {
        super(context, glist);
        this.childLayoutId = childLayoutId;
        this.headerLayoutId = headerLayoutId;
        this.footerLayoutId = footerLayoutId;
    }

    @Override
    public boolean hasHeader(int groupPosition) {
        if(this.headerLayoutId>0){
            return true;
        }
        return false;
    }

    public ComonRecycerGroup getFirstGroup(){
        if(this.gLists.size()==0){
            throw new NullPointerException("ComonRecycerGroup glist size is 0 ... ");
        }
        return this.gLists.get(0);
    }

    public T getChild(int groupPosition, int childPosition){
        List<ComonRecycerGroup> groups = getList();
        ComonRecycerGroup item = groups.get(groupPosition);
        List<T> lists = item.getList();
        return lists.get(childPosition);
    }

    @Override
    public boolean hasFooter(int groupPosition) {
        if(this.footerLayoutId>0){
            return true;
        }
        return false;
    }

    @Override
    public int getHeaderLayout(int viewType) {
        return this.headerLayoutId;
    }

    @Override
    public int getFooterLayout(int viewType) {
        return this.footerLayoutId;
    }

    @Override
    public int getChildLayout(int viewType) {
        return this.childLayoutId;
    }


    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {
        if(mViewHolder!=null){
            mViewHolder.onBindHeaderViewHolder(holder,groupPosition);
        }
    }

    @Override
    public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {
        if(mViewHolder!=null){
            mViewHolder.onBindFooterViewHolder(holder,groupPosition);
        }
    }

    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {
        if(mViewHolder!=null){
            mViewHolder.onBindChildViewHolder(holder,groupPosition,childPosition);
        }
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return 0;
    }

    private ViewHolder mViewHolder;
    public void setViewHolder(ViewHolder viewHolder){
        this.mViewHolder = viewHolder;
    }

    public interface ViewHolder{
        void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition);
        void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition);
        void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition);
    }
}
