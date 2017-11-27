package com.adammcneilly.cashcaretaker.addtransaction

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import com.adammcneilly.cashcaretaker.DatePickerFragment
import com.adammcneilly.cashcaretaker.R
import com.androidessence.utility.asUIString
import kotlinx.android.synthetic.main.dialog_add_transaction.*
import java.util.*
import android.text.InputFilter
import com.adammcneilly.cashcaretaker.DecimalDigitsInputFilter


/**
 * Dialog for adding a new transaction.
 */
class AddTransactionDialog : DialogFragment(), AddTransactionView {
    private val accountName: String by lazy { arguments?.getString(ARG_ACCOUNT_NAME).orEmpty() }
    private val withdrawalArgument: Boolean by lazy { arguments?.getBoolean(ARG_IS_WITHDRAWAL) ?: true }
    private val isWithdrawal: Boolean
        get() = withdrawalSwitch.isChecked
    private val presenter: AddTransactionPresenter by lazy { AddTransactionPresenterImpl(this, AddTransactionInteractorImpl()) }

    private var selectedDate: Date = Date()
        set(value) {
            transactionDate.setText(value.asUIString())
            field = value
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.dialog_add_transaction, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        withdrawalSwitch.isChecked = withdrawalArgument

        submitButton.setOnClickListener {
            presenter.insert(accountName, transactionDescription.text.toString(), transactionAmount.text.toString(), isWithdrawal, selectedDate)
        }

        val inputFilters = arrayOf(DecimalDigitsInputFilter())
        transactionAmount.filters = inputFilters

        transactionDate.setOnClickListener { showDatePicker() }
        selectedDate = Date()

        transactionDescription.requestFocus()
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