package com.androidessence.cashcaretaker.ui.addaccount

import com.androidessence.cashcaretaker.TestDispatcherProvider
import com.androidessence.cashcaretaker.fakes.FakeAnalyticsTracker
import com.androidessence.cashcaretaker.fakes.FakeCCRepository
import com.androidessence.cashcaretaker.testObserver
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.collect

class AddAccountViewModelRobot {
    private val fakeRepository = FakeCCRepository()
    private val fakeAnalyticsTracker = FakeAnalyticsTracker()
    private val testDispatcherProvider = TestDispatcherProvider()

    private lateinit var viewModel: AddAccountViewModel

    fun buildViewModel() = apply {
        viewModel = AddAccountViewModel(
            repository = fakeRepository,
            analyticsTracker = fakeAnalyticsTracker,
            dispatcherProvider = testDispatcherProvider
        )
    }

    fun addAccount(
        accountName: String?,
        balanceString: String?
    ) = apply {
        viewModel.addAccount(accountName, balanceString)
    }

    suspend fun assertDismissEventEmitted() = apply {
        viewModel.dismissEvents.collect { shouldDismiss ->
            assertThat(shouldDismiss).isTrue()
        }
    }

    fun assertCallToInsertAccount() = apply {
        assertThat(fakeRepository.getInsertAccountCallCount()).isEqualTo(1)
    }

    fun assertCallToTrackAccountAdded() = apply {
        assertThat(fakeAnalyticsTracker.getTrackAccountAddedCount()).isEqualTo(1)
    }

    fun assertAccountNameError(expectedValue: Int) = apply {
        val actualValue = viewModel.accountNameError.testObserver().observedValue
        assertThat(actualValue).isEqualTo(expectedValue)
    }

    fun assertAccountBalanceError(expectedValue: Int) = apply {
        val actualValue = viewModel.accountBalanceError.testObserver().observedValue
        assertThat(actualValue).isEqualTo(expectedValue)
    }
}
