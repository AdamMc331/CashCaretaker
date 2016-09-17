package com.androidessence.cashcaretaker.alarms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Date;

/**
 * Broadcast receiver that runs whenever the device boots and restarts service.
 *
 * Created by adammcneilly on 11/15/15.
 */
public class RepeatingTransactionAlarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Handle boot
        if (intent.getAction() != null && intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Set the alarm here.
            setAlarm(context);
        }

        Intent serviceIntent = new Intent(context, RepeatingTransactionService.class);
        context.startService(serviceIntent);
    }

    /**
     * Sets the alarm to check for repeating transactions (calls a service) repeating daily at midnight.
     */
    public void setAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, RepeatingTransactionAlarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        // Called every day at midnight.
        alarmManager.setRepeating(AlarmManager.RTC, new Date().getTime(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}
