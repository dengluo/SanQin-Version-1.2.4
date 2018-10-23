package com.pbids.sanqin.utils;

import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

public class ToastUtil {

    private static Toast toast;
    private static int textview_id;

    public static void show(Context context, String str) {
        if (toast == null){
            toast = Toast.makeText(context.getApplicationContext(), str, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
        }
        else
            toast.setText(str);
        if (textview_id == 0)
            textview_id = Resources.getSystem().getIdentifier("message", "id", "android");
        ((TextView) toast.getView().findViewById(textview_id)).setGravity(Gravity.CENTER);
        toast.show();    }

        public static void show(Context context, int resId) {
        if (toast == null)
            toast = Toast.makeText(context.getApplicationContext(), resId, Toast.LENGTH_SHORT);
        else
            toast.setText(resId);
        if (textview_id == 0)
            textview_id = Resources.getSystem().getIdentifier("message", "id", "android");
        ((TextView) toast.getView().findViewById(textview_id)).setGravity(Gravity.CENTER);
        toast.show();
    }

}
