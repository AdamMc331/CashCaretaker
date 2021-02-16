package com.androidessence.cashcaretaker.ui.transactionlist

import com.adammcneilly.cashcaretaker.analytics.AnalyticsTracker
import com.androidessence.cashcaretaker.core.models.Transaction
import com.androidessence.cashcaretaker.data.CCRepository
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

    fun assertViewState(expectedViewState: TransactionListViewState) = apply {
        val actualViewState = viewModel.viewState.value
        assertThat(actualViewState).isEqualTo(expectedViewState)
    }
}
