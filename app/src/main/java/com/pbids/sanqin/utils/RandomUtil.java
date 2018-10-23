package com.pbids.sanqin.utils;

import java.util.Random;

//随机内容
public class RandomUtil {

    static final String LETTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    //RandomString
    public static String  randomString(int order){
        StringBuilder strBuilder = new StringBuilder();
        Random ran = new Random();
        for(int j = 0;j<order;j++){
            //得到字符串长度的随机数
            int r1 = ran.nextInt(LETTERS.length());
            strBuilder.append(LETTERS.charAt(r1));
        }
        return strBuilder.toString();
    }
}
