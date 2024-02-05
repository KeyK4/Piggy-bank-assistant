package com.example.piggy_bank_assistant.data.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.piggy_bank_assistant.data.roomdb.CategoriesAndPatternsDatabase.Companion.CATEGORIES_TABLE_NAME
import com.example.piggy_bank_assistant.data.roomdb.CategoriesAndPatternsDatabase.Companion.CATEGORY_PATTERN_PROPORTION_TABLE_NAME
import com.example.piggy_bank_assistant.data.roomdb.CategoriesAndPatternsDatabase.Companion.CAT_ID_COL
import com.example.piggy_bank_assistant.data.roomdb.CategoriesAndPatternsDatabase.Companion.PATTERNS_TABLE_NAME
import com.example.piggy_bank_assistant.data.roomdb.CategoriesAndPatternsDatabase.Companion.PAT_ID_COL
import com.example.piggy_bank_assistant.data.roomdb.CategoriesAndPatternsDatabase.Companion.TRANSACTION_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriesAndPatternsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCategory(categoryEntity: CategoryEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPattern(patternEntity: PatternEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCategoryPatternProportion(categoryPatternProportionEntity: CategoryPatternProportionEntity)

    @Update
    suspend fun updateCategory(categoryEntity: CategoryEntity)

    @Query("SELECT * FROM $CATEGORIES_TABLE_NAME")
    suspend fun getAllCategories(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM $PATTERNS_TABLE_NAME")
    suspend fun getAllPatterns(): Flow<List<PatternEntity>>

    @Query("SELECT * FROM $CATEGORY_PATTERN_PROPORTION_TABLE_NAME WHERE $PAT_ID_COL = :id")
    suspend fun getCategoryProportionByPattern(id: Int): Flow<List<CategoryPatternProportionEntity>>

    @Query("SELECT * FROM $TRANSACTION_TABLE_NAME WHERE $CAT_ID_COL = :id")
    suspend fun getCategoryTransactions(id: Int): Flow<List<TransactionEntity>>
}