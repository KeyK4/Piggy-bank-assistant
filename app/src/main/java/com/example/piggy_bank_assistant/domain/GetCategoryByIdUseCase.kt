package com.example.piggy_bank_assistant.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoryByIdUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend fun getCategoryById(id: Int): Flow<Category> {
        return repository.getCategoryById(id)
    }
}