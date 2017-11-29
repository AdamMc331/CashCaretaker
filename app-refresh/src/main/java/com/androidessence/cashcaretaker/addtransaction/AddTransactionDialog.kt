package com.androidessence.cashcaretaker.addtransaction

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import com.androidessence.cashcaretaker.DatePickerFragment
import com.androidessence.cashcaretaker.DecimalDigitsInputFilter
import com.androidessence.cashcaretaker.R
import com.androidessence.utility.asUIString
import kotlinx.android.synthetic.main.dialog_add_transaction.*
import java.util.*


/**
 * Dialog for adding a new transaction.
 *
 * @property[accountName] The name of the account that we're creating a transaction for.
 * @property[withdrawalArgument] The initial argument for whether this transaction is a withdrawal.
 * @property[isWithdrawal] Flag determining if this transaction is a withdrawal based on the switch.
 * @property[presenter] The presenter that connects to the data layer.
 * @property[selectedDate] The date that will be applied to the transaction.
 */
class AddTransactionDialog : DialogFragment(), AddTransactionController {
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

        transactionAmount.filters = arrayOf(DecimalDigitsInputFilter())

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
        /**
         * Key for the account name argument.
         */
        private val ARG_ACCOUNT_NAME: String = "AccountName"

        /**
         * Key for the withdrawal flag argument.
         */
        private val ARG_IS_WITHDRAWAL: String = "IsWithdrawal"

        /**
         * Request code for the date picker.
         */
        private val REQUEST_DATE = 0

        /**
         * The tag used when displaying this dialog.
         */
        val FRAGMENT_NAME: String = AddTransactionDialog::class.java.simpleName

        /**
         * Creates a new dialog fragment to add a transaction.
         *
         * @param[accountName] The name of the account to create a transaction for.
         * @param[isWithdrawal] Flag for the initial status of the withdrawal switch.
         */
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