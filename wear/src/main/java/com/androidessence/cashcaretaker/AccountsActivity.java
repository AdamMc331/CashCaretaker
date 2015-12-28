package com.androidessence.cashcaretaker;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AccountsActivity extends Activity {
    private TextView mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                setupListView();
            }
        });

        mText = (TextView) findViewById(R.id.text);
        setupLocalBroadcastReceiver();
    }

    private void setupListView() {
        // WearableListView accountListView = (WearableListView) findViewById(R.id.account_list_view);
        // accountListView.setAdapter(new WearableAccountAdapter(this, getAccounts()));
        ListView accountListView = (ListView) findViewById(R.id.account_list_view);
        accountListView.setAdapter(new AccountAdapter(this, getAccounts()));
    }

    private List<Account> getAccounts() {
        List<Account> accounts = new ArrayList<>();

        accounts.add(new Account("Checking", 100.00));
        accounts.add(new Account("Savings", 100.50));

        return accounts;
    }

    private void setupLocalBroadcastReceiver() {
        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        MessageReceiver messageReceiver = new MessageReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, messageFilter);
        Log.v("ADAMANT", "Setup receiver");
    }

    private class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            // mText.setText(message);
            Log.v("ADAMANT", "Received stuff." + message);
        }
    }
}
