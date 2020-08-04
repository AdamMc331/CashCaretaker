package com.androidessence.cashcaretaker.ui.transfer

import app.cash.turbine.test
import com.adammcneilly.cashcaretaker.analytics.AnalyticsTracker
import com.androidessence.cashcaretaker.core.models.Account
import com.androidessence.cashcaretaker.data.CCRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import java.util.Date
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf

@ExperimentalTime
@ExperimentalCoroutinesApi
class AddTransferViewModelRobot {
    private val mockRepository = mockk<CCRepository>(relaxed = true)
    private val mockAnalyticsTracker = mockk<AnalyticsTracker>(relaxed = true)
    private lateinit var viewModel: AddTransferViewModel

    fun mockAccountsFromRepo(accounts: List<Account>) = apply {
        coEvery {
            mockRepository.fetchAllAccounts()
        } returns flowOf(accounts)
    }

    fun buildViewModel() = apply {
        viewModel = AddTransferViewModel(
            repository = mockRepository,
            analyticsTracker = mockAnalyticsTracker
        )
    }

    suspend fun assertAccounts(expectedAccount: List<Account>) = apply {
        viewModel.viewState.test {
            assertThat(expectItem().accounts).isEqualTo(expectedAccount)
            expectComplete()
        }
    }

    suspend fun assertFromAccountError(expectedValue: Int) = apply {
        viewModel.viewState.test {
            assertThat(expectItem().fromAccountErrorRes).isEqualTo(expectedValue)
            expectComplete()
        }
    }

    suspend fun assertToAccountError(expectedValue: Int) = apply {
        viewModel.viewState.test {
            assertThat(expectItem().toAccountErrorRes).isEqualTo(expectedValue)
            expectComplete()
        }
    }

    suspend fun assertAmountError(expectedValue: Int) = apply {
        viewModel.viewState.test {
            assertThat(expectItem().amountErrorRes).isEqualTo(expectedValue)
            expectComplete()
        }
    }

    @ExperimentalTime
    suspend fun assertDismissEventEmitted() = apply {
        viewModel.dismissEvents.test {
            assertThat(expectItem()).isTrue()
            expectComplete()
        }
    }

    fun addTransfer(
        fromAccount: Account?,
        toAccount: Account?,
        amount: String,
        date: Date
    ) = apply {
        viewModel.addTransfer(
            fromAccount,
            toAccount,
            amount,
            date
        )
    }

    fun assertCallToCreateTransfer(
        fromAccount: Account,
        toAccount: Account,
        amount: Double,
        date: Date
    ) = apply {
        coVerify(exactly = 1) {
            mockRepository.transfer(
                fromAccount,
                toAccount,
                amount,
                date
            )
        }
    }

    fun assertCallToTrackTransfer() = apply {
        verify(exactly = 1) {
            mockAnalyticsTracker.trackTransferAdded()
        }
    }
}
