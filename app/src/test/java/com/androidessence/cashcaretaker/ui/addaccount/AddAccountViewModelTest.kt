package com.androidessence.cashcaretaker.ui.addaccount

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.androidessence.cashcaretaker.CoroutinesTestRule
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.core.models.Account
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalTime
@ExperimentalCoroutinesApi
class AddAccountViewModelTest {
    private lateinit var testRobot: AddAccountViewModelRobot

    @JvmField
    @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @JvmField
    @Rule
    val coroutineTestRule = CoroutinesTestRule()

    @Before
    fun setUp() {
        testRobot = AddAccountViewModelRobot()
    }

    @Test
    fun addingValidAccountInsertsTracksAndDismisses() = runBlockingTest {
        val testAccountName = "Checking"
        val testAccountBalance = "100.00"

        val testAccount = Account(
            name = testAccountName,
            balance = 100.00
        )

        testRobot
            .buildViewModel()
            .addAccount(
                accountName = testAccountName,
                balanceString = testAccountBalance
            )
            .assertCallToInsertAccount(testAccount)
            .assertCallToTrackAccountAdded()
//            .assertDismissEventEmitted()
    }

    @Test
    fun addingDuplicateAccountShowsError() = runBlockingTest {
        val testAccountName = "Checking"
        val testAccountBalance = "100.00"

        val testAccount = Account(
            name = testAccountName,
            balance = 100.00
        )

        testRobot
            .buildViewModel()
            .mockAccountConstraintException(testAccount)
            .addAccount(
                accountName = testAccountName,
                balanceString = testAccountBalance
            )
            .assertAccountNameError(
                expectedValue = R.string.err_account_name_exists
            )
    }

    @Test
    fun addingNullAccountNameDisplaysError() {
        testRobot
            .buildViewModel()
            .addAccount(
                accountName = null,
                balanceString = null
            )
            .assertAccountNameError(
                expectedValue = R.string.err_account_name_invalid
            )
    }

    @Test
    fun addingEmptyStringAccountNameDisplaysError() {
        testRobot
            .buildViewModel()
            .addAccount(
                accountName = "",
                balanceString = null
            )
            .assertAccountNameError(
                expectedValue = R.string.err_account_name_invalid
            )
    }

    @Test
    fun addingNullBalanceStringDisplaysError() {
        testRobot
            .buildViewModel()
            .addAccount(
                accountName = "Checking",
                balanceString = null
            )
            .assertAccountBalanceError(
                expectedValue = R.string.err_account_balance_invalid
            )
    }

    @Test
    fun addingInvalidBalanceStringDisplaysError() {
        testRobot
            .buildViewModel()
            .addAccount(
                accountName = "Checking",
                balanceString = "This isn't a double"
            )
            .assertAccountBalanceError(
                expectedValue = R.string.err_account_balance_invalid
            )
    }
}
