package com.pbids.sanqin.common;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.pbids.sanqin.R;
import com.pbids.sanqin.model.db.SystemMessageManager;
import com.pbids.sanqin.model.entity.SystemMessage;

import java.util.Date;
import java.util.Map;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:33
 * @desscribe 类描述:阿里推送消息接受
 * @remark 备注
 * @see
 */

public class AliyunMessageReceiver extends MessageReceiver {

    private Context mContext;


    // 消息接收部分的LOG_TAG

    public static final String REC_TAG = "aili_push";


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        this.mContext = context;
    }

    /**

     * 推送通知的回调方法

     * @param context

     * @param title

     * @param summary

     * @param extraMap

     */



    @SuppressLint("WrongConstant")
    @Override
    public void onNotification(Context context, String title, String summary, Map<String, String> extraMap) {
        String id = "1124564";
        // TODO 处理推送通知
        if ( null != extraMap ) {
            for (Map.Entry<String, String> entry : extraMap.entrySet()) {
                Log.i(REC_TAG,"@Get diy param : Key=" + entry.getKey() + " , Value=" + entry.getValue());
                id = entry.getValue();
            }
        } else {
            Log.i(REC_TAG,"@收到通知 && 自定义消息为空");
        }
        String ids = extraMap.get("_ALIYUN_NOTIFICATION_ID_");
        //显示推送
        showPush(title,summary,context,ids);
        Log.i(REC_TAG,"收到一条推送通知 ： " + title + ", summary:" + summary);
//        MainApplication.setConsoleText("收到一条推送通知 ： " + title + ", summary:" + summary);

        JSONObject msgJson ;
        try{
            //{"icon":"","topic":"","body":"今天很热","type":1,"url":""}
           // msgJson = JSONObject.parseObject(summary) ;
            SystemMessage msg = new SystemMessage() ;
            msg.setTitle(title);
            msg.setTime(new Date().getTime());
            msg.setFromAccount("");
            msg.setSessionId("");
            msg.setUrl( extraMap.get("url") );
            msg.setIcon( extraMap.get("icon") );
            msg.setTopic( extraMap.get("topic") );
            msg.setContent( extraMap.get("body") );
            msg.setSubTitle(summary);
            msg.setType( Integer.parseInt(extraMap.get("type"))  );
            msg.setSurname(extraMap.get("surname"));
            msg.setTags(extraMap.get("tags"));
            msg.setSurnameIcon(extraMap.get("surnameIcon"));
            msg.setMsgtype(SystemMessageManager.MSG_TYPE_SYSTEM);
            //解释系统消息
            MyApplication.parseSystemMessage(MyApplication.getApplication(),msg );

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 显示推送
     * @param title
     * @param content
     * @param context
     * @param id
     */
    private void showPush(String title,String content,Context context,String id) {
         NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(title, content, NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(context)
                    .setChannelId(title)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher_round).build();
        } else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                    .setWhen(System.currentTimeMillis())
                    .setOngoing(true);
            notification = notificationBuilder.build();
        }
        notificationManager.notify(Integer.parseInt(id), notification);
    }


    /**
     * 应用处于前台时通知到达回调。注意:该方法仅对自定义样式通知有效,相关详情请参考https://help.aliyun.com/document_detail/30066.html?spm=5176.product30047.6.620.wjcC87#h3-3-4-basiccustompushnotification-api
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     * @param openType
     * @param openActivity
     * @param openUrl
     */
    @Override
    protected void onNotificationReceivedInApp(Context context, String title, String summary, Map<String, String> extraMap, int openType, String openActivity, String openUrl) {
        Log.i(REC_TAG,"onNotificationReceivedInApp ： " + " : " + title
                + " : " + summary + "  " + extraMap + " : " + openType + " : " + openActivity + " : " + openUrl);
//        MainApplication.setConsoleText("onNotificationReceivedInApp ： " + " : " + title + " : " + summary);
        String id = "1124564";
        // TODO 处理推送通知
        if ( null != extraMap ) {
            for (Map.Entry<String, String> entry : extraMap.entrySet()) {
                Log.i(REC_TAG,"@Get diy param : Key=" + entry.getKey() + " , Value=" + entry.getValue());
                id = entry.getValue();
            }
        } else {
            Log.i(REC_TAG,"@收到通知 && 自定义消息为空");
        }
        String ids = extraMap.get("_ALIYUN_NOTIFICATION_ID_");
        //显示推送
        showPush(title,summary,context,ids);
    }



    /**

     * 推送消息的回调方法

     * @param context

     * @param cPushMessage

     */

    @Override
    public void onMessage(Context context, CPushMessage cPushMessage) {
        Log.i(REC_TAG,"收到一条推送消息 ： " + cPushMessage.getTitle() + ", content:" + cPushMessage.getContent());
//        MainApplication.setConsoleText(cPushMessage.getTitle() + ", content:" + cPushMessage.getContent());

    }



    /**
     * 从通知栏打开通知的扩展处理
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     */
    @Override
    public void onNotificationOpened(Context context, String title, String summary, String extraMap) {
        Log.i(REC_TAG,"onNotificationOpened ： " + " : " + title + " : " + summary + " : " + extraMap);
//        MainApplication.setConsoleText("onNotificationOpened ： " + " : " + title + " : " + summary + " : " + extraMap);
    }



    /**
     * 通知删除回调
     * @param context
     * @param messageId
     */
    @Override
    public void onNotificationRemoved(Context context, String messageId) {
        Log.i(REC_TAG, "onNotificationRemoved ： " + messageId);
//        MainApplication.setConsoleText("onNotificationRemoved ： " + messageId);
    }



    /**
     * 无动作通知点击回调。当在后台或阿里云控制台指定的通知动作为无逻辑跳转时,通知点击回调为onNotificationClickedWithNoAction而不是onNotificationOpened
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     */
    @Override
    protected void onNotificationClickedWithNoAction(Context context, String title, String summary, String extraMap) {
        Log.i(REC_TAG,"onNotificationClickedWithNoAction ： " + " : " + title + " : " + summary + " : " + extraMap);
//        MainApplication.setConsoleText("onNotificationClickedWithNoAction ： " + " : " + title + " : " + summary + " : " + extraMap);

    }

}
