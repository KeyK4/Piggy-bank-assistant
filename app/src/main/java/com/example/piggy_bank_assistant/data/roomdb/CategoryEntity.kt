package com.example.piggy_bank_assistant.data.roomdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.piggy_bank_assistant.data.roomdb.CategoriesAndPatternsDatabase.Companion.AMOUNT_COL
import com.example.piggy_bank_assistant.data.roomdb.CategoriesAndPatternsDatabase.Companion.CATEGORIES_TABLE_NAME
import com.example.piggy_bank_assistant.data.roomdb.CategoriesAndPatternsDatabase.Companion.COLOR_COL
import com.example.piggy_bank_assistant.data.roomdb.CategoriesAndPatternsDatabase.Companion.ID_COL
import com.example.piggy_bank_assistant.data.roomdb.CategoriesAndPatternsDatabase.Companion.NAME_COl

@Entity(tableName = CATEGORIES_TABLE_NAME)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID_COL)
    val id: Int,

    @ColumnInfo(name = NAME_COl)
    val name: String,

    @ColumnInfo(name = AMOUNT_COL)
    val amount: Float,

    @ColumnInfo(name = COLOR_COL)
    val color: Int
)