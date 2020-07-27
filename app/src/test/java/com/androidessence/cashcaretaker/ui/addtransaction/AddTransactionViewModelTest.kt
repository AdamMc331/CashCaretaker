package com.androidessence.cashcaretaker.ui.addtransaction

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.androidessence.cashcaretaker.CoroutinesTestRule
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.core.models.Transaction
import java.util.Date
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AddTransactionViewModelTest {
    private lateinit var testRobot: AddTransactionViewModelRobot

    @JvmField
    @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @JvmField
    @Rule
    val coroutineTestRule = CoroutinesTestRule()

    @Before
    fun setUp() {
        testRobot = AddTransactionViewModelRobot()
    }

    @Test
    fun addValidTransactionInsertsTracksAndDismisses() = runBlockingTest {
        val accountName = "Checking"
        val transactionDescription = "Test Transaction"
        val transactionAmountString = "100.00"
        val isWithdrawal = true
        val date = Date()

        val testTransaction = Transaction(
            accountName = accountName,
            description = transactionDescription,
            amount = transactionAmountString.toDouble(),
            withdrawal = isWithdrawal,
            date = date
        )

        testRobot
            .buildViewModel()
            .addTransaction(
                accountName = accountName,
                transactionDescription = transactionDescription,
                transactionAmount = transactionAmountString,
                withdrawal = isWithdrawal,
                date = date
            )
            .assertCallToInsertTransaction(testTransaction)
            .assertCallToTrackTransactionInserted()
            .assertDismissEventEmitted()
    }

    @Test
    fun editValidTransactionUpdatesTracksAndDismisses() = runBlockingTest {
        val accountName = "Checking"
        val transactionDescription = "Test Transaction"
        val transactionAmountString = "100.00"
        val isWithdrawal = true
        val date = Date()

        val testTransaction = Transaction(
            accountName = accountName,
            description = transactionDescription,
            amount = transactionAmountString.toDouble(),
            withdrawal = isWithdrawal,
            date = date
        )

        val input = AddTransactionViewModel.TransactionInput(
            id = 0L,
            accountName = accountName,
            transactionDescription = transactionDescription,
            transactionAmount = transactionAmountString,
            withdrawal = isWithdrawal,
            date = date
        )

        testRobot
            .buildViewModel()
            .updateTransaction(input)
            .assertCallToUpdateTransaction(testTransaction)
            .assertCallToTrackTransactionEdited()
            .assertDismissEventEmitted()
    }

    @Test
    fun addTransactionWithInvalidAmountDisplaysError() {
        testRobot
            .buildViewModel()
            .addTransaction(
                accountName = "Checking",
                transactionDescription = "Test transaction",
                transactionAmount = "This is not currency",
                withdrawal = true,
                date = Date()
            )
            .assertTransactionAmountError(
                expectedValue = R.string.error_invalid_amount
            )
    }

    @Test
    fun editTransactionWithInvalidAmountDisplaysError() {
        val input = AddTransactionViewModel.TransactionInput(
            id = 0L,
            accountName = "Checking",
            transactionDescription = "Test transaction",
            transactionAmount = "This is not currency",
            withdrawal = true,
            date = Date()
        )

        testRobot
            .buildViewModel()
            .updateTransaction(input)
            .assertTransactionAmountError(
                expectedValue = R.string.error_invalid_amount
            )
    }
}
