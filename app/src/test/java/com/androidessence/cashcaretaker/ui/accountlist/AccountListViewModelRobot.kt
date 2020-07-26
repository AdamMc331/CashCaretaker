package com.androidessence.cashcaretaker.ui.accountlist

import com.androidessence.cashcaretaker.core.models.Account
import com.androidessence.cashcaretaker.fakes.FakeAnalyticsTracker
import com.androidessence.cashcaretaker.fakes.FakeCCRepository
import com.androidessence.cashcaretaker.testObserver
import com.google.common.truth.Truth.assertThat

class AccountListViewModelRobot {
    private val fakeRepository = FakeCCRepository()
    private val fakeAnalyticsTracker = FakeAnalyticsTracker()
    private lateinit var viewModel: AccountListViewModel

    fun buildViewModel() = apply {
        viewModel = AccountListViewModel(
            repository = fakeRepository,
            analyticsTracker = fakeAnalyticsTracker
        )
    }

    fun mockAccounts(accounts: List<Account>) = apply {
        fakeRepository.mockAccounts(accounts)
    }

    fun assertAccounts(expectedAccounts: List<Account>) = apply {
        val actualAccounts = viewModel.accounts.testObserver().observedValue
        assertThat(actualAccounts).isEqualTo(expectedAccounts)
    }

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
