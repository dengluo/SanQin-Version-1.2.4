package com.pbids.sanqin.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

/**
 * Created by pbids903 on 2018/2/6.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:24
 * @desscribe 类描述:富文本view
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.me.MeCampaignFragment
 */
public class VerticalImageSpan extends ImageSpan {
    public VerticalImageSpan(Context context, int resourceId) {
        super(context, resourceId);
    }

    public VerticalImageSpan(Context context, int resourceId ,int verticalAlignment) {
        super(context, resourceId,verticalAlignment);
    }

    public VerticalImageSpan(Drawable drawable){
        super(drawable);
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end,
                     float x, int top, int y, int bottom, Paint paint) {
        Drawable drawable = getDrawable();
        canvas.save();
        int transY = (Math.abs(paint.getFontMetricsInt().bottom + y + 8) - Math.abs(drawable.getBounds().bottom)) / 2;
        canvas.translate(x, transY);
        drawable.draw(canvas);
        canvas.restore();
    }
}
