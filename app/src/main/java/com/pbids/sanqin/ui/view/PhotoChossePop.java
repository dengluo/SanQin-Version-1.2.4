package com.pbids.sanqin.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.pbids.sanqin.R;
import com.pbids.sanqin.common.CustomPopView;

import butterknife.Bind;

/**
 * Created by pbids903 on 2017/12/6.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:20
 * @desscribe 类描述:选择图片弹窗
 * @remark 备注:改为dialog
 * @see
 */
public class PhotoChossePop extends CustomPopView implements View.OnClickListener{

    public final static int REQUEST_CODE_TAKE_PICTURE = 111;
    public final static int REQUEST_CODE_TAKE_PICTURE_PESSION = 0x101;
    public static final int IMAGE_REQUEST_CODE = 0x102;

    @Bind(R.id.me_picture_pop_photograph)
    TextView mePicturePopPhotograph;
    @Bind(R.id.me_picture_pop_choose)
    TextView mePicturePopChoose;
    @Bind(R.id.me_picture_pop_history)
    TextView mePicturePopHistory;
    @Bind(R.id.me_picture_pop_cancel)
    TextView mePicturePopCancel;
    Context mContext;

    public PhotoChossePop(Context context) {
        super(context);
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.pop_me_information_picture, contentContainer);
        mePicturePopPhotograph = (TextView) findViewById(R.id.me_picture_pop_photograph);
        mePicturePopChoose = (TextView) findViewById(R.id.me_picture_pop_choose);
        mePicturePopHistory = (TextView) findViewById(R.id.me_picture_pop_history);
        mePicturePopCancel = (TextView) findViewById(R.id.me_picture_pop_cancel);
        mePicturePopPhotograph.setOnClickListener(this);
        mePicturePopChoose.setOnClickListener(this);
        mePicturePopHistory.setOnClickListener(this);
        mePicturePopCancel.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.me_picture_pop_photograph:
                if(onPhotoResultLisener!=null){
                    onPhotoResultLisener.takePhoto();
                }
                break;
            case R.id.me_picture_pop_choose:
                onPhotoResultLisener.openAlem();
                break;
            case R.id.me_picture_pop_history:
                break;
            case R.id.me_picture_pop_cancel:
                dismiss();
                break;
        }
    }

    OnPhotoResultLisener onPhotoResultLisener;
    public void setOnPhotoResultLisener(OnPhotoResultLisener onPhotoResultLisener){
        this.onPhotoResultLisener = onPhotoResultLisener;
    }

    public interface OnPhotoResultLisener{
        public void takePhoto();
        public void openAlem();
    }
}
