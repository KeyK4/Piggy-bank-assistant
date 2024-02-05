package com.example.piggy_bank_assistant.data.roomdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.piggy_bank_assistant.data.roomdb.CategoriesAndPatternsDatabase.Companion.ID_COL
import com.example.piggy_bank_assistant.data.roomdb.CategoriesAndPatternsDatabase.Companion.NAME_COl
import com.example.piggy_bank_assistant.data.roomdb.CategoriesAndPatternsDatabase.Companion.PATTERNS_TABLE_NAME

@Entity(tableName = PATTERNS_TABLE_NAME)
data class PatternEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID_COL)
    val id: Int,

    @ColumnInfo(name = NAME_COl)
    val name: String,
)