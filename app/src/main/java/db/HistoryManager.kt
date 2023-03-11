package db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

class HistoryManager(context: Context) {

    val dbHelper = DbHelper(context)

    fun getHistory(id: Int):Cursor {
        val db = dbHelper.readableDatabase
        return db.rawQuery("SELECT * FROM ${DbHelper.CAT_HISTORY_TABLE_NAME}" +
                " WHERE ${DbHelper.CAT_ID_COL} = ${id}", null)
    }

    fun addRecord(amount:Int, date:String, catId:Int)
    {
        val db = dbHelper.writableDatabase
        val values = ContentValues()
        values.put(DbHelper.AMOUNT_COL, amount)
        values.put(DbHelper.DATE_COL, date)
        values.put(DbHelper.CAT_ID_COL, catId)
        db.insert(DbHelper.CAT_HISTORY_TABLE_NAME, null, values)
        db.close()
    }
}