package com.example.piggy_bank_assistant.data.roomdb

import com.example.piggy_bank_assistant.domain.Category
import com.example.piggy_bank_assistant.domain.CategoryProportion
import com.example.piggy_bank_assistant.domain.Pattern
import com.example.piggy_bank_assistant.domain.Transaction

class Mapper {
    fun entityToCategory(entity: CategoryEntity): Category{
        return Category(
            id = entity.id,
            name = entity.name,
            amount = entity.amount,
            color = entity.color
        )
    }

    fun categoryToEntity(category: Category): CategoryEntity{
        return CategoryEntity(
            id = category.id,
            name = category.name,
            amount = category.amount,
            color = category.color
        )
    }

    fun entityToPattern(entity: PatternEntity, categoryProportions: List<CategoryProportion>): Pattern {
        return Pattern(
            id = entity.id,
            name = entity.name,
            categoriesProportions = categoryProportions
        )
    }

    fun entitiesToProportions(
        entities: List<CategoryPatternProportionEntity>,
        categories: List<CategoryEntity>,
    ): List<CategoryProportion>{
        val result = mutableListOf<CategoryProportion>()
        for(i in 0..entities.size){
            result.add(CategoryProportion(
                id = entities[i].id,
                category = entityToCategory(categories[i]),
                proportion = (entities[i].percentage) / 100.0.toFloat()
            ))
        }
        return result
    }

    fun patternToEntity(pattern: Pattern): PatternEntity{
        return PatternEntity(id = pattern.id, name = pattern.name)
    }

    fun categoryProportionsEntityFromPattern(pattern: Pattern): List<CategoryPatternProportionEntity>{
        val result = mutableListOf<CategoryPatternProportionEntity>()
        for(i in pattern.categoriesProportions){
            result.add(CategoryPatternProportionEntity(
                id = i.id,
                categoryId = i.category.id,
                patternId = pattern.id,
                percentage = (i.proportion*100).toInt()
            ))
        }
        return result
    }

    fun entityToTransaction(entity: TransactionEntity): Transaction{
        return Transaction(
            amount = entity.amount,
            date = entity.date
        )
    }
}