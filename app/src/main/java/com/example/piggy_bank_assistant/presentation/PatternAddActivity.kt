package com.example.piggy_bank_assistant.presentation

import Models.PatternIncome
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.LinearLayout.LayoutParams
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import com.example.piggy_bank_assistant.R
import com.example.piggy_bank_assistant.financialReserveId
import com.example.piggy_bank_assistant.data.db.CategoryManager
import com.example.piggy_bank_assistant.data.db.DbHelper
import com.example.piggy_bank_assistant.data.db.PatternManager


class PatternAddActivity : AppCompatActivity() {

    val categoryManager = CategoryManager(this)
    val patternManager = PatternManager(this)
    val categories = mutableListOf<String>()
    val catColors = mutableListOf<Int>()

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                v.clearFocus()
                val imm = baseContext.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
            }
        }
        return super.dispatchTouchEvent(event)
    }
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pattern_add)

        val cursor = categoryManager.getCats()
        with(cursor)
        {
            while (this?.moveToNext()!!) {
                val id = cursor?.getString(cursor.getColumnIndexOrThrow(
                    DbHelper.ID_COL)).toString().toLong()
                if (id == financialReserveId)
                    continue
                categories.add(cursor?.getString(cursor.getColumnIndexOrThrow(
                    DbHelper.NAME_COl)).toString())
                catColors.add(cursor?.getString(cursor.getColumnIndexOrThrow(
                    DbHelper.COLOR_COL)).toString().toInt())
            }
        }

        val categoryAddButton: Button = findViewById(R.id.add_cat)
        categoryAddButton.setOnClickListener {
            addButtonClicked()
        }

        val patternCreateButton: Button = findViewById(R.id.add_btn2)
        patternCreateButton.setOnClickListener{
            createPattern()
            val intent = Intent(this@PatternAddActivity, IncomeAddActivity::class.java)
            startActivity(intent)
            finish()
        }

        val cancelButton: Button = findViewById(R.id.back_btn2)
        cancelButton.setOnClickListener {
            val intent = Intent(this@PatternAddActivity, IncomeAddActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    val colors = listOf(
        R.color.light_brown, R.color.light_red, R.color.light_red2,
        R.color.light_orange, R.color.light_yellow, R.color.light_green2, R.color.light_blue,
        R.color.light_blue2, R.color.light_purple, R.color.light_violet,
    )

    fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun refreshDiagram() {
        val table: TableLayout = findViewById(R.id.tableLayout)
        val linearMaster: LinearLayout = findViewById(R.id.linear_master)
        linearMaster.removeAllViews()
        var button: Button = findViewById(R.id.button_point)
        var color = button.currentTextColor
        val text: TextView = findViewById(R.id.financial_reserve_cat_enter_box2)
        var weight_ = text.text.toString().toFloat()
        var newLayout = LinearLayout(this)
        newLayout.layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.MATCH_PARENT
        )
        newLayout.updateLayoutParams<LayoutParams> {
            weight = weight_
        }
        newLayout.setBackgroundResource(R.drawable.diagram_lines)
        newLayout.background.setTint(color)
        linearMaster.addView(newLayout)

        for (i in 1 until table.childCount) {
            val row = table.getChildAt(i) as TableRow
            val parentLayout = row.getChildAt(2) as LinearLayout
            val editText = parentLayout.getChildAt(0) as EditText
            weight_ = editText.text.toString().toFloat()
            button = row.getChildAt(0) as Button
            color = button.currentTextColor

            newLayout = LinearLayout(this)
            newLayout.layoutParams = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT
            )
            newLayout.updateLayoutParams<LayoutParams> {
                weight = weight_
            }
            newLayout.setBackgroundResource(R.drawable.diagram_lines)
            newLayout.background.setTint(color)

//            val gradientDrawable = GradientDrawable()
//            gradientDrawable.cornerRadii = floatArrayOf(50f, 50f, 50f, 50f, 50f, 50f, 50f, 50f)
            linearMaster.addView(newLayout)
            //linearMaster.background = gradientDrawable

            linearMaster.invalidate()
        }
    }

    fun getSumPercentages(): Int
    {
        var sum = 0
        val table: TableLayout = findViewById(R.id.tableLayout)
        for (i in 1 until table.childCount) {
            val row = table.getChildAt(i) as TableRow
            val parentLayout = row.getChildAt(2) as LinearLayout
            val editText = parentLayout.getChildAt(0) as EditText
            sum += editText.text.toString().toInt()
        }
        return sum
    }

    fun setFinancialReservePercentage()
    {
        val textView: TextView = findViewById(R.id.financial_reserve_cat_enter_box2)
        textView.setText("${100 - getSumPercentages()}")
    }

    fun addButtonClicked()
    {
        val categoriesArray: Array<String> = categories.toTypedArray()
        val table: TableLayout = findViewById(R.id.tableLayout)

        if(categories.count() == 0)
        {
            Toast.makeText(applicationContext, R.string.error_no_category, Toast.LENGTH_SHORT).show()
            return
        }
        val row = TableRow(this)
        val rowparams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        row.layoutParams = rowparams
        row.setBackgroundResource(R.drawable.table_rows)


        val btn = Button(this)
        btn.setBackgroundResource(R.drawable.category_point)
        val color = ContextCompat.getColor(applicationContext, colors.random())
        btn.background.setTint(color)
        btn.setTextColor(color)
        btn.layoutParams = TableRow.LayoutParams(
            10, convertDpToPixel(17f, baseContext).toInt()
        )
        btn.updateLayoutParams<TableRow.LayoutParams> {
            setMargins(0, 34, 0, 0)
        }
        btn.setOnClickListener {
            val color_ = ContextCompat.getColor(applicationContext, colors.random())
            btn.background.setTint(color_)
            btn.setTextColor(color_)
            refreshDiagram()
        }


        val spinner = Spinner(this)
        spinner.setBackgroundResource(R.drawable.add_pattern_input)
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                categoriesArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.setAdapter(adapter)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val color_ = catColors[position]
                btn.background.setTint(color_)
                btn.setTextColor(color_)
                refreshDiagram()
            }

        }
        spinner.setSelection(0)
        val inplayout = LinearLayout(this)
        inplayout.setBackgroundResource(R.drawable.input_linear_layout)
        inplayout.layoutParams = rowparams
        spinner.layoutParams = rowparams
        spinner.updateLayoutParams<LayoutParams> {
            weight = 1f
            width = 220
        }
        inplayout.addView(spinner)


        val inp2 = EditText(this)
        inp2.setRawInputType(InputType.TYPE_CLASS_NUMBER)
        inp2.setText("0")
        inp2.setFilters(arrayOf<InputFilter>(LengthFilter(3)))
        val inp2layout = LinearLayout(this)
        inp2layout.setBackgroundResource(R.drawable.input_linear_layout)
        inp2layout.layoutParams = rowparams
        inp2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
        inp2.setBackgroundResource(R.drawable.add_pattern_input)
        inp2.layoutParams = rowparams
        inp2.updateLayoutParams<LayoutParams> {
            weight = 1f
        }
        inp2.setOnFocusChangeListener { view, b ->
            if (b == false)
            {
                var value: Int
                try {
                    value = inp2.text.toString().toInt()
                }
                catch (e: java.lang.Exception){
                    inp2.setText("0")
                    return@setOnFocusChangeListener
                }
                val min = 0
                val frEditText: TextView = findViewById(R.id.financial_reserve_cat_enter_box2)
                val max = frEditText.text.toString().toInt() +
                        inp2.id
                if (value > max)
                    inp2.setText("$max")
                if (value < min)
                    inp2.setText("$min")

                setFinancialReservePercentage()
                refreshDiagram()
            }
            else
                inp2.id = inp2.text.toString().toInt()
        }
        inp2layout.addView(inp2)


        row.addView(btn, 0)
        row.addView(inplayout, 1)
        row.addView(inp2layout, 2)
        table.addView(row, 1)
    }

    fun createPattern()
    {
        val table: TableLayout = findViewById(R.id.tableLayout)
        val categoriesWithAmount = mutableListOf<PatternIncome>()
        val finReservePercentage = (findViewById(R.id.financial_reserve_cat_enter_box2)
                as TextView).text.toString().toInt()
        categoriesWithAmount.add(PatternIncome(financialReserveId, finReservePercentage))

        for (i in 1 until table.childCount) {
            val row = table.getChildAt(i) as TableRow
            val percentage = ((row.getChildAt(2) as LinearLayout).getChildAt(0)
                    as EditText).text.toString().toInt()
            var catId = ((row.getChildAt(1) as LinearLayout).getChildAt(0)
                    as Spinner).selectedItemId+1
            if (catId >= financialReserveId)
                catId++
            categoriesWithAmount.add(PatternIncome(catId, percentage))
        }
        val name = (findViewById(R.id.name_enter_box) as EditText).text.toString()
        patternManager.setPattern(name, categoriesWithAmount)

    }

    fun checkInputAndRefresh(view: EditText)
    {

    }
}