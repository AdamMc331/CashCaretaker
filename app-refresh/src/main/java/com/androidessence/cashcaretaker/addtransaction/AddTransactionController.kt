package com.androidessence.cashcaretaker.addtransaction

import android.app.DatePickerDialog
import com.androidessence.cashcaretaker.core.DataController


/**
 * Controller for adding a transaction.
 */
interface AddTransactionController : DataController, DatePickerDialog.OnDateSetListener {
    /**
     * Callback when the transaction description supplied is invalid.
     */
    fun showTransactionDescriptionError()

    /**
     * Callback when the transaction amount supplied is invalid.
     */
    fun showTransactionAmountError()

    /**
     * Callback with the identifiers of the inserted accounts.
     */
    fun onInserted(ids: List<Long>)

    /**
     * Displays a date picker that allows the user to pick the date of their transaction.
     */
    fun showDatePicker()
}