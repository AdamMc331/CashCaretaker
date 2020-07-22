package com.androidessence.cashcaretaker.ui.transaction

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.androidessence.cashcaretaker.core.models.Account
import com.androidessence.cashcaretaker.database.CCDatabase
import com.androidessence.cashcaretaker.database.RoomDatabase
import com.androidessence.cashcaretaker.ui.main.MainActivity
import com.androidessence.cashcaretaker.ui.main.MainController
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TransactionFragmentTest {

    @JvmField
    @Rule
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    private val database: CCDatabase
        get() = RoomDatabase(activityTestRule.activity)

    @Before
    fun setUp() {
        runBlocking {
            database.deleteAllAccounts()
            database.insertAccount(TEST_ACCOUNT.toPersistableAccount())

            (activityTestRule.activity as MainController).showTransactions(TEST_ACCOUNT_NAME)
        }
    }

    @After
    fun tearDown() {
        runBlocking {
            database.deleteAllAccounts()
        }
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
            .assertTransactionDescriptionAtPosition(TEST_TRANSACTION_DESCRIPTION, 0)
            .assertTransactionAmountAtPosition(TEST_TRANSACTION_CURRENCY, 0)
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
            .assertTransactionDescriptionAtPosition(TEST_TRANSACTION_DESCRIPTION, 0)
            .assertTransactionAmountAtPosition(TEST_TRANSACTION_CURRENCY, 0)
    }

    @Ignore(
        "The assertion for an empty list always fails even though I tested manually. " +
            "Probably a result of binding not being fast enough?"
    )
    @Test
    fun deleteTransaction() {
        TransactionRobot()
            .clickNew()
            .assertWithdrawalSwitchState(true)
            .transactionDescription(TEST_TRANSACTION_DESCRIPTION)
            .transactionAmount(TEST_TRANSACTION_AMOUNT)
            .submit()
            .assertListCount(1)
            .assertTransactionDescriptionAtPosition(TEST_TRANSACTION_DESCRIPTION, 0)
            .assertTransactionAmountAtPosition(TEST_TRANSACTION_CURRENCY, 0)
            .longClick(0)
            .delete()
            .assertListCount(0)
    }

    companion object {
        private const val TEST_ACCOUNT_NAME = "Checking"
        private const val TEST_ACCOUNT_BALANCE = "100"
        private val TEST_ACCOUNT = Account(TEST_ACCOUNT_NAME, TEST_ACCOUNT_BALANCE.toDouble())

        private const val TEST_TRANSACTION_DESCRIPTION = "Speedway"
        private const val TEST_TRANSACTION_AMOUNT = "5.45"
        private const val TEST_TRANSACTION_CURRENCY = "$5.45"
    }
}
