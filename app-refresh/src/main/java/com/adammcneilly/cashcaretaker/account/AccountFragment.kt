package com.adammcneilly.cashcaretaker.account

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.adammcneilly.cashcaretaker.DividerItemDecoration
import com.adammcneilly.cashcaretaker.R
import com.adammcneilly.cashcaretaker.entity.EntityPresenter
import com.adammcneilly.cashcaretaker.main.MainController
import com.androidessence.utility.hide
import com.androidessence.utility.show
import timber.log.Timber

/**
 * Displays a list of accounts to the user.
 */
class AccountFragment: Fragment(), AccountController {
    private val adapter = AccountAdapter(this)
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private val presenter: EntityPresenter<Account> by lazy { AccountPresenterImpl(this, AccountInteractorImpl()) }
    private val mainController: MainController by lazy { (activity as MainController) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        progressBar = view?.findViewById<ProgressBar>(R.id.progress) as ProgressBar
        recyclerView = view.findViewById<RecyclerView>(R.id.accounts) as RecyclerView

        val layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        //TODO: If context were null?
        recyclerView.addItemDecoration(DividerItemDecoration(context!!))

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
        recyclerView.hide()
        progressBar.show()
    }

    override fun hideProgress() {
        progressBar.hide()
        recyclerView.show()
    }

    override fun setAccounts(accounts: List<Account>) {
        adapter.items = accounts
    }

    override fun onWithdrawalButtonClicked(account: Account) {
        //TODO: Create AddTransactionDialog
        Timber.d("Withdrawal button clicked.")
    }

    override fun onDepositButtonClicked(account: Account) {
        //TODO: Create AddTransactionDialog
        Timber.d("Deposit button clicked.")
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