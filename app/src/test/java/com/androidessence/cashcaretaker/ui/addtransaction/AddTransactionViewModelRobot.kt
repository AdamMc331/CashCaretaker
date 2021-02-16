package com.androidessence.cashcaretaker.ui.addtransaction

import app.cash.turbine.test
import com.adammcneilly.cashcaretaker.analytics.AnalyticsTracker
import com.androidessence.cashcaretaker.core.models.Transaction
import com.androidessence.cashcaretaker.data.CCRepository
import com.androidessence.cashcaretaker.testObserver
import com.google.common.truth.Truth.assertThat
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import java.util.Date
import kotlin.time.ExperimentalTime

@ExperimentalTime
class AddTransactionViewModelRobot {
    private val mockRepository = mockk<CCRepository>(relaxed = true)
    private val mockAnalyticsTracker = mockk<AnalyticsTracker>(relaxed = true)
    private lateinit var viewModel: AddTransactionViewModel

    fun buildViewModel() = apply {
        viewModel = AddTransactionViewModel(
            repository = mockRepository,
            analyticsTracker = mockAnalyticsTracker
        )
    }

    /**
     * TODO: Test this
     */
    fun assertTransactionDescriptionError(expectedValue: Int) = apply {
        val actualValue = viewModel.transactionDescriptionError.testObserver().observedValue
        assertThat(actualValue).isEqualTo(expectedValue)
    }

    fun assertTransactionAmountError(expectedValue: Int) = apply {
        val actualValue = viewModel.transactionAmountError.testObserver().observedValue
        assertThat(actualValue).isEqualTo(expectedValue)
    }

    suspend fun assertDismissEventEmitted() = apply {
        viewModel.dismissEvents.test {
            assertThat(expectItem()).isTrue()
            expectComplete()
        }
    }

    fun addTransaction(
        accountName: String,
        transactionDescription: String,
        transactionAmount: String,
        withdrawal: Boolean,
        date: Date
    ) = apply {
        viewModel.addTransaction(
            accountName,
            transactionDescription,
            transactionAmount,
            withdrawal,
            date
        )
    }

    fun updateTransaction(
        input: AddTransactionViewModel.TransactionInput
    ) = apply {
        viewModel.updateTransaction(input)
    }

    fun assertCallToInsertTransaction(transaction: Transaction) = apply {
        coVerify(exactly = 1) {
            mockRepository.insertTransaction(transaction)
        }
    }

    fun assertCallToUpdateTransaction(transaction: Transaction) = apply {
        coVerify(exactly = 1) {
            mockRepository.updateTransaction(transaction)
        }
    }

    fun assertCallToTrackTransactionInserted() = apply {
        verify(exactly = 1) {
            mockAnalyticsTracker.trackTransactionAdded()
        }
    }

    fun assertCallToTrackTransactionEdited() = apply {
        verify(exactly = 1) {
            mockAnalyticsTracker.trackTransactionEdited()
        }
    }
}
