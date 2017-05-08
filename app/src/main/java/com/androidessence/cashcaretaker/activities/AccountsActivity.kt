package com.androidessence.cashcaretaker.activities

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.androidessence.cashcaretaker.DatabaseToJSON
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.adapters.AccountAdapter
import com.androidessence.cashcaretaker.alarms.RepeatingTransactionAlarm
import com.androidessence.cashcaretaker.core.CoreActivity
import com.androidessence.cashcaretaker.fragments.AccountsFragment
import com.androidessence.utility.default
import com.crashlytics.android.Crashlytics
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.wearable.Wearable
import io.fabric.sdk.android.Fabric
import org.json.JSONException
import timber.log.Timber

/**
 * Activity that displays a list of accounts to the user.

 * Created by adam.mcneilly on 9/5/16.
 */
class AccountsActivity : CoreActivity(),
        GoogleApiClient.OnConnectionFailedListener,
        AccountAdapter.OnAccountDeletedListener,
        GoogleApiClient.ConnectionCallbacks {

    private var googleApiClient: GoogleApiClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_accounts)

        setupToolbar(false)
        startAlarm()
        setupClient()

        val accountsFragment = AccountsFragment.newInstance()
        supportFragmentManager.beginTransaction().replace(R.id.fragment_accounts, accountsFragment, "TODO:").commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_manage_repeating_transactions -> {
                startRepeatingTransactionsActivity()
                return true
            }
            R.id.action_manage_categories -> {
                startManageCategoriesActivity()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun startRepeatingTransactionsActivity() {
        val repeatingTransactionsIntent = Intent(this, RepeatingTransactionsActivity::class.java)
        startActivity(repeatingTransactionsIntent)
    }

    private fun startManageCategoriesActivity() {
        val manageCategories = Intent(this, ManageCategoriesActivity::class.java)
        startActivity(manageCategories)
    }

    private fun startAlarm() {
        val receiver = ComponentName(this, RepeatingTransactionAlarm::class.java)
        packageManager.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)

        val alarm = RepeatingTransactionAlarm()
        alarm.setAlarm(this)
    }

    private fun setupClient() {
        googleApiClient = GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build()
    }

    override fun onStart() {
        super.onStart()

        // Connect to data layer
        googleApiClient?.connect()
    }

    override fun onConnected(bundle: Bundle?) {
        // Send over accounts
        try {
            val accountsJson = DatabaseToJSON(this).accountJSON.toString()
            SendToDataLayerThread(getString(R.string.add_account_path), accountsJson).start()
        } catch (joe: JSONException) {
            Timber.e(joe)
        }

    }

    override fun onStop() {
        // Disconnect
        if (googleApiClient?.isConnected.default(false)) googleApiClient?.disconnect()

        super.onStop()
    }

    override fun onConnectionSuspended(i: Int) {

    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {

    }

    private inner class SendToDataLayerThread(private val mPath: String, private val mMessage: String) : Thread() {

        override fun run() {
            val nodes = Wearable.NodeApi.getConnectedNodes(googleApiClient).await()

            nodes.nodes.forEach {
                Wearable.MessageApi.sendMessage(googleApiClient, it.id, mPath, mMessage.toByteArray()).await()
            }
        }
    }

    override fun onAccountDeleted(id: Long) {
        // Send id as string
        SendToDataLayerThread(getString(R.string.delete_account_path), id.toString()).start()
    }
}
