package com.androidessence.cashcaretaker.refresh

import android.support.v4.app.Fragment

/**
 * Base Fragment class.
 *
 * Created by adam.mcneilly on 2/25/17.
 */
open class CoreFragment: Fragment() {
    val dataSource: CCDataSource?
        get() = (activity as? CoreActivity)?.dataSource
}