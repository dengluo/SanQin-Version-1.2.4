package com.pbids.sanqin.ui.activity.zhizong;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.pbids.sanqin.R;
import com.pbids.sanqin.model.entity.Catlog;
import com.pbids.sanqin.utils.AppUtils;
import com.pbids.sanqin.utils.ScreenUtils;

import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Created by pbids903 on 2018/3/26.
 */

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
    /**
     * 字体大小
     */
    private int mFontSize, mNumFontSize;
    /**
     * 每页行数
     */
    private int mPageLineCount;
    /**
     * 行间距
     **/
    private int mLineSpace;
    /**
     * 字节长度
     */
    private int mbBufferLen;
    /**
     * MappedByteBuffer：高效的文件内存映射
     */
//    private MappedByteBuffer mbBuff;
    /**
     * 页首页尾的位置
     */
    private int curEndPos = 0, curBeginPos = 0, tempBeginPos, tempEndPos;
    private int currentChapter, tempChapter;
    private Vector<String> mLines = new Vector<>();

    private Paint mPaint;
    private Paint mTitlePaint;
    private Bitmap mBookPageBg;
    private List<Vector<String>> vectors= new ArrayList<>();

//    public int getIndexSize(int index){
//        return vectors.get(index).size();
//    }

    public PageFactory(Context context, int width, int height, int fontSize) {
        mContext = context;
        mWidth = width;
        mHeight = height;
        mFontSize = fontSize;
        mLineSpace = mFontSize / 5 * 2;
        mNumFontSize = ScreenUtils.dpToPxInt(20);
//        marginWidth = ScreenUtils.dpToPxInt(15);
//        marginHeight = ScreenUtils.dpToPxInt(15);
        marginWidth = (int)context.getResources().getDimension(R.dimen.dp_5);
        marginHeight = (int)context.getResources().getDimension(R.dimen.dp_5);
//        mVisibleHeight = mHeight - marginHeight * 2 - mNumFontSize * 2 - mLineSpace * 2;
        mVisibleHeight = mHeight - marginHeight * 2 - mNumFontSize - mLineSpace*2;
//        mVisibleHeight = mHeight - marginHeight * 2;
        mVisibleWidth = mWidth - marginWidth * 2;
        mPageLineCount = mVisibleHeight / (mFontSize + mLineSpace);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mFontSize);
        mPaint.setTextSize(ContextCompat.getColor(context, R.color.main_title_text_color));
        mPaint.setColor(Color.BLACK);
        mTitlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTitlePaint.setTextSize(mNumFontSize);
        mTitlePaint.setColor(ContextCompat.getColor(AppUtils.getAppContext(), R.color.main_title_text_color));
        mTitlePaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        // Typeface typeface = Typeface.createFromAsset(context.getAssets(),"fonts/FZBYSK.TTF");
        // mPaint.setTypeface(typeface);
        // mNumPaint.setTypeface(typeface);
    }

    public String pageDown(String strParagraph) {
//        String strParagraph = "";
        Log.i("wzh","strParagraph: "+strParagraph);
        Vector<String> lines = new Vector<>();
        int paraSpace = 0;
        mPageLineCount = mVisibleHeight / (mFontSize + mLineSpace);
//        while ((lines.size() < mPageLineCount)) {
        while ((lines.size() <= mPageLineCount)) {
//            strParagraph = strParagraph.replaceAll("\r\n", "  ")
//                    .replaceAll("\n", " "); // 段落中的换行符去掉，绘制的时候再换行

            while (strParagraph.length() > 0) {
                int paintSize = mPaint.breakText(strParagraph, true, mVisibleWidth, null);
                String lineStr = strParagraph.substring(0, paintSize);
                int index = lineStr.indexOf("\n");
                if(index!=-1){
                    lineStr = strParagraph.substring(0, index);
                    lines.add(lineStr);
                    strParagraph = strParagraph.substring(index+1);
                }else{
                    lines.add(strParagraph.substring(0, paintSize));
                    strParagraph = strParagraph.substring(paintSize);
                }
                if (lines.size() >= mPageLineCount) {
//                    continue;
                    break;
                }
            }
//            lines.set(lines.size() - 1, lines.get(lines.size() - 1) + "@");
            paraSpace += mLineSpace;
//            mPageLineCount = (mVisibleHeight - paraSpace) / (mFontSize + mLineSpace);
            mPageLineCount = (mVisibleHeight - paraSpace) / (mFontSize + mLineSpace);
        }
        vectors.add(lines);
        return strParagraph;
    }

    public synchronized void onDraw(Canvas canvas,int index,List<Integer> integers,ArrayList<Catlog> catlogs) {
        index = index-2;
        Vector<String> lines = vectors.get(index);
        if (lines.size() > 0) {
            int y = marginHeight + (mLineSpace << 1);
            // 绘制背景
//            if (mBookPageBg != null) {
//                canvas.drawBitmap(mBookPageBg, null, rectF, null);
//            } else {
//                canvas.drawColor(Color.WHITE);
//            }
            // 绘制标题
            for(int i=0;i<integers.size();i++){
                int k = 0;
                for(int j=0;j<i;j++){
                    k+=integers.get(j);
                    Log.i("wzh","integers.get(j): "+integers.get(j));
                }
                Log.i("wzh","k: "+k);
                Log.i("wzh","index: "+index);
                if(index==k){
                    canvas.drawText(catlogs.get(i).getCatlog(), marginWidth, y, mTitlePaint);
                    y += mLineSpace + mNumFontSize;
                }
            }
//            canvas.drawText(chaptersList.get(currentChapter - 1).title, marginWidth, y, mTitlePaint);
//            y += mLineSpace + mNumFontSize;
            // 绘制阅读页面文字

            for (String line : lines) {
                y += mLineSpace;
                if (line.endsWith("@")) {
                    canvas.drawText(line.substring(0, line.length() - 1), marginWidth, y, mPaint);
                    y += mLineSpace;
                } else {
                    canvas.drawText(line, marginWidth, y, mPaint);
                }
                y += mFontSize;
            }
            // 绘制提示内容
//            if (batteryBitmap != null) {
//                canvas.drawBitmap(batteryBitmap, marginWidth + 2,
//                        mHeight - marginHeight - ScreenUtils.dpToPxInt(12), mTitlePaint);
//            }

//            float percent = (float) currentChapter * 100 / chapterSize;
//            canvas.drawText(decimalFormat.format(percent) + "%", (mWidth - percentLen) / 2,
//                    mHeight - marginHeight, mTitlePaint);

//            String mTime = dateFormat.format(new Date());
//            canvas.drawText(mTime, mWidth - marginWidth - timeLen, mHeight - marginHeight, mTitlePaint);

            // 保存阅读进度
//            SettingManager.getInstance().saveReadProgress(bookId, currentChapter, curBeginPos, curEndPos);
        }
    }

    public int getVectorsSize(){
//        int size = 0;
//        for(int i =0;i<vectors.size();i++){
//            size +=vectors.get(i).size();
//        }
//        return size;
        return vectors.size();
    }
}
