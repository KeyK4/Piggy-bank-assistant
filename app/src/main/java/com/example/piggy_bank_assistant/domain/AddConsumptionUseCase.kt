package com.example.piggy_bank_assistant.domain

import javax.inject.Inject

class AddConsumptionUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend fun addConsumption(categoriesProportion: List<CategoryProportion>, amount: Float){
        repository.addTransaction(categoriesProportion, amount)
    }
}