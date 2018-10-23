package com.pbids.sanqin.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.pbids.sanqin.R;
import com.pbids.sanqin.common.CustomPopView;

/**
 * 过时
 */

public class PopMeSexChoose extends CustomPopView{
    public static final int SEX_TYPE_MAN = 0;
    public static final int SEX_TYPE_WOMEN = 1;
    public PopMeSexChoose(Context context,int type) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.pop_me_information_sex,contentContainer);
        final ImageView manIv = (ImageView) view.findViewById(R.id.pop_me_sex_man);
        final ImageView womenIv = (ImageView) view.findViewById(R.id.pop_me_sex_women);
        final LinearLayout popMeSexLayout = (LinearLayout) view.findViewById(R.id.pop_me_sex_layout);
        final RelativeLayout womenLayout = (RelativeLayout) view.findViewById(R.id.pop_me_sex_women_layout);
        final RelativeLayout manLayout = (RelativeLayout) view.findViewById(R.id.pop_me_sex_man_layout);
        switch (type){
            case SEX_TYPE_MAN:
                manIv.setSelected(true);
                womenIv.setSelected(false);
                break;
            case SEX_TYPE_WOMEN:
                manIv.setSelected(false);
                womenIv.setSelected(true);
                break;
            default:
                manIv.setSelected(false);
                womenIv.setSelected(false);
                break;
        }
        popMeSexLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        womenLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manIv.setSelected(false);
                womenIv.setSelected(true);
                lisenear.onClick(SEX_TYPE_WOMEN);
                dismiss();
            }
        });
        manLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manIv.setSelected(true);
                womenIv.setSelected(false);
                lisenear.onClick(SEX_TYPE_MAN);
                dismiss();
            }
        });
//        manIv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                manIv.setSelected(true);
//                womenIv.setSelected(false);
//                lisenear.onClick(SEX_TYPE_MAN);
//                dismiss();
//            }
//        });
//        womenIv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                manIv.setSelected(false);
//                womenIv.setSelected(true);
//                lisenear.onClick(SEX_TYPE_WOMEN);
//                dismiss();
//            }
//        });
    }

    OnPopSexClickLisenear lisenear;

    public void setOnPopSexClickLisenear(OnPopSexClickLisenear lisenear){
        this.lisenear = lisenear;
    }

    public interface OnPopSexClickLisenear{
        void onClick(int type);
    }
}
