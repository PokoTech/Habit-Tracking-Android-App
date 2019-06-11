package com.habitrack.elliotedgington.habitrack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {
    private int notificationId = System.identityHashCode(this);

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getBooleanExtra(HabitCreateActivity.EXTRA_COMPLETED, false)) {
            String title = intent.getStringExtra(HabitCreateActivity.EXTRA_TITLE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,
                    HabitListActivity.CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_done_24dp)
                    .setContentTitle("HabiTracker Reminder!")
                    .setContentText(title + " hasn't been checked today!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManagerCompat =
                    NotificationManagerCompat.from(context);
            notificationManagerCompat.notify(notificationId, builder.build());
        }
    }

}
