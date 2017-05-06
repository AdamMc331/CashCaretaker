package com.androidessence.cashcaretaker.alarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import java.util.Date

/**
 * Broadcast receiver that runs whenever the device boots and restarts service.

 * Created by adammcneilly on 11/15/15.
 */
class RepeatingTransactionAlarm : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        // Handle boot
        if (intent?.action == "android.intent.action.BOOT_COMPLETED") {
            // Set the alarm here.
            setAlarm(context)
        }

        val serviceIntent = Intent(context, RepeatingTransactionService::class.java)
        context.startService(serviceIntent)
    }

    /**
     * Sets the alarm to check for repeating transactions (calls a service) repeating daily at midnight.
     */
    fun setAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, RepeatingTransactionAlarm::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        // Called every day at midnight.
        alarmManager.setRepeating(AlarmManager.RTC, Date().time, AlarmManager.INTERVAL_DAY, pendingIntent)
    }
}
