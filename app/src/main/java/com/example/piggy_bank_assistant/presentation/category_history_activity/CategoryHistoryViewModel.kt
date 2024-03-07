package com.example.piggy_bank_assistant.presentation.category_history_activity

import androidx.lifecycle.ViewModel
import com.example.piggy_bank_assistant.domain.AddTransactionUseCase
import com.example.piggy_bank_assistant.domain.Category
import com.example.piggy_bank_assistant.domain.CategoryHistory
import com.example.piggy_bank_assistant.domain.CategoryProportion
import com.example.piggy_bank_assistant.domain.GetCategoryByIdUseCase
import com.example.piggy_bank_assistant.domain.GetCategoryHistoryUseCase
import com.example.piggy_bank_assistant.domain.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CategoryHistoryViewModel @Inject constructor(
    private val getCategoryHistoryUseCase: GetCategoryHistoryUseCase,
    private val addTransactionUseCase: AddTransactionUseCase,
    private val getCategoryByIdUseCase: GetCategoryByIdUseCase
) : ViewModel() {
    suspend fun getCategoryHistory(category: Category): Flow<CategoryHistory> {
        return getCategoryHistoryUseCase.getCategoryHistory(category = category)
    }

    suspend fun addTransaction(categoryProportions: List<CategoryProportion>, amount: Float){
        addTransactionUseCase.addTransaction(categoryProportions, amount)
    }

    suspend fun getCategoryById(id: Int): Flow<Category> {
        return getCategoryByIdUseCase.getCategoryById(id)
    }
}