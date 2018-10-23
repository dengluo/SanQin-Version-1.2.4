package com.pbids.sanqin.ui.activity.zhizong;

import com.pbids.sanqin.common.BaseView;
import com.pbids.sanqin.common.PayView;
import com.pbids.sanqin.model.entity.CampaignEnrollEntity;
import com.pbids.sanqin.model.entity.CampaignEnrollExtendEntity;

/**
 * Created by pbids903 on 2018/1/30.
 */

public interface CampaignEnrollView extends BaseView{
    public void getInfomation(CampaignEnrollExtendEntity enrollExtendEntity);
}
