package com.randy.anniversarycalculator;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

public class AlarmReceive extends BroadcastReceiver {   //BroadcastReceiver 가필요함
    private static final String TAG = "MYD - AlarmReceive";

    Item mItem = null;

    @Override
    public void onReceive(Context context, Intent intent) {//알람 시간이 되었을때 onReceive를 호출함
        Log.d(TAG, "Start Alarm");
        DBHandler db = new DBHandler(context.getApplicationContext());

        int _id = intent.getIntExtra("id", -1);
        //Log.d(TAG, "id : " + _id);

        String title = context.getString(R.string.app_name);
        String txt = String.valueOf(_id);

        Intent intentAlarm = new Intent(context.getApplicationContext(), MainActivity.class);
        intentAlarm.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //intentAlarm.putExtra("text", txt);

        PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, intentAlarm,
                PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mCompatBuilder = new NotificationCompat.Builder(context.getApplicationContext());
        //mCompatBuilder.setSmallIcon(R.drawable.ic_launcher);
        mCompatBuilder.setContentTitle(title);
        mCompatBuilder.setContentText(txt);
        mCompatBuilder.setAutoCancel(true);                         // 알림을 터치하면 사라짐.
        mCompatBuilder.setSound(defaultSoundUri);
        mCompatBuilder.setContentIntent(pendingIntent);             // 알림을 터치하면 실행할 액티비티

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, mCompatBuilder.build());

    }
}
