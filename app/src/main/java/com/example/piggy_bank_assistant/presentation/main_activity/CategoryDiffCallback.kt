package com.example.piggy_bank_assistant.presentation.main_activity

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import com.example.piggy_bank_assistant.domain.Category

class CategoryDiffCallback: DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        Log.d("DiffUtil", "old = $oldItem; new = $newItem")
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }

}