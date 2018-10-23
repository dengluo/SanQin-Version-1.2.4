package com.pbids.sanqin.utils.eventbus;

import android.net.Uri;

/**
 * Created by pbids903 on 2017/12/6.
 */

public class CameraEvent {
    public int resultCode;
    public int requestCode;
    public Uri uri;

    public void setResultCode(int resultCode){
        this.resultCode = resultCode;
    }
    public void setRequestCode(int requestCode){
        this.requestCode = requestCode;
    }

    public void setUri(Uri uri){
        this.uri = uri;
    }
}