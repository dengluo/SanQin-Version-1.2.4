package com.netease.nim.uikit.business.eventbus;

/**
 * Created by pbids903 on 2017/12/19.
 */

public class PermissionEvent {
    public final static int REQUEST_CODE_TAKE_PICTURE_PESSION = 0x201;
    public final static int REQUEST_CODE_IMAGES_PESSION = 0x202;
    public final static int REQUEST_CODE_CAMERA = 0x203;
    public final static int REQUEST_CODE_LOCATION = 0x204;
    public final static int RESULT_ERROR_CODE = -1;

    public int requestCode;
    public int pessionResultCode;

    public int getPessionRequestCode() {
        return requestCode;
    }

    public int getPessionResultCode() {
        return pessionResultCode;
    }

    public void setPessionResultCode(int pessionResultCode) {
        this.pessionResultCode = pessionResultCode;
    }

    public void setPessionRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }
}