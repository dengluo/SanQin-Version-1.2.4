package com.pbids.sanqin.ui.activity.zongquan;

import com.pbids.sanqin.common.BaseView;
import com.pbids.sanqin.model.entity.ZCRankingInfo;

import java.util.List;

/**
 * @author 巫哲豪
 * @date on 2018/3/5 17:05
 * @desscribe 类描述:家族宗祠贡献排行
 * @remark 备注:
 * @see
 */

public interface ZCRankingView extends BaseView {
    void getZCRankInfo(List<ZCRankingInfo> rankingInfos);
}
