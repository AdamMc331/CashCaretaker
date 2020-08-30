package com.androidessence.cashcaretaker.ui.accountlist

import android.view.View
import com.androidessence.cashcaretaker.core.models.Account
import com.androidessence.cashcaretaker.databinding.ListItemAccountBinding
import com.androidessence.cashcaretaker.ui.account.AccountViewModel
import me.ibrahimyilmaz.kiel.core.RecyclerViewHolder

class AccountViewHolder(
    view: View
) : RecyclerViewHolder<Account>(view) {

    val binding = ListItemAccountBinding.bind(view)

    val viewModel = AccountViewModel()

    init {
        binding.viewModel = viewModel
    }

    override fun bind(position: Int, item: Account) {
        super.bind(position, item)
        viewModel.account = item

        // TODO: Text color resource
        binding.composeView.setContent {
            AccountListItem(account = item)
        }
    }
}
