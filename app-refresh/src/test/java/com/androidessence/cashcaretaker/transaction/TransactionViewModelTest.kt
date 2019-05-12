package com.androidessence.cashcaretaker.transaction

import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.transaction.TransactionViewModel.Companion.NO_DESCRIPTION
import com.androidessence.utility.asCurrency
import com.androidessence.utility.asUIString
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Date

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