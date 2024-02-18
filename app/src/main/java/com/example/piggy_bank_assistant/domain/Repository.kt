package com.example.piggy_bank_assistant.domain

import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun addCategory(category: Category)

    suspend fun addTransaction(categoriesProportion: List<CategoryProportion>, amount: Float)

    suspend fun addPattern(pattern: Pattern)

    suspend fun getAllCategories(): Flow<List<Category>>

    suspend fun getCategoryHistory(category: Category): Flow<CategoryHistory>

    suspend fun getAllPatterns(): Flow<List<Pattern>>

    suspend fun deleteCategory(category: Category)
}