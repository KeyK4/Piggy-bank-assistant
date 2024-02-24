package com.example.piggy_bank_assistant.domain

import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend fun addTransaction(categoriesProportion: List<CategoryProportion>, amount: Float){
        repository.addTransaction(categoriesProportion, amount)
    }
}