package com.androidessence.cashcaretaker.addaccount

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidessence.cashcaretaker.DecimalDigitsInputFilter
import com.androidessence.cashcaretaker.R
import kotlinx.android.synthetic.main.dialog_add_account.*

/**
 * Dialog to insert an account.
 *
 * @property[presenter] Presenter used to connect to the data layer.
 */
class AddAccountDialog: DialogFragment(), AddAccountController {
    private val presenter: AddAccountPresenter by lazy { AddAccountPresenterImpl(this, AddAccountInteractorImpl()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.dialog_add_account, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accountBalance.filters = arrayOf(DecimalDigitsInputFilter())

        submitButton.setOnClickListener {
            addAccount(accountName.text.toString(), accountBalance.text.toString())
        }

        accountName.requestFocus()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setTitle(R.string.add_account)
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

    override fun addAccount(accountName: String, accountBalance: String) {
        presenter.insert(accountName, accountBalance)
    }

    override fun onInsertConflict() {
        accountName.error = getString(R.string.err_account_name_exists)
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
        /**
         * The tag that is used when this account is inserted.
         */
        val FRAGMENT_NAME: String = AddAccountDialog::class.java.simpleName
    }
}