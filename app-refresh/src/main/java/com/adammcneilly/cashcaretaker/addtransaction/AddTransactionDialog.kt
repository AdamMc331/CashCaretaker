package com.adammcneilly.cashcaretaker.addtransaction

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.adammcneilly.cashcaretaker.R

/**
 * Dialog for adding a new transaction.
 */
class AddTransactionDialog : DialogFragment(), AddTransactionView {
    private val accountName: String by lazy { arguments?.getString(ARG_ACCOUNT_NAME).orEmpty() }
    private val isWithdrawal: Boolean by lazy { arguments?.getBoolean(ARG_IS_WITHDRAWAL) ?: true }
    private lateinit var transactionDescription: TextInputEditText
    private lateinit var transactionAmount: TextInputEditText
    private val presenter: AddTransactionPresenter by lazy { AddTransactionPresenterImpl(this, AddTransactionInteractorImpl()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_add_transaction, container, false)

        transactionDescription = view.findViewById<TextInputEditText>(R.id.transaction_description) as TextInputEditText
        transactionAmount = view.findViewById<TextInputEditText>(R.id.transaction_amount) as TextInputEditText

        view.findViewById<Button>(R.id.submit)?.setOnClickListener {
            presenter.insert(accountName, transactionDescription.text.toString(), transactionAmount.text.toString(), isWithdrawal)
        }

        transactionDescription.requestFocus()

        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        val title = if (isWithdrawal) getString(R.string.add_withdrawal) else getString(R.string.add_deposit)
        dialog.setTitle(title)
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

    companion object {
        private val ARG_ACCOUNT_NAME: String = "AccountName"
        private val ARG_IS_WITHDRAWAL: String = "IsWithdrawal"
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