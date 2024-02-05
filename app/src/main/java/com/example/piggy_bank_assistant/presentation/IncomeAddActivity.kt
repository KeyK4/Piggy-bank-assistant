package com.example.piggy_bank_assistant.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.widget.*
import android.widget.LinearLayout.LayoutParams
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.core.widget.addTextChangedListener
import com.example.piggy_bank_assistant.R
import com.example.piggy_bank_assistant.data.db.CategoryManager
import com.example.piggy_bank_assistant.data.db.DbHelper
import com.example.piggy_bank_assistant.data.db.PatternManager


class IncomeAddActivity : AppCompatActivity() {

    val patternManager = PatternManager(this)
    val categoryManager = CategoryManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_income_add)
        fill_spiner()
        (findViewById(R.id.imageView) as ImageView).setOnClickListener{
            val intent = Intent(this@IncomeAddActivity, PatternAddActivity::class.java)
            startActivity(intent)
            finish()
        }
        (findViewById(R.id.editTextAmount) as EditText).addTextChangedListener {
            try{
                it.toString().toInt()
            }
            catch (e: java.lang.Exception)
            {
                return@addTextChangedListener
            }
            updateAmount()
        }
        (findViewById(R.id.income_btn2) as Button).setOnClickListener {
            try{
                (findViewById(R.id.editTextAmount) as EditText).text.toString().toInt()
            }
            catch (e: java.lang.Exception)
            {
                Toast.makeText(applicationContext, R.string.error_field_no_num, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            addIncome()
            finish()
        }

        (findViewById(R.id.consumption_btn2) as Button).setOnClickListener {
            finish()
        }
    }

    fun fill_spiner()
    {
        val patterns = mutableListOf<String>()
        val cursor = patternManager.getPatterns()
        with(cursor)
        {
            while (this?.moveToNext()!!) {
                patterns.add(cursor?.getString(cursor.getColumnIndexOrThrow(
                    com.example.piggy_bank_assistant.data.db.DbHelper.NAME_COl)).toString())
            }
        }
        val categories: Array<String> = patterns.toTypedArray()
        val spinner: Spinner = findViewById(R.id.spinner)
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.setAdapter(adapter)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                fill_scroll()
            }

        }
        spinner.setSelection(0)
    }

    fun fill_scroll()
    {
        val table: TableLayout = findViewById(R.id.tableLayout)
        table.removeAllViews()
        var id = (findViewById(R.id.spinner) as Spinner).selectedItemId + 1
        val cursor: Cursor? = patternManager.getPatternWithCats(id)
        val amount = (findViewById(R.id.editTextAmount) as EditText).text.toString().toFloat()
        with(cursor)
        {
            while (this?.moveToNext()!!) {
                id = cursor?.getString(cursor.getColumnIndexOrThrow(
                    DbHelper.ID_COL)).toString().toLong()
                val color = cursor?.getString(cursor.getColumnIndexOrThrow(
                    DbHelper.COLOR_COL)).toString().toInt()
                val name = cursor?.getString(cursor.getColumnIndexOrThrow(
                    DbHelper.NAME_COl)).toString()
                val percentage = cursor?.getString(cursor.getColumnIndexOrThrow(
                    DbHelper.PERCENTAGE_COL)).toString()

                val btn = createButton(color)
                val nameText = createTextView(name)
                nameText.updateLayoutParams<TableRow.LayoutParams> {
                    weight = 0.3f
                }
                val percentageText = createTextView(percentage+"%")
                val amountText = createTextView((amount / 100 * percentage.toFloat()).toString())
                amountText.setTextColor(ContextCompat.getColor(applicationContext, R.color.green))
                amountText.id = id.toInt()

                val tableRow = TableRow(baseContext)
                tableRow.layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT)
                tableRow.setBackgroundResource(R.drawable.table_rows)
                tableRow.addView(btn)
                tableRow.addView(nameText)
                tableRow.addView(percentageText)
                tableRow.addView(amountText)
                table.addView(tableRow)
            }
        }
    }

    fun createButton(color:Int):Button
    {
        val btn = Button(this)
        btn.setBackgroundResource(R.drawable.category_point)
        btn.background.setTint(color)
        btn.layoutParams = TableRow.LayoutParams(
            convertDpToPixel(17f, baseContext).toInt(), convertDpToPixel(17f, baseContext).toInt()
        )
        btn.updateLayoutParams<TableRow.LayoutParams> {
            setMargins(0, 34, 0, 0)
        }
        return btn
    }

    fun createTextView(text:String):TextView
    {
        val textView = TextView(this)
        textView.setText(text)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
        textView.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT)
        textView.updateLayoutParams<LayoutParams> {
            setMargins(50, 0, 0, 0)
        }
        return textView
    }


    fun updateAmount(){
        val table: TableLayout = findViewById(R.id.tableLayout)
        val amount = (findViewById(R.id.editTextAmount) as EditText).text.toString().toFloat()
        for (i in 0 until table.childCount) {
            val row = table.getChildAt(i) as TableRow
            val percentage = (row.getChildAt(2) as TextView
                    ).text.toString().replaceFirst(".$".toRegex(), "").toFloat()
            val catAmount = row.getChildAt(3) as TextView
            catAmount.setText((amount/100*percentage).toString())
        }
    }

    fun addIncome()
    {
        val table: TableLayout = findViewById(R.id.tableLayout)
        for (i in 0 until table.childCount) {
            val row = table.getChildAt(i) as TableRow
            val amountView = row.getChildAt(3) as TextView
            val amount = amountView.text.toString().toFloat()
            val id = amountView.id
            categoryManager.updateCatAmount(id, amount)
        }
    }

    fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
}

