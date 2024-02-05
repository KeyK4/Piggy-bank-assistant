package com.example.piggy_bank_assistant.domain

data class CategoryHistory(
    val category: Category,
    val transactions: List<Transaction>
)
