package com.androidessence.cashcaretaker.ui.transfer

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.androidessence.cashcaretaker.CoroutinesTestRule
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.core.models.Account
import java.util.Date
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddTransferViewModelTest {
    private lateinit var testRobot: AddTransferViewModelRobot

    @JvmField
    @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @JvmField
    @Rule
    val coroutineTestRule = CoroutinesTestRule()

    @Before
    fun setUp() {
        testRobot = AddTransferViewModelRobot()
    }

    @Test
    fun createValidTransferInsertsTracksAndDismisses() = runBlockingTest {
        val fromAccount = Account(name = "From")
        val toAccount = Account(name = "to")
        val amount = 100.00
        val date = Date()

        testRobot
            .buildViewModel()
            .addTransfer(
                fromAccount = fromAccount,
                toAccount = toAccount,
                amount = amount.toString(),
                date = date
            )
            .assertCallToCreateTransfer(
                fromAccount = fromAccount,
                toAccount = toAccount,
                amount = amount,
                date = date
            )
            .assertCallToTrackTransfer()
            .assertCallToCreateTransfer(
                fromAccount,
                toAccount,
                amount,
                date
            )
            .assertDismissEventEmitted()
    }

    @Test
    fun createTransferWithInvalidFromAccountShowsError() = runBlockingTest {
        testRobot
            .buildViewModel()
            .addTransfer(
                fromAccount = null,
                toAccount = Account(),
                amount = "100.00",
                date = Date()
            )
            .assertFromAccountError(
                expectedValue = R.string.from_account_invalid
            )
    }

    @Test
    fun createTransferWithInvalidToAccountShowsError() = runBlockingTest {
        testRobot
            .buildViewModel()
            .addTransfer(
                fromAccount = Account(),
                toAccount = null,
                amount = "100.00",
                date = Date()
            )
            .assertToAccountError(
                expectedValue = R.string.to_account_invalid
            )
    }

    @Test
    fun createTransferWithInvalidAmountShowsError() = runBlockingTest {
        testRobot
            .buildViewModel()
            .addTransfer(
                fromAccount = Account(),
                toAccount = Account(),
                amount = "This is not currency",
                date = Date()
            )
            .assertAmountError(
                expectedValue = R.string.amount_invalid
            )
    }

    @Test
    fun fetchAccountsOnInitialization() = runBlockingTest {
        val fakeAccountList = listOf(
            Account(name = "Test Account")
        )

        testRobot
            .mockAccountsFromRepo(fakeAccountList)
            .buildViewModel()
            .assertAccounts(fakeAccountList)
    }
}
