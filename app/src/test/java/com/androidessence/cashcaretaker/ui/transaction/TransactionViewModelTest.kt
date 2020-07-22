package com.androidessence.cashcaretaker.ui.transaction

import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.core.models.Transaction
import com.androidessence.cashcaretaker.ui.transaction.TransactionViewModel.Companion.NO_DESCRIPTION
import com.androidessence.cashcaretaker.util.asCurrency
import com.androidessence.cashcaretaker.util.asUIString
import java.util.Date
import org.junit.Assert.assertEquals
import org.junit.Test

class TransactionViewModelTest {
    private val viewModel = TransactionViewModel()

    @Test
    fun getBlankDescription() {
        viewModel.transaction = null
        assertEquals(NO_DESCRIPTION, viewModel.description)

        viewModel.transaction = Transaction(description = "")
        assertEquals(NO_DESCRIPTION, viewModel.description)
    }

    @Test
    fun getActualDescription() {
        val testDescription = "testDescription"
        viewModel.transaction = Transaction(description = testDescription)
        assertEquals(testDescription, viewModel.description)
    }

    @Test
    fun getDateString() {
        val testDate = Date()
        viewModel.transaction = Transaction(date = testDate)
        assertEquals(testDate.asUIString(), viewModel.dateString)
    }

    @Test
    fun getAmount() {
        val testAmount = 10.00
        viewModel.transaction = Transaction(amount = testAmount)
        assertEquals(testAmount.asCurrency(), viewModel.amount)
    }

    @Test
    fun getIndicatorColorResource() {
        viewModel.transaction = Transaction(withdrawal = true)
        assertEquals(R.color.mds_red_500, viewModel.indicatorColorResource)

        viewModel.transaction = Transaction(withdrawal = false)
        assertEquals(R.color.mds_green_500, viewModel.indicatorColorResource)
    }
}
