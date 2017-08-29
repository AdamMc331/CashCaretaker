package com.adammcneilly.cashcaretaker.addaccount

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import com.adammcneilly.cashcaretaker.R
import com.adammcneilly.cashcaretaker.main.MainActivity

/**
 * Dialog to insert an account.
 */
class AddAccountDialog: DialogFragment(), AddAccountView {
    private lateinit var accountName: TextInputEditText
    private lateinit var accountBalance: TextInputEditText
    private val presenter: AddAccountPresenter by lazy { AddAccountPresenterImpl(this, AddAccountInteractorImpl()) }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.dialog_add_account, container, false)

        accountName = view?.findViewById<TextInputEditText>(R.id.account_name) as TextInputEditText
        accountBalance = view.findViewById<TextInputEditText>(R.id.account_balance) as TextInputEditText

        view.findViewById<Button>(R.id.submit)?.setOnClickListener {
            addAccount(accountName.text.toString(), accountBalance.text.toString())
        }

        accountName.requestFocus()
        //TODO: ??
        (activity as MainActivity).showKeyboard()

        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onResume() {
        super.onResume()

        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onDismiss(dialog: DialogInterface?) {
        //TODO: ??
        (activity as MainActivity).hideKeyboard()

        super.onDismiss(dialog)
    }

    override fun showProgress() {
        //TODO:
    }

    override fun hideProgress() {
        //TODO:
    }

    override fun addAccount(accountName: String, accountBalance: String) {
        presenter.insert(accountName, accountBalance)
    }

    override fun onInsertConflict() {
        accountName.error = getString(R.string.error_account_name_exists)
    }

    override fun showAccountNameError() {
        accountName.error = getString(R.string.err_account_name_invalid)
    }

    override fun showAccountBalanceError() {
        accountBalance.error = getString(R.string.err_account_balance_invalid)
    }

    override fun onInserted(ids: List<Long>) {
        dismiss()
    }

    companion object {
        val FRAGMENT_NAME: String = AddAccountDialog::class.java.simpleName
    }
}