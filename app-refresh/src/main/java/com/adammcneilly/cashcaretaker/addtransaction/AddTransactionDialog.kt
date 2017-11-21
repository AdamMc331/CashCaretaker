package com.adammcneilly.cashcaretaker.addtransaction

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.Switch
import com.adammcneilly.cashcaretaker.DatePickerFragment
import com.adammcneilly.cashcaretaker.R
import com.androidessence.utility.Utility
import java.util.*

/**
 * Dialog for adding a new transaction.
 */
class AddTransactionDialog : DialogFragment(), AddTransactionView {
    private val accountName: String by lazy { arguments?.getString(ARG_ACCOUNT_NAME).orEmpty() }
    private val withdrawalArgument: Boolean by lazy { arguments?.getBoolean(ARG_IS_WITHDRAWAL) ?: true }
    private val isWithdrawal: Boolean
        get() = withdrawalSwitch.isChecked
    private lateinit var transactionDescription: TextInputEditText
    private lateinit var transactionAmount: TextInputEditText
    private lateinit var transactionDate: TextInputEditText
    private lateinit var withdrawalSwitch: Switch
    private val presenter: AddTransactionPresenter by lazy { AddTransactionPresenterImpl(this, AddTransactionInteractorImpl()) }

    private var selectedDate: Date = Date()
        set(value) {
            transactionDate.setText(Utility.getUIDateString(value))
            field = value
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_add_transaction, container, false)

        transactionDescription = view.findViewById<TextInputEditText>(R.id.transaction_description) as TextInputEditText
        transactionAmount = view.findViewById<TextInputEditText>(R.id.transaction_amount) as TextInputEditText

        withdrawalSwitch = view.findViewById(R.id.withdrawal_switch)
        withdrawalSwitch.isChecked = withdrawalArgument

        view.findViewById<Button>(R.id.submit)?.setOnClickListener {
            presenter.insert(accountName, transactionDescription.text.toString(), transactionAmount.text.toString(), isWithdrawal, selectedDate)
        }

        transactionDate = view.findViewById(R.id.transaction_date)
        transactionDate.setOnClickListener { showDatePicker() }
        selectedDate = Date()

        transactionDescription.requestFocus()

        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.setTitle(getString(R.string.add_transaction))
        return dialog
    }

    override fun onResume() {
        super.onResume()

        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun showProgress() {
        //TODO:
    }

    override fun hideProgress() {
        //TODO:
    }

    override fun showTransactionDescriptionError() {
        transactionDescription.error = getString(R.string.error_invalid_description)
    }

    override fun showTransactionAmountError() {
        transactionAmount.error = getString(R.string.error_invalid_amount)
    }

    override fun onInserted(ids: List<Long>) {
        dismiss()
    }

    override fun showDatePicker() {
        val datePickerFragment = DatePickerFragment.newInstance(selectedDate)
        datePickerFragment.setTargetFragment(this, REQUEST_DATE)
        datePickerFragment.show(fragmentManager, AddTransactionDialog::class.java.simpleName)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        selectedDate = calendar.time
    }

    companion object {
        private val ARG_ACCOUNT_NAME: String = "AccountName"
        private val ARG_IS_WITHDRAWAL: String = "IsWithdrawal"
        private val REQUEST_DATE = 0
        val FRAGMENT_NAME: String = AddTransactionDialog::class.java.simpleName

        fun newInstance(accountName: String, isWithdrawal: Boolean): AddTransactionDialog {
            val args = Bundle()
            args.putString(ARG_ACCOUNT_NAME, accountName)
            args.putBoolean(ARG_IS_WITHDRAWAL, isWithdrawal)

            val fragment = AddTransactionDialog()
            fragment.arguments = args

            return fragment
        }
    }
}