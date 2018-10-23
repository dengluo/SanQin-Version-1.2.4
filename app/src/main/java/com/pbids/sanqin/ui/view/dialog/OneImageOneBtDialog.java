package com.pbids.sanqin.ui.view.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pbids.sanqin.R;
import com.pbids.sanqin.common.OnDialogClickListener;
import com.pbids.sanqin.ui.activity.me.InviteTips;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pbids903 on 2018/1/29.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:09
 * @desscribe 类描述:一张图片一个按钮dialog
 * @remark 备注:
 * @see
 */
public class OneImageOneBtDialog extends BaseDialog {

    public static final String APP_UPDATA = "app_update";
    public static final String POP_CLEAR_CRASH = "pop_clear_crash";
    public static final String POP_GIFR_EXCHANGE = "pop_gift_exchange";
    public static final String POP_ME_AUTHENTICATION = "pop_me_authentication";
    @Bind(R.id.one_image)
    ImageView oneImage;
    @Bind(R.id.one_text)
    TextView oneText;
    @Bind(R.id.two_text)
    TextView twoText;
    @Bind(R.id.one_button)
    Button oneButton;

    public OneImageOneBtDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    void initView() {
        setContentView(R.layout.pop_one_image_one_button);
        ButterKnife.bind(this);
        setGrayCenter();
        setCanceledOnTouchOutside(false);
    }

    public void setType(String type){
        if(APP_UPDATA.equals("type")){
            oneImage.setVisibility(View.GONE);
            oneText.setVisibility(View.GONE);
            twoText.setText("当前版本已经是最新版本");
            oneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }else if(POP_CLEAR_CRASH.equals(type)){
            oneButton.setVisibility(View.GONE);
            twoText.setVisibility(View.GONE);
            int size = (int)getContext().getResources().getDimension(R.dimen.dp_85);
//            Glide.with(getContext()).load(R.drawable.me_icon_qinglihuancun_default)
//                    .skipMemoryCache(true).override(size,size).into(oneImage);
            oneImage.setImageResource(R.drawable.me_icon_qinglihuancun_default);
            oneText.setText("清理缓存中...");
        }else if(POP_GIFR_EXCHANGE.equals(type)){
            oneImage.setImageResource(R.drawable.me_icon_chenggong_default);
            oneText.setText("兑换成功");
            twoText.setText("现金已放到\"我的资金\"里");
            oneButton.setText("我知道了");
            oneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }else if(POP_ME_AUTHENTICATION.equals(type)){
            oneImage.setImageResource(R.drawable.me_icon_chenggong_default);
            oneText.setText("实名认证成功");
//            twoText.setText("现金已放到\"我的资金\"里");
            oneButton.setText("我知道了");
            oneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(inviteTips!=null){
                        inviteTips.confirm(v);
                    }else{
                        dismiss();
                    }
                }
            });
        }
    }

    public void setBlewText(String text){
        twoText.setText(text);
    }
    public void setAboveText(String text){
        oneText.setText(text);
    }

    public void setBlewTextSize(float textSize){
        twoText.setTextSize(textSize);
    }

    public void setBlewTextColor(int color){
        twoText.setTextColor(color);
    }

    private OnDialogClickListener onDialogClickLisenrar;

    public void setOnDialogClickLisenrar(OnDialogClickListener onComfirmLisenear){
        this.onDialogClickLisenrar = onComfirmLisenear;
    }

    private InviteTips inviteTips;

    public void setInviteTips(InviteTips inviteTips) {
        this.inviteTips = inviteTips;
    }

}
