package db

import Models.PatternIncome
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.TypedValue
import android.view.Gravity
import android.widget.Button
import com.example.piggy_bank_assistant.R

class PatternManager(context: Context) {
    val dbHelper = DbHelper(context)

    fun getPatterns(): Cursor?
    {
        val db = dbHelper.writableDatabase

        // below code returns a cursor to
        // read data from the database
        return db.rawQuery("SELECT * FROM ${DbHelper.PATTERNS_TABLE_NAME}", null)
    }

    fun setPattern(name:String, list: List<PatternIncome>){
        var values = ContentValues()
        values.put(DbHelper.NAME_COl, name)
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val patId: Long = db.insert(DbHelper.PATTERNS_TABLE_NAME, null, values)

        for(element in list)
        {
            values = ContentValues()
            values.put(DbHelper.CAT_ID_COL, element.Id)
            values.put(DbHelper.PERCENTAGE_COL, element.Percentage)
            values.put(DbHelper.PAT_ID_COL, patId)
            db.insert(DbHelper.PATTERN_CATS_TABLE_NAME, null, values)
        }

        db.close()

    }

    fun getPatternWithCats(id:Long): Cursor?
    {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        return db.rawQuery("SELECT cat.${DbHelper.ID_COL}, cat.${DbHelper.COLOR_COL},"+
                " cat.${DbHelper.NAME_COl}, pc.${DbHelper.PERCENTAGE_COL} FROM"+
                " ${DbHelper.PATTERN_CATS_TABLE_NAME} AS pc "+
                "JOIN ${DbHelper.CATEGORIES_TABLE_NAME} AS cat ON "+
                "pc.${DbHelper.CAT_ID_COL} = cat.${DbHelper.ID_COL}"+
                " WHERE pc.${DbHelper.PAT_ID_COL} = ${id}", null)
    }
}