package com.pbids.sanqin.ui.view.calendar.schedule;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.pbids.sanqin.R;
import com.pbids.sanqin.ui.view.calendar.CalendarUtils;
import com.pbids.sanqin.ui.view.calendar.OnCalendarClickListener;
import com.pbids.sanqin.ui.view.calendar.month.MonthCalendarView;
import com.pbids.sanqin.ui.view.calendar.month.MonthView;
import com.pbids.sanqin.ui.view.calendar.week.WeekCalendarView;
import com.pbids.sanqin.ui.view.calendar.week.WeekView;

import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.List;

/**
 * Created by pbids903 on 2018/2/5.
 */

public class CalendarLayout extends FrameLayout{

    private final int DEFAULT_MONTH = 0;
    private final int DEFAULT_WEEK = 1;

    private MonthCalendarView mcvCalendar;
    private WeekCalendarView wcvCalendar;
    private RelativeLayout rlMonthCalendar;
//    private RelativeLayout rlScheduleList;
//    private ScheduleRecyclerView rvScheduleList;

    private int mCurrentSelectYear;
    private int mCurrentSelectMonth;
    private int mCurrentSelectDay;
    private int mRowSize;
    private int mMinDistance;
    private int mAutoScrollDistance;
    private int mDefaultView;
    private float mDownPosition[] = new float[2];
    private boolean mIsScrolling = false;
    private boolean mIsAutoChangeMonthRow;
    private boolean mCurrentRowsIsSix = true;

    private ScheduleState mState;
    private OnCalendarClickListener mOnCalendarClickListener;
    private GestureDetector mGestureDetector;
//    private Animation openAnimation;
//    private Animation closeAnimation;
    private Context mContext;
    TranslateAnimation closeAnimation;
    TranslateAnimation openAnimation;

    public CalendarLayout(Context context) {
        this(context, null);
    }

    public CalendarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initAttrs(context.obtainStyledAttributes(attrs, R.styleable.ScheduleLayout));
        initDate();
    }

    private void initAttrs(TypedArray array) {
        mDefaultView = array.getInt(R.styleable.ScheduleLayout_default_view, DEFAULT_MONTH);
        mIsAutoChangeMonthRow = array.getBoolean(R.styleable.ScheduleLayout_auto_change_month_row, false);
        array.recycle();
        mState = ScheduleState.OPEN;
        mRowSize = getResources().getDimensionPixelSize(R.dimen.week_calendar_height);
        mMinDistance = getResources().getDimensionPixelSize(R.dimen.calendar_min_distance);
        mAutoScrollDistance = getResources().getDimensionPixelSize(R.dimen.auto_scroll_distance);
    }

    public void setMonthViewPosition(int position){
        mcvCalendar.setCurrentItem(position);
    }
