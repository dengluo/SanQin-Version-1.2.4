/**
 * Copyright 2016 JustWayward Team
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pbids.sanqin.ui.view.pager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.pbids.sanqin.R;
import com.pbids.sanqin.utils.ScreenUtils;

import java.nio.MappedByteBuffer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PageFactory {
    private Context mContext;
    /**
     * 屏幕宽高
     */
    private int mHeight, mWidth;
    /**
     * 文字区域宽高
     */
    private int mVisibleHeight, mVisibleWidth;
    /**
     * 间距
     */
    private int marginHeight, marginWidth;
//    /**
//     * 字体大小
//     */
//    private int mFontSize, mNumFontSize;
    /**
     * 每页行数
     */
//    private int mPageLineCount;
    /**
     * 行间距
     **/
//    private int mLineSpace;
    /**
     * 字节长度
     */
//    private int mbBufferLen;
    /**
     * MappedByteBuffer：高效的文件内存映射
     */
    private MappedByteBuffer mbBuff;
    /**
     * 页首页尾的位置
     */
//    private int curEndPos = 0, curBeginPos = 0, tempBeginPos, tempEndPos;
    private int currentChapter, tempChapter;
//    private Vector<String> mLines = new Vector<>();

    private Paint mPaint;
    private Paint mTitlePaint;
    private Bitmap mBookPageBg;

    private DecimalFormat decimalFormat = new DecimalFormat("#0.00");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
    private int timeLen = 0, percentLen = 0;
    private String time;
    private int battery = 40;
    private Rect rectF;
    private ProgressBar batteryView;
    private Bitmap batteryBitmap;
    private int pager=0;

//    private List<BookMixAToc.mixToc.Chapters> chaptersList;
    private int chapterSize = 0;
    private int currentPage = 1;

    private OnReadStateChangeListener listener;
    private String charset = "UTF-8";
    private ArrayList<ViewGroup> mViews;

    public PageFactory(Context context,ArrayList<ViewGroup> views) {
        this(context, ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight(),views);
    }

    public PageFactory(Context context, int width, int height,ArrayList<ViewGroup> views) {
        mContext = context;
        mWidth = width;
        mHeight = height;
        marginWidth = ScreenUtils.dpToPxInt(15);
        marginHeight = ScreenUtils.dpToPxInt(15);
        mVisibleHeight = mHeight;
        mVisibleWidth = mWidth;
        rectF = new Rect(0, 0, mWidth, mHeight);
        time = dateFormat.format(new Date());
        Log.i("wzh","views.size(): "+views.size());
        mViews = views;
        this.pager = views.size();
    }

    /**
     * 绘制阅读页面
     *
     * @param canvas
     */
    public synchronized void onDraw(Canvas canvas,int page) {
        Log.i("wzh","page: "+page);
//        switch (page){
//            case 1:
//                mViews.get(page-1);
//                break;
//            case 2:
//                break;
//            case 3:
//                break;
//            case 4:
//                break;
//        }
        //加载xml布局文件
//        LayoutInflater factory = LayoutInflater.from(mContext);
//        ViewGroup view_donate_records = (ViewGroup) factory.inflate(R.layout.app_first_name_pager, null);
//            TextView textView = (TextView) view_donate_records.findViewById(R.id.app_write_name_small_tv);
//            textView.setText("氏族谱");
//            Typeface typeface = Typeface.createFromAsset(mContext.getAssets(),"fonts/wenyue.ttf");
//            textView.setTypeface(typeface);
        //调用下面这个方法非常重要，如果没有调用这个方法，得到的bitmap为null
        layoutView(mViews.get(page-1),(int)mContext.getResources().getDimension(R.dimen.dp_346),
                (int)mContext.getResources().getDimension(R.dimen.dp_422));

        mViews.get(page-1).draw(canvas);
    }

        //然后View和其内部的子View都具有了实际大小，也就是完成了布局，相当与添加到了界面上。接着就可以创建位图并在上面绘制了：
    private void layoutView(View v, int width, int height) {
        // 整个View的大小 参数是左上角 和右下角的坐标
        v.layout(0, 0, width, height);
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
//        int measuredHeight = View.MeasureSpec.makeMeasureSpec(10000, View.MeasureSpec.AT_MOST);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST);
        /** 当然，measure完后，并不会实际改变View的尺寸，需要调用View.layout方法去进行布局。
                  * 按示例调用layout函数后，View的大小将会变成你想要设置成的大小。
                  */
        v.measure(measuredWidth, measuredHeight);
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
    }

    public boolean hasNextPage() {
//        return currentChapter < chaptersList.size() || curEndPos < mbBufferLen;
        return currentPage < pager ;
    }

    public boolean hasPrePage() {
//        return currentChapter > 1 || (currentChapter == 1 && curBeginPos > 0);
        return currentPage > 1;
    }

    public int getCurrentPage(){
        return currentPage;
    }
    public int getNextPage(){
        return currentPage+1;
    }

    public int getPrePage(){
        return currentPage-1;
    }

    /**
     * 跳转下一页
     */
    public BookStatus nextPage() {
        if (!hasNextPage()) { // 最后一章的结束页
            return BookStatus.NO_NEXT_PAGE;
        } else {
            tempChapter = currentChapter;
//            tempBeginPos = curBeginPos;
//            tempEndPos = curEndPos;
//            if (curEndPos >= mbBufferLen) { // 中间章节结束页
//                currentChapter++;
//                int ret = openBook(currentChapter, new int[]{0, 0}); // 打开下一章
//                if (ret == 0) {
//                    onLoadChapterFailure(currentChapter);
//                    currentChapter--;
//                    curBeginPos = tempBeginPos;
//                    curEndPos = tempEndPos;
//                    return BookStatus.NEXT_CHAPTER_LOAD_FAILURE;
//                } else {
//                    currentPage = 0;
//                    onChapterChanged(currentChapter);
//                }
//            } else {
//                curBeginPos = curEndPos; // 起始指针移到结束位置
//            }
//            mLines.clear();
//            mLines = pageDown(); // 读取一页内容
            onPageChanged(currentChapter, ++currentPage);
//            return BookStatus.NEXT_CHAPTER_LOAD_FAILURE;
        }
        return BookStatus.LOAD_SUCCESS;
    }

    /**
     * 跳转上一页
     */
    public BookStatus prePage() {
        if (!hasPrePage()) { // 第一章第一页
            return BookStatus.NO_PRE_PAGE;
        } else {
            // 保存当前页的值
//            tempChapter = currentChapter;
//            tempBeginPos = curBeginPos;
//            tempEndPos = curEndPos;
//            if (curBeginPos <= 0) {
//                currentChapter--;
//                int ret = openBook(currentChapter, new int[]{0, 0});
//                if (ret == 0) {
//                    onLoadChapterFailure(currentChapter);
//                    currentChapter++;
//                    return BookStatus.PRE_CHAPTER_LOAD_FAILURE;
//                } else { // 跳转到上一章的最后一页
//                    mLines.clear();
//                    mLines = pageLast();
//                    onChapterChanged(currentChapter);
//                    onPageChanged(currentChapter, currentPage);
//                    return BookStatus.LOAD_SUCCESS;
//                }
//            }
//            mLines.clear();
//            pageUp(); // 起始指针移到上一页开始处
//            mLines = pageDown(); // 读取一页内容
            onPageChanged(currentChapter, --currentPage);
        }
        return BookStatus.LOAD_SUCCESS;
    }

    public void cancelPage() {
//        currentChapter = tempChapter;
//        curBeginPos = tempBeginPos;
//        curEndPos = curBeginPos;
//
//        int ret = openBook(currentChapter, new int[]{curBeginPos, curEndPos});
//        if (ret == 0) {
//            onLoadChapterFailure(currentChapter);
//            return;
//        }
//        mLines.clear();
//        mLines = pageDown();
    }

    /**
     * 设置字体颜色
     *
     * @param textColor
     * @param titleColor
     */
    public void setTextColor(int textColor, int titleColor) {
        mPaint.setColor(textColor);
        mTitlePaint.setColor(titleColor);
    }

    public void setBgBitmap(Bitmap BG) {
        mBookPageBg = BG;
    }

    public void setOnReadStateChangeListener(OnReadStateChangeListener listener) {
        this.listener = listener;
    }

    private void onChapterChanged(int chapter) {
        if (listener != null)
            listener.onChapterChanged(chapter);
    }

    private void onPageChanged(int chapter, int page) {
        if (listener != null)
            listener.onPageChanged(chapter, page);
    }

    private void onLoadChapterFailure(int chapter) {
        if (listener != null)
            listener.onLoadChapterFailure(chapter);
    }

