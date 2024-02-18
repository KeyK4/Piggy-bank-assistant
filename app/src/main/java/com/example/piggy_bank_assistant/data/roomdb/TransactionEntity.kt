package com.example.piggy_bank_assistant.data.roomdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.piggy_bank_assistant.data.roomdb.CategoriesAndPatternsDatabase.Companion.AMOUNT_COL
import com.example.piggy_bank_assistant.data.roomdb.CategoriesAndPatternsDatabase.Companion.TRANSACTION_TABLE_NAME
import com.example.piggy_bank_assistant.data.roomdb.CategoriesAndPatternsDatabase.Companion.CAT_ID_COL
import com.example.piggy_bank_assistant.data.roomdb.CategoriesAndPatternsDatabase.Companion.DATE_COL
import com.example.piggy_bank_assistant.data.roomdb.CategoriesAndPatternsDatabase.Companion.ID_COL
import org.jetbrains.annotations.NotNull
import java.util.Date

@Entity(tableName = TRANSACTION_TABLE_NAME, foreignKeys = [ForeignKey(
    entity = CategoryEntity::class,
    parentColumns = arrayOf(ID_COL),
    childColumns = arrayOf(CAT_ID_COL),
    onDelete = ForeignKey.CASCADE,
    onUpdate = ForeignKey.CASCADE
)], indices = [Index(value = [CAT_ID_COL])])
data class TransactionEntity (
    @PrimaryKey
    @ColumnInfo(name = ID_COL)
    val id: Int,
    @ColumnInfo(name = DATE_COL)
    val date: String,
    @ColumnInfo(name = AMOUNT_COL)
    val amount: Float,
    @ColumnInfo(name = CAT_ID_COL)
    val categoryId: Int
)
