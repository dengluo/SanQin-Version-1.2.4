package com.pbids.sanqin.ui.view.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.pbids.sanqin.R;
import com.pbids.sanqin.ui.view.PhotoChossePop;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pbids903 on 2018/1/19.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:10
 * @desscribe 类描述:我的个人信息-图片选择弹窗dialog
 * @remark 备注:
 * @see
 */
public class PhotoChosseDialog extends BaseDialog {

    Context mContext;
    @Bind(R.id.me_picture_pop_photograph)
    TextView mePicturePopPhotograph;
    @Bind(R.id.me_picture_pop_choose)
    TextView mePicturePopChoose;
    @Bind(R.id.me_picture_pop_history)
    TextView mePicturePopHistory;
    @Bind(R.id.me_picture_pop_cancel)
    TextView mePicturePopCancel;

    public PhotoChosseDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    void initView() {
        setContentView(R.layout.pop_me_information_picture);
        ButterKnife.bind(this);
    }

    @Override
    public void onDetachedFromWindow() {
//        ButterKnife.unbind(this);
        super.onDetachedFromWindow();
    }

    @OnClick({R.id.me_picture_pop_photograph, R.id.me_picture_pop_choose
            , R.id.me_picture_pop_history, R.id.me_picture_pop_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
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
