package com.androidessence.cashcaretaker.ui.transfer

import com.androidessence.cashcaretaker.core.models.Account
import com.androidessence.cashcaretaker.fakes.FakeAnalyticsTracker
import com.androidessence.cashcaretaker.fakes.FakeCCRepository
import com.androidessence.cashcaretaker.testObserver
import com.google.common.truth.Truth.assertThat
import java.util.Date
import kotlinx.coroutines.flow.collect

class AddTransferViewModelRobot {
    private val fakeRepository = FakeCCRepository()
    private val fakeAnalyticsTracker = FakeAnalyticsTracker()
    private lateinit var viewModel: AddTransferViewModel

    fun mockAccountsFromRepo(accounts: List<Account>) = apply {
        fakeRepository.mockAccounts(accounts)
    }

    fun buildViewModel() = apply {
        viewModel = AddTransferViewModel(
            repository = fakeRepository,
            analyticsTracker = fakeAnalyticsTracker
        )
    }

    fun assertAccounts(expectedAccount: List<Account>) = apply {
        val actualAccounts = viewModel.accounts.testObserver().observedValue
        assertThat(actualAccounts).isEqualTo(expectedAccount)
    }

    fun assertFromAccountError(expectedValue: Int) = apply {
        val actualValue = viewModel.fromAccountError.testObserver().observedValue
        assertThat(actualValue).isEqualTo(expectedValue)
    }

    fun assertToAccountError(expectedValue: Int) = apply {
        val actualValue = viewModel.toAccountError.testObserver().observedValue
        assertThat(actualValue).isEqualTo(expectedValue)
    }

    fun assertAmountError(expectedValue: Int) = apply {
        val actualValue = viewModel.amountError.testObserver().observedValue
        assertThat(actualValue).isEqualTo(expectedValue)
    }

    suspend fun assertDismissEventEmitted() = apply {
        viewModel.dismissEvents.collect { shouldDismiss ->
            assertThat(shouldDismiss).isTrue()
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

    fun assertCallToCreateTransfer() = apply {
        assertThat(fakeRepository.getCreateTransferCallCount()).isEqualTo(1)
    }

    fun assertCallToTrackTransfer() = apply {
        assertThat(fakeAnalyticsTracker.getTrackTransferAddedCount()).isEqualTo(1)
    }
}
