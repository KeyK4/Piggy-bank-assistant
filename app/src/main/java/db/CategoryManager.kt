package db

import android.content.ContentValues
import android.content.Context
import android.content.res.Resources
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.core.content.ContextCompat
import com.example.piggy_bank_assistant.R
import com.example.piggy_bank_assistant.financialReserveId
import kotlinx.coroutines.test.withTestContext

class CategoryManager (context_: Context)
{
    val context: Context = context_
    val dbHelper = DbHelper(context_)

    // This method is for adding data in our database
    fun addCat(name : String, amount : Int, color: String ){

        // below we are creating
        // a content values variable
        val values = ContentValues()

        // we are inserting our values
        // in the form of key-value pair
        values.put(DbHelper.NAME_COl, name)
        values.put(DbHelper.AMOUNT_COL, amount)
        values.put(DbHelper.COLOR_COL, color)

        // here we are creating a
        // writable variable of
        // our database as we want to
        // insert value in our database
        val db: SQLiteDatabase = dbHelper.writableDatabase

        // all values are inserted into database
        db.insert(DbHelper.CATEGORIES_TABLE_NAME, null, values)

        // at last we are
        // closing our database
        db.close()
    }

    // below method is to get
    // all data from our database
    fun getCats(): Cursor? {

        // here we are creating a readable
        // variable of our database
        // as we want to read value from it
        val db = dbHelper.readableDatabase

        // below code returns a cursor to
        // read data from the database
        return db.rawQuery("SELECT * FROM ${DbHelper.CATEGORIES_TABLE_NAME}", null)

    }


    fun updateCatAmount(id: Int, amount: Float)
    {
        val db: SQLiteDatabase = dbHelper.writableDatabase

        val cursor = db.rawQuery("SELECT ${DbHelper.AMOUNT_COL}" +
                " FROM ${DbHelper.CATEGORIES_TABLE_NAME}" +
                " WHERE ${DbHelper.ID_COL} = ${id}",
            null)
        var oldAmount = 0f
        with(cursor!!)
        {
            while (this.moveToNext()) {
                oldAmount = cursor.getString(cursor.getColumnIndexOrThrow(
                    DbHelper.AMOUNT_COL)).toFloat()
            }
        }
        val values = ContentValues()
        values.put(DbHelper.AMOUNT_COL, amount+oldAmount)
        db.update(DbHelper.CATEGORIES_TABLE_NAME, values,
            "${DbHelper.ID_COL}=$id", arrayOf())

        db.close()
    }

    fun getCatById(id:Int):Cursor
    {
        val db = dbHelper.readableDatabase
        return db.rawQuery("SELECT * FROM ${DbHelper.CATEGORIES_TABLE_NAME} WHERE ${DbHelper.ID_COL} = ${id}", null)
    }

    fun wipeData(){
        val db = dbHelper.writableDatabase
        db.execSQL("DROP TABLE IF EXISTS ${DbHelper.CATEGORIES_TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${DbHelper.CAT_HISTORY_TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${DbHelper.PATTERNS_TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${DbHelper.PATTERN_CATS_TABLE_NAME}")
        db.execSQL("DROP TRIGGER IF EXISTS ${DbHelper.HISTORY_TRIGGER}")

        var query = ("CREATE TABLE ${DbHelper.CATEGORIES_TABLE_NAME}(" +
                "${DbHelper.ID_COL} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${DbHelper.NAME_COl} TEXT UNIQUE NOT NULL," +
                "${DbHelper.AMOUNT_COL} REAL NOT NULL, " +
                "${DbHelper.COLOR_COL} TEXT NOT NULL" + ")")
        db.execSQL(query)

        query = "CREATE TABLE ${DbHelper.CAT_HISTORY_TABLE_NAME}(" +
                "${DbHelper.ID_COL} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${DbHelper.AMOUNT_COL} REAL NOT NULL, " +
                "${DbHelper.DATE_COL} TEXT NOT NULL, " +
                "${DbHelper.CAT_ID_COL} INTEGER  NOT NULL, " +
                "FOREIGN KEY (${DbHelper.CAT_ID_COL}) REFERENCES ${DbHelper.CATEGORIES_TABLE_NAME}(${DbHelper.ID_COL})"+
                " ON DELETE CASCADE" + ")"
        db.execSQL(query)

        query = "CREATE TABLE ${DbHelper.PATTERNS_TABLE_NAME}(" +
                "${DbHelper.ID_COL} INTEGER PRIMARY KEY, " +
                "${DbHelper.NAME_COl} TEXT UNIQUE NOT NULL" + ")"
        db.execSQL(query)

        query = "CREATE TABLE ${DbHelper.PATTERN_CATS_TABLE_NAME}(" +
                "${DbHelper.ID_COL} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${DbHelper.CAT_ID_COL} INTEGER NOT NULL, " +
                "${DbHelper.PAT_ID_COL} INTEGER NOT NULL, " +
                "${DbHelper.PERCENTAGE_COL} INTEGER NOT NULL, " +
                "FOREIGN KEY (${DbHelper.CAT_ID_COL}) REFERENCES ${DbHelper.CATEGORIES_TABLE_NAME}(${DbHelper.ID_COL})"+
                " ON DELETE CASCADE, " +
                "FOREIGN KEY (${DbHelper.PAT_ID_COL}) REFERENCES ${DbHelper.PATTERNS_TABLE_NAME}(${DbHelper.ID_COL})"+
                " ON DELETE CASCADE" + ")"
        db.execSQL(query)

        db.execSQL("CREATE TRIGGER ${DbHelper.HISTORY_TRIGGER} BEFORE UPDATE OF ${DbHelper.AMOUNT_COL} ON ${DbHelper.CATEGORIES_TABLE_NAME} " +
                "BEGIN " +
                "INSERT INTO ${DbHelper.CAT_HISTORY_TABLE_NAME} " +
                "(${DbHelper.AMOUNT_COL}, ${DbHelper.DATE_COL}, ${DbHelper.CAT_ID_COL}) " +
                "VALUES (" +
                "(SELECT NEW.${DbHelper.AMOUNT_COL} - OLD.${DbHelper.AMOUNT_COL})," +
                "(SELECT strftime('%d.%m.%Y', DATE())), " +
                "OLD.${DbHelper.ID_COL} " +
                ");" +
                "END;")



        val values = ContentValues()
        val res: Resources = context.getResources()
        values.put(DbHelper.NAME_COl, res.getString(R.string.financial_reserve))
        values.put(DbHelper.AMOUNT_COL, 0)
        values.put(DbHelper.COLOR_COL, ContextCompat.getColor(context, R.color.light_purple).toString())
        financialReserveId = db.insert(DbHelper.CATEGORIES_TABLE_NAME, null, values)
    }
}