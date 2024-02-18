package com.example.piggy_bank_assistant.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCategoriesUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend fun getAllCategories(): Flow<List<Category>> {
        return repository.getAllCategories()
    }
}