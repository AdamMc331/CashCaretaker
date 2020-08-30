package com.androidessence.cashcaretaker.ui.accountlist

import android.view.View
import com.androidessence.cashcaretaker.core.models.Account
import com.androidessence.cashcaretaker.databinding.ListItemAccountBinding
import com.androidessence.cashcaretaker.ui.account.AccountViewModel
import me.ibrahimyilmaz.kiel.core.RecyclerViewHolder

class AccountViewHolder(
    view: View
) : RecyclerViewHolder<Account>(view) {

    /**
     * NOTE: This field injection here isn't great, but we'll remove this VH
     * entirely when the scroller itself is in Compose, so it's fine for this step.
     */
    var withdrawalClickListener: (Account) -> Unit = {}
    var depositClickListener: (Account) -> Unit = {}
    var accountClickListener: (Account) -> Unit = {}
    var accountLongClickListener: (Account) -> Unit = {}

    val binding = ListItemAccountBinding.bind(view)

    val viewModel = AccountViewModel()

    init {
        binding.viewModel = viewModel
    }

    override fun bind(position: Int, item: Account) {
        super.bind(position, item)
        viewModel.account = item

        binding.composeView.setContent {
            AccountListItem(
                account = item,
                accountClickListener = { account ->
                    accountClickListener.invoke(account)
                },
                accountLongClickListener = { account ->
                    accountLongClickListener.invoke(account)
                },
                withdrawalClickListener = { account ->
                    withdrawalClickListener.invoke(account)
                },
                depositClickListener = { account ->
                    depositClickListener.invoke(account)
                }
            )
        }
    }
}
