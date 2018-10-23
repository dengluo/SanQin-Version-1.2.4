package com.pbids.sanqin.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.CustomPopView;
import com.pbids.sanqin.model.entity.FamilyRank;
import com.pbids.sanqin.ui.activity.HomePageActivity;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by pbids903 on 2017/11/16.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:13
 * @desscribe 类描述:籍贯选择view
 * @remark 备注:
 * @see
 */
public class AreaSelectorView extends CustomPopView implements View.OnClickListener{
    Context mContext;
    View mAreaSelectorViewLayout;

    RelativeLayout homeChoose;
    TextView homeLocation;
    Button homeComfirm;
    ArrayList<FamilyRank> mFamilyRanks;

    public AreaSelectorView(Context context,ArrayList<FamilyRank> familyRanks) {
        super(context);
        this.mContext = context;
        this.mFamilyRanks = familyRanks;
        mAreaSelectorViewLayout = LayoutInflater.from(context).inflate(R.layout.write_home, contentContainer);

        initView();
        initEvent();
    }

    public void initView(){
        homeChoose = (RelativeLayout) mAreaSelectorViewLayout.findViewById(R.id.home_choose);
        homeLocation = (TextView) mAreaSelectorViewLayout.findViewById(R.id.home_location);
        homeComfirm = (Button) mAreaSelectorViewLayout.findViewById(R.id.home_comfirm);

        TextView rankingFirstName = (TextView) mAreaSelectorViewLayout.findViewById(R.id.home_ranking_first_name);
        TextView rankingSecondName = (TextView) mAreaSelectorViewLayout.findViewById(R.id.home_ranking_second_name);
        TextView rankingThirdName = (TextView) mAreaSelectorViewLayout.findViewById(R.id.home_ranking_third_name);
        TextView rankingFourthName = (TextView) mAreaSelectorViewLayout.findViewById(R.id.home_ranking_fourth_name);

        TextView rankingFirstNumber = (TextView) mAreaSelectorViewLayout.findViewById(R.id.home_ranking_first_number);
        TextView rankingSecondNumber = (TextView) mAreaSelectorViewLayout.findViewById(R.id.home_ranking_second_number);
        TextView rankingThirdNumber = (TextView) mAreaSelectorViewLayout.findViewById(R.id.home_ranking_third_number);
        TextView rankingFourthNumber = (TextView) mAreaSelectorViewLayout.findViewById(R.id.home_ranking_fourth_number);

        TextView rankingFourth = (TextView) mAreaSelectorViewLayout.findViewById(R.id.home_ranking_fourth);

        RelativeLayout rankingFirstLayout = (RelativeLayout) mAreaSelectorViewLayout.findViewById(R.id.ranking_first_layout);
        RelativeLayout rankingSecondLayout = (RelativeLayout) mAreaSelectorViewLayout.findViewById(R.id.ranking_second_layout);
        RelativeLayout rankingThirdLayout = (RelativeLayout) mAreaSelectorViewLayout.findViewById(R.id.ranking_third_layout);
        RelativeLayout rankingFourthLayout = (RelativeLayout) mAreaSelectorViewLayout.findViewById(R.id.ranking_fourth_layout);
        if(mFamilyRanks.size()==1){
            rankingFirstName.setText(mFamilyRanks.get(0).getSurname()+"氏家族");
            rankingFirstNumber.setText(""+mFamilyRanks.get(0).getPeopleNum());
            rankingSecondLayout.setVisibility(View.GONE);
            rankingThirdLayout.setVisibility(View.GONE);
            rankingFourthLayout.setVisibility(View.GONE);
        }else if(mFamilyRanks.size()==2){
            rankingFirstName.setText(mFamilyRanks.get(0).getSurname()+"氏家族");
            rankingSecondName.setText(mFamilyRanks.get(1).getSurname()+"氏家族");
            rankingFirstNumber.setText(""+mFamilyRanks.get(0).getPeopleNum());
            rankingSecondNumber.setText(""+mFamilyRanks.get(1).getPeopleNum());
            rankingThirdLayout.setVisibility(View.GONE);
            rankingFourthLayout.setVisibility(View.GONE);
        }else if(mFamilyRanks.size()==3){
            rankingFirstName.setText(mFamilyRanks.get(0).getSurname()+"氏家族");
            rankingSecondName.setText(mFamilyRanks.get(1).getSurname()+"氏家族");
            rankingThirdName.setText(mFamilyRanks.get(2).getSurname()+"氏家族");
            rankingFirstNumber.setText(""+mFamilyRanks.get(0).getPeopleNum());
            rankingSecondNumber.setText(""+mFamilyRanks.get(1).getPeopleNum());
            rankingThirdNumber.setText(""+mFamilyRanks.get(2).getPeopleNum());
            rankingFourthLayout.setVisibility(View.GONE);
        }else if(mFamilyRanks.size()==4){
            rankingFirstName.setText(mFamilyRanks.get(0).getSurname()+"氏家族");
            rankingSecondName.setText(mFamilyRanks.get(1).getSurname()+"氏家族");
            rankingThirdName.setText(mFamilyRanks.get(2).getSurname()+"氏家族");
            rankingFourthName.setText(mFamilyRanks.get(3).getSurname()+"氏家族");

            rankingFirstNumber.setText(""+mFamilyRanks.get(0).getPeopleNum());
            rankingSecondNumber.setText(""+mFamilyRanks.get(1).getPeopleNum());
            rankingThirdNumber.setText(""+mFamilyRanks.get(2).getPeopleNum());
            rankingFourthNumber.setText(""+mFamilyRanks.get(3).getPeopleNum());

            rankingFourth.setText(""+mFamilyRanks.get(3).getRank());
        }
    }

    public void initEvent(){
        homeChoose.setOnClickListener(this);
        homeComfirm.setOnClickListener(this);
    }

    public void setArea(String str){
        if(null != homeLocation){
            homeLocation.setText(str);
        }
    }

    public String getArea(){
        String[] strings = homeLocation.getText().toString().split("[-]");
        String s ="";
        for(int i=0;i<strings.length;i++){
            if(i==0){
                s +=strings[i];
            }else{
                s +=","+strings[i];
            }
        }
        return s;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_choose:
//                HomePageActivity homePageActivity = (HomePageActivity)mContext;
//                homePageActivity.showAreaSelector();
                if(areaShowLinsener!=null){
                    areaShowLinsener.show();
                }
                break;
            case R.id.home_comfirm:
                areaShowLinsener.submitInfotmation();
//                Glide.get(mContext).clearMemory();
//                dismiss();
                break;
        }
    }

    AreaShowLinsener areaShowLinsener;

    public void setAreaLinsener(AreaShowLinsener areaShowLinsener){
        this.areaShowLinsener = areaShowLinsener;
    }

    public interface AreaShowLinsener{
        public void show();
        void submitInfotmation();
    }
}
