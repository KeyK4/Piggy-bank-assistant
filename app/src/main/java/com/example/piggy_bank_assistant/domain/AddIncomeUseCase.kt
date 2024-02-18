package com.example.piggy_bank_assistant.domain

import javax.inject.Inject

class AddIncomeUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend fun addIncome(pattern: Pattern, amount: Float){
        repository.addTransaction(pattern.categoriesProportions, amount)
    }
}