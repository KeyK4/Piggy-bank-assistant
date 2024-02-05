package com.example.piggy_bank_assistant.domain

class GetAllCategoriesUseCase (
    private val repository: Repository
) {

    fun getAllCategories(): List<Category>{
        return repository.getAllCategories()
    }
}