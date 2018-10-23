package com.pbids.sanqin.ui.activity.zhizong;

import com.pbids.sanqin.common.BaseView;
import com.pbids.sanqin.model.entity.CampaignEnrollEntity;
import com.pbids.sanqin.model.entity.CampaignEnrollExtendEntity;
import com.pbids.sanqin.model.entity.NewsArticle;

import java.util.List;

/**
 * Created by pbids903 on 2018/2/24.
 */

public interface CampaignEnrollOverView extends BaseView{
    public void getInfomation(List<CampaignEnrollEntity> enrollEntity, CampaignEnrollExtendEntity enrollExtendEntity, NewsArticle newsArticle);
}
