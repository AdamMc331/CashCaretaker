package com.androidessence.cashcaretaker.ui.accountlist

import android.view.View
import androidx.compose.foundation.Text
import androidx.compose.material.MaterialTheme
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

        binding.composeView.setContent {
            MaterialTheme {
                Text(text = "Hello World")
            }
        }
    }

    override fun bind(position: Int, item: Account) {
        super.bind(position, item)
        viewModel.account = item
        binding.executePendingBindings()
    }
}
