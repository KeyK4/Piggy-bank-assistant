package com.example.piggy_bank_assistant.domain

class AddConsumptionUseCase (
    private val repository: Repository
) {

    fun addConsumption(categoriesProportion: List<CategoryProportion>, amount: Float){
        repository.addConsumption(categoriesProportion, amount)
    }
}