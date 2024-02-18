package com.example.piggy_bank_assistant.domain

import javax.inject.Inject

class AddPatternUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend fun addPattern(pattern: Pattern){
        repository.addPattern(pattern)
    }
}