package com.example.piggy_bank_assistant.presentation.main_activity

import androidx.lifecycle.ViewModel
import com.example.piggy_bank_assistant.domain.Category
import com.example.piggy_bank_assistant.domain.DeleteCategoryUseCase
import com.example.piggy_bank_assistant.domain.GetAllCategoriesUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase
): ViewModel() {
    suspend fun getAllCategories(): Flow<List<Category>>{
        return getAllCategoriesUseCase.getAllCategories()
    }

    suspend fun deleteCategory(category: Category){
        deleteCategoryUseCase.deleteCategory(category)
    }
}