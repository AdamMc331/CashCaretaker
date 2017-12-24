package com.androidessence.cashcaretaker.transaction

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.androidessence.cashcaretaker.account.Account
import com.androidessence.cashcaretaker.data.AccountDAO
import com.androidessence.cashcaretaker.data.CCDatabase
import com.androidessence.cashcaretaker.main.MainActivity
import com.androidessence.cashcaretaker.main.MainController
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TransactionFragmentTest {

    @JvmField
    @Rule
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    private val accountDao: AccountDAO
        get() = CCDatabase.getInMemoryDatabase(activityTestRule.activity).accountDao()

    @Before
    fun setUp() {
        accountDao.deleteAll()
        accountDao.insert(listOf(TEST_ACCOUNT))

        (activityTestRule.activity as MainController).showTransactions(TEST_ACCOUNT_NAME)
    }

    @After
    fun tearDown() {
        accountDao.deleteAll()
    }

    @Test
    fun testInsertWithdrawal() {
        TransactionRobot()
                .clickNew()
                .assertWithdrawalSwitchState(true)
                .transactionDescription(TEST_TRANSACTION_DESCRIPTION)
                .transactionAmount(TEST_TRANSACTION_AMOUNT)
                .submit()
                .assertListCount(1)
    }

    @Test
    fun testInsertDeposit() {
        TransactionRobot()
                .clickNew()
                .setWithdrawalSwitch(false)
                .assertWithdrawalSwitchState(false)
                .transactionDescription(TEST_TRANSACTION_DESCRIPTION)
                .transactionAmount(TEST_TRANSACTION_AMOUNT)
                .submit()
                .assertListCount(1)
    }

    @Test
    fun deleteTransaction() {
        TransactionRobot()
                .clickNew()
                .assertWithdrawalSwitchState(true)
                .transactionDescription(TEST_TRANSACTION_DESCRIPTION)
                .transactionAmount(TEST_TRANSACTION_AMOUNT)
                .submit()
                .assertListCount(1)
                .longClick(0)
                .delete()
                .assertListCount(0)
    }

    companion object {
        private val TEST_ACCOUNT_NAME = "Checking"
        private val TEST_ACCOUNT_BALANCE = "100"
        private val TEST_BALANCE_STRING = "$100.00"
        private val TEST_ACCOUNT = Account(TEST_ACCOUNT_NAME, TEST_ACCOUNT_BALANCE.toDouble())
        private val TEST_TRANSACTION = Transaction(accountName = TEST_ACCOUNT_NAME)

        private val TEST_TRANSACTION_DESCRIPTION = "Speedway"
        private val TEST_TRANSACTION_AMOUNT = "5.45"
        private val TEST_BALANCE_AFTER_WITHDRAWAL_STRING = "$94.55"
        private val TEST_BALANCE_AFTER_DEPOSIT_STRING = "$105.45"
    }
}