package com.example.piggy_bank_assistant.data.roomdb

import com.example.piggy_bank_assistant.domain.Category
import com.example.piggy_bank_assistant.domain.CategoryHistory
import com.example.piggy_bank_assistant.domain.CategoryProportion
import com.example.piggy_bank_assistant.domain.Pattern
import com.example.piggy_bank_assistant.domain.Repository

class RepositoryImpl(private val cnpDao: CategoriesAndPatternsDao):Repository {

    private val mapper = Mapper()

    override suspend fun addCategory(category: Category) {
        val categoryEntity = mapper.categoryToEntity(category)
        cnpDao.addCategory(categoryEntity)
    }

    override suspend fun addConsumption(categoriesProportion: List<CategoryProportion>, amount: Float) {

    }

    override suspend fun addIncome(pattern: Pattern, amount: Float) {
        TODO("Not yet implemented")
    }

    override suspend fun addPattern(pattern: Pattern) {

    }

    override suspend fun getAllCategories(): List<Category> {
        TODO("Not yet implemented")
    }

    override suspend fun getCategoryHistory(category: Category): CategoryHistory {
        TODO("Not yet implemented")
    }
}