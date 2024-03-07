package com.example.piggy_bank_assistant.presentation.category_history_activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.piggy_bank_assistant.R
import com.example.piggy_bank_assistant.databinding.CategoryHistoryItemBinding
import com.example.piggy_bank_assistant.databinding.MainCategoryItemBinding
import com.example.piggy_bank_assistant.domain.Category
import com.example.piggy_bank_assistant.domain.Transaction
import com.example.piggy_bank_assistant.presentation.main_activity.CategoryDiffCallback
import com.example.piggy_bank_assistant.presentation.main_activity.MainCategoryAdapter

class TransactionAdapter: ListAdapter<Transaction, TransactionAdapter.TransactionViewHolder>(
    TransactionDiffCallback()
){

    var positiveTransactionColor: Int = DEFAULT_POSITIVE_COLOR

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val layout = R.layout.category_history_item
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            layout,
            parent,
            false
        )
        if(binding is CategoryHistoryItemBinding && viewType == POSITIVE_TRANSACTION){
            binding.tvAmount.setTextColor(positiveTransactionColor)
        }
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        var transaction = getItem(position)
        val binding = holder.binding
        if(binding is CategoryHistoryItemBinding){
            if(transaction.amount<0){
                transaction = Transaction(transaction.id, transaction.date, -transaction.amount)
            }
            binding.transaction = transaction
            binding.tvAmount.text = String.format("%.2f", transaction.amount)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val transaction = getItem(position)
        if(transaction.amount < 0){
            return NEGATIVE_TRANSACTION
        }
        return POSITIVE_TRANSACTION
    }

    companion object{
        const val POSITIVE_TRANSACTION = 100
        const val NEGATIVE_TRANSACTION = 101

        const val DEFAULT_POSITIVE_COLOR = 10000
    }

    class TransactionViewHolder(
        val binding: ViewDataBinding
    ) : RecyclerView.ViewHolder(binding.root)
}