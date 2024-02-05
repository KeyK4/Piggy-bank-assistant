package com.example.piggy_bank_assistant.data.roomdb

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.piggy_bank_assistant.data.db.DbHelper

@Database(entities = [CategoryEntity::class,
    PatternEntity::class,
    CategoryPatternProportionEntity::class,
    TransactionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CategoriesAndPatternsDatabase:RoomDatabase() {

    abstract fun getCategoriesAndPatternsDao(): CategoriesAndPatternsDao

    companion object{
        const val DATABASE_NAME = "categories_and_patterns.db"

        const val CATEGORIES_TABLE_NAME = "categories"

        const val TRANSACTION_TABLE_NAME = "transactions"

        const val PATTERNS_TABLE_NAME = "patterns"

        const val CATEGORY_PATTERN_PROPORTION_TABLE_NAME = "category_pattern_proportion"

        const val ID_COL = "id"

        const val NAME_COl = "name"

        const val AMOUNT_COL = "amount"

        const val COLOR_COL = "color"

        const val DATE_COL = "date"

        const val CAT_ID_COL = "cat_id"

        const val PAT_ID_COL = "pat_id"

        const val PERCENTAGE_COL = "percentage"

        val HISTORY_TRIGGER = "history_trigger"
        private var INSTANCE: CategoriesAndPatternsDatabase? = null
        private val LOCK = Any()

        fun getInstance(application: Application): CategoriesAndPatternsDatabase {
            INSTANCE?.let {
                return it
            }
            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }
                val db = Room.databaseBuilder(
                    application,
                    CategoriesAndPatternsDatabase::class.java,
                    DATABASE_NAME
                ).addCallback(object : RoomDatabase.Callback(){
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        db.execSQL("CREATE TRIGGER $HISTORY_TRIGGER BEFORE UPDATE OF $AMOUNT_COL ON $CATEGORIES_TABLE_NAME " +
                                "BEGIN " +
                                "INSERT INTO $TRANSACTION_TABLE_NAME " +
                                "($AMOUNT_COL, $DATE_COL, $CAT_ID_COL) " +
                                "VALUES (" +
                                "(SELECT NEW.$AMOUNT_COL - OLD.$AMOUNT_COL)," +
                                "(SELECT strftime('%d.%m.%Y', DATE())), " +
                                "OLD.$ID_COL " +
                                ");" +
                                "END;")
                    }
                })
                    .build()
                INSTANCE = db
                return db
            }
        }
    }

}