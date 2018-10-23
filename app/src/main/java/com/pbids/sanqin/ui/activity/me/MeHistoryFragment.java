package com.pbids.sanqin.ui.activity.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.model.db.NewsArticleManager;
import com.pbids.sanqin.model.entity.NewsArticle;
import com.pbids.sanqin.ui.adapter.MeCollectionAdapter;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.ui.view.calendar.OnCalendarClickListener;
import com.pbids.sanqin.ui.view.calendar.schedule.CalendarLayout;
import com.pbids.sanqin.ui.view.calendar.schedule.ScheduleState;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pbids903 on 2017/12/20.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 14:24
 * @desscribe 类描述:浏览历史界面
 * @remark 备注:
 * @see
 */
public class MeHistoryFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear {
    //    @Bind(R.id.slSchedule)
    @Bind(R.id.rvScheduleList)
    RecyclerView rvScheduleList;
    @Bind(R.id.rlCalendarLayout)
    CalendarLayout rlCalendarLayout;
    @Bind(R.id.rvScheduleList_bt)
    View rvScheduleListBt;
    @Bind(R.id.rl1)
    RelativeLayout rl1;
    @Bind(R.id.preTime)
    ImageView preTime;
    @Bind(R.id.nextTime)
    ImageView nextTime;
    @Bind(R.id.tvTime)
    TextView tvTime;
    @Bind(R.id.preTimeLayout)
    RelativeLayout preTimeLayout;
    @Bind(R.id.nextTimeLayout)
    RelativeLayout nextTimeLayout;
//    @Bind(R.id.mcvCalendar)
//    MonthCalendarView mcvCalendar;
//    @Bind(R.id.rlMonthCalendar)
//    RelativeLayout rlMonthCalendar;
//    @Bind(R.id.wcvCalendar)
//    WeekCalendarView wcvCalendar;
//    @Bind(R.id.rvScheduleList_bt)
//    View rvScheduleListBt;
//    @Bind(R.id.rlScheduleList)
//    RelativeLayout rlScheduleList;
    private MeCollectionAdapter commonNewsAdapter;
    List<NewsArticle> newsArticles;
    int currentPagePosition = 2;

