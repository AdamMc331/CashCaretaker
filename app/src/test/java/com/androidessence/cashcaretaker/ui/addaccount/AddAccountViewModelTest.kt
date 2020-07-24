package com.androidessence.cashcaretaker.ui.addaccount

import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class AddAccountViewModelTest {
    private lateinit var testRobot: AddAccountViewModelRobot

    @Before
    fun setUp() {
        testRobot = AddAccountViewModelRobot()
    }

    @Test
    fun addingAccountEmitsDismissEvent() = runBlockingTest {
        testRobot
            .buildViewModel()
            .addAccount(
                accountName = "Checking",
                balanceString = "100.00"
            )
            .assertCallToInsertAccount()
            .assertCallToTrackAccountAdded()
            .assertDismissEventEmitted()
    }
}
