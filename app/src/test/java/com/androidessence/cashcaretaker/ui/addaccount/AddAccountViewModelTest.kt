package com.androidessence.cashcaretaker.ui.addaccount

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.androidessence.cashcaretaker.R
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddAccountViewModelTest {
    private lateinit var testRobot: AddAccountViewModelRobot

    @JvmField
    @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        testRobot = AddAccountViewModelRobot()
    }

    @Test
    fun addingValidAccountInsertsTracksAndDismisses() = runBlockingTest {
        testRobot
            .buildViewModel()
            .addAccount(
                accountName = "Checking",
                balanceString = "100.00"
            )
            .assertCallToInsertAccount()
            .assertCallToTrackAccountAdded()
            .assertDismissEventEmitted()
    }

    @Test
    fun addingDuplicateAccountShowsError() = runBlockingTest {
        testRobot
            .buildViewModel()
            .mockAccountConstraintException()
            .addAccount(
                accountName = "Checking",
                balanceString = "100.00"
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