    public static MeHistoryFragment newInstance() {
        MeHistoryFragment fragment = new MeHistoryFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me_history, container, false);
        ButterKnife.bind(this, view);
//        rvScheduleListBt = view_donate_records.findViewById(R.id.rvScheduleList_bt);
        initView();
        return view;
    }

    public void initView() {
//        setToolBarGone();
        newsArticles = NewsArticleManager.queryNewsArticle(_mActivity, new Date().getTime());
        initScheduleList();
//        slSchedule.setOnCalendarClickListener(new OnCalendarClickListener() {
//            @Override
//            public void onClickDate(int year, int month, int day) {
//                if (titleText != null) {
//                    commonNewsAdapter.dagouVisible = 0;
//                    titleText.setVisibility(View.INVISIBLE);
//                    titleImageView.setVisibility(View.VISIBLE);
//                }
////                Log.i("wzh", "year: " + year);
////                Log.i("wzh", "month: " + month);
////                Log.i("wzh", "day: " + day);
////                Log.i("wzh", "" + TimeUtil.getCurrentDayZeroTime(new Date().getTime()));
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
//                String newMonth = (month + 1) + "";
//                String newDay = day + "";
//                if (newMonth.length() < 2) {
//                    newMonth = "0" + newMonth;
//                }
//                if (newDay.length() < 2) {
//                    newDay = "0" + newDay;
//                }
//                String historyTime = "" + year + "" + newMonth + "" + newDay;
//                try {
//                    Date date = simpleDateFormat.parse(historyTime);
//                    newsArticles = NewsArticleManager.queryNewsArticle(_mActivity, date.getTime());
//                    commonNewsAdapter.updateView(newsArticles);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onPageChange(int year, int month, int day) {
//
//            }
//        });
        rvScheduleListBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                slSchedule.clickChangeCalendarStatus();
//                if(mcvCalendar.getVisibility() == View.VISIBLE){
//                    mcvCalendar.setVisibility(View.GONE);
//                    wcvCalendar.setVisibility(View.VISIBLE);
//                }else if(mcvCalendar.getVisibility() == View.GONE){
//                    mcvCalendar.setVisibility(View.VISIBLE);
//                    wcvCalendar.setVisibility(View.GONE);
//                }
                rlCalendarLayout.open();

            }
        });

        String time="";
        if(rlCalendarLayout.getCurrentSelectMonth()<10){
            time = rlCalendarLayout.getCurrentSelectYear()+"年0"+(rlCalendarLayout.getCurrentSelectMonth()+1)+"月";
        }else if(rlCalendarLayout.getCurrentSelectMonth()>=10){
            time = rlCalendarLayout.getCurrentSelectYear()+"年"+(rlCalendarLayout.getCurrentSelectMonth()+1)+"月";
        }
        tvTime.setText(time);

        preTimeLayout.setClickable(true);
        preTime.setBackgroundResource(R.drawable.me_button_left_default);
        nextTimeLayout.setClickable(false);
        nextTime.setBackgroundResource(R.drawable.me_button_right_disabled);

        rlCalendarLayout.setOnCalendarLayoutLisener(new CalendarLayout.OnCalendarLayoutLisener() {
            @Override
            public void onPageChange(int year, int mother, int day, int currentPosition) {
                String time="";
                if(mother<10){
                    time = year+"年0"+(mother+1)+"月";
                }else if(mother>=10){
                    time = year+"年"+(mother+1)+"月";
                }
                tvTime.setText(time);
                switch (currentPosition){
                    case 0:
                        preTimeLayout.setClickable(false);
                        preTime.setBackgroundResource(R.drawable.me_button_left_disabled);
                        nextTimeLayout.setClickable(true);
                        nextTime.setBackgroundResource(R.drawable.me_button_right_default);
                        break;
                    case 1:
                        preTimeLayout.setClickable(true);
                        preTime.setBackgroundResource(R.drawable.me_button_left_default);
                        nextTimeLayout.setClickable(true);
                        nextTime.setBackgroundResource(R.drawable.me_button_right_default);
                        break;
                    case 2:
                        preTimeLayout.setClickable(true);
                        preTime.setBackgroundResource(R.drawable.me_button_left_default);
                        nextTimeLayout.setClickable(false);
                        nextTime.setBackgroundResource(R.drawable.me_button_right_disabled);
                        break;
                }
                MeHistoryFragment.this.currentPagePosition = currentPosition;
            }

            @Override
            public void onScheduleStateChange(ScheduleState state) {
                if(state == ScheduleState.OPEN){
                    rl1.setVisibility(View.VISIBLE);
                    rvScheduleListBt.setBackgroundResource(R.drawable.me_btn_jiantou_default_);
                }else if(state == ScheduleState.CLOSE){
                    rl1.setVisibility(View.GONE);
                    rvScheduleListBt.setBackgroundResource(R.drawable.me_btn_jiantou_default);
                }
            }
        });

        preTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (currentPagePosition){
                    case 0:
                        break;
                    case 1:
                        rlCalendarLayout.setMonthViewPosition(0);
                        break;
                    case 2:
                        rlCalendarLayout.setMonthViewPosition(1);
                        break;
                }
            }
        });

        nextTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (currentPagePosition){
                    case 0:
                        rlCalendarLayout.setMonthViewPosition(1);
                        break;
                    case 1:
                        rlCalendarLayout.setMonthViewPosition(2);
                        break;
                    case 2:
                        break;
                }
            }
        });
