package com.pbids.sanqin.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pbids.sanqin.R;
import com.pbids.sanqin.common.CommonGlideInstance;
import com.pbids.sanqin.common.CustomPopView;
import com.pbids.sanqin.model.entity.Gift;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:19
 * @desscribe 类描述:一张图片一个按钮的弹窗pop
 * @remark 备注:pop改为dialog
 * @see
 */
public class OneImageOneBtPop extends CustomPopView{

    public static final String APP_UPDATA ="app_updata"; // app 更新
    public static final String POP_CLEAR_CRASH = "pop_clear_crash";
    public static final String POP_GIFR_EXCHANGE = "pop_gift_exchange";
    public static final String POP_WITHDRAW_CASH = "pop_withdraw_cash"; //提现

    Context mContext;
    ImageView imageView;
    TextView aboveTextView;
    TextView blewTextView;
    Button button;
    String mType;

    private String giftCode;

    public OneImageOneBtPop(Context context, String type,String giftCode){
        super(context);
        mContext = context;
        mType = type;
        this.giftCode = giftCode;

        View view = LayoutInflater.from(context).inflate(R.layout.pop_one_image_one_button, contentContainer);
        imageView = (ImageView) view.findViewById(R.id.one_image);
        aboveTextView = (TextView) view.findViewById(R.id.one_text);
        blewTextView = (TextView) view.findViewById(R.id.two_text);
        button = (Button) view.findViewById(R.id.one_button);

        if(APP_UPDATA.equals(type)){
            imageView.setVisibility(View.GONE);
            aboveTextView.setVisibility(View.GONE);
            blewTextView.setText("当前版本已经是最新版本");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }else if(type.equals(POP_CLEAR_CRASH)){
            button.setVisibility(View.GONE);
            blewTextView.setVisibility(View.GONE);
            int size = (int)context.getResources().getDimension(R.dimen.dp_85);
            new CommonGlideInstance().setImageViewBackgroundForUrl(context
                    ,imageView,R.drawable.me_icon_qinglihuancun_default);
            aboveTextView.setText("清理缓存中...");
        }else if(type.equals(POP_GIFR_EXCHANGE)){
            imageView.setImageResource(R.drawable.me_icon_chenggong_default);
            aboveTextView.setText("兑换成功");
            if(giftCode.equals("VIRTUAL")){
//                blewTextView.setText("去\"烧砖页面\"里查看");
                blewTextView.setText("去烧砖页面里查看");
            }else{
//                blewTextView.setText("现金已放到\"我的资金\"里");
                blewTextView.setText("现金已放到我的资金里");
            }
            button.setText("我知道了");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }else if(type.equals(POP_WITHDRAW_CASH)){
            //提现结果
            imageView.setImageResource(R.drawable.me_icon_yitijiao_default);
            aboveTextView.setText("");
            blewTextView.setText("申请提交成功");
            button.setText("我知道了");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }
    public OneImageOneBtPop(Context context, String type){
        super(context);
        mContext = context;
        mType = type;

        View view = LayoutInflater.from(context).inflate(R.layout.pop_one_image_one_button, contentContainer);
        imageView = (ImageView) view.findViewById(R.id.one_image);
        aboveTextView = (TextView) view.findViewById(R.id.one_text);
        blewTextView = (TextView) view.findViewById(R.id.two_text);
        button = (Button) view.findViewById(R.id.one_button);

        if(APP_UPDATA.equals(type)){
            imageView.setVisibility(View.GONE);
            aboveTextView.setVisibility(View.GONE);
            blewTextView.setText("当前版本已经是最新版本");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }else if(type.equals(POP_CLEAR_CRASH)){
            button.setVisibility(View.GONE);
            blewTextView.setVisibility(View.GONE);
            int size = (int)context.getResources().getDimension(R.dimen.dp_85);
            new CommonGlideInstance().setImageViewBackgroundForUrl(context
                    ,imageView,R.drawable.me_icon_qinglihuancun_default);
            aboveTextView.setText("清理缓存中...");
        }/*else if(type.equals(POP_GIFR_EXCHANGE)){
            imageView.setImageResource(R.drawable.me_icon_chenggong_default);
            aboveTextView.setText("兑换成功");
            blewTextView.setText("现金已放到\"我的资金\"里");
            button.setText("我知道了");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }*/else if(type.equals(POP_WITHDRAW_CASH)){
            //提现结果
            imageView.setImageResource(R.drawable.me_icon_yitijiao_default);
            aboveTextView.setText("");
            blewTextView.setText("申请提交成功");
            button.setText("我知道了");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }

    public void setTitle(String title){
        aboveTextView.setText(title);
        aboveTextView.setVisibility(View.VISIBLE);
    }
    public void setNoTitle( ){
        aboveTextView.setVisibility(View.GONE);
    }


    public void setContent(String content){
        blewTextView.setText(content);
    }

    public void setClearSuccess(){
        if(mType.equals(POP_CLEAR_CRASH)){
            int size = (int)mContext.getResources().getDimension(R.dimen.dp_85);
            new CommonGlideInstance().setImageViewBackgroundForUrl(mContext
                    ,imageView,R.drawable.me_icon_chenggong_default);
            aboveTextView.setText("清理缓存成功");
        }
    }
}
