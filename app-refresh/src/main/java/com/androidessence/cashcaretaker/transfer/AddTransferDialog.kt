package com.androidessence.cashcaretaker.transfer

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidessence.cashcaretaker.DecimalDigitsInputFilter
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.account.Account
import com.androidessence.cashcaretaker.data.CCRepository
import com.androidessence.cashcaretaker.views.SpinnerInputEditText
import com.androidessence.utility.asUIString
import kotlinx.android.synthetic.main.dialog_add_transfer.*
import java.util.*

/**
 * Dialog that allows a user to transfer money from one account to another.
 */
class AddTransferDialog : DialogFragment(), AddTransferController {

    private val presenter: AddTransferPresenter by lazy { AddTransferPresenterImpl(this, AddTransferInteractorImpl()) }

    private lateinit var fromAccount: SpinnerInputEditText<Account>
    private lateinit var toAccount: SpinnerInputEditText<Account>

    private var selectedDate: Date = Date()
        set(value) {
            transferDate.setText(value.asUIString())
            field = value
        }

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

        CCRepository.getAllAccounts().subscribe {
            fromAccount.items = it
            toAccount.items = it
        }

        transferAmount.filters = arrayOf(DecimalDigitsInputFilter())

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

    override fun onInserted() {
        dismiss()
    }

    override fun onError() {
        //TODO:
    }
}