//        Log.i("wzh", "queryNewsArticle: " + newsArticles.toString());
    }

    private void initScheduleList() {
//        rvScheduleList = slSchedule.getSchedulerRecyclerView();
        LinearLayoutManager manager = new LinearLayoutManager(_mActivity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvScheduleList.setLayoutManager(manager);
        DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setSupportsChangeAnimations(false);
        rvScheduleList.setItemAnimator(itemAnimator);
        commonNewsAdapter = new MeCollectionAdapter(newsArticles, _mActivity);
        commonNewsAdapter.setType("history");
        rvScheduleList.setAdapter(commonNewsAdapter);
        rvScheduleList.addItemDecoration(new HorizontalDividerItemDecoration.Builder(_mActivity)
                .color(_mActivity.getResources().getColor(R.color.main_status_color))
                .sizeResId(R.dimen.dp_10)
                .build());
        rlCalendarLayout.setOnCalendarClickListener(new OnCalendarClickListener() {
            @Override
            public void onClickDate(int year, int month, int day) {
                if (titleText != null) {
                    commonNewsAdapter.dagouVisible = 0;
                    titleText.setVisibility(View.INVISIBLE);
                    titleImageView.setVisibility(View.VISIBLE);
                }
//                Log.i("wzh", "year: " + year);
//                Log.i("wzh", "month: " + month);
//                Log.i("wzh", "day: " + day);
//                Log.i("wzh", "" + TimeUtil.getCurrentDayZeroTime(new Date().getTime()));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
                String newMonth = (month + 1) + "";
                String newDay = day + "";
                if (newMonth.length() < 2){
                    newMonth = "0" + newMonth;
                }
                if (newDay.length() < 2){
                    newDay = "0" + newDay;
                }
                String historyTime = "" + year + "" + newMonth + "" + newDay;
                try {
                    Date date = simpleDateFormat.parse(historyTime);
                    newsArticles = NewsArticleManager.queryNewsArticle(_mActivity, date.getTime());
                    commonNewsAdapter.updateView(newsArticles);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPageChange(int year, int month, int day ,int position) {

            }
        });
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftArrowCenterTextTitleRightImage("我的浏览历史", _mActivity);
    }

    TextView titleText;
    ImageView titleImageView;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.rvScheduleList_bt:
//                break;
            case R.id.main_left_layout:
                pop();
                break;
            case R.id.main_right_layout:
                if (!(newsArticles.size() > 0)) {
                    return;
                }
                if (titleText == null) {
                    titleImageView = (ImageView) v.findViewById(R.id.me_title_right_image);
                    titleText = (TextView) v.findViewById(R.id.me_title_right_text);
                }
                if (titleText.getVisibility() == View.INVISIBLE) {
                    titleText.setVisibility(View.VISIBLE);
                    titleImageView.setVisibility(View.INVISIBLE);
                } else {
                    titleText.setVisibility(View.INVISIBLE);
                    titleImageView.setVisibility(View.VISIBLE);
                }
//                int visibility = ((TextView)v).getVisibility();
//                if(visibility == View.VISIBLE){
//                String ids = commonNewsAdapter.getDeleteNewsArticleIds();
//                if(!ids.equals("")){
//                    getLoadingPop("正在删除").show();
//                    HttpParams params = new HttpParams();
//                    params.put("uid", MyApplication.getUserInfo().getUserId());
//                    params.put("ids",commonNewsAdapter.getDeleteNewsArticleIds());
//                    Log.i("wzh","ids: "+commonNewsAdapter.getDeleteNewsArticleIds());

//                    Log.i("wzh","delete");
//                }
                NewsArticleManager.deleteNewsArticles(_mActivity, commonNewsAdapter.getDeleteNewsArticles());
                List<NewsArticle> deleteNewsArticles = commonNewsAdapter.getDeleteNewsArticles();
                for (int i = 0; i < deleteNewsArticles.size(); i++) {
                    commonNewsAdapter.getNewsArticles().remove(deleteNewsArticles.get(i));
                }

//                }
//                commonNewsAdapter.newsArticles =
                commonNewsAdapter.setDagouVisible();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

//    @OnClick(R.id.rvScheduleList_bt)
//    public void onViewClicked() {
//    }
}
