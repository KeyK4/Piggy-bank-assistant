package com.example.piggy_bank_assistant.domain

import java.util.Date

data class Transaction(
    val id: Int,
    val date: String,
    val amount: Float
)
