package com.pbids.sanqin.common;

import android.util.Log;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:35
 * @desscribe 类描述:公用util类
 * @remark 备注
 * @see
 */

public class CommonUtil {
    /**
     * 屏蔽部分身份证号码
     * @param idNumber
     * @return
     */
    public static String shieldIdNumber(String idNumber){
        String s1 = idNumber.substring(0,3);
        String s2 = idNumber.substring(idNumber.length()-4,idNumber.length());
        String[]ss1 = idNumber.split(s1);
        for(int i=0;i<ss1.length;i++){
            Log.i("wzh",""+i+": "+ss1[i]);
        }
        String[]ss = ss1[1].split(s2);
//        idNumber = idNumber.substring(0,3)+"***********"+idNumber.substring(idNumber.length()-4,idNumber.length());
        idNumber = s1+ss[0].replaceAll("\\d","*")+s2;
        return idNumber;
    }

    /**
     * 屏蔽部分身份证号码
     * @param idNumber
     * @return
     */
    public static String shieldIdNumberSpace(String idNumber){
        String s1 = idNumber.substring(0,3);
        String s2 = idNumber.substring(idNumber.length()-4,idNumber.length());
        String[]ss1 = idNumber.split(s1);
        String[]ss = ss1[1].split(s2);
        String s3 = ss[0].replaceAll("\\d","*");
//        idNumber = idNumber.substring(0,3)+"***********"+idNumber.substring(idNumber.length()-4,idNumber.length());
//        idNumber = s1+" "+ss[0].replaceAll("\\d","*")+" "+s2;
        idNumber = s1+" "+s3.substring(0,3)+" "+s3.substring(4,ss[0].length())+" "+s2;
        return idNumber;
    }

    /**
     * 屏蔽部分银行卡号码
     * @param cardNumber
     * @return
     */
    public static String shieldCardNumber(String cardNumber){
        //隐藏部分银行卡号
        String s = cardNumber.substring(cardNumber.length() - 4,cardNumber.length());
        String[]ss = cardNumber.split(s);
        s = ss[0].replaceAll("\\d","*")+s;
        return s;
    }
}
