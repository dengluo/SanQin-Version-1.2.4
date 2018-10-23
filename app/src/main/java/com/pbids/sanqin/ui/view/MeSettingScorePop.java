package com.pbids.sanqin.ui.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.pbids.sanqin.R;
import com.pbids.sanqin.common.CustomPopView;
import com.pbids.sanqin.model.entity.AppInfo;
import com.pbids.sanqin.utils.AppMarketUtil;
import com.pbids.sanqin.utils.AppUtils;

import java.util.ArrayList;

/**
 * Created by pbids903 on 2017/12/12.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:18
 * @desscribe 类描述:应用市场评分pop
 * @remark 备注:
 * @see
 */
public class MeSettingScorePop extends CustomPopView{
    public MeSettingScorePop(final Context context, ArrayList<AppInfo> appInfos) {
        super(context);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        final View view = layoutInflater.inflate(R.layout.pop_me_setting_score,null,false);
        WarpLinearLayout layout = (WarpLinearLayout) view.findViewById(R.id.me_setting_score_layout);
        for(int i=0;i<appInfos.size();i++){
            final AppInfo appInfo = appInfos.get(i);
            View view1 = layoutInflater.inflate(R.layout.pop_me_setting_score_item,null,false);
            ImageView imageView = (ImageView) view1.findViewById(R.id.me_setting_score_item_iv);
            TextView textView = (TextView) view1.findViewById(R.id.me_setting_score_item_tv);
            imageView.setImageDrawable(appInfo.getAppIcon());
            textView.setText(appInfo.getAppName());

            view1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppMarketUtil.launchAppDetail(context,context.getPackageName(),appInfo.getPackageName());
                }
            });

            layout.addView(view1);
        }

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.i("wzh","view_donate_records.getHeight(): "+view.getHeight());
                Log.i("wzh","AppUtils.getScreenSize(mContext))[1](): "+(AppUtils.getScreenSize(context))[1]);
//                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//                params.setMargins(0,AppUtils.getScreenSize(mContext)[1]-view_donate_records.getHeight(),0,0);
//                view_donate_records.setLayoutParams(params);
//                view_donate_records.setTop();
                view.setY(AppUtils.getScreenSize(context)[1]-(view.getHeight()+AppUtils.getStatusBarHeight(context)));
            }
        });
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        view.setLayoutParams(params);
//        Log.i("wzh","view_donate_records.getHeight(): "+view_donate_records.getHeight());
        contentContainer.addView(view);
    }
}
