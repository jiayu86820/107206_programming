package org.secuso.privacyfriendlynotes;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
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
       // int Flag = intent3.getIntExtra("flag", 2);
     //   if (Flag == 1) {  //如果Flag=1
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE); //取得NotificationManager服務
            Notification notification = new Notification.Builder(context.getApplicationContext()) //產生Notification notification
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))//設定通知視窗LargeIcon樣式
                    .setSmallIcon(R.drawable.fr_note)//設定通知視窗SmallIcon樣式
                    .setContentTitle("睡眠指數過低!")//設定通知視窗標題
                    .setContentText("精神不濟,外出請小心,補充睡眠,保持體力!")//設定通知視窗內文
                    .build();//建立
            notification.defaults |= Notification.DEFAULT_VIBRATE; // 某些手機不支援 請加 // try catch
            notificationManager.notify(0, notification); //送出Notification
        //}

    }
}



