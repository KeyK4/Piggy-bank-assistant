package com.example.piggy_bank_assistant.presentation

import androidx.lifecycle.ViewModel
import com.example.piggy_bank_assistant.domain.AddTransactionUseCase
import com.example.piggy_bank_assistant.domain.GetCategoryHistoryUseCase
import javax.inject.Inject

class CategoryHistoryViewModel @Inject constructor(
    private val getCategoryHistoryUseCase: GetCategoryHistoryUseCase,
    private val addTransactionUseCase: AddTransactionUseCase
)
    : ViewModel() {

}