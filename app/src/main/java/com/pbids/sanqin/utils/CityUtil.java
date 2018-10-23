package com.pbids.sanqin.utils;

import android.content.Context;
import android.os.AsyncTask;

import com.pbids.sanqin.model.entity.Area;
import com.pbids.sanqin.model.entity.County;
import com.pbids.sanqin.model.entity.District;
import com.pbids.sanqin.model.entity.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pbids903 on 2017/12/15.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:29
 * @desscribe 类描述:异步的加载城市选择数据util
 * @remark 备注:
 * @see
 */
public class CityUtil {
    private int type=0;
    Context context;
    public CityUtil(Context context,int type){
        this.context = context;
        this.type = type;
    }

    private OnCityUtilLisenear cityUtilLisenear;

    public void setOnCityUtilLisenear(OnCityUtilLisenear onCityUtilLisenear){
        cityUtilLisenear = onCityUtilLisenear;
    }

    public void doCityUtilAsyncTask(){
        new AsyncTask<Void, Void, Area>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if(CityUtil.this.cityUtilLisenear==null){
                    this.cancel(true);
                }
            }

            @Override
            protected Area doInBackground(Void... params) {
                return AppUtils.readAreaJson(context.getApplicationContext(), Area.class);
            }

            @Override
            protected void onPostExecute(Area areaModel) {
                super.onPostExecute(areaModel);

                if (null == areaModel) {
                    return;
                }
                List<Province> provinces = areaModel.getData();
                if(type==0){
                    cityUtilLisenear.cityCall((ArrayList<Province>) provinces);
                    return;
                }

                final ArrayList<String> provinceNameList = new ArrayList<String>();
                final ArrayList<String> provinceIdList = new ArrayList<String>();
                final ArrayList<ArrayList<String>> districtList = new ArrayList<ArrayList<String>>();
                final ArrayList<ArrayList<String>> districtIdList = new ArrayList<ArrayList<String>>();
                final ArrayList<ArrayList<ArrayList<String>>> countyListList = new ArrayList<ArrayList<ArrayList<String>>>();
                final ArrayList<ArrayList<ArrayList<String>>> countyListIdList = new ArrayList<ArrayList<ArrayList<String>>>();

                for (Province province : provinces) {//对数组进行遍历得到每一个jsonobject对象
                    provinceNameList.add(province.getProvinceName());//向集合里面添加元素
                    provinceIdList.add(province.getProvinceId());
//                    provinceIdList.insert
                    ArrayList<String> districtNameList = new ArrayList<String>();
                    ArrayList<String> districtIdList2 = new ArrayList<String>();
                    ArrayList<ArrayList<String>> countyNameList = new ArrayList<ArrayList<String>>();
                    ArrayList<ArrayList<String>> countyIdList = new ArrayList<ArrayList<String>>();
//                    cities = new ArrayList<>();//创建存放城市名称集合
//                    areasList = new ArrayList<>();//创建存放区县名称的集合的集合
                    for (District district : province.getDistrict()) {//遍历每个省份集合下的城市列表
                        districtNameList.add(district.getDistrictName());//向集合里面添加元素
                        districtIdList2.add(district.getDistrictId());
                        ArrayList countyList = new ArrayList<>();//创建存放区县名称的集合
                        ArrayList countyIdList2 = new ArrayList<>();//创建存放区县名称的集合
                        for (County county : district.getCounty()) {
                            countyList.add(county.getCountyName());
                            countyIdList2.add(county.getCountyId());
                        }
                        countyNameList.add(countyList);
                        countyIdList.add(countyIdList2);
                    }
                    districtList.add(districtNameList);
                    countyListList.add(countyNameList);
                    districtIdList.add(districtIdList2);
                    countyListIdList.add(countyIdList);
                }
                cityUtilLisenear.cityCall(provinceNameList,districtList,countyListList,provinceIdList,districtIdList,countyListIdList);
            }
        }.execute();
    }



    public interface OnCityUtilLisenear {
        public void cityCall(ArrayList<String> provinceNameList,ArrayList<ArrayList<String>> districtList
                ,ArrayList<ArrayList<ArrayList<String>>> countyListList,ArrayList<String> provinceIdList
                ,ArrayList<ArrayList<String>> districtIdList,ArrayList<ArrayList<ArrayList<String>>> countyListIdList);
        public void cityCall(ArrayList<Province> provinces);
    }
}
