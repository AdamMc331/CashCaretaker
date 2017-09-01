package com.adammcneilly.cashcaretaker.account

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.adammcneilly.cashcaretaker.R
import com.adammcneilly.cashcaretaker.main.MainView

/**
 * Displays a list of accounts to the user.
 */
class AccountCardFragment : Fragment(), AccountView {
    private lateinit var accountCardView: AccountCardView
    private val presenter: AccountPresenter by lazy { AccountPresenterImpl(this, AccountInteractorImpl()) }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_account_card, container, false)

        accountCardView = view?.findViewById<AccountCardView>(R.id.account_card) as AccountCardView

        accountCardView.findViewById<Button>(R.id.add_account).setOnClickListener {
            //TODO:
            (activity as MainView).navigateToAddAccount()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun showProgress() {
        //TODO:
    }

    override fun hideProgress() {
        //TODO:
    }

    override fun setAccounts(accounts: List<Account>) {
        accountCardView.setAccounts(accounts)
    }

    companion object {
        val FRAGMENT_NAME: String = AccountCardFragment::class.java.simpleName

        fun newInstance(): AccountCardFragment {
            val fragment = AccountCardFragment()

            val args = Bundle()
            fragment.arguments = args

            return fragment
        }
    }
}