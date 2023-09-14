package com.hekmatullahamin.plan.model;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Handler;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.hekmatullahamin.plan.R;
import com.hekmatullahamin.plan.activities.PlansActivity;
import com.hekmatullahamin.plan.utils.Constants;

public class AlarmBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        final MediaPlayer[] mediaPlayer = {MediaPlayer.create(context, Settings.System.DEFAULT_ALARM_ALERT_URI)};
        mediaPlayer[0].start();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mediaPlayer[0].stop();
                mediaPlayer[0] = null;
            }
        }, 15000);

        int id = (int) intent.getLongExtra(Constants.NOTIFICATION_MANAGER_COMPAT_ID, 0);
        String message = intent.getStringExtra(Constants.NOTIFICATION_MESSAGE);
        Intent planActivityIntent = new Intent(context, PlansActivity.class);
        planActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, planActivityIntent, 0);

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_round);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID);
        builder.setContentTitle("Plan");
        builder.setContentText(message);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(bitmap);
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(id, builder.build());
    }
}
