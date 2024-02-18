package com.example.piggy_bank_assistant.data.roomdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.piggy_bank_assistant.data.roomdb.CategoriesAndPatternsDatabase.Companion.CAT_ID_COL
import com.example.piggy_bank_assistant.data.roomdb.CategoriesAndPatternsDatabase.Companion.ID_COL
import com.example.piggy_bank_assistant.data.roomdb.CategoriesAndPatternsDatabase.Companion.CATEGORY_PATTERN_PROPORTION_TABLE_NAME
import com.example.piggy_bank_assistant.data.roomdb.CategoriesAndPatternsDatabase.Companion.PAT_ID_COL
import com.example.piggy_bank_assistant.data.roomdb.CategoriesAndPatternsDatabase.Companion.PERCENTAGE_COL

@Entity(tableName = CATEGORY_PATTERN_PROPORTION_TABLE_NAME, foreignKeys = [ForeignKey(
    CategoryEntity::class,
    parentColumns = arrayOf(ID_COL),
    childColumns = arrayOf(CAT_ID_COL),
    onDelete = ForeignKey.CASCADE,
    onUpdate = ForeignKey.CASCADE
),ForeignKey(
    PatternEntity::class,
    parentColumns = arrayOf(ID_COL),
    childColumns = arrayOf(PAT_ID_COL),
    onDelete = ForeignKey.CASCADE,
    onUpdate = ForeignKey.CASCADE
)
], indices = [Index(value = [CAT_ID_COL]), Index(value = [PAT_ID_COL])])
data class CategoryPatternProportionEntity(
    @PrimaryKey
    @ColumnInfo(name = ID_COL)
    val id: Int,

    @ColumnInfo(name = CAT_ID_COL)
    val categoryId: Int,

    @ColumnInfo(name = PAT_ID_COL)
    val patternId: Int,

    @ColumnInfo(name = PERCENTAGE_COL)
    val percentage: Int
)