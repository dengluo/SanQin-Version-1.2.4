package com.pbids.sanqin.utils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by pbids903 on 2018/3/16.
 */

public class StringUtil {
    //把string转成list
    public static List<String> convertToEntityProperty(String databaseValue,String splitCharacter) {
        if (databaseValue == null) {
            return null;
        }
        else {
            List<String> list = Arrays.asList(databaseValue.split(splitCharacter));
            return list;
        }
    }

    //把list转string
    public static String convertToDatabaseValue(List<String> entityProperty,String splitCharacter) {
        if(entityProperty==null){
            return null;
        }

        if(entityProperty.size()==0){
            return "";
        }

        StringBuilder sb= new StringBuilder();
        for(int i=0;i<entityProperty.size();i++){
            String link = entityProperty.get(i);
            sb.append(link);
            if(entityProperty.size()-1!=i){
                sb.append(splitCharacter);
            }
        }
//            for(String link:entityProperty){
//                sb.append(link);
//                sb.append(splitCharacter);
//            }
            return sb.toString();
    }

    //把list转string
    public static String convertToDatabaseValue(Set<String> entityProperty, String splitCharacter) {
        if(entityProperty==null){
            return null;
        }

        if(entityProperty.size()==0){
            return "";
        }

        StringBuilder sb= new StringBuilder();
        Iterator<String> iter = entityProperty.iterator();
        while(iter.hasNext()){
            if(sb.length()>0){
                sb.append(splitCharacter);
            }
            sb.append( iter.next());
        }
        return sb.toString();
    }
}
