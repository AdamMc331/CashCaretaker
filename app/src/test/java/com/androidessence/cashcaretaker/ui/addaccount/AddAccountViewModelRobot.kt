package com.androidessence.cashcaretaker.ui.addaccount

import android.database.sqlite.SQLiteConstraintException
import app.cash.turbine.test
import com.adammcneilly.cashcaretaker.analytics.AnalyticsTracker
import com.androidessence.cashcaretaker.core.models.Account
import com.androidessence.cashcaretaker.data.CCRepository
import com.androidessence.cashcaretaker.testObserver
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@ExperimentalTime
class AddAccountViewModelRobot {
    private val mockRepository = mockk<CCRepository>(relaxed = true)
    private val mockAnalyticsTracker = mockk<AnalyticsTracker>(relaxed = true)

    private lateinit var viewModel: AddAccountViewModel

    fun buildViewModel() = apply {
        viewModel = AddAccountViewModel(
            repository = mockRepository,
            analyticsTracker = mockAnalyticsTracker
        )
    }

    fun mockAccountConstraintException(account: Account) = apply {
        coEvery {
            mockRepository.insertAccount(account)
        } throws SQLiteConstraintException()
    }

    fun addAccount(
        accountName: String?,
        balanceString: String?
    ) = apply {
        viewModel.addAccount(accountName, balanceString)
    }

    suspend fun assertDismissEventEmitted() = apply {
        viewModel.dismissEvents.test {
            assertThat(expectItem()).isTrue()
            expectComplete()
        }
    }

    fun assertCallToInsertAccount(account: Account) = apply {
        coVerify(exactly = 1) {
            mockRepository.insertAccount(account)
        }
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
