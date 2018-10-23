package com.pbids.sanqin.ui.activity.zhizong.component;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.pbids.sanqin.model.entity.GenealogyInformation;
import com.pbids.sanqin.ui.activity.zhizong.GenealogyView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author caiguoliang
 * @date on 2018/3/2 15:08
 * @desscribe 类描述:我的族谱界面 ->组件-翻页
 * @remark 备注:
 * @see
 */

public class GenealogyPaper extends ViewPager {

    GenealogyView genealogyView;

    public void setGenealogyView(GenealogyView genealogyView) {
        this.genealogyView = genealogyView;
    }


    public GenealogyPaper(Context context) {
        super(context);
        initView();
    }

    public GenealogyPaper(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }


    private void initView(){


    }

    /*



    this.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
        @Override
        public void onPageScrolled(int position, float positionOffset,int positionOffsetPixels) {
            //
        }
        @Override
        public void onPageSelected(int position) {
            //
        }
        @Override
        public void onPageScrollStateChanged(int state) {
            //
        }
    });

    */

}
