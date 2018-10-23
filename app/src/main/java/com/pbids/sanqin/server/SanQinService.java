package com.pbids.sanqin.server;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

public class SanQinService extends Service {

    public static final int GET_DATA = 1;
    public static final int SET_DATA = 2;

    //back service
    public class SQBinder extends Binder implements ISQBinder  {
        //service
        public SanQinService getService(){
            return SanQinService.this;
        }

    }

    private final IBinder mBinder = new SQBinder();



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("SanQinService","onCreate...");
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, flags, startId);

        new Thread(){
            @Override
            public void run() {
                /*while (true){
                    Log.v("SanQinService","run ...");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }*/
            }
        }.run();
//        return START_REDELIVER_INTENT;
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }




}
