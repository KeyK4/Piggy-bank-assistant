package com.example.piggy_bank_assistant.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoryHistoryUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend fun getCategoryHistory(category: Category): Flow<CategoryHistory> {
        return repository.getCategoryHistory(category)
    }
}