package com.androidessence.cashcaretaker.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.androidessence.cashcaretaker.R;
import com.androidessence.cashcaretaker.alarms.RepeatingTransactionAlarm;

/**
 * Context for displaying the list of accounts to a user.
 */
public class AccountsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

        // Set toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        startAlarm();
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Starts the activity for managing repeating transactions.
     */
    private void startRepeatingTransactionsActivity() {
        Intent repeatingTransactionsIntent = new Intent(this, RepeatingTransactionsActivity.class);
        startActivity(repeatingTransactionsIntent);
    }

    /**
     * Starts an alarm that updates the Repeating Transactions table.
     */
    private void startAlarm(){
        ComponentName receiver = new ComponentName(this, RepeatingTransactionAlarm.class);
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        RepeatingTransactionAlarm alarm = new RepeatingTransactionAlarm();
        alarm.setAlarm(this);
    }
}
