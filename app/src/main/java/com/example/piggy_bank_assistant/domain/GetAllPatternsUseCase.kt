package com.example.piggy_bank_assistant.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllPatternsUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend fun getAllPatterns(): Flow<List<Pattern>>{
        return repository.getAllPatterns()
    }
}