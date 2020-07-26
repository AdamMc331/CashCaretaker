package com.androidessence.cashcaretaker.ui.addaccount

import com.androidessence.cashcaretaker.data.analytics.AnalyticsTracker
import com.androidessence.cashcaretaker.fakes.FakeCCRepository
import com.androidessence.cashcaretaker.testObserver
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.collect

class AddAccountViewModelRobot {
    private val fakeRepository = FakeCCRepository()
    private val mockAnalyticsTracker = mockk<AnalyticsTracker>(relaxUnitFun = true)

    private lateinit var viewModel: AddAccountViewModel

    fun buildViewModel() = apply {
        viewModel = AddAccountViewModel(
            repository = fakeRepository,
            analyticsTracker = mockAnalyticsTracker
        )
    }

    fun mockAccountConstraintException() = apply {
        fakeRepository.mockAccountConstraintException(true)
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
        verify(exactly = 1) {
            mockAnalyticsTracker.trackAccountAdded()
        }
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
