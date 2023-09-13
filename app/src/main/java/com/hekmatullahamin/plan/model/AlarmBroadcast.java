package com.hekmatullahamin.plan.model;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.hekmatullahamin.plan.R;
import com.hekmatullahamin.plan.activities.PlannedActivity;
import com.hekmatullahamin.plan.utils.Constants;

public class AlarmBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int id = (int) intent.getLongExtra(Constants.NOTIFICATION_MANAGER_COMPAT_ID, 0);
        String message = intent.getStringExtra(Constants.NOTIFICATION_MESSAGE);
        Intent planActivityIntent = new Intent(context, PlannedActivity.class);
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
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(id, builder.build());
    }
}
