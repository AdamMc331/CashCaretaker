package com.androidessence.cashcaretaker

import android.database.sqlite.SQLiteException
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.androidessence.cashcaretaker.refresh.Account
import com.androidessence.cashcaretaker.refresh.AccountsActivity
import com.androidessence.cashcaretaker.refresh.CCDataSource
import junit.framework.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import timber.log.Timber

/**
 * Tests methods inside the data source.
 *
 * Created by adam.mcneilly on 2/19/17.
 */
@RunWith(AndroidJUnit4::class)
class CCDataSourceTest {

    @Rule @JvmField
    val activityTestRule = ActivityTestRule<AccountsActivity>(AccountsActivity::class.java)

    var dataSource: CCDataSource? = null

    @Before
    fun setup() {
        dataSource = CCDataSource(activityTestRule.activity)
        dataSource?.open()
        dataSource?.deleteAccounts()
    }

    @After
    fun teardown() {
        dataSource?.deleteAccounts()
        dataSource = null
    }

    @Test
    fun testGetAccounts() {
        // Since we start with empty, the size should be zero.
        val accounts = dataSource?.getAccounts()
        assertEquals(0, accounts?.size)
    }

    @Test
    fun testAddAccount() {
        val account = Account()
        account.name = "Checking"
        account.balance = 100.00

        var id: Long? = null

        try {
            id = dataSource?.addAccount(account)
        } catch (e: Exception) {
            Timber.e(e)
        }

        assertTrue("Id is not null.", id != null)
        assertTrue("Id is not zero.", id != null && id > 0)
    }

    @Test
    fun testDeleteAccounts() {
        val account = Account()
        account.name = "Checking"
        account.balance = 100.00
        dataSource?.addAccount(account)

        dataSource?.deleteAccounts()
        assertEquals(0, dataSource?.getAccounts()?.size)
    }
}