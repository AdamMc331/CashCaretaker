package com.androidessence.cashcaretaker.account

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
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

        init {
            binding.viewModel = viewModel
        }

        init {
            binding.withdrawalButton.setOnClickListener { withdrawalClickSubject.onNext(items[adapterPosition]) }
            binding.depositButton.setOnClickListener { depositClickSubject.onNext(items[adapterPosition]) }
            itemView.setOnClickListener { accountClickSubject.onNext(items[adapterPosition]) }
            itemView.setOnLongClickListener {
                accountLongClickSubject.onNext(items[adapterPosition])
                true
            }
        }

        fun bindItem(item: Account?) {
            viewModel.account = item
            binding.executePendingBindings()
        }
    }
}