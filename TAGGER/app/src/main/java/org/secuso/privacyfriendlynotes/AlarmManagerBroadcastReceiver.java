package org.secuso.privacyfriendlynotes;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;


/**
 * Created by Win7 on 2016/5/9.
 */
public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override

    public void onReceive(Context context, Intent intent3) {

        //取得TabFragment4.class的值  並依序命名
        String infoName = intent3.getStringExtra("InfoName");
        String infoContent = intent3.getStringExtra("InfoContent");
        //   if (Flag == 1) {  //如果Flag=1
        NotificationManager barmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notice;
        Notification.Builder builder = new Notification.Builder(context)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setWhen(System.currentTimeMillis())
                
                .setSmallIcon(R.drawable.fr_note)
                .setStyle(new Notification.BigTextStyle().bigText(infoContent));
        Intent appIntent = null;
        appIntent = new Intent(context, calender_Show.class);
        appIntent.setAction(Intent.ACTION_MAIN);
        appIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);//关键的一步，设置启动模式
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notice = builder.setContentIntent(contentIntent).setContentTitle(infoName).setContentText(infoContent).build();
            notice.flags = Notification.FLAG_AUTO_CANCEL;
            notice.defaults |= Notification.DEFAULT_ALL;
            barmanager.notify(1, notice);
            //}

        }
    }
}



