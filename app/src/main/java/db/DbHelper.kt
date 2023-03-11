package db

import android.content.ContentValues
import android.content.Context
import android.content.res.Resources
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.content.ContextCompat
import com.example.piggy_bank_assistant.R
import com.example.piggy_bank_assistant.financialReserveId


class DbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val context_: Context
    init{
        context_ = context
    }
    // below is the method for creating a database by a sqlite query
    override fun onCreate(db: SQLiteDatabase) {
        // below is a sqlite query, where column names
        // along with their data types is given
        var query = ("CREATE TABLE $CATEGORIES_TABLE_NAME(" +
                "$ID_COL INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$NAME_COl TEXT UNIQUE NOT NULL," +
                "$AMOUNT_COL REAL NOT NULL, " +
                "$COLOR_COL TEXT NOT NULL" + ")")

        // we are calling sqlite
        // method for executing our query
        db.execSQL(query)

        query = "CREATE TABLE $CAT_HISTORY_TABLE_NAME(" +
                "$ID_COL INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$AMOUNT_COL REAL NOT NULL, " +
                "$DATE_COL TEXT NOT NULL, " +
                "$CAT_ID_COL INTEGER  NOT NULL, " +
                "FOREIGN KEY ($CAT_ID_COL) REFERENCES $CATEGORIES_TABLE_NAME($ID_COL)"+
                " ON DELETE CASCADE" + ")"
        db.execSQL(query)

        query = "CREATE TABLE $PATTERNS_TABLE_NAME(" +
                "$ID_COL INTEGER PRIMARY KEY, " +
                "$NAME_COl TEXT UNIQUE NOT NULL" + ")"
        db.execSQL(query)

        query = "CREATE TABLE $PATTERN_CATS_TABLE_NAME(" +
                "$ID_COL INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$CAT_ID_COL INTEGER NOT NULL, " +
                "$PAT_ID_COL INTEGER NOT NULL, " +
                "$PERCENTAGE_COL INTEGER NOT NULL, " +
                "FOREIGN KEY ($CAT_ID_COL) REFERENCES $CATEGORIES_TABLE_NAME($ID_COL)"+
                " ON DELETE CASCADE, " +
                "FOREIGN KEY ($PAT_ID_COL) REFERENCES ${PATTERNS_TABLE_NAME}($ID_COL)"+
                " ON DELETE CASCADE" + ")"
        db.execSQL(query)

            db.execSQL("CREATE TRIGGER $HISTORY_TRIGGER BEFORE UPDATE OF $AMOUNT_COL ON $CATEGORIES_TABLE_NAME " +
                    "BEGIN " +
                    "INSERT INTO $CAT_HISTORY_TABLE_NAME " +
                    "($AMOUNT_COL, $DATE_COL, $CAT_ID_COL) " +
                    "VALUES (" +
                    "(SELECT NEW.$AMOUNT_COL - OLD.$AMOUNT_COL)," +
                    "(SELECT strftime('%d.%m.%Y', DATE())), " +
                    "OLD.$ID_COL " +
                    ");" +
                    "END;")

        //создание категории "Финансовая подушка"
        val values = ContentValues()
        val res: Resources = context_.getResources()
        values.put(NAME_COl, res.getString(R.string.financial_reserve))
        values.put(AMOUNT_COL, 0)
        values.put(COLOR_COL, ContextCompat.getColor(context_, R.color.light_purple).toString())
        financialReserveId = db.insert(CATEGORIES_TABLE_NAME, null, values)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        // this method is to check if table already exists
        db.execSQL("DROP TABLE IF EXISTS $CATEGORIES_TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $CAT_HISTORY_TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $PATTERNS_TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $PATTERN_CATS_TABLE_NAME")
        db.execSQL("DROP TRIGGER IF EXISTS $HISTORY_TRIGGER")
        onCreate(db)
    }

    companion object{
        // here we have defined variables for our database

        // below is variable for database name
        val DATABASE_NAME = "PiBA.db"

        // below is the variable for database version
        private val DATABASE_VERSION = 17

        // below is the variable for table name
        val CATEGORIES_TABLE_NAME = "categories"

        // below is the variable for table name
        val CAT_HISTORY_TABLE_NAME = "cat_history"

        // below is the variable for table name
        val PATTERNS_TABLE_NAME = "patterns"

        // below is the variable for table name
        val PATTERN_CATS_TABLE_NAME = "pattern_cats"

        // below is the variable for id column
        val ID_COL = "id"

        // below is the variable for name column
        val NAME_COl = "name"

        // below is the variable for age column
        val AMOUNT_COL = "amount"

        // below is the variable for age column
        val COLOR_COL = "color"

        // below is the variable for age column
        val DATE_COL = "date"

        // below is the variable for age column
        val CAT_ID_COL = "cat_id"

        // below is the variable for age column
        val PAT_ID_COL = "pat_id"

        // below is the variable for age column
        val PERCENTAGE_COL = "percentage"

        val HISTORY_TRIGGER = "history_trigger"
    }
}