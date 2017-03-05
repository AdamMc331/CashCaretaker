package com.androidessence.refresh

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.androidessence.refresh.R

/**
 * A placeholder fragment containing a simple view.
 */
class AccountsActivityFragment : CoreFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_accounts, container, false)
    }
}
