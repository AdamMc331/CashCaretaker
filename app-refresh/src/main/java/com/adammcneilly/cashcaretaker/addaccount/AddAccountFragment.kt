package com.adammcneilly.cashcaretaker.addaccount

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ProgressBar
import com.adammcneilly.cashcaretaker.R
import com.adammcneilly.cashcaretaker.main.MainView
import com.androidessence.utility.hide
import com.androidessence.utility.show

class AddAccountFragment : Fragment(), AddAccountView {
    private lateinit var accountName: TextInputEditText
    private lateinit var accountBalance: TextInputEditText
    private lateinit var progressBar: ProgressBar
    private val presenter: AddAccountPresenter by lazy { AddAccountPresenterImpl(this, AddAccountInteractorImpl()) }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_add_account, container, false)

        accountName = view?.findViewById<TextInputEditText>(R.id.account_name) as TextInputEditText
        accountBalance = view.findViewById<TextInputEditText>(R.id.account_balance) as TextInputEditText
        progressBar = view.findViewById<ProgressBar>(R.id.progress) as ProgressBar

        view.findViewById<Button>(R.id.submit).setOnClickListener({
            addAccount(accountName.text.toString(), accountBalance.text.toString())
        })

        accountName.requestFocus()
        val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()

        hideKeyboard()
    }

    override fun showProgress() {
        progressBar.show()
    }

    override fun hideProgress() {
        progressBar.hide()
    }

    override fun onInsertConflict() {
        accountName.error = getString(R.string.error_account_name_exists)
    }

    override fun addAccount(accountName: String, accountBalance: String) {
        presenter.insert(accountName, accountBalance)
    }

    override fun showAccountNameError() {
        accountName.error = getString(R.string.err_account_name_invalid)
    }

    override fun showAccountBalanceError() {
        accountBalance.error = getString(R.string.err_account_balance_invalid)
    }

    override fun onInserted(ids: List<Long>) {
        //TODO: There has to be a better way than forcing this to be coupled to the activity.
        (activity as MainView).onAccountInserted()
    }

    private fun hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        // check if no view has focus:
        val v = activity.currentFocus ?: return
        inputManager.hideSoftInputFromWindow(v.windowToken, 0)
    }

    companion object {
        val FRAGMENT_NAME: String = AddAccountFragment::class.java.simpleName

        fun newInstance(): AddAccountFragment {
            val fragment = AddAccountFragment()

            val args = Bundle()
            fragment.arguments = args

            return fragment
        }
    }
}
