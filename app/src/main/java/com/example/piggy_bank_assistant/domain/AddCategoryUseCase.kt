package com.example.piggy_bank_assistant.domain

import javax.inject.Inject

class AddCategoryUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend fun addCategory(category: Category){
        repository.addCategory(category)
    }
}