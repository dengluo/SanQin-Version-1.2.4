package com.pbids.sanqin.ui.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pbids.sanqin.R;
import com.pbids.sanqin.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:23
 * @desscribe 类描述:烧砖中的签到天数view
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.zongquan.BrickFragment
 */
public class SignInDaysView extends RelativeLayout {

    Context mContext;
    @Bind(R.id.prestige_1)
    TextView prestige1;
    @Bind(R.id.prestige_2)
    TextView prestige2;
    @Bind(R.id.prestige_3)
    TextView prestige3;
    @Bind(R.id.prestige_4)
    TextView prestige4;
    @Bind(R.id.prestige_5)
    TextView prestige5;
    @Bind(R.id.prestige_6)
    TextView prestige6;
    @Bind(R.id.prestige_7)
    TextView prestige7;
    @Bind(R.id.zong_quan_progress)
    MutiProgress zongQuanProgress;
    @Bind(R.id.day_1)
    TextView day1;
    @Bind(R.id.day_2)
    TextView day2;
    @Bind(R.id.day_3)
    TextView day3;
    @Bind(R.id.day_4)
    TextView day4;
    @Bind(R.id.day_5)
    TextView day5;
    @Bind(R.id.day_6)
    TextView day6;
    @Bind(R.id.day_7)
    TextView day7;

    List<TextView> daysTvs;
    List<TextView> prestigeTvs;

//    public SignInDaysView(Context mContext) {
//        super(mContext, null);
//    }

    public SignInDaysView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

//    public SignInDaysView(Context mContext, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(mContext, attrs, defStyleAttr);
//        mContext = mContext;
////        initView();
//        initView();
//    }

    public void initView(Context context) {
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.view_sgin_proess, this);
        ButterKnife.bind(this, view);

//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
//                ,ViewGroup.LayoutParams.WRAP_CONTENT);
//        view_donate_records.setLayoutParams(params);
//        addView(view_donate_records);
    }

    // 显示签到信息
    public void setDays(String dayStr,int days,int isSign) {
        int curPoint ,preDay;
        if(days==0){
            curPoint = -1;
            preDay = 0;
        }else{
            preDay = days - isSign;
            curPoint = preDay ;
            if (isSign == 0) {
                curPoint -= 1;
            }
        }
//        Log.v("cgl","preDay:"+preDay +" curPoint:"+curPoint);
        zongQuanProgress.setCurrNodeNO(curPoint);
        initTvList();
        List<String> daysList = TimeUtil.getAWeekDays(dayStr,-(preDay+1));
        for(int i=0;i<daysTvs.size();i++){
            daysTvs.get(i).setText(daysList.get(i));
        }
        int dnum = days;
        if(isSign==0) dnum = days+1;
        int color = mContext.getResources().getColor(R.color.main_remind_color_aother);
        for(int i=0;i<dnum;i++){
            //ToDo 发生越界问题
            if(daysTvs.size()>i){
                daysTvs.get(i).setTextColor(color);
            }
            if(prestigeTvs.size()>i){
                prestigeTvs.get(i).setTextColor(color);
            }
        }
        zongQuanProgress.invalidate();
        invalidate();
    }

    private void initTvList(){
        if(daysTvs==null){
            daysTvs = new ArrayList<>();
            daysTvs.add(day1);
            daysTvs.add(day2);
            daysTvs.add(day3);
            daysTvs.add(day4);
            daysTvs.add(day5);
            daysTvs.add(day6);
            daysTvs.add(day7);
        }
        if(prestigeTvs==null){
            prestigeTvs = new ArrayList<>();
            prestigeTvs.add(prestige1);
            prestigeTvs.add(prestige2);
            prestigeTvs.add(prestige3);
            prestigeTvs.add(prestige4);
            prestigeTvs.add(prestige5);
            prestigeTvs.add(prestige6);
            prestigeTvs.add(prestige7);
        }
    }
}