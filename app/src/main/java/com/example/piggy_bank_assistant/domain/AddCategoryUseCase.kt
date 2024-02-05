package com.example.piggy_bank_assistant.domain

class AddCategoryUseCase(
    private val repository: Repository
) {

    fun addCategory(category: Category){
        repository.addCategory(category)
    }
}