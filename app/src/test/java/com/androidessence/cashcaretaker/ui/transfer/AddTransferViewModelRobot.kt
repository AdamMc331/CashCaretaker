package com.androidessence.cashcaretaker.ui.transfer

import com.adammcneilly.cashcaretaker.analytics.AnalyticsTracker
import com.androidessence.cashcaretaker.core.models.Account
import com.androidessence.cashcaretaker.data.CCRepository
import com.androidessence.cashcaretaker.testObserver
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import java.util.Date
import kotlinx.coroutines.flow.flowOf

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

    fun assertAccounts(expectedAccount: List<Account>) = apply {
        val actualAccounts = viewModel.viewState.testObserver().observedValue?.accounts
        assertThat(actualAccounts).isEqualTo(expectedAccount)
    }

    fun assertFromAccountError(expectedValue: Int) = apply {
        val actualRes = viewModel.viewState.testObserver().observedValue?.fromAccountErrorRes
        assertThat(actualRes).isEqualTo(expectedValue)
    }

    fun assertToAccountError(expectedValue: Int) = apply {
        val actualRes = viewModel.viewState.testObserver().observedValue?.toAccountErrorRes
        assertThat(actualRes).isEqualTo(expectedValue)
    }

    fun assertAmountError(expectedValue: Int) = apply {
        val actualRes = viewModel.viewState.testObserver().observedValue?.amountErrorRes
        assertThat(actualRes).isEqualTo(expectedValue)
    }

    suspend fun assertDismissEventEmitted() = apply {
        assertThat(viewModel.dismissEventChannel.receive()).isTrue()
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
