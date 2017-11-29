package com.androidessence.cashcaretaker.account

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.androidessence.cashcaretaker.data.CCDatabase
import com.androidessence.cashcaretaker.main.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests the fragment responsible for displaying accounts.
 */
@RunWith(AndroidJUnit4::class)
class AccountFragmentTest {

    @JvmField
    @Rule
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setUp() {
        CCDatabase.getInMemoryDatabase(activityTestRule.activity).accountDao().deleteAll()
    }

    @After
    fun tearDown() {
        CCDatabase.getInMemoryDatabase(activityTestRule.activity).accountDao().deleteAll()
    }

    @Test
    fun testEmptyList() {
        AccountRobot().assertListCount(0)
    }

    @Test
    fun addAccount() {
        AccountRobot()
                .assertListCount(0)
                .clickNew()
                .accountName(TEST_ACCOUNT_NAME)
                .accountBalance(TEST_ACCOUNT_BALANCE)
                .submit()
                .assertListCount(1)
                .assertAccountNameInList(TEST_ACCOUNT_NAME, 0)
                .assertAccountBalanceInList(TEST_BALANCE_STRING, 0)
    }

    companion object {
        private val TEST_ACCOUNT_NAME = "Checking"
        private val TEST_ACCOUNT_BALANCE = "100"
        private val TEST_BALANCE_STRING = "$100.00"
    }
}