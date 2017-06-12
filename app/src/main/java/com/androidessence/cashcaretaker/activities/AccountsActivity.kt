package com.androidessence.cashcaretaker.activities

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.alarms.RepeatingTransactionAlarm
import com.androidessence.cashcaretaker.core.CoreActivity
import com.androidessence.cashcaretaker.fragments.AccountsFragment
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric

/**
 * Activity that displays a list of accounts to the user.

 * Created by adam.mcneilly on 9/5/16.
 */
class AccountsActivity : CoreActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_accounts)

        setupToolbar(false)
        startAlarm()

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
}
