package com.androidessence.cashcaretaker.ui.transactionlist

import com.androidessence.cashcaretaker.TestDispatcherProvider
import com.androidessence.cashcaretaker.core.models.Transaction
import com.androidessence.cashcaretaker.fakes.FakeAnalyticsTracker
import com.androidessence.cashcaretaker.fakes.FakeCCRepository
import com.androidessence.cashcaretaker.testObserver
import com.google.common.truth.Truth.assertThat

class TransactionListViewModelRobot {
    private val fakeRepository = FakeCCRepository()
    private val fakeAnalyticsTracker = FakeAnalyticsTracker()
    private val testDispatcherProvider = TestDispatcherProvider()
    private lateinit var viewModel: TransactionListViewModel

    fun mockTransactions(transactions: List<Transaction>) = apply {
        fakeRepository.mockTransactions(transactions)
    }

    fun buildViewModel(accountName: String) = apply {
        viewModel = TransactionListViewModel(
            accountName = accountName,
            repository = fakeRepository,
            analyticsTracker = fakeAnalyticsTracker,
            dispatcherProvider = testDispatcherProvider
        )
    }

    fun assertTransactions(expectedTransactions: List<Transaction>) = apply {
        val actualTransactions = viewModel.transactions.testObserver().observedValue
        assertThat(actualTransactions).isEqualTo(expectedTransactions)
    }

    fun assertShowTransactions(expectedShowing: Boolean) = apply {
        val actualShowing = viewModel.showTransactions.testObserver().observedValue
        assertThat(actualShowing).isEqualTo(expectedShowing)
    }

    fun assertShowEmptyMessage(expectedShowing: Boolean) = apply {
        val actualShowing = viewModel.showEmptyMessage.testObserver().observedValue
        assertThat(actualShowing).isEqualTo(expectedShowing)
    }

    fun assertShowLoading(expectedShowing: Boolean) = apply {
        val actualShowing = viewModel.showLoading.testObserver().observedValue
        assertThat(actualShowing).isEqualTo(expectedShowing)
    }
}