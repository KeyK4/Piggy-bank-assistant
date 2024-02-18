package com.example.piggy_bank_assistant.domain

import javax.inject.Inject

class DeleteCategoryUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend fun deleteCategory(category: Category){
        repository.deleteCategory(category)
    }
}