//    private void initGestureDetector() {
//        mGestureDetector = new GestureDetector(getContext(), new OnScheduleScrollListener(this));
//    }

    private void initDate() {
        Calendar calendar = Calendar.getInstance();
        resetCurrentSelectDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
//        wcvCalendar.setClickable(false);

        openAnimation = openAnimation = new TranslateAnimation(0f,0f,0f,(float)mRowSize*5);
        openAnimation.setDuration(300);
        openAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                rlMonthCalendar.setVisibility(View.VISIBLE);
                wcvCalendar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                int deltaX = (int) (0);
                int deltaY = (int) (0f-(float)-mRowSize*5);
                int layoutX = CalendarLayout.this.getLeft();
                int layoutY = CalendarLayout.this.getTop();
                int tempWidth = CalendarLayout.this.getWidth();
                int tempHeight = CalendarLayout.this.getHeight();
//                    if (isBack == false) {
                layoutX += deltaX;
                layoutY += deltaY;
                CalendarLayout.this.layout(layoutX, layoutY, layoutX + tempWidth, layoutY + tempHeight);

                CalendarLayout.this.clearAnimation();
                mState = ScheduleState.OPEN;
//                    } else {
//                        view_donate_records.layout(layoutX, layoutY, layoutX + tempWidth, layoutY + tempHeight);
//                    }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        closeAnimation = new TranslateAnimation(0f,0f,0f,(float)-mRowSize*5);
        closeAnimation.setDuration(0);
        closeAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                FrameLayout.LayoutParams ll = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                ll.setMargins(0,0,0,0);
//                CalendarLayout.this.setLayoutParams(ll);
                wcvCalendar.setVisibility(VISIBLE);
//                rlMonthCalendar.setVisibility(INVISIBLE);
                rlMonthCalendar.setVisibility(GONE);
                mState = ScheduleState.CLOSE;

                int deltaX = (int) (0);
                int deltaY = (int) ((float)-mRowSize*5 - 0f);
                int layoutX = CalendarLayout.this.getLeft();
                int layoutY = CalendarLayout.this.getTop();
                int tempWidth = CalendarLayout.this.getWidth();
                int tempHeight = CalendarLayout.this.getHeight();
//                    if (isBack == false) {
                layoutX += deltaX;
                layoutY += deltaY;
                CalendarLayout.this.layout(layoutX, layoutY, layoutX + tempWidth, layoutY + tempHeight);
                CalendarLayout.this.clearAnimation();
//                    } else {
//                        view_donate_records.layout(layoutX, layoutY, layoutX + tempWidth, layoutY + tempHeight);
//                    }
                float h = mContext.getResources().getDimension(R.dimen.week_calendar_height)
                        +mContext.getResources().getDimension(R.dimen.week_bar_height_);
                FrameLayout.LayoutParams ll = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)h);
                ll.setMargins(0,0,0,0);
                CalendarLayout.this.setLayoutParams(ll);
                if(onCalendarLayoutLisener!=null){
                    onCalendarLayoutLisener.onScheduleStateChange(mState);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mcvCalendar = (MonthCalendarView) findViewById(R.id.mcvCalendar);
        wcvCalendar = (WeekCalendarView) findViewById(R.id.wcvCalendar);
        rlMonthCalendar = (RelativeLayout) findViewById(R.id.rlMonthCalendar);
//        rlScheduleList = (RelativeLayout) findViewById(R.id.rlScheduleList);
//        rvScheduleList = (ScheduleRecyclerView) findViewById(R.id.rvScheduleList);
        bindingMonthAndWeekCalendar();
    }

    private void bindingMonthAndWeekCalendar() {
        mcvCalendar.setOnCalendarClickListener(mMonthCalendarClickListener);
        wcvCalendar.setOnCalendarClickListener(mWeekCalendarClickListener);
        // 初始化视图
        Calendar calendar = Calendar.getInstance();
        if (mIsAutoChangeMonthRow) {
            mCurrentRowsIsSix = CalendarUtils.getMonthRows(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)) == 6;
        }
        if (mDefaultView == DEFAULT_MONTH) {
            wcvCalendar.setVisibility(INVISIBLE);
            rlMonthCalendar.setVisibility(VISIBLE);
            mState = ScheduleState.OPEN;
            if(onCalendarLayoutLisener!=null){
                onCalendarLayoutLisener.onScheduleStateChange(mState);
            }
            if (!mCurrentRowsIsSix) {
//                rlScheduleList.setY(rlScheduleList.getY() - mRowSize);
            }
        } else if (mDefaultView == DEFAULT_WEEK) {
            wcvCalendar.setVisibility(VISIBLE);
            rlMonthCalendar.setVisibility(View.INVISIBLE);
            mState = ScheduleState.CLOSE;
            int row = CalendarUtils.getWeekRow(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            if(onCalendarLayoutLisener!=null){
                onCalendarLayoutLisener.onScheduleStateChange(mState);
            }
//            int deltaX = (int) (0);
//            int deltaY = (int) ((float)-mRowSize*5 - 0f);
//            int layoutX = CalendarLayout.this.getLeft();
//            int layoutY = CalendarLayout.this.getTop();
//            int tempWidth = CalendarLayout.this.getWidth();
////            int tempHeight = CalendarLayout.this.getHeight();
//            int tempHeight = (int) mContext.getResources().getDimension(R.dimen.calendar_height);
////                    if (isBack == false) {
//            layoutX += deltaX;
//            layoutY += deltaY;
//            CalendarLayout.this.layout(layoutX, layoutY, layoutX + tempWidth, layoutY + tempHeight);
            this.startAnimation(closeAnimation);

//            rlMonthCalendar.setY(-5 * mRowSize);
//            rlScheduleList.setY(rlScheduleList.getY() - 5 * mRowSize);
        }
    }

    private void resetCurrentSelectDate(int year, int month, int day) {
        mCurrentSelectYear = year;
        mCurrentSelectMonth = month;
        mCurrentSelectDay = day;
    }

    private OnCalendarClickListener mMonthCalendarClickListener = new OnCalendarClickListener() {
        @Override
        public void onClickDate(int year, int month, int day) {
            wcvCalendar.setOnCalendarClickListener(null);
            int weeks = CalendarUtils.getWeeksAgo(mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay, year, month, day);
            resetCurrentSelectDate(year, month, day);
            int position = wcvCalendar.getCurrentItem() + weeks;
            if (weeks != 0) {
                wcvCalendar.setCurrentItem(position, false);
            }
            resetWeekView(position);
            wcvCalendar.setOnCalendarClickListener(mWeekCalendarClickListener);
        }

        @Override
        public void onPageChange(int year, int month, int day ,int position) {
            computeCurrentRowsIsSix(year, month);
            if(onCalendarLayoutLisener!=null){
                onCalendarLayoutLisener.onPageChange(year,month,day,position);
            }
            Log.i("wzh","year: "+year);
            Log.i("wzh","month: "+month);
        }
    };

    private void computeCurrentRowsIsSix(int year, int month) {
        if (mIsAutoChangeMonthRow) {
            boolean isSixRow = CalendarUtils.getMonthRows(year, month) == 6;
            if (mCurrentRowsIsSix != isSixRow) {
                mCurrentRowsIsSix = isSixRow;
                if (mState == ScheduleState.OPEN) {
                    if (mCurrentRowsIsSix) {
//                        AutoMoveAnimation animation = new AutoMoveAnimation(rlScheduleList, mRowSize);
//                        rlScheduleList.startAnimation(animation);
                    } else {
//                        AutoMoveAnimation animation = new AutoMoveAnimation(rlScheduleList, -mRowSize);
//                        rlScheduleList.startAnimation(animation);
                    }
                }
            }
        }
    }

    private void resetWeekView(int position) {
        WeekView weekView = wcvCalendar.getCurrentWeekView();
        if (weekView != null) {
            weekView.setSelectYearMonth(mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay);
            weekView.invalidate();
        } else {
            WeekView newWeekView = wcvCalendar.getWeekAdapter().instanceWeekView(position);
            newWeekView.setSelectYearMonth(mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay);
            newWeekView.invalidate();
            wcvCalendar.setCurrentItem(position);
        }
        if (mOnCalendarClickListener != null) {
            mOnCalendarClickListener.onClickDate(mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay);
        }
    }

    private OnCalendarClickListener mWeekCalendarClickListener = new OnCalendarClickListener() {
        @Override
        public void onClickDate(int year, int month, int day) {
            mcvCalendar.setOnCalendarClickListener(null);
            int months = CalendarUtils.getMonthsAgo(mCurrentSelectYear, mCurrentSelectMonth, year, month);
            resetCurrentSelectDate(year, month, day);
            if (months != 0) {
                int position = mcvCalendar.getCurrentItem() + months;
                mcvCalendar.setCurrentItem(position, false);
            }
//            resetMonthView();

//            if (mOnCalendarClickListener != null) {//new.can_delete
//                mOnCalendarClickListener.onClickDate(mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay);
//            }
            mcvCalendar.setOnCalendarClickListener(mMonthCalendarClickListener);
            if (mIsAutoChangeMonthRow) {
//                mCurrentRowsIsSix = CalendarUtils.getMonthRows(year, month) == 6;
            }
        }

        @Override
        public void onPageChange(int year, int month, int day,int position) {
            if (mIsAutoChangeMonthRow) {
                if (mCurrentSelectMonth != month) {
                    mCurrentRowsIsSix = CalendarUtils.getMonthRows(year, month) == 6;
                }
            }
        }
    };

    public void close(){

    }

    public void open(){
        if(mState == ScheduleState.OPEN){
            TranslateAnimation closeAnimation = new TranslateAnimation(0f,0f,0f,(float)-mRowSize*5);
            closeAnimation.setDuration(300);
            closeAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    int deltaX = (int) (0);
                    int deltaY = (int) ((float)-mRowSize*5 - 0f);
                    int layoutX = CalendarLayout.this.getLeft();
                    int layoutY = CalendarLayout.this.getTop();
                    int tempWidth = CalendarLayout.this.getWidth();
                    int tempHeight = CalendarLayout.this.getHeight();
//                    if (isBack == false) {
                    layoutX += deltaX;
                    layoutY += deltaY;
                    CalendarLayout.this.layout(layoutX, layoutY, layoutX + tempWidth, layoutY + tempHeight);
                    CalendarLayout.this.clearAnimation();
                    wcvCalendar.setVisibility(VISIBLE);
//                    rlMonthCalendar.setVisibility(INVISIBLE);
                    rlMonthCalendar.setVisibility(GONE);
                    mState = ScheduleState.CLOSE;
//                    } else {
//                        view_donate_records.layout(layoutX, layoutY, layoutX + tempWidth, layoutY + tempHeight);
//                    }

                    float h = mContext.getResources().getDimension(R.dimen.week_calendar_height)
                            +mContext.getResources().getDimension(R.dimen.week_bar_height_);
                    FrameLayout.LayoutParams ll = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)h);
                    ll.setMargins(0,0,0,0);
                    CalendarLayout.this.setLayoutParams(ll);
                    if(onCalendarLayoutLisener!=null){
                        onCalendarLayoutLisener.onScheduleStateChange(mState);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            this.startAnimation(closeAnimation);
        }else if(mState == ScheduleState.CLOSE){
//            TranslateAnimation openAnimation = new TranslateAnimation(0f,0f,0f,(float)mRowSize*5);
            TranslateAnimation openAnimation = new TranslateAnimation(0f,0f,-(float)mRowSize*5,0);
            openAnimation.setDuration(300);
            openAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    rlMonthCalendar.setVisibility(View.VISIBLE);
                    wcvCalendar.setVisibility(View.GONE);
                    float h = mContext.getResources().getDimension(R.dimen.calendar_height_);
                    FrameLayout.LayoutParams ll = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)h);
                    ll.setMargins(0,0,0,0);
                    CalendarLayout.this.setLayoutParams(ll);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    int deltaX = (int) (0);
                    int deltaY = (int) (0f-(float)-mRowSize*5);
                    int layoutX = CalendarLayout.this.getLeft();
                    int layoutY = CalendarLayout.this.getTop();
                    int tempWidth = CalendarLayout.this.getWidth();
                    int tempHeight = CalendarLayout.this.getHeight();
