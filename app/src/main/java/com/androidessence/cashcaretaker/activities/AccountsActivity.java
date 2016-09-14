package com.androidessence.cashcaretaker.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.androidessence.cashcaretaker.DatabaseToJSON;
import com.androidessence.cashcaretaker.R;
import com.androidessence.cashcaretaker.adapters.AccountAdapter;
import com.androidessence.cashcaretaker.alarms.RepeatingTransactionAlarm;
import com.androidessence.cashcaretaker.core.CoreActivity;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import io.fabric.sdk.android.Fabric;
import org.json.JSONException;

/**
 * Activity that displays a list of accounts to the user.
 *
 * Created by adam.mcneilly on 9/5/16.
 */
public class AccountsActivity extends CoreActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, AccountAdapter.OnAccountDeletedListener {
    private static final String LOG_TAG = AccountsActivity.class.getSimpleName();
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_accounts);

        setupToolbar(false);
        startAlarm();
        setupClient();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_manage_repeating_transactions:
                startRepeatingTransactionsActivity();
                return true;
            case R.id.action_manage_categories:
                startManageCategoriesActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startRepeatingTransactionsActivity() {
        Intent repeatingTransactionsIntent = new Intent(this, RepeatingTransactionsActivity.class);
        startActivity(repeatingTransactionsIntent);
    }

    private void startManageCategoriesActivity() {
        Intent manageCategories = new Intent(this, ManageCategoriesActivity.class);
        startActivity(manageCategories);
    }

    private void startAlarm(){
        ComponentName receiver = new ComponentName(this, RepeatingTransactionAlarm.class);
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        RepeatingTransactionAlarm alarm = new RepeatingTransactionAlarm();
        alarm.setAlarm(this);
    }

    private void setupClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Connect to data layer
        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Send over accounts
        try{
            String accountsJson = (new DatabaseToJSON(this)).getAccountJSON().toString();
            new SendToDataLayerThread(getString(R.string.add_account_path), accountsJson).start();
        } catch(JSONException joe) {
            Log.e(LOG_TAG, joe.getMessage());
        }
    }

    @Override
    protected void onStop() {
        // Disconnect
        if(googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }

        super.onStop();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private class SendToDataLayerThread extends Thread {
        private String mPath;
        private String mMessage;

        public SendToDataLayerThread(String path, String message) {
            this.mPath = path;
            this.mMessage = message;
        }

        @Override
        public void run() {
            NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(googleApiClient).await();
            for(Node node : nodes.getNodes()) {
                Wearable.MessageApi.sendMessage(googleApiClient, node.getId(), mPath, mMessage.getBytes()).await();
            }
        }
    }

    @Override
    public void onAccountDeleted(long id) {
        // Send id as string
        new SendToDataLayerThread(getString(R.string.delete_account_path), String.valueOf(id)).start();
    }
}
