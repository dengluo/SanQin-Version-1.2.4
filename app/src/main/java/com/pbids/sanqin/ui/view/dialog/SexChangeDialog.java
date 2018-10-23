package com.pbids.sanqin.ui.view.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.pbids.sanqin.R;
import com.pbids.sanqin.common.CommonFinalVariable;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pbids903 on 2018/1/19.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:11
 * @desscribe 类描述:性别选择dialog
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.me.MeInformationFragment
 */
public class SexChangeDialog extends BaseDialog {

    @Bind(R.id.pop_me_sex_man)
    ImageView popMeSexMan;
    @Bind(R.id.pop_me_sex_man_layout)
    RelativeLayout popMeSexManLayout;
    @Bind(R.id.pop_me_sex_women)
    ImageView popMeSexWomen;
    @Bind(R.id.pop_me_sex_women_layout)
    RelativeLayout popMeSexWomenLayout;
    @Bind(R.id.pop_me_sex_layout)
    LinearLayout popMeSexLayout;

    int type;

    public SexChangeDialog(@NonNull Context context,int type) {
        super(context);
        this.type = type;
    }

    @Override
    void initView() {
        setContentView(R.layout.pop_me_information_sex);
        ButterKnife.bind(this);

        switch (type){
            case CommonFinalVariable.SEX_TYPE_MAN:
                popMeSexMan.setSelected(true);
                popMeSexWomen.setSelected(false);
                break;
            case CommonFinalVariable.SEX_TYPE_WOMEN:
                popMeSexMan.setSelected(false);
                popMeSexWomen.setSelected(true);
                break;
            default:
                popMeSexMan.setSelected(false);
                popMeSexWomen.setSelected(false);
                break;
        }
    }

    @OnClick({R.id.pop_me_sex_man_layout
            , R.id.pop_me_sex_women_layout, R.id.pop_me_sex_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pop_me_sex_man_layout:
                popMeSexMan.setSelected(true);
                popMeSexWomen.setSelected(false);
                lisenear.onClick(CommonFinalVariable.SEX_TYPE_MAN);
                dismiss();
                break;
            case R.id.pop_me_sex_women_layout:
                popMeSexMan.setSelected(false);
                popMeSexWomen.setSelected(true);
                lisenear.onClick(CommonFinalVariable.SEX_TYPE_WOMEN);
                dismiss();
                break;
            case R.id.pop_me_sex_layout:

                break;
        }
    }

    public void setSexType(int type){
        this.type = type;
    }

    OnPopSexClickLisenear lisenear;

    public void setOnPopSexClickLisenear(OnPopSexClickLisenear lisenear){
        this.lisenear = lisenear;
    }

    public interface OnPopSexClickLisenear{
        void onClick(int type);
    }
}
