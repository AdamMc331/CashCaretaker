package com.androidessence.cashcaretaker.ui.addtransaction

import com.androidessence.cashcaretaker.TestDispatcherProvider
import com.androidessence.cashcaretaker.fakes.FakeAnalyticsTracker
import com.androidessence.cashcaretaker.fakes.FakeCCRepository
import com.androidessence.cashcaretaker.testObserver
import com.google.common.truth.Truth.assertThat
import java.util.Date
import kotlinx.coroutines.flow.collect

class AddTransactionViewModelRobot {
    private val fakeRepository = FakeCCRepository()
    private val fakeAnalyticsTracker = FakeAnalyticsTracker()
    private val testDispatcherProvider = TestDispatcherProvider()
    private lateinit var viewModel: AddTransactionViewModel

    fun buildViewModel() = apply {
        viewModel = AddTransactionViewModel(
            repository = fakeRepository,
            analyticsTracker = fakeAnalyticsTracker,
            dispatcherProvider = testDispatcherProvider
        )
    }

    fun assertTransactionDescriptionError(expectedValue: Int) = apply {
        val actualValue = viewModel.transactionDescriptionError.testObserver().observedValue
        assertThat(actualValue).isEqualTo(expectedValue)
    }

    fun assertTransactionAmountError(expectedValue: Int) = apply {
        val actualValue = viewModel.transactionAmountError.testObserver().observedValue
        assertThat(actualValue).isEqualTo(expectedValue)
    }

    suspend fun assertDismissEventEmitted() = apply {
        viewModel.dismissEvents.collect { dismissEvent ->
            assertThat(dismissEvent).isTrue()
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

    fun assertCallToInsertTransaction() = apply {
        assertThat(fakeRepository.getInsertTransactionCallCount()).isEqualTo(1)
    }

    fun assertCallToUpdateTransaction() = apply {
        assertThat(fakeRepository.getUpdateTransactionCallCount()).isEqualTo(1)
    }

    fun assertCallToTrackTransactionInserted() = apply {
        assertThat(fakeAnalyticsTracker.getTrackTransactionAddedCount()).isEqualTo(1)
    }

    fun assertCallToTrackTransactionEdited() = apply {
        assertThat(fakeAnalyticsTracker.getTrackTransactionEditedCount()).isEqualTo(1)
    }
}
