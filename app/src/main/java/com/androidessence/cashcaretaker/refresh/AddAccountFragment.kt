package com.androidessence.cashcaretaker.refresh

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidessence.cashcaretaker.R

/**
 * Allows the user to add an account.
 *
 * Created by adam.mcneilly on 2/6/17.
 */
class AddAccountFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_add_account, container, false)

        return view
    }
}