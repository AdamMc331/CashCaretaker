package com.androidessence.cashcaretaker.transfer

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
import com.androidessence.cashcaretaker.account.Account
import com.androidessence.cashcaretaker.addtransaction.AddTransactionDialog
import com.androidessence.cashcaretaker.data.CCDatabase
import com.androidessence.cashcaretaker.data.CCRepository
import com.androidessence.cashcaretaker.views.SpinnerInputEditText
import com.androidessence.utility.asUIString
import kotlinx.android.synthetic.main.dialog_add_transfer.*
import java.util.*

/**
 * Dialog that allows a user to transfer money from one account to another.
 */
class AddTransferDialog : DialogFragment(), AddTransferController {

    private val presenter: AddTransferPresenter by lazy { AddTransferPresenterImpl(this, AddTransferInteractorImpl(this, CCRepository(CCDatabase.getInMemoryDatabase(context!!)))) }

    private lateinit var fromAccount: SpinnerInputEditText<Account>
    private lateinit var toAccount: SpinnerInputEditText<Account>

    private var selectedDate: Date = Date()
        set(value) {
            transferDate.setText(value.asUIString())
            field = value
        }

    private val repository: CCRepository by lazy { CCRepository(CCDatabase.getInMemoryDatabase(context!!)) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.dialog_add_transfer, container, false)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.setTitle(getString(R.string.add_transfer))
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fromAccount = view.findViewById(R.id.transferFromAccount)
        toAccount = view.findViewById(R.id.transferToAccount)

        repository.getAllAccounts().subscribe {
            fromAccount.items = it
            toAccount.items = it
        }

        transferAmount.filters = arrayOf(DecimalDigitsInputFilter())

        selectedDate = Date()
        transferDate.setOnClickListener { showDatePicker() }

        submitButton.setOnClickListener {
            addTransfer(
                    fromAccount.selectedItem,
                    toAccount.selectedItem,
                    transferAmount.text.toString(),
                    selectedDate
            )
        }
    }

    override fun showProgress() {
        //TODO:
    }

    override fun hideProgress() {
        //TODO:
    }

    override fun addTransfer(fromAccount: Account?, toAccount: Account?, amount: String, date: Date) {
        presenter.addTransfer(fromAccount, toAccount, amount, date)
    }

    override fun showFromAccountError() {
        transferFromAccount.error = "Must select an account to transfer funds from."
    }

    override fun showToAccountError() {
        transferToAccount.error = "Must select an account to transfer funds to."
    }

    override fun showAmountError() {
        transferAmount.error = "Amount must not be blank."
    }

    override fun showSameAccountError() {
        transferToAccount.error = "Must select a different account than the from account."
    }

    override fun onInserted() {
        dismiss()
    }

    override fun onError(error: Throwable) {
        //TODO:
    }

    override fun showDatePicker() {
        val datePickerFragment = DatePickerFragment.newInstance(selectedDate)
        datePickerFragment.setTargetFragment(this, AddTransferDialog.REQUEST_DATE)
        datePickerFragment.show(fragmentManager, AddTransactionDialog::class.java.simpleName)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        selectedDate = calendar.time
    }

    companion object {
        /**
         * Request code for the date picker.
         */
        private val REQUEST_DATE = 0
    }
}