//package com.androidessence.cashcaretaker.activities;
//
//import android.content.ComponentName;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//
//import com.androidessence.cashcaretaker.DatabaseToJSON;
//import com.androidessence.cashcaretaker.R;
//import com.androidessence.cashcaretaker.adapters.AccountAdapter;
//import com.androidessence.cashcaretaker.alarms.RepeatingTransactionAlarm;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.wearable.MessageApi;
//import com.google.android.gms.wearable.Node;
//import com.google.android.gms.wearable.NodeApi;
//import com.google.android.gms.wearable.Wearable;
//
//import org.json.JSONException;
//
///**
// * Context for displaying the list of accounts to a user.
// */
//public class AccountsActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, AccountAdapter.OnAccountDeletedListener {
//    private GoogleApiClient mGoogleClient;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_accounts);
//
//        // Set toolbar.
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        startAlarm();
//
//        setupClient();
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch(item.getItemId()) {
//            case R.id.action_manage_repeating_transactions:
//                startRepeatingTransactionsActivity();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    /**
//     * Starts the activity for managing repeating transactions.
//     */
//    private void startRepeatingTransactionsActivity() {
//        Intent repeatingTransactionsIntent = new Intent(this, RepeatingTransactionsActivity.class);
//        startActivity(repeatingTransactionsIntent);
//    }
//
//    /**
//     * Starts an alarm that updates the Repeating Transactions table.
//     */
//    private void startAlarm(){
//        ComponentName receiver = new ComponentName(this, RepeatingTransactionAlarm.class);
//        PackageManager pm = getPackageManager();
//        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
//
//        RepeatingTransactionAlarm alarm = new RepeatingTransactionAlarm();
//        alarm.setAlarm(this);
//    }
//
//    private void setupClient() {
//        mGoogleClient = new GoogleApiClient.Builder(this)
//                .addApi(Wearable.API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .build();
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        // Connect to data layer
//        mGoogleClient.connect();
//    }
//
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        // Send over accounts
//        try{
//            String accountsJson = (new DatabaseToJSON(this)).getAccountJSON().toString();
//            new SendToDataLayerThread(getString(R.string.add_account_path), accountsJson).start();
//        } catch(JSONException joe) {
//            // TODO:
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        // Disconnect
//        if(mGoogleClient != null && mGoogleClient.isConnected()) {
//            mGoogleClient.disconnect();
//        }
//
//        super.onStop();
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
//
//    private class SendToDataLayerThread extends Thread {
//        private String mPath;
//        private String mMessage;
//
//        public SendToDataLayerThread(String path, String message) {
//            this.mPath = path;
//            this.mMessage = message;
//        }
//
//        @Override
//        public void run() {
//            NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mGoogleClient).await();
//            for(Node node : nodes.getNodes()) {
//                MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(mGoogleClient, node.getId(), mPath, mMessage.getBytes()).await();
//
//                Log.v("ADAM", "Sending message: " + mMessage + " to: " + node.getDisplayName());
//
//                if(result.getStatus().isSuccess()) {
//                    // SUCCESS
//                    Log.v("ADAM", "MESSAGE SENT!");
//                } else {
//                    // FAILURE
//                    Log.v("ADAM", "MESSAGE FAILED TO SEND");
//                }
//            }
//        }
//    }
//
//    @Override
//    public void onAccountDeleted(long id) {
//        // Send id as string
//        new SendToDataLayerThread(getString(R.string.delete_account_path), String.valueOf(id)).start();
//    }
//}
