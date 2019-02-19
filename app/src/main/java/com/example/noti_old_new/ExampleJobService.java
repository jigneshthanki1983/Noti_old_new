package com.example.noti_old_new;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;

public class ExampleJobService extends JobService {
    private static final String TAG = "JobService";
    private boolean jobcancelled = false;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Job Started");
        dobackgroundwork(params);
        return true;
    }

    private void dobackgroundwork(final JobParameters params) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ExampleJobService.this, NotificationReceiverActivity.class);
                PendingIntent pIntent = PendingIntent.getActivity(ExampleJobService.this, (int) System.currentTimeMillis(), intent, 0);
                Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


                    Notification noti = new Notification.Builder(ExampleJobService.this)
                            .setContentTitle("New mail from " + "test@gmail.com")
                            .setContentText("Subject").setSmallIcon(R.drawable.ic_ic)
                            .setSound(soundUri)
                            .setContentIntent(pIntent)
                            .addAction(R.drawable.ic_ic, "Call", pIntent)
                            .addAction(R.drawable.ic_ic, "More", pIntent)
                            .addAction(R.drawable.ic_ic, "And more", pIntent).build();
                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                    // hide the notification after its selected
                    noti.flags |= Notification.FLAG_AUTO_CANCEL;

                    notificationManager.notify(0, noti);
        jobFinished(params,false);
            }
        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
       // Log.d(TAG,"Job Cancellation has been done before starts");
        Toast.makeText(getApplicationContext(),"Job Cancellation has been done before starts",Toast.LENGTH_LONG).show();
        jobcancelled=true;
        return true;
    }
}
