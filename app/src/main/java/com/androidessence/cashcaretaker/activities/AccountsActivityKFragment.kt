package com.androidessence.cashcaretaker.activities

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.androidessence.cashcaretaker.R

/**
 * A placeholder fragment containing a simple view.
 */
class AccountsActivityKFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_accounts_activity_k, container, false)
    }

    companion object {
        fun newInstance(): AccountsActivityKFragment {
            val args = Bundle()

            val fragment = AccountsActivityKFragment()
            fragment.arguments = args

            return fragment
        }
    }
}
