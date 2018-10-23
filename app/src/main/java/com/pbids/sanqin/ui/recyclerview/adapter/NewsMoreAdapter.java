package com.pbids.sanqin.ui.recyclerview.adapter;

import android.content.Context;
import android.view.View;

import com.pbids.sanqin.R;
import com.pbids.sanqin.model.entity.NewsInformation;
import com.pbids.sanqin.ui.recyclerview.holder.BaseViewHolder;

import java.util.List;


/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:05
 * @desscribe 类描述:首页的资讯列表list中加载更多
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.zhizong.ZhiZongMoreFragment
 */
public class NewsMoreAdapter extends BaseNewsAdapter{


    public NewsMoreAdapter(Context context, List<NewsInformation> newsInformations) {
        super(context, newsInformations);
    }

    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {
        super.onBindChildViewHolder(holder, groupPosition, childPosition);
        NewsInformation newsInformation = newsInformations.get(groupPosition);
        if((newsInformation.getList().size()-1)==childPosition){
            View view = holder.get(R.id.home_item_decoration);
            view.setVisibility(View.GONE);
        }else{
            View view = holder.get(R.id.home_item_decoration);
            view.setVisibility(View.VISIBLE);
        }
    }
}
