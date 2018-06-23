package com.androidessence.cashcaretaker.account

import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.data.AccountDAO
import com.androidessence.cashcaretaker.data.CCDatabase
import com.androidessence.cashcaretaker.main.MainActivity
import com.androidessence.cashcaretaker.transaction.TransactionRobot
import org.junit.*
import org.junit.runner.RunWith

/**
 * Tests the fragment responsible for displaying accounts.
 */
@RunWith(AndroidJUnit4::class)
class AccountFragmentTest {

    @JvmField
    @Rule
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    private val accountDao: AccountDAO
        get() = CCDatabase.getInMemoryDatabase(activityTestRule.activity).accountDao()

    @Before
    fun setUp() {
        accountDao.deleteAll()
    }

    @After
    fun tearDown() {
        accountDao.deleteAll()
    }

    @Test
    fun testEmptyList() {
        AccountRobot().assertListCount(0)
    }

    @Test
    fun addAccountSuccess() {
        AccountRobot()
                .clickNew()
                .accountName(TEST_ACCOUNT_NAME)
                .accountBalance(TEST_ACCOUNT_BALANCE)
                .submit()
                .assertListCount(1)
                .assertAccountNameInList(TEST_ACCOUNT_NAME, 0)
                .assertAccountBalanceInList(TEST_BALANCE_STRING, 0)
    }

    @Test
    fun addAccountNameError() {
        AccountRobot()
                .clickNew()
                .accountName("")
                .accountBalance(TEST_ACCOUNT_BALANCE)
                .submit()
                .assertAccountNameError(activityTestRule.activity.getString(R.string.err_account_name_invalid))
    }

    @Test
    fun addAccountBalanceError() {
        AccountRobot()
                .clickNew()
                .accountName(TEST_ACCOUNT_NAME)
                .accountBalance("")
                .submit()
                .assertAccountBalanceError(activityTestRule.activity.getString(R.string.err_account_balance_invalid))
    }

    @Test
    fun addAccountEmptyFieldsError() {
        AccountRobot()
                .clickNew()
                .accountName("")
                .accountBalance("")
                .submit()
                .assertAccountNameError(activityTestRule.activity.getString(R.string.err_account_name_invalid))
    }

    @Test
    fun addDuplicateAccountNameError() {
        // Try to insert dupe
        AccountRobot()
                .clickNew()
                .accountName(TEST_ACCOUNT_NAME)
                .accountBalance(TEST_ACCOUNT_BALANCE)
                .submit()
                .assertListCount(1)
                .clickNew()
                .accountName(TEST_ACCOUNT_NAME)
                .accountBalance(TEST_ACCOUNT_BALANCE)
                .submit()
                .assertAccountNameError(activityTestRule.activity.getString(R.string.err_account_name_exists))
    }

    @Test
    fun addWithdrawalFromRow() {
        // Add transaction
        AccountRobot()
                .clickNew()
                .accountName(TEST_ACCOUNT_NAME)
                .accountBalance(TEST_ACCOUNT_BALANCE)
                .submit()
                .assertListCount(1)
                .clickWithdrawalInList(0)

        TransactionRobot()
                .assertWithdrawalSwitchState(true)
                .transactionDescription(TEST_TRANSACTION_DESCRIPTION)
                .transactionAmount(TEST_TRANSACTION_AMOUNT)
                .submit()

        // Verify
        AccountRobot().assertAccountBalanceInList(TEST_BALANCE_AFTER_WITHDRAWAL_STRING, 0)
    }

    @Test
    fun addDepositFromRow() {
        // Add transaction
        AccountRobot()
                .clickNew()
                .accountName(TEST_ACCOUNT_NAME)
                .accountBalance(TEST_ACCOUNT_BALANCE)
                .submit()
                .assertListCount(1)
                .clickDepositInList(0)

        TransactionRobot()
                .assertWithdrawalSwitchState(false)
                .transactionDescription(TEST_TRANSACTION_DESCRIPTION)
                .transactionAmount(TEST_TRANSACTION_AMOUNT)
                .submit()

        // Verify
        AccountRobot().assertAccountBalanceInList(TEST_BALANCE_AFTER_DEPOSIT_STRING, 0)
    }

    @Test
    fun deleteAccount() {
        AccountRobot()
                .clickNew()
                .accountName(TEST_ACCOUNT_NAME)
                .accountBalance(TEST_ACCOUNT_BALANCE)
                .submit()
                .assertListCount(1)
                .longClick(0)
                .delete()
                .assertListCount(0)
    }

    companion object {
        private const val TEST_ACCOUNT_NAME = "Checking"
        private const val TEST_ACCOUNT_BALANCE = "100"
        private const val TEST_BALANCE_STRING = "$100.00"

        private const val TEST_TRANSACTION_DESCRIPTION = "Speedway"
        private const val TEST_TRANSACTION_AMOUNT = "5.45"
        private const val TEST_BALANCE_AFTER_WITHDRAWAL_STRING = "$94.55"
        private const val TEST_BALANCE_AFTER_DEPOSIT_STRING = "$105.45"
    }
}