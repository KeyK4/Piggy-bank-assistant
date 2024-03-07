package com.example.piggy_bank_assistant.presentation.category_history_activity

import androidx.recyclerview.widget.DiffUtil
import com.example.piggy_bank_assistant.domain.Category
import com.example.piggy_bank_assistant.domain.Transaction

class TransactionDiffCallback: DiffUtil.ItemCallback<Transaction>() {
    override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem == newItem
    }
}