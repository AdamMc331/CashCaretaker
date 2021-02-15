package com.androidessence.cashcaretaker.ui.accountlist

import com.adammcneilly.cashcaretaker.analytics.AnalyticsTracker
import com.androidessence.cashcaretaker.core.models.Account
import com.androidessence.cashcaretaker.data.CCRepository
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

    fun assertViewState(expectedViewState: AccountListViewState) {
        val actualViewState = viewModel.viewState.value
        assertThat(actualViewState).isEqualTo(expectedViewState)
    }
}
