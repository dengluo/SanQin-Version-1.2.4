package com.pbids.sanqin.ui.view;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pbids.sanqin.R;

/**
 * Created by pbids903 on 2017/12/18.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:12
 * @desscribe 类描述:公共标题栏
 * @remark 备注:
 * @see
 */
public class AppToolBar extends LinearLayout{

    protected Context mContext;

    protected View middleView;

    private TextView rightTv;

    public AppToolBar(Context context) {
        super(context,null);
        this.mContext = context;
        init();
    }

    public AppToolBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs,0);
        this.mContext = context;
        init();
    }

    public AppToolBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    public void init(){

    }
    //左边返回键，中间文字
    public void setLeftArrowCenterTextTitle(String title,Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.me_title_left_arrow,null,false);
        RelativeLayout leftView = (RelativeLayout) view.findViewById(R.id.main_left_layout);
        TextView titleText = (TextView) view.findViewById(R.id.me_title_text);
        titleText.setText(title);
        leftView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onToolBarClickLisenear.onClick(v);
            }
        });
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                ,ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        this.addView(view);
    }
    //中间文字
    public void setCenterTextTitle(String title,Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.me_title_left_arrow,null,false);
        ImageView imageView = (ImageView) view.findViewById(R.id.me_title_left_arrow);
        imageView.setVisibility(View.GONE);
        TextView titleText = (TextView) view.findViewById(R.id.me_title_text);
        titleText.setText(title);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                ,ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        this.addView(view);
    }
    //左边文字，中间文字
    public void setLeftTextCenterTextTitle(String leftText,String title,Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.me_title_left_text,null,false);
        RelativeLayout leftView = (RelativeLayout) view.findViewById(R.id.main_left_layout);
        TextView titleText = (TextView) view.findViewById(R.id.me_title_text);
        TextView leftTextTv = (TextView) view.findViewById(R.id.me_title_left_text);
        titleText.setText(title);
        leftTextTv.setText(leftText);
        leftView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onToolBarClickLisenear.onClick(v);
            }
        });
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                ,ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        this.addView(view);
    }
    //左边返回键，中间文字，右边图片
    public void setLeftArrowCenterTextTitleRightImage(String title,Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.me_title_left_arrow_right_image,null,false);
        RelativeLayout leftView = (RelativeLayout) view.findViewById(R.id.main_left_layout);
        RelativeLayout rightView = (RelativeLayout) view.findViewById(R.id.main_right_layout);
        TextView titleText = (TextView) view.findViewById(R.id.me_title_text);
