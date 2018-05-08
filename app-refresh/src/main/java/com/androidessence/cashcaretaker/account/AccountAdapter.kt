package com.androidessence.cashcaretaker.account

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.androidessence.cashcaretaker.databinding.ListItemAccountBinding

/**
 * Adapter for displaying Accounts in a RecyclerView.
 *
 * @property[controller] A controller supplied to handle callbacks for item selection.
 * @property[items] The list of accounts to display.
 */
class AccountAdapter(private val controller: AccountController?, items: List<Account> = ArrayList()): RecyclerView.Adapter<AccountAdapter.AccountViewHolder>() {

    var items: List<Account> = items
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemAccountBinding.inflate(inflater, parent, false)
        return AccountViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.bindItem(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class AccountViewHolder(private val binding: ListItemAccountBinding): RecyclerView.ViewHolder(binding.root) {
        private val viewModel: AccountDataModel = AccountDataModel()
        private val withdrawalButton = binding.withdrawalButton
        private val depositButton = binding.depositButton

        init {
            binding.account = viewModel
        }

        init {
            withdrawalButton.setOnClickListener { controller?.onTransactionButtonClicked(items[adapterPosition], true) }
            depositButton.setOnClickListener { controller?.onTransactionButtonClicked(items[adapterPosition], false) }
            itemView.setOnClickListener { controller?.onAccountSelected(items[adapterPosition]) }
            itemView.setOnLongClickListener {
                controller?.onAccountLongClicked(items[adapterPosition])
                true
            }
        }

        /**
         * Binds an Account object to the row view for display.
         */
        fun bindItem(item: Account?) {
            viewModel.account = item
            binding.executePendingBindings()
        }
    }
}