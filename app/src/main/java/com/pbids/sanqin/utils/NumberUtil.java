package com.pbids.sanqin.utils;

import java.text.DecimalFormat;

/**
 * Created by pbids903 on 2018/1/4.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:34
 * @desscribe 类描述:对text进行格式化
 * @remark 备注:
 * @see
 */
public class NumberUtil {

    public static String scalerWan(int number){
        if(number<10000){
            return ""+number;
        }else{
            double n = (double)number/10000;
            return String.format("%.1f",n)+"万";
        }
    }

    public static String scalerWanForEn(int number){
        if(number<10000){
            return ""+number;
        }else{
            double n = (double)number/10000;
            return String.format("%.1f",n)+"W";
        }
    }

    public static String scalerNoWan(int number){
        if(number<10000){
            return ""+number;
        }else{
            double n = (double)number/10000;
            return String.format("%.1f",n);
        }
    }

    public static String spaceAt4(String str) {
        StringBuilder sb = new StringBuilder();
        int length = str.length();
//        for (int i = 0; i < length; i += 4) {
//            if (length - i <= 8) {      //防止ArrayIndexOutOfBoundsException
//                sb.append(str.substring(i, i + 4)).append(" ");
////                sb.append(str.substring(i + 4));
//                break;
//            }
//            sb.append(str.substring(i, i + 4)).append(" ");
//        }
        for(int i = 0; i < length; i++){
//            sb.append(str.substring(i,i+1)).append(" ");
            sb.append(str.substring(i,i+1));
            if((i+1)%4==0){
                sb.append(" ");
            }
        }

        return sb.toString();
    }
}