//    public void convertBetteryBitmap() {
//        batteryView = (ProgressBar) LayoutInflater.from(mContext).inflate(R.layout.layout_battery_progress, null);
//        batteryView.setProgressDrawable(ContextCompat.getDrawable(mContext,
//                SettingManager.getInstance().getReadTheme() < 4 ?
//                        R.drawable.seekbar_battery_bg : R.drawable.seekbar_battery_night_bg));
//        batteryView.setProgress(battery);
//        batteryView.setDrawingCacheEnabled(true);
//        batteryView.measure(View.MeasureSpec.makeMeasureSpec(ScreenUtils.dpToPxInt(26), View.MeasureSpec.EXACTLY),
//                View.MeasureSpec.makeMeasureSpec(ScreenUtils.dpToPxInt(14), View.MeasureSpec.EXACTLY));
//        batteryView.layout(0, 0, batteryView.getMeasuredWidth(), batteryView.getMeasuredHeight());
//        batteryView.buildDrawingCache();
//        //batteryBitmap = batteryView.getDrawingCache();
//        // tips: @link{https://github.com/JustWayward/BookReader/issues/109}
//        batteryBitmap = Bitmap.createBitmap(batteryView.getDrawingCache());
//        batteryView.setDrawingCacheEnabled(false);
//        batteryView.destroyDrawingCache();
//    }

//    public void setBattery(int battery) {
//        this.battery = battery;
//        convertBetteryBitmap();
//    }

    public void setTime(String time) {
        this.time = time;
    }

    public void recycle() {
        if (mBookPageBg != null && !mBookPageBg.isRecycled()) {
            mBookPageBg.recycle();
            mBookPageBg = null;
//            LogUtils.d("mBookPageBg recycle");
        }

        if (batteryBitmap != null && !batteryBitmap.isRecycled()) {
            batteryBitmap.recycle();
            batteryBitmap = null;
//            LogUtils.d("batteryBitmap recycle");
        }
    }
}
