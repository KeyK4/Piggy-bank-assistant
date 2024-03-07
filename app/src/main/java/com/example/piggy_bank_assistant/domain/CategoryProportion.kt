package com.example.piggy_bank_assistant.domain

data class CategoryProportion(
    val id: Int = UNDEFINED_ID,

    val category: Category,
    val proportion: Float
)
{
    companion object{
        const val UNDEFINED_ID = -1
    }
}
