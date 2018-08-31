package com.androidessence.cashcaretaker.account

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.androidessence.cashcaretaker.account.AccountAdapter.AccountViewHolder
import com.androidessence.cashcaretaker.databinding.ListItemAccountBinding
import io.reactivex.subjects.PublishSubject

/**
 * Adapter for displaying Accounts in a RecyclerView.
 *
 * This exposes all click events from the [AccountViewHolder] through the various [PublishSubject]s.
 */
class AccountAdapter(items: List<Account> = ArrayList()) : RecyclerView.Adapter<AccountAdapter.AccountViewHolder>() {
    val accountClickSubject: PublishSubject<Account> = PublishSubject.create()
    val accountLongClickSubject: PublishSubject<Account> = PublishSubject.create()
    val withdrawalClickSubject: PublishSubject<Account> = PublishSubject.create()
    val depositClickSubject: PublishSubject<Account> = PublishSubject.create()

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

    inner class AccountViewHolder(private val binding: ListItemAccountBinding) : RecyclerView.ViewHolder(binding.root) {
        private val viewModel = AccountDataModel()
        private val currentItem: Account
            get() = items[adapterPosition]

        init {
            binding.viewModel = viewModel
            setClickListeners()
        }

        /**
         * Sets a click listener on each of the UI items in this view holder.
         */
        private fun setClickListeners() {
            binding.withdrawalButton.setOnClickListener { withdrawalClickSubject.onNext(currentItem) }
            binding.depositButton.setOnClickListener { depositClickSubject.onNext(currentItem) }
            itemView.setOnClickListener { accountClickSubject.onNext(currentItem) }
            itemView.setOnLongClickListener {
                accountLongClickSubject.onNext(currentItem)
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