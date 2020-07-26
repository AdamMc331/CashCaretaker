package com.androidessence.cashcaretaker.ui.accountlist

import com.androidessence.cashcaretaker.core.models.Account
import com.androidessence.cashcaretaker.data.CCRepository
import com.androidessence.cashcaretaker.data.analytics.AnalyticsTracker
import com.androidessence.cashcaretaker.testObserver
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf

class AccountListViewModelRobot {
    private val mockRepository = mockk<CCRepository>()
    private val mockAnalyticsTracker = mockk<AnalyticsTracker>()
    private lateinit var viewModel: AccountListViewModel

    fun buildViewModel() = apply {
        viewModel = AccountListViewModel(
            repository = mockRepository,
            analyticsTracker = mockAnalyticsTracker
        )
    }

    fun mockAccounts(accounts: List<Account>) = apply {
        coEvery {
            mockRepository.fetchAllAccounts()
        } returns flowOf(accounts)
    }

    fun assertAccounts(expectedAccounts: List<Account>) = apply {
        val actualAccounts = viewModel.accounts.testObserver().observedValue
        assertThat(actualAccounts).isEqualTo(expectedAccounts)
    }

    /**
     * TODO: Test this
     */
    fun assertShouldAllowTransfers(shouldAllow: Boolean) = apply {
        assertThat(viewModel.allowTransfers).isEqualTo(shouldAllow)
    }

    fun assertShowingAccounts(expectedShowing: Boolean) = apply {
        val actualShowing = viewModel.showAccounts.testObserver().observedValue
        assertThat(actualShowing).isEqualTo(expectedShowing)
    }

    fun assertShowingEmptyMessage(expectedShowing: Boolean) = apply {
        val actualShowing = viewModel.showEmptyMessage.testObserver().observedValue
        assertThat(actualShowing).isEqualTo(expectedShowing)
    }

    fun assertShowLoading(expectedShowing: Boolean) = apply {
        val actualShowing = viewModel.showLoading.testObserver().observedValue
        assertThat(actualShowing).isEqualTo(expectedShowing)
    }
}
