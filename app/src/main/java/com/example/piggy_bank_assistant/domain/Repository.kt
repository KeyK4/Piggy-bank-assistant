package com.example.piggy_bank_assistant.domain

interface Repository {
    suspend fun addCategory(category: Category)

    suspend fun addConsumption(categoriesProportion: List<CategoryProportion>, amount: Float)

    suspend fun addIncome(pattern: Pattern, amount: Float)

    suspend fun addPattern(pattern: Pattern)

    suspend fun getAllCategories(): List<Category>

    suspend fun getCategoryHistory(category: Category): CategoryHistory
}