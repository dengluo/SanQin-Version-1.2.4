package com.pbids.sanqin.utils;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by pbids903 on 2018/1/8.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:38
 * @desscribe 类描述:
 * @remark 备注:带，号的String切转list，list转带，号的string
 * @see
 */
public class StringConverter implements PropertyConverter<List<String>, String> {

    //把string转成list
    @Override
    public List<String> convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        else {
            List<String> list = Arrays.asList(databaseValue.split(","));
            return list;
        }
    }

    //把list转string
    @Override
    public String convertToDatabaseValue(List<String> entityProperty) {
        if(entityProperty==null){
            return null;
        }
        else{
            StringBuilder sb= new StringBuilder();
            for(String link:entityProperty){
                sb.append(link);
                sb.append(",");
            }
            return sb.toString();
        }
    }
}
