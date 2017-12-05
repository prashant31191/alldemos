package com.services;

/**
 * Created by Admin on 10/3/2016.
 */


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import com.darktheme.R;
import com.overlay.SampleOverlayHideActivity;
import com.overlay.SampleOverlayView;

public class SampleOverlayService extends OverlayService {

    public static SampleOverlayService instance;

    private static SampleOverlayView overlayView;
    private String colorCode = "";

    Notification myNotication;
    //NotificationManager manager;


    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
       // manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        colorCode = getColorCode();

        Log.i("=Code==","==111Color code="+colorCode);

        if (colorCode != null && colorCode.length() >= 8) {
            overlayView = new SampleOverlayView(this, colorCode);
        } else {
            overlayView = new SampleOverlayView(this);
        }

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getExtras() != null && intent.getExtras().getString("colorCode") != null) {
            colorCode = intent.getExtras().getString("colorCode");

            Log.i("=Code==","==555===Color code="+colorCode);
        }


        if (colorCode != null && colorCode.length() >= 8) {
            overlayView = new SampleOverlayView(this, colorCode);
        } else {
            overlayView = new SampleOverlayView(this);
        }

        return START_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();

        if (overlayView != null) {
            overlayView.destory();
        }

    }

    static public void stop() {
        if (instance != null) {
            instance.stopSelf();
        }
    }



   /* @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        if (intent.getExtras() != null && intent.getExtras().getString("colorCode") != null) {
            colorCode = intent.getExtras().getString("colorCode");

            Log.i("=Code==","==111Color code="+colorCode);
        }
    }
*/
    @Override
    protected Notification foregroundNotification(int notificationId) {
       //Notification notification;



        //API level 11
      //  Intent intent = new Intent("com.rj.notitfications.SECACTIVITY");

       // PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 1, intent, 0);

        Notification.Builder builder = new Notification.Builder(this);

       // builder.setAutoCancel(false);
       // builder.setTicker("this is ticker text");
        builder.setContentTitle(getString(R.string.app_name));
        builder.setContentText(getString(R.string.message_notification));
        builder.setSmallIcon(R.drawable.ic_settings_brightness_black_24dp);
        builder.setContentIntent(notificationIntent());
      //  builder.setOngoing(true);
   //     builder.setSubText("This is subtext...");   //API level 16
      //  builder.setNumber(100);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder.setChannelId("101");
        }

        builder.build();

        myNotication = builder.getNotification();
        myNotication.flags = myNotication.flags | Notification.FLAG_ONGOING_EVENT | Notification.FLAG_ONLY_ALERT_ONCE;



        //manager.notify(11, myNotication);


      /*  notification = new Notification(R.drawable.ic_settings_brightness_black_24dp, getString(R.string.title_notification), System.currentTimeMillis());

        notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT | Notification.FLAG_ONLY_ALERT_ONCE;

        notification.setLatestEventInfo(this, getString(R.string.title_notification), getString(R.string.message_notification), notificationIntent());
*/
        return myNotication;
    }


    private PendingIntent notificationIntent() {
        Intent intent = new Intent(this, SampleOverlayHideActivity.class);

        PendingIntent pending = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return pending;
    }

}