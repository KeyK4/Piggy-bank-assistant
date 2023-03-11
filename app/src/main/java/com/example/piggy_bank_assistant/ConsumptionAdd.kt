package com.example.piggy_bank_assistant

import Models.PatternIncome
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.LinearLayout.LayoutParams
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.core.widget.addTextChangedListener
import db.CategoryManager
import db.PatternManager


class ConsumptionAdd : AppCompatActivity() {

    val categoryManager = CategoryManager(this)
    val categories = mutableListOf<String>()
    val catColors = mutableListOf<Int>()
    var sumPercent = 0

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
        setContentView(R.layout.activity_consumption_add)

        val cursor = categoryManager.getCats()
        with(cursor)
        {
            while (this?.moveToNext()!!) {
                categories.add(cursor?.getString(cursor.getColumnIndexOrThrow(
                    db.DbHelper.NAME_COl)).toString())
                catColors.add(cursor?.getString(cursor.getColumnIndexOrThrow(
                    db.DbHelper.COLOR_COL)).toString().toInt())
            }
        }

        val categoryAddButton: Button = findViewById(R.id.add_cat)
        categoryAddButton.setOnClickListener {
            addButtonClicked()
        }

        val patternCreateButton: Button = findViewById(R.id.add_btn2)
        patternCreateButton.setOnClickListener{
            try{
                (findViewById(R.id.nominal_enter_box) as EditText).text.toString().toFloat()
            }
            catch (e: java.lang.Exception)
            {
                Toast.makeText(applicationContext, R.string.error_field_no_num, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(getSumPercentages()!=100)
            {
                Toast.makeText(applicationContext, R.string.error_sum_percentages, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            confirmConsumption()
            val intent = Intent(this@ConsumptionAdd, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val cancelButton: Button = findViewById(R.id.back_btn2)
        cancelButton.setOnClickListener {
            val intent = Intent(this@ConsumptionAdd, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        (findViewById(R.id.nominal_enter_box) as EditText).addTextChangedListener {
            try{
                it.toString().toFloat()
            }
            catch (e: java.lang.Exception)
            {
                return@addTextChangedListener
            }
            updateAmounts()
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

        for (i in 0 until table.childCount) {
            val row = table.getChildAt(i) as TableRow
            val editText = row.getChildAt(2) as EditText
            val weight_ = editText.text.toString().toFloat()
            val button = row.getChildAt(0) as Button
            val color = button.currentTextColor

            val newLayout = LinearLayout(this)
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
        for (i in 0 until table.childCount) {
            val row = table.getChildAt(i) as TableRow
            val editText = row.getChildAt(2) as EditText
            sum += editText.text.toString().toInt()
        }
        return sum
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
        row.updateLayoutParams<TableRow.LayoutParams> {
        }
        row.setBackgroundResource(R.drawable.table_rows)


        val btn = Button(this)
        btn.setBackgroundResource(R.drawable.category_point)
        val color = ContextCompat.getColor(applicationContext, colors.random())
        btn.background.setTint(color)
        btn.setTextColor(color)
        btn.layoutParams = TableRow.LayoutParams(
            convertDpToPixel(17f, baseContext).toInt(), convertDpToPixel(17f, baseContext).toInt()
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
            ArrayAdapter<String>(this, R.layout.spinner_item,
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
        spinner.setBackgroundResource(R.drawable.add_pattern_input)
        spinner.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        spinner.updateLayoutParams<TableRow.LayoutParams> {
            width = 500
            setMargins(35, 0, 0, 0)
        }


        val inp2 = EditText(this)
        inp2.setRawInputType(InputType.TYPE_CLASS_NUMBER)
        inp2.setText("0")
        inp2.setFilters(arrayOf<InputFilter>(LengthFilter(2)))
        inp2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
        inp2.setBackgroundResource(R.drawable.add_pattern_input)
        inp2.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        inp2.id = 0
        inp2.updateLayoutParams<TableRow.LayoutParams> {
            width = 145
            setMargins(35, 0, 0, 0)
        }
        inp2.setOnFocusChangeListener { view, b ->
            if (b == false)
            {
                val value: Int
                try {
                    value = inp2.text.toString().toInt()
                }
                catch (e: java.lang.Exception){
                    (view as EditText).setText("0")
                    Toast.makeText(applicationContext, R.string.error_field_no_int, Toast.LENGTH_SHORT).show()
                    return@setOnFocusChangeListener
                }
                val min = 0
                val max = 100 - sumPercent +
                        inp2.id
                if (value > max)
                    inp2.setText("$max")
                if (value < min)
                    inp2.setText("$min")

                sumPercent = getSumPercentages()
                updateAmounts()
                refreshDiagram()
            }
            else
                inp2.id = inp2.text.toString().toInt()
        }

        val textView = TextView(this)
        textView.setText("0")
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
        textView.maxLines = 1
        textView.ellipsize = TextUtils.TruncateAt.END
        textView.isSingleLine = true
        textView.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT)
        textView.updateLayoutParams<LayoutParams> {
            setMargins(35, 0, 0, 0)
            width = 175
        }


        row.addView(btn, 0)
        row.addView(spinner, 1)
        row.addView(inp2, 2)
        row.addView(textView, 3)
        table.addView(row, 0)
    }

    fun confirmConsumption()
    {
        val table: TableLayout = findViewById(R.id.tableLayout)
        for (i in 0 until table.childCount) {
            val row = table.getChildAt(i) as TableRow
            val amount = (row.getChildAt(3) as TextView).text.toString().toFloat()
            val catId = (row.getChildAt(1) as Spinner).selectedItemId+1
            categoryManager.updateCatAmount(catId.toInt(), amount*-1)
        }

    }

    private fun updateAmounts(){
        val table: TableLayout = findViewById(R.id.tableLayout)
        val nominal:Float
        try {
            nominal = (findViewById(R.id.nominal_enter_box) as EditText).text.toString().toFloat()
        }
        catch (e: java.lang.Exception)
        {
            return
        }
        for (i in 0 until table.childCount) {
            val row = table.getChildAt(i) as TableRow
            val percentage = (row.getChildAt(2) as EditText).text.toString().toFloat()
            val amount = (nominal/100*percentage)
            if(amount == amount.toInt().toFloat())
                (row.getChildAt(3) as TextView).setText((nominal/100*percentage).toInt().toString())
            else
                (row.getChildAt(3) as TextView).setText((nominal/100*percentage).toString())
        }
    }
}