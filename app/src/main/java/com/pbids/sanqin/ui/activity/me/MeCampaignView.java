package com.pbids.sanqin.ui.activity.me;

import com.pbids.sanqin.common.BaseView;
import com.pbids.sanqin.model.entity.CampaignInfo;
import com.pbids.sanqin.model.entity.NewsArticle;

import java.util.ArrayList;

/**
 * Created by pbids903 on 2017/12/28.
 */

public interface MeCampaignView extends BaseView{
    void onUpdataInformation(ArrayList<NewsArticle> newsArticles);
}
