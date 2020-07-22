package com.androidessence.cashcaretaker.ui.accountlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.androidessence.cashcaretaker.core.models.Account
import com.androidessence.cashcaretaker.databinding.ListItemAccountBinding
import com.androidessence.cashcaretaker.ui.account.AccountViewModel
import com.androidessence.cashcaretaker.ui.accountlist.AccountListAdapter.AccountViewHolder

/**
 * Adapter for displaying Accounts in a RecyclerView.
 */
class AccountListAdapter(
    private val accountClicked: (Account) -> Unit,
    private val accountLongClicked: (Account) -> Unit,
    private val withdrawalClicked: (Account) -> Unit,
    private val depositClicked: (Account) -> Unit
) : RecyclerView.Adapter<AccountViewHolder>() {
    var items: List<Account> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemAccountBinding.inflate(inflater, parent, false)
        return AccountViewHolder(
            binding,
            accountClicked,
            accountLongClicked,
            withdrawalClicked,
            depositClicked
        )
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.bindItem(items[position])
    }

    override fun getItemCount(): Int = items.size

    class AccountViewHolder(
        private val binding: ListItemAccountBinding,
        private val accountClicked: (Account) -> Unit,
        private val accountLongClicked: (Account) -> Unit,
        private val withdrawalClicked: (Account) -> Unit,
        private val depositClicked: (Account) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        private val viewModel = AccountViewModel()

        init {
            binding.viewModel = viewModel
            setClickListeners()
        }

        /**
         * Sets a click listener on each of the UI items in this view holder.
         */
        private fun setClickListeners() {
            binding.withdrawalButton.setOnClickListener {
                viewModel.account?.let(withdrawalClicked::invoke)
            }

            binding.depositButton.setOnClickListener {
                viewModel.account?.let(depositClicked::invoke)
            }

            itemView.setOnClickListener {
                viewModel.account?.let(accountClicked::invoke)
            }
            itemView.setOnLongClickListener {
                viewModel.account?.let(accountLongClicked::invoke)
                true
            }
        }

        /**
         * Binds an [Account] with this particular view holder.
         */
        fun bindItem(item: Account?) {
            viewModel.account = item
            binding.executePendingBindings()
        }
    }
}
