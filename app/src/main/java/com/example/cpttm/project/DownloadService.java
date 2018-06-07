package com.example.cpttm.project;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class DownloadService extends Service{

    Timer t;

    int notificationId = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Service LifeCycle", "onStartCommand");

        // Trigger a notification.
        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, "0" )
                .setSmallIcon( R.drawable.ic_launcher_background )
                .setContentTitle( "TITLE" )                     // 推送來自
                .setContentText( "Content" );   // A notification payload.
        Intent mainActivityIntent = new Intent( this, MainActivity.class );
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, mainActivityIntent, 0
        );
        builder.setContentIntent( pendingIntent );

        // Use default sound and vibrate.
        int defaults = 0;
        defaults |= android.app.Notification.DEFAULT_SOUND;
        defaults |= android.app.Notification.DEFAULT_VIBRATE;
        builder.setDefaults( defaults );

        NotificationManager noticationManager = ( NotificationManager ) getSystemService( Context.NOTIFICATION_SERVICE );
        noticationManager.notify( 0, builder.build() );

//        if (t != null)
//            t.cancel();
//        t = new Timer();
//        t.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                Log.d("Count Down", "3 Secs Passed");
//                sendNotifcation();
//            }
//        }, 0, 3000);
        return Service.START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
