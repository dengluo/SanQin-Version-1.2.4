package com.pbids.sanqin.ui.activity.me;

/**
 * Created by pbids903 on 2017/12/23.
 */

public interface MePageView {
    public static final String ME_REVICE_NAME = "1";
    public static final String ME_REVICE_LOCATION = "2";
    public static final String ME_REVICE_PICTURE = "3";
    public static final String ME_REVICE_SEX = "4";
    public static final String ME_REVICE_AUTOGRAHT = "5";
    public static final String ME_REVICE_PHONE = "6";
    public static final String ME_REVICE_NATIVEPLACE = "7";
    public static final String ME_REVICE_NICK_NAME = "8";
    public void reviseSuccess(String type);
    public void reviseError(String type);
}
