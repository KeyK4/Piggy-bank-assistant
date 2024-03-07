package com.example.piggy_bank_assistant.presentation.category_add_activity

import androidx.lifecycle.ViewModel
import com.example.piggy_bank_assistant.domain.AddCategoryUseCase
import com.example.piggy_bank_assistant.domain.Category
import javax.inject.Inject

class CategoryAddViewModel @Inject constructor(
    private val addCategoryUseCase: AddCategoryUseCase
): ViewModel() {

    suspend fun addCategory(category: Category){
        addCategoryUseCase.addCategory(category = category)
    }
}