//        final TextView titleText1 = (TextView) view_donate_records.findViewById(R.id.me_title_right_text);
//        final ImageView imageView = (ImageView) view_donate_records.findViewById(R.id.me_title_right_image);
        titleText.setText(title);
        leftView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onToolBarClickLisenear.onClick(v);
            }
        });
        rightView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(titleText1.getVisibility() == View.INVISIBLE){
//                    titleText1.setVisibility(VISIBLE);
//                    imageView.setVisibility(INVISIBLE);
//                }else{
//                    titleText1.setVisibility(INVISIBLE);
//                    imageView.setVisibility(VISIBLE);
//                }
                onToolBarClickLisenear.onClick(v);
            }
        });
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                ,ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        this.addView(view);
    }

    //左边返回键，中间文字，右边文字
    public void setLeftArrowCenterTextTitleRightText(String title,String rightTitleText,Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.main_title_left_arrow_right_text,null,false);
        RelativeLayout leftView = (RelativeLayout) view.findViewById(R.id.main_left_layout);
        RelativeLayout rightView = (RelativeLayout) view.findViewById(R.id.main_right_layout);
        TextView titleText = (TextView) view.findViewById(R.id.me_title_text);
        TextView rightText = (TextView) view.findViewById(R.id.me_title_right_text);
        this.rightTv = rightText;
        titleText.setText(title);
        rightText.setText(rightTitleText);
        leftView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onToolBarClickLisenear.onClick(v);
            }
        });
        rightView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onToolBarClickLisenear.onClick(v);
            }
        });
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                ,ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        this.addView(view);
    }

    //左边文字，中间文字，右边文字
    public void setLeftTextCenterTextTitleRightText(String leftText,String title,String rightText,Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.me_title_left_text_right_text,null,false);
        RelativeLayout leftView = (RelativeLayout) view.findViewById(R.id.main_left_layout);
        RelativeLayout rightView = (RelativeLayout) view.findViewById(R.id.main_right_layout);
        TextView titleText = (TextView) view.findViewById(R.id.me_title_text);
        TextView leftText_ = (TextView) view.findViewById(R.id.me_title_left_text);
        TextView rightText_ = (TextView) view.findViewById(R.id.me_title_right_text);
        titleText.setText(title);
        leftText_.setText(leftText);
        rightText_.setText(rightText);
        leftView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onToolBarClickLisenear.onClick(v);
            }
        });
        rightView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onToolBarClickLisenear.onClick(v);
            }
        });
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                ,ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        this.addView(view);
    }

    //左边图片，中间View，右边图片
    public void setLeftImageCenterViewTitleRightImage(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.main_title_search,null,false);
        RelativeLayout leftView = (RelativeLayout) view.findViewById(R.id.main_left_layout);
        RelativeLayout rightView = (RelativeLayout) view.findViewById(R.id.main_right_layout);
        LinearLayout centerView = (LinearLayout) view.findViewById(R.id.hp_search);
        centerView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onToolBarClickLisenear.onClick(v);
            }
        });
        leftView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onToolBarClickLisenear.onClick(v);
            }
        });
        rightView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onToolBarClickLisenear.onClick(v);
            }
        });
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                ,ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        this.addView(view);
    }

    //左边图片，中间标题，右边图片  --->联系人列表
    public void setLeftImageCenterTitleRightImage(Context context,int leftImgId,String title, int rightImgId){
        View view = LayoutInflater.from(context).inflate(R.layout.toolbar_left_img_center_title_right_img,null,false);
//        RelativeLayout leftView = (RelativeLayout) view.findViewById(R.id.main_left_layout);
//        RelativeLayout rightView = (RelativeLayout) view.findViewById(R.id.main_right_layout);
//        LinearLayout centerView = (LinearLayout) view.findViewById(R.id.hp_search);
		RelativeLayout leftView = (RelativeLayout) view.findViewById(R.id.main_left_layout);
		ImageView leftImg = (ImageView) view.findViewById(R.id.img_yaoyao);
		leftImg.setImageResource(leftImgId);

		RelativeLayout rightView = (RelativeLayout) view.findViewById(R.id.main_right_layout);
		ImageView rightImg = (ImageView) view.findViewById(R.id.img_add_friend);
		rightImg.setImageResource(rightImgId);
//		LinearLayout centerView = (LinearLayout) view.findViewById(R.id.hp_search);
		TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
		tvTitle.setText(title);
        leftView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onToolBarClickLisenear.onClick(v);
            }
        });
        rightView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onToolBarClickLisenear.onClick(v);
            }
        });
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                ,ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        this.addView(view);
    }





    // set
    public void setTab(int id){
        final TextView book1 = (TextView) middleView.findViewById(R.id.main_family_book_1);
        final TextView book2 = (TextView) middleView.findViewById(R.id.main_family_book_2);
        Resources resources = mContext.getResources();
        final int white = resources.getColor(R.color.white);
        final int family = resources.getColor(R.color.title_family_book);

        switch (id){
            case R.id.main_family_book_1:
                book1.setBackgroundColor(family);
                book2.setBackgroundColor(white);
                book1.setTextColor(white);
                book2.setTextColor(family);
                break;

            case R.id.main_family_book_2:
                book1.setBackgroundColor(white);
                book2.setBackgroundColor(family);
                book1.setTextColor(family);
                book2.setTextColor(white);
                break;
        }

    }
    //左边返回键，中间文字，右边图片 -- 族谱 toolbar
    public void setLeftArrowCenterBook(Context context  ){
        middleView = LayoutInflater.from(context).inflate(R.layout.title_family_book,null,false);
        RelativeLayout leftView = (RelativeLayout) middleView.findViewById(R.id.main_left_layout);
        RelativeLayout rightView = (RelativeLayout) middleView.findViewById(R.id.main_right_layout);

        final TextView book1 = (TextView) middleView.findViewById(R.id.main_family_book_1);
        final TextView book2 = (TextView) middleView.findViewById(R.id.main_family_book_2);
        Resources resources = context.getResources();
        final int white = resources.getColor(R.color.white);
        final int family = resources.getColor(R.color.title_family_book);
        book1.setTextColor(white);
        book2.setTextColor(family);
        book1.setBackgroundColor(family);
        book2.setBackgroundColor(white);
        book1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                book1.setBackgroundColor(family);
                book2.setBackgroundColor(white);
                book1.setTextColor(white);
                book2.setTextColor(family);
                onToolBarClickLisenear.onClick(v);
            }
        });
        book2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                book1.setBackgroundColor(white);
                book2.setBackgroundColor(family);
                book1.setTextColor(family);
                book2.setTextColor(white);
                onToolBarClickLisenear.onClick(v);
            }
        });
        rightView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onToolBarClickLisenear.onClick(v);
            }
        });
        leftView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onToolBarClickLisenear.onClick(v);
            }
        });
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                ,ViewGroup.LayoutParams.MATCH_PARENT);
        middleView.setLayoutParams(params);
        this.addView(middleView);
    }

    // 中间文字，右边显示联系人操作
    public void setIMContactToolbar(String title,Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_toobar_contact_list,null,false);
        TextView titleText = (TextView) view.findViewById(R.id.me_title_text);
        ImageView imgMenu =  (ImageView) view.findViewById(R.id.img_im_contacts_menu);
        titleText.setText(title);
        imgMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onToolBarClickLisenear.onClick(v);
            }
        });
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        this.addView(view);
    }

    private OnToolBarClickLisenear onToolBarClickLisenear;

    public void setOnToolBarClickLisenear(OnToolBarClickLisenear onToolBarClickLisenear){
        this.onToolBarClickLisenear = onToolBarClickLisenear;
    }

    public interface OnToolBarClickLisenear{
        void onClick(View v);
    }

    public TextView getRightTv(){
        return rightTv;
    }
}
