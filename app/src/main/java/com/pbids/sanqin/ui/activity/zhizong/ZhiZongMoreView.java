package com.pbids.sanqin.ui.activity.zhizong;

import com.pbids.sanqin.common.BaseView;
import com.pbids.sanqin.model.entity.NewsArticle;
import com.pbids.sanqin.model.entity.NewsInformation;

import java.util.List;

/**
 * Created by pbids903 on 2018/1/20.
 */

public interface ZhiZongMoreView extends BaseView{
    void getMoreNewsInformation(List<NewsInformation> newsInformations,String type);
}
