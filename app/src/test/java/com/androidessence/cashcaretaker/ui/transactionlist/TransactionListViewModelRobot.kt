package com.androidessence.cashcaretaker.ui.transactionlist

import com.androidessence.cashcaretaker.core.models.Transaction
import com.androidessence.cashcaretaker.data.analytics.AnalyticsTracker
import com.androidessence.cashcaretaker.fakes.FakeCCRepository
import com.androidessence.cashcaretaker.testObserver
import com.google.common.truth.Truth.assertThat
import io.mockk.mockkClass

class TransactionListViewModelRobot {
    private val fakeRepository = FakeCCRepository()
    private val mockAnalyticsTracker = mockkClass(AnalyticsTracker::class)
    private lateinit var viewModel: TransactionListViewModel

    fun mockTransactions(transactions: List<Transaction>) = apply {
        fakeRepository.mockTransactions(transactions)
    }

    fun buildViewModel(accountName: String) = apply {
        viewModel = TransactionListViewModel(
            accountName = accountName,
            repository = fakeRepository,
            analyticsTracker = mockAnalyticsTracker
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
