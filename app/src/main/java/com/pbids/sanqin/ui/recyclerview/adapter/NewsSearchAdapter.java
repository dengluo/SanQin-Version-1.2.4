package com.pbids.sanqin.ui.recyclerview.adapter;

import android.content.Context;

import com.pbids.sanqin.R;
import com.pbids.sanqin.model.entity.NewsArticle;
import com.pbids.sanqin.model.entity.NewsInformation;
import com.pbids.sanqin.ui.recyclerview.holder.BaseViewHolder;

import java.util.List;

/**新闻查找
 * adapter
 * Created by caiguoliang on 2018/1/19.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:05
 * @desscribe 类描述:首页的资讯列表list设配器
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.zhizong.ZhiZongFragment
 */
public class NewsSearchAdapter extends BaseNewsAdapter {

    boolean isLoaded = false;

    public NewsSearchAdapter(Context context, List<NewsInformation> newsInformation) {
         super(context,newsInformation);
    }

    @Override
    public boolean hasFooter(int groupPosition) {
        //return newsInformations.get(groupPosition).getIsmore()==1?true:false;
        return false;
    }

    @Override
    public int getFooterLayout(int viewType) {
        return R.layout.hp_news_more;
    }

    @Override
    public boolean hasHeader(int groupPosition) {
        if (!isLoaded) return false;
        return newsInformations.get(groupPosition).getList().size() == 0;
    }

    @Override
    public int getHeaderLayout(int viewType) {
        return R.layout.no_data;
    }

    public void setLoaded( boolean s){
        isLoaded = s;
    }

    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {
        super.onBindChildViewHolder(holder, groupPosition, childPosition);
        NewsInformation newsInformation = newsInformations.get(groupPosition);
//        NewsArticle newsArticle = newsInformation.getList().get(childPosition);
        /*
        if((newsInformation.getList().size()-1)==childPosition){
            Log.i("wzh","groupPosition :"+groupPosition);
            Log.i("wzh","childPosition :"+childPosition);
            Log.i("wzh","newsInformation.getIsmore() :"+newsInformation.getIsmore());

            View view = holder.get(R.id.home_item_decoration);
            view.setVisibility(View.GONE);
        }else{
            View view = holder.get(R.id.home_item_decoration);
            view.setVisibility(View.VISIBLE);
        }
        */
        //View view = holder.get(R.id.home_item_decoration);
        //view.setVisibility(View.VISIBLE);
    }

}
