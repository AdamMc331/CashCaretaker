package com.adammcneilly.cashcaretaker.account

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adammcneilly.cashcaretaker.R
import com.adammcneilly.cashcaretaker.addtransaction.AddTransactionDialog
import com.adammcneilly.cashcaretaker.entity.EntityPresenter
import com.adammcneilly.cashcaretaker.main.MainController
import com.androidessence.utility.hide
import com.androidessence.utility.show
import kotlinx.android.synthetic.main.fragment_account.*

/**
 * Displays a list of accounts to the user.
 */
class AccountFragment: Fragment(), AccountController {
    private val adapter = AccountAdapter(this)
    private val presenter: EntityPresenter<Account> by lazy { AccountPresenterImpl(this, AccountInteractorImpl()) }
    private val mainController: MainController by lazy { (activity as MainController) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        val layoutManager = LinearLayoutManager(context)
        accountsRecyclerView.adapter = adapter
        accountsRecyclerView.layoutManager = layoutManager
        accountsRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener({
            //TODO: Is there a better way?
            (activity as MainController).navigateToAddAccount()
        })

        return view
    }

    override fun onResume() {
        super.onResume()
        presenter.onAttach()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun showProgress() {
        accountsRecyclerView.hide()
        progressBar.show()
    }

    override fun hideProgress() {
        progressBar.hide()
        accountsRecyclerView.show()
    }

    override fun setAccounts(accounts: List<Account>) {
        adapter.items = accounts
    }

    override fun onWithdrawalButtonClicked(account: Account) {
        val dialog = AddTransactionDialog.newInstance(account.name, true)
        dialog.show(fragmentManager, AddTransactionDialog.FRAGMENT_NAME)
    }

    override fun onDepositButtonClicked(account: Account) {
        val dialog = AddTransactionDialog.newInstance(account.name, false)
        dialog.show(fragmentManager, AddTransactionDialog.FRAGMENT_NAME)
    }

    override fun onAccountSelected(account: Account) {
        mainController.showTransactions(account.name)
    }

    companion object {
        val FRAGMENT_NAME: String = AccountFragment::class.java.simpleName

        fun newInstance(): AccountFragment {
            val fragment = AccountFragment()

            val args = Bundle()
            fragment.arguments = args

            return fragment
        }
    }
}