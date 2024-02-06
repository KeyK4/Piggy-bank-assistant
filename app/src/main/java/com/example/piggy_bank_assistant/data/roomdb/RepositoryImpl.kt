package com.example.piggy_bank_assistant.data.roomdb

import com.example.piggy_bank_assistant.domain.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RepositoryImpl(private val cnpDao: CategoriesAndPatternsDao):Repository {

    private val mapper = Mapper()

    override suspend fun addCategory(category: Category) {
        val categoryEntity = mapper.categoryToEntity(category)
        cnpDao.addCategory(categoryEntity)
    }

    override suspend fun addTransaction(
        categoriesProportion: List<CategoryProportion>,
        amount: Float
    ) {
        for(cp in categoriesProportion){
            val newAmount = cp.category.amount+amount*cp.proportion
            val categoryEntity = mapper.categoryToEntity(Category(
                id = cp.category.id,
                amount = newAmount,
                color = cp.category.color,
                name = cp.category.name
            ))
            cnpDao.updateCategory(categoryEntity)
        }
    }

    override suspend fun addPattern(pattern: Pattern) {
        cnpDao.addPattern(mapper.patternToEntity(pattern))
        val catPats = mapper.categoryProportionsEntityFromPattern(pattern)
        for(i in catPats){
            cnpDao.addCategoryPatternProportion(i)
        }
    }

    override suspend fun getAllCategories(): Flow<List<Category>> {
        return flow{
            cnpDao.getAllCategories().collect{
                val newList = mutableListOf<Category>()
                for(ce in it){
                    newList.add(mapper.entityToCategory(ce))
                }
                emit(newList)
            }
        }
    }

    override suspend fun getCategoryHistory(category: Category): Flow<CategoryHistory> {
        return flow{
            cnpDao.getCategoryTransactions(category.id).collect{
                val transactions = mutableListOf<Transaction>()
                for(i in it){
                    transactions.add(mapper.entityToTransaction(i))
                }
                emit(CategoryHistory(category, transactions))
            }
        }
    }

    override suspend fun getAllPatterns(): Flow<List<Pattern>> {
        return flow{
            val patternsRes = mutableListOf<Pattern>()
            cnpDao.getAllPatterns().collect{ patterns ->
                for(pattern in patterns){
                    val catPatsRes = mutableListOf<CategoryProportion>()
                    cnpDao.getCategoryProportionByPattern(pattern.id).collect{catPats ->
                        for(catPat in catPats){
                            cnpDao.getCategoryById(catPat.categoryId).collect{
                                catPatsRes.add(CategoryProportion(
                                    category = mapper.entityToCategory(it),
                                    id = catPat.id,
                                    proportion = catPat.percentage / 100.0.toFloat()
                                ))
                            }
                        }
                    }
                    patternsRes.add(mapper.entityToPattern(pattern, catPatsRes))
                }
            }
            emit(patternsRes)
        }
    }

}