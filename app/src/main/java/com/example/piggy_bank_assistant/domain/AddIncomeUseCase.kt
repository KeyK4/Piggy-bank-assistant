package com.example.piggy_bank_assistant.domain

class AddIncomeUseCase (
    private val repository: Repository
) {

    fun addIncome(pattern: Pattern, amount: Float){
        repository.addIncome(pattern, amount)
    }
}