package com.example.piggy_bank_assistant.domain

data class Pattern (
    val id: Int,
    val name: String,
    val categoriesProportions: List<CategoryProportion>
)