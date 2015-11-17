package com.androidessence.cashcaretaker.alarms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Broadcast receiver that runs whenever the device boots and restarts service.
 *
 * Created by adammcneilly on 11/15/15.
 */
public class AutoLaunchService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, RepeatingTransactionService.class);
        context.startService(serviceIntent);
    }
}
