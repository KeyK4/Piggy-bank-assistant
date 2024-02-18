package com.example.piggy_bank_assistant.presentation.main_activity

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.piggy_bank_assistant.R
import com.example.piggy_bank_assistant.databinding.MainCategoryItemBinding
import com.example.piggy_bank_assistant.domain.Category

class MainCategoryAdapter: ListAdapter<Category, MainCategoryAdapter.MainCategoryViewHolder>(
    CategoryDiffCallback()
){

    var onCategoryClickListener: ((Category)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainCategoryViewHolder {
        val layout = R.layout.main_category_item
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            layout,
            parent,
            false
        )
        return MainCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainCategoryViewHolder, position: Int) {
        val binding = holder.binding
        if(binding is MainCategoryItemBinding){
            Log.d("MainCategoryAdapter", "position = $position")
            val category = getItem(position)
            binding.category = category
            binding.root.setOnClickListener{
                onCategoryClickListener?.invoke(category)
            }
        }

    }

    class MainCategoryViewHolder(
        val binding: ViewDataBinding
    ) : RecyclerView.ViewHolder(binding.root)
}