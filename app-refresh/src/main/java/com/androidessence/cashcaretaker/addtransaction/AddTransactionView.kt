package com.androidessence.cashcaretaker.addtransaction

import android.app.DatePickerDialog
import com.androidessence.cashcaretaker.core.DataController


/**
 * View to add a transaction.
 */
interface AddTransactionView: DataController, DatePickerDialog.OnDateSetListener {
    fun showTransactionDescriptionError()
    fun showTransactionAmountError()
    fun onInserted(ids: List<Long>)
    fun showDatePicker()
}