//                    if (isBack == false) {
                    layoutX += deltaX;
                    layoutY += deltaY;
                    CalendarLayout.this.layout(layoutX, layoutY, layoutX + tempWidth, layoutY + tempHeight);

                    CalendarLayout.this.clearAnimation();
                    mState = ScheduleState.OPEN;
//                    } else {
//                        view_donate_records.layout(layoutX, layoutY, layoutX + tempWidth, layoutY + tempHeight);
//                    }
                    float h = mContext.getResources().getDimension(R.dimen.calendar_height_);
                    FrameLayout.LayoutParams ll = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)h);
                    ll.setMargins(0,0,0,0);
                    CalendarLayout.this.setLayoutParams(ll);
                    if(onCalendarLayoutLisener!=null){
                        onCalendarLayoutLisener.onScheduleStateChange(mState);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            this.startAnimation(openAnimation);
        }
    }

    private void resetMonthView() {
        MonthView monthView = mcvCalendar.getCurrentMonthView();
        if (monthView != null) {
            monthView.setSelectYearMonth(mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay);
            monthView.invalidate();
        }
        if (mOnCalendarClickListener != null) {
            mOnCalendarClickListener.onClickDate(mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay);
        }
        resetCalendarPosition();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
//        resetViewHeight(rlScheduleList, height - mRowSize);
        Log.i("wzh","height: "+height);
        Log.i("wzh","heightMeasureSpec: "+heightMeasureSpec);
        resetViewHeight(this, height);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void resetViewHeight(View view, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams.height != height) {
            layoutParams.height = height;
            view.setLayoutParams(layoutParams);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    private void resetCalendarPosition() {
        if (mState == ScheduleState.OPEN) {
            rlMonthCalendar.setY(0);
            if (mCurrentRowsIsSix) {
//                rlScheduleList.setY(mcvCalendar.getHeight());
            } else {
//                rlScheduleList.setY(mcvCalendar.getHeight() - mRowSize);
            }
        } else {
            rlMonthCalendar.setY(-CalendarUtils.getWeekRow(mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay) * mRowSize);
//            rlScheduleList.setY(mRowSize);
        }
    }

    private void resetCalendar() {
        if (mState == ScheduleState.OPEN) {
            mcvCalendar.setVisibility(VISIBLE);
            wcvCalendar.setVisibility(INVISIBLE);
        } else {
            mcvCalendar.setVisibility(INVISIBLE);
            wcvCalendar.setVisibility(VISIBLE);
        }
    }

    private void changeState() {
        if (mState == ScheduleState.OPEN) {
            mState = ScheduleState.CLOSE;
            mcvCalendar.setVisibility(INVISIBLE);
            wcvCalendar.setVisibility(VISIBLE);
            rlMonthCalendar.setY((1 - mcvCalendar.getCurrentMonthView().getWeekRow()) * mRowSize);
            checkWeekCalendar();
        } else {
            mState = ScheduleState.OPEN;
            mcvCalendar.setVisibility(VISIBLE);
            wcvCalendar.setVisibility(INVISIBLE);
            rlMonthCalendar.setY(0);
        }
    }

    private void checkWeekCalendar() {
        WeekView weekView = wcvCalendar.getCurrentWeekView();
        DateTime start = weekView.getStartDate();
        DateTime end = weekView.getEndDate();
        DateTime current = new DateTime(mCurrentSelectYear, mCurrentSelectMonth + 1, mCurrentSelectDay, 23, 59, 59);
        int week = 0;
        while (current.getMillis() < start.getMillis()) {
            week--;
            start = start.plusDays(-7);
        }
        current = new DateTime(mCurrentSelectYear, mCurrentSelectMonth + 1, mCurrentSelectDay, 0, 0, 0);
        if (week == 0) {
            while (current.getMillis() > end.getMillis()) {
                week++;
                end = end.plusDays(7);
            }
        }
        if (week != 0) {
            int position = wcvCalendar.getCurrentItem() + week;
            if (wcvCalendar.getWeekViews().get(position) != null) {
                wcvCalendar.getWeekViews().get(position).setSelectYearMonth(mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay);
                wcvCalendar.getWeekViews().get(position).invalidate();
            } else {
                WeekView newWeekView = wcvCalendar.getWeekAdapter().instanceWeekView(position);
                newWeekView.setSelectYearMonth(mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay);
                newWeekView.invalidate();
            }
            wcvCalendar.setCurrentItem(position, false);
        }
    }

    private void resetScrollingState() {
        mDownPosition[0] = 0;
        mDownPosition[1] = 0;
        mIsScrolling = false;
    }

    public void setOnCalendarClickListener(OnCalendarClickListener onCalendarClickListener) {
        mOnCalendarClickListener = onCalendarClickListener;
    }

    private void resetMonthViewDate(final int year, final int month, final int day, final int position) {
        if (mcvCalendar.getMonthViews().get(position) == null) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    resetMonthViewDate(year, month, day, position);
                }
            }, 50);
        } else {
            mcvCalendar.getMonthViews().get(position).clickThisMonth(year, month, day);
        }
    }

    /**
     * 初始化年月日
     *
     * @param year
     * @param month (0-11)
     * @param day   (1-31)
     */
    public void initData(int year, int month, int day) {
        int monthDis = CalendarUtils.getMonthsAgo(mCurrentSelectYear, mCurrentSelectMonth, year, month);
        int position = mcvCalendar.getCurrentItem() + monthDis;
        mcvCalendar.setCurrentItem(position);
        resetMonthViewDate(year, month, day, position);
    }

    /**
     * 添加多个圆点提示
     *
     * @param hints
     */
    public void addTaskHints(List<Integer> hints) {
        CalendarUtils.getInstance(getContext()).addTaskHints(mCurrentSelectYear, mCurrentSelectMonth, hints);
        if (mcvCalendar.getCurrentMonthView() != null) {
            mcvCalendar.getCurrentMonthView().invalidate();
        }
        if (wcvCalendar.getCurrentWeekView() != null) {
            wcvCalendar.getCurrentWeekView().invalidate();
        }
    }

    /**
     * 删除多个圆点提示
     *
     * @param hints
     */
    public void removeTaskHints(List<Integer> hints) {
        CalendarUtils.getInstance(getContext()).removeTaskHints(mCurrentSelectYear, mCurrentSelectMonth, hints);
        if (mcvCalendar.getCurrentMonthView() != null) {
            mcvCalendar.getCurrentMonthView().invalidate();
        }
        if (wcvCalendar.getCurrentWeekView() != null) {
            wcvCalendar.getCurrentWeekView().invalidate();
        }
    }

    /**
     * 添加一个圆点提示
     *
     * @param day
     */
    public void addTaskHint(Integer day) {
        if (mcvCalendar.getCurrentMonthView() != null) {
            if (mcvCalendar.getCurrentMonthView().addTaskHint(day)) {
                if (wcvCalendar.getCurrentWeekView() != null) {
                    wcvCalendar.getCurrentWeekView().invalidate();
                }
            }
        }
    }

    /**
     * 删除一个圆点提示
     *
     * @param day
     */
    public void removeTaskHint(Integer day) {
        if (mcvCalendar.getCurrentMonthView() != null) {
            if (mcvCalendar.getCurrentMonthView().removeTaskHint(day)) {
                if (wcvCalendar.getCurrentWeekView() != null) {
                    wcvCalendar.getCurrentWeekView().invalidate();
                }
            }
        }
    }

    public MonthCalendarView getMonthCalendar() {
        return mcvCalendar;
    }

    public WeekCalendarView getWeekCalendar() {
        return wcvCalendar;
    }

    public int getCurrentSelectYear() {
        return mCurrentSelectYear;
    }

    public int getCurrentSelectMonth() {
        return mCurrentSelectMonth;
    }

    public int getCurrentSelectDay() {
        return mCurrentSelectDay;
    }

    OnCalendarLayoutLisener onCalendarLayoutLisener;

    public interface OnCalendarLayoutLisener{
        void onPageChange(int year,int mother,int day,int currentPosition);
        void onScheduleStateChange(ScheduleState state);
    }

    public void setOnCalendarLayoutLisener(OnCalendarLayoutLisener onCalendarLayoutLisener){
        this.onCalendarLayoutLisener = onCalendarLayoutLisener;
    }
}
