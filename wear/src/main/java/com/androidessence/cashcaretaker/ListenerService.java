package com.androidessence.cashcaretaker;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by adammcneilly on 12/27/15.
 */
public class ListenerService extends WearableListenerService {
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.v("ADAMANT", "onMessageReceived");
        Log.v("ADAMANT", "Path is: " + messageEvent.getPath());

        if(messageEvent.getPath().equals("/message_path")) {
            String message = new String(messageEvent.getData());
            // Broadcast message
            Intent messageIntent = new Intent();
            messageIntent.setAction(Intent.ACTION_SEND);
            messageIntent.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
            Log.v("ADAMANT", "MESSAGE RECEIVED!");
        } else {
            super.onMessageReceived(messageEvent);
        }
    }
}
