package com.androidessence.cashcaretaker.ui.transactionlist

import com.androidessence.cashcaretaker.core.models.Transaction
import com.androidessence.cashcaretaker.data.CCRepository
import com.androidessence.cashcaretaker.data.analytics.AnalyticsTracker
import com.androidessence.cashcaretaker.testObserver
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf

class TransactionListViewModelRobot {
    private val mockRepository = mockk<CCRepository>(relaxed = true)
    private val mockAnalyticsTracker = mockk<AnalyticsTracker>(relaxed = true)
    private lateinit var viewModel: TransactionListViewModel

    fun mockTransactionsForAccount(
        accountName: String,
        transactions: List<Transaction>
    ) = apply {
        coEvery {
            mockRepository.fetchTransactionsForAccount(accountName)
        } returns flowOf(transactions)
    }

    fun buildViewModel(accountName: String) = apply {
        viewModel = TransactionListViewModel(
            accountName = accountName,
            repository = mockRepository,
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
