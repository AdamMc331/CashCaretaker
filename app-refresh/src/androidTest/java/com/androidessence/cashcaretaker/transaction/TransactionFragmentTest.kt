package com.androidessence.cashcaretaker.transaction

import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
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
        accountDao.insert(TEST_ACCOUNT)

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