package com.pbids.sanqin.ui.view;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.pbids.sanqin.R;
import com.pbids.sanqin.common.OnDialogClickListener;
import com.pbids.sanqin.model.entity.Catlog;
import com.pbids.sanqin.ui.activity.zhizong.GenealogyFragment;
import com.pbids.sanqin.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:22
 * @desscribe 类描述:族谱目录弹窗
 * @remark 备注:
 * @see GenealogyFragment
 */
public class GenealogyCatalogPop extends PopupWindow {
    //    private int
    OnDialogClickListener onDialogClickLisenrar;
    Context mContext;

    private List<TextView> tvList ;

    @Bind(R.id.layout)
    LinearLayout layout;

    public GenealogyCatalogPop(Context context, ArrayList<Catlog> catlogs,Typeface typeface) {
        super(context);
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.pop_genealogy_catalog, null);
        tvList = new ArrayList<>();

        tvList.add((TextView) view.findViewById(R.id.tv1));
        tvList.add((TextView) view.findViewById(R.id.tv2));
        tvList.add((TextView) view.findViewById(R.id.tv3));
        tvList.add((TextView) view.findViewById(R.id.tv4));
        tvList.add((TextView) view.findViewById(R.id.tv5));
        tvList.add((TextView) view.findViewById(R.id.tv6));
        tvList.add((TextView) view.findViewById(R.id.tv7));

        for (int i=0;i<tvList.size();i++){
            TextView item = tvList.get(i);
            item.setVisibility(View.INVISIBLE);
            item.getPaint().setTypeface(typeface);
        }

        //获取自身的长宽高
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        // 设置可以获得焦点
        ButterKnife.bind(this, view);
        setFocusable(true);
        // 设置弹窗内可点击
        setTouchable(true);
        // 设置弹窗外可点击
        setOutsideTouchable(true);

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        for(int i=0;i<catlogs.size();i++){
            if(i<tvList.size()){
                final Catlog catlog = catlogs.get(i);
                TextView tv = tvList.get(i);
                tv.setText(catlog.getCatlog());
                tv.setVisibility(View.VISIBLE);
                final int p = i;
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //点击事件
                        if(catalogClickListener!=null){
                            catalogClickListener.onClick(catlog, p);
                        }
                    }
                });
            }
        }

        ColorDrawable dw = new ColorDrawable(context.getResources().getColor(R.color.main_bg_color));
        setBackgroundDrawable(dw);
        setAnimationStyle(R.style.GenealogyCatalog);
        setContentView(view);
    }

    public void showDown(View v) {
        //获取需要在其上方显示的控件的位置信息
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        showAtLocation(v, Gravity.NO_GRAVITY, v.getWidth(), v.getHeight() + ScreenUtils.getStatusBarHeight(mContext));
    }

    private OnCatalogClickListener catalogClickListener;

    public void setCatalogClickListener(OnCatalogClickListener catalogClickListener) {
        this.catalogClickListener = catalogClickListener;
    }

    public interface OnCatalogClickListener{
        void onClick(Catlog catlog, int position);
    }
}
