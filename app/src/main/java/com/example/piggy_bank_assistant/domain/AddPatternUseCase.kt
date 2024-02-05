package com.example.piggy_bank_assistant.domain

class AddPatternUseCase (
    private val repository: Repository
) {

    fun addPattern(pattern: Pattern){
        repository.addPattern(pattern)
    }
}