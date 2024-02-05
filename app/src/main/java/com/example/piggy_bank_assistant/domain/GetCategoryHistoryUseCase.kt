package com.example.piggy_bank_assistant.domain

class GetCategoryHistoryUseCase (
    private val repository: Repository
) {

    fun getCategoryHistory(category: Category): CategoryHistory{
        return repository.getCategoryHistory(category)
